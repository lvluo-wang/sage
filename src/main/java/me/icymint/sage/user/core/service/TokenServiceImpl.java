package me.icymint.sage.user.core.service;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import me.icymint.sage.base.spec.annotation.NotifyEvent;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.BaseCode;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.exception.InvalidArgumentException;
import me.icymint.sage.base.spec.exception.UnauthorizedException;
import me.icymint.sage.base.spec.internal.api.EventProducer;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.base.spec.internal.api.SageValidator;
import me.icymint.sage.base.util.HMacs;
import me.icymint.sage.user.data.mapper.TokenMapper;
import me.icymint.sage.user.rest.context.TokenContext;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.api.ClaimService;
import me.icymint.sage.user.spec.api.IdentityService;
import me.icymint.sage.user.spec.api.TokenService;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.def.UserCode;
import me.icymint.sage.user.spec.entity.Identity;
import me.icymint.sage.user.spec.entity.Token;
import me.icymint.sage.user.spec.internal.api.AuthorizationMethod;
import me.icymint.sage.user.spec.internal.entity.LoginEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by daniel on 16/9/4.
 */
@Service
public class TokenServiceImpl implements TokenService {
    private final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);
    private final Map<String, AuthorizationMethod> authorizationMethodMap = Maps.newHashMap();

    @Autowired
    ApplicationContext context;
    @Autowired
    AuthorizationMethod[] authorizationMethods;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    IdentityService identityService;
    @Autowired
    Clock clock;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    CacheManager cacheManager;
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    ClaimService claimService;
    @Autowired
    SageValidator sageValidator;

    @PostConstruct
    protected void init() {
        for (AuthorizationMethod method : authorizationMethods) {
            Assert.isNull(authorizationMethodMap.put(method.methodHeader(), method),
                    "Authorization method " + method.methodHeader() + " duplicated!");
        }
    }

    @Override
    @Transactional
    @NotifyEvent(eventProducerClass = LoginEventProducer.class)
    public Token login(Long identityId, Long clientId, String nonce, Long timestamp, String hash) {
        if (identityId == null) {
            throw new InvalidArgumentException(context, BaseCode.PARAM__ILLEGAL, "uid");
        }
        if (clientId == null) {
            throw new InvalidArgumentException(context, BaseCode.PARAM__ILLEGAL, "cid");
        }
        if (nonce == null) {
            throw new InvalidArgumentException(context, BaseCode.PARAM__ILLEGAL, "nonce");
        }
        if (timestamp == null) {
            throw new InvalidArgumentException(context, BaseCode.PARAM__ILLEGAL, "ts");
        }
        if (hash == null) {
            throw new InvalidArgumentException(context, BaseCode.PARAM__ILLEGAL, "sign");
        }
        //Step 1
        Instant now = clock.now();
        Instant ts = Instant.ofEpochSecond(timestamp);
        if (now.plusSeconds(Magics.TOKEN_SPAN).isBefore(ts)
                || now.minusSeconds(Magics.TOKEN_SPAN).isAfter(ts)) {
            throw new UnauthorizedException(context, UserCode.ACCESS_SPAN_NOT_VALID);
        }

        //Step 2
        String cacheKey = nonce + ":" + timestamp + ":" + hash;
        if (getCache().get(cacheKey) != null) {
            throw new UnauthorizedException(context, UserCode.ACCESS_TOKEN_ILLEGAL);
        }

        //Step 3
        Identity identity = checkAndGetIdentity(identityId, IdentityType.USER);
        String calculatedHash = calculateLoginHash(identityId, clientId, nonce, timestamp, identity.getPassword());
        if (!Objects.equals(hash, calculatedHash)) {
            throw new UnauthorizedException(context, UserCode.ACCESS_TOKEN_ILLEGAL);
        }

        //Step 4
        Identity client = checkAndGetIdentity(clientId, IdentityType.CLIENT);
        if (client.getValidSeconds() == null || client.getValidSeconds() <= 0) {
            throw new UnauthorizedException(context, UserCode.CLIENT_ID__ILLEGAL, clientId);
        }

        //Step 5
        runtimeContext.setClientId(String.valueOf(clientId));
        runtimeContext.setUserId(identityId);
        Token token = new Token()
                .setExpireTime(now.plusSeconds(client.getValidSeconds()))
                .setAccessSecret(UUID.randomUUID().toString())
                .setClientId(clientId)
                .setCreateTime(now)
                .setOwnerId(identityId)
                .setSessionId(runtimeContext.getSessionId());
        tokenMapper.save(token);
        getCache().put(cacheKey, "true");
        return tokenMapper.findOne(token.getId());
    }

    public String calculateLoginHash(Long identityId, Long clientId, String nonce, Long timestamp, String password) {
        String data = Stream.of(identityId,
                clientId,
                nonce,
                timestamp)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        return HMacs.encodeToBase64(password, data);
    }

    private Identity checkAndGetIdentity(Long identityId, IdentityType... types) {
        Identity identity = identityService.findOne(identityId);
        if (identity == null) {
            throw new InvalidArgumentException(context, UserCode.IDENTITY__NOT_FOUND, identityId);
        }
        if (identity.getIsBlocked() == Bool.Y) {
            throw new UnauthorizedException(context, UserCode.IDENTITY__IS_BLOCKED, identityId);
        }
        for (IdentityType type : types) {
            if (identity.getType() == type) {
                return identity;
            }
        }
        throw new UnauthorizedException(context, UserCode.IDENTITY__ILLEGAL, identityId);
    }


    private Cache getCache() {
        return cacheManager.getCache(Magics.CACHE_SIGNATURE);
    }

    @Override
    public Token findOne(Long tokenId) {
        return tokenMapper.findOne(tokenId);
    }

    @Override
    @Transactional
    public boolean isExpire(Long tokenId) {
        if (tokenId == null) {
            return true;
        }
        Token token = findOne(tokenId);
        if (token != null) {
            Instant now = clock.now();
            if (token.getExpireTime().isAfter(now)) {
                return false;
            }
            expire(tokenId);
        }
        return true;
    }

    @Override
    @Transactional
    public void expire(Long tokenId) {
        tokenMapper.delete(tokenId);
    }

    @Transactional
    public void expireBySessionId(String sessionId) {
        tokenMapper.deleteBySessionId(sessionId);
    }

    public void authorize(CheckToken checkToken, boolean expireTimeCheck) {
        try {
            String header = runtimeContext.getHeader(Magics.HEADER_AUTHORIZATION);
            if (checkToken.allowNone() && Strings.isNullOrEmpty(header)) {
                return;
            }
            doAuthorize(header, checkToken.allowRoles(), expireTimeCheck);
        } finally {
            if (runtimeContext.getTokenId() == null) {
                runtimeContext.setTokenId(null);
            }
            if (runtimeContext.getUserId() == null) {
                runtimeContext.setUserId(null);
            }
        }
    }


    private TokenContext parseTokenContext(String tokenString) {
        if (StringUtils.isEmpty(tokenString)
                || !tokenString.startsWith(Magics.TOKEN_AUTHORIZATION_HEAD)
                || tokenString.equals(Magics.TOKEN_AUTHORIZATION_HEAD)) {
            throw new UnauthorizedException(this.context, UserCode.AUTHORIZATION_HEADER_UNKNOWN);
        }
        tokenString = tokenString.substring(Magics.TOKEN_AUTHORIZATION_HEAD.length());
        List<String> list = Splitter.on(" ")
                .trimResults()
                .omitEmptyStrings()
                .limit(2)
                .splitToList(tokenString);
        if (list.size() != 2) {
            return null;
        }
        AuthorizationMethod method = authorizationMethodMap.get(list.get(0));
        if (method == null) {
            throw new UnauthorizedException(this.context, UserCode.AUTHORIZATION_HEADER__NOT_SUPPORTED, list.get(0));
        }
        return method.parse(list.get(1));
    }

    private void doAuthorize(String header, RoleType[] roleTypes, boolean expireTimeCheck) {
        TokenContext tokenContext = parseTokenContext(header);
        if (tokenContext == null) {
            throw new UnauthorizedException(context, BaseCode.AUTHORIZATION_REQUIRED);
        }
        logger.debug("authorize tokenContext passed in :{}", tokenContext);

        sageValidator.validate(tokenContext, "tokenContext");

        String nonce = tokenContext.getNonce();
        Long timestamp = tokenContext.getTimestamp();

        //Step 1
        Instant now = clock.now();
        Instant ts = Instant.ofEpochSecond(timestamp);
        if (now.plusSeconds(Magics.TOKEN_SPAN).isBefore(ts)
                || now.minusSeconds(Magics.TOKEN_SPAN).isAfter(ts)) {
            throw new UnauthorizedException(context, UserCode.ACCESS_SPAN_NOT_VALID);
        }

        //Step 2
        String hash = tokenContext.getSign();
        String cacheKey = nonce + ":" + timestamp + ":" + hash;
        if (getCache().get(cacheKey) != null) {
            throw new UnauthorizedException(context, UserCode.ACCESS_TOKEN_ILLEGAL);
        }

        //Step 3
        Token token = findOne(tokenContext.getTokenId());
        if (token == null) {
            throw new UnauthorizedException(context, UserCode.TOKEN__NOT_FOUND, tokenContext.getTokenId());
        }
        if (!Objects.equals(token.getClientId(), tokenContext.getClientId())) {
            throw new UnauthorizedException(context, UserCode.CLIENT_ID__ILLEGAL, tokenContext.getClientId());
        }
        runtimeContext.setClientId(String.valueOf(tokenContext.getClientId()));

        //Step 4
        if (expireTimeCheck && (token.getExpireTime() == null || token.getExpireTime().isBefore(ts))) {
            throw new UnauthorizedException(context, UserCode.TOKEN__EXPIRED, token.getId());
        }
        String calculatedHash = calculateTokenHash(tokenContext.getClientId(),
                tokenContext.getNonce(),
                tokenContext.getTimestamp(),
                tokenContext.getTokenId(),
                token.getAccessSecret());
        if (!Objects.equals(hash, calculatedHash)) {
            logger.warn("auth sign not valid");
            throw new UnauthorizedException(context, UserCode.ACCESS_TOKEN_ILLEGAL);
        }
        //Step 5
        if (!claimService.hasRoles(token.getOwnerId(), roleTypes)) {
            logger.warn("user {} has no roles {}", token.getOwnerId(), Arrays.toString(roleTypes));
            throw new UnauthorizedException(context, UserCode.ACCESS_PERMISSION_DENIED);
        }

        tokenContext.setOwnerId(token.getOwnerId());
        runtimeContext.setUserId(token.getOwnerId());
        runtimeContext.setTokenId(token.getId());

        getCache().put(cacheKey, "true");
    }

    public String calculateTokenHash(Long clientId, String nonce, Long timestamp, Long tokenId, String secret) {
        String data = Stream.of(clientId,
                nonce,
                timestamp,
                tokenId)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        return HMacs.encodeToBase64(secret, data);
    }

    public static class LoginEventProducer implements EventProducer<Token, LoginEvent> {
        @Override
        public Class<Token> resultClass() {
            return Token.class;
        }

        @Override
        public LoginEvent apply(Token token) {
            return new LoginEvent()
                    .setOwnerId(token.getOwnerId())
                    .setTokenId(token.getId())
                    .setClientId(String.valueOf(token.getClientId()))
                    .setSessionId(token.getSessionId());
        }
    }
}
