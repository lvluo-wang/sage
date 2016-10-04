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
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.api.TokenService;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.def.PermissionStrategy;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.UserCode;
import me.icymint.sage.user.spec.entity.Identity;
import me.icymint.sage.user.spec.entity.IdentityExtension;
import me.icymint.sage.user.spec.entity.Token;
import me.icymint.sage.user.spec.internal.api.AuthorizationMethod;
import me.icymint.sage.user.spec.internal.entity.LoginEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
    IdentityServiceImpl identityService;
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
    @Autowired
    TokenServiceImpl tokenService;

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
        String cacheKey = nonce + ":" + timestamp + ":" + hash;
        if (getCache().get(cacheKey) != null) {
            throw new UnauthorizedException(context, UserCode.ACCESS_TOKEN_ILLEGAL);
        }

        //Step 2
        Instant now = clock.now();
        Instant ts = Instant.ofEpochSecond(timestamp);
        if (now.plusSeconds(Magics.TOKEN_SPAN).isBefore(ts)
                || now.minusSeconds(Magics.TOKEN_SPAN).isAfter(ts)) {
            throw new UnauthorizedException(context, UserCode.ACCESS_SPAN_NOT_VALID);
        }

        //Step 3
        Identity identity = checkAndGetIdentity(identityId, IdentityType.MEMBER);

        if (identity.getIsBlocked() != Bool.N || identity.getPassword().length() < 32) {
            throw new UnauthorizedException(context, UserCode.USER__LOGIN_DENIED, identityId);
        }

        String calculatedHash = calculateLoginHash(identityId, clientId, nonce, timestamp, identity.getPassword());
        if (!Objects.equals(hash, calculatedHash)) {
            throw new UnauthorizedException(context, UserCode.ACCESS_TOKEN_ILLEGAL);
        }

        //Step 4
        Identity client = checkAndGetIdentity(clientId, IdentityType.CLIENT);
        IdentityExtension clientExtension = getClientExtension(client);
        if (clientExtension.getValidSeconds() == null || clientExtension.getValidSeconds() <= 0) {
            throw new UnauthorizedException(context, UserCode.CLIENT_ID__ILLEGAL, clientId);
        }

        //Step 5
        tokenService.expireByClientId(clientId);

        //Step 6
        runtimeContext.setClientId(clientId);
        runtimeContext.setUserId(identityId);
        Token token = new Token()
                .setExpireTime(now.plusSeconds(clientExtension.getValidSeconds()))
                .setAccessSecret(UUID.randomUUID().toString())
                .setClientId(clientId)
                .setCreateTime(now)
                .setOwnerId(identityId)
                .setIp(runtimeContext.getUserAddress())
                .setTimeZone(Optional.ofNullable(runtimeContext.getTimeZone())
                        .map(ZoneId::getId).orElse(null))
                .setSessionId(runtimeContext.getSessionId());
        tokenMapper.create(token);


        getCache().put(cacheKey, "true");
        return tokenService.findOne(token.getId());
    }

    private IdentityExtension getClientExtension(Identity client) {
        return client.getExtension() == null
                ? new IdentityExtension().setValidSeconds(600L)
                : client.getExtension();
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
    @Cacheable(value = Magics.CACHE_TOKEN, key = "#tokenId")
    public Token findOne(Long tokenId) {
        return tokenMapper.findOne(tokenId);
    }

    @Override
    @Transactional
    public boolean isExpire(Long tokenId) {
        if (tokenId == null) {
            return true;
        }
        Token token = tokenService.findOne(tokenId);
        if (token != null) {
            Instant now = clock.now();
            if (token.getExpireTime().isAfter(now)) {
                return false;
            }
            tokenService.expire(tokenId);
        }
        return true;
    }

    @Override
    @Transactional
    @CacheEvict(value = Magics.CACHE_TOKEN, key = "#tokenId")
    public void expire(Long tokenId) {
        tokenMapper.delete(tokenId);
    }


    @Transactional
    public void expireByClientId(Long clientId) {
        tokenMapper.findByClientId(clientId).forEach(tokenService::expire);
    }

    public void authorize(CheckToken checkToken, Permission classPermission) {
        try {
            boolean expireTimeCheck = checkToken == null;
            String header = runtimeContext.getHeader(Magics.HEADER_AUTHORIZATION);
            if (checkToken != null
                    && checkToken.allowNone()
                    && Strings.isNullOrEmpty(header)) {
                return;
            }
            doAuthorize(header, classPermission, expireTimeCheck);
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

    private void doAuthorize(String header, Permission classPermission, boolean expireTimeCheck) {
        TokenContext tokenContext = parseTokenContext(header);
        if (tokenContext == null) {
            throw new UnauthorizedException(context, BaseCode.AUTHORIZATION_REQUIRED);
        }
        logger.debug("authorize tokenContext passed in :{}", tokenContext);

        sageValidator.validate(tokenContext, "tokenContext");

        String nonce = tokenContext.getNonce();
        Long timestamp = tokenContext.getTimestamp();

        //Step 1
        String hash = tokenContext.getSign();
        String cacheKey = nonce + ":" + timestamp + ":" + hash;
        if (getCache().get(cacheKey) != null) {
            throw new UnauthorizedException(context, UserCode.ACCESS_TOKEN_ILLEGAL);
        }

        //Step 2
        Instant now = clock.now();
        Instant ts = Instant.ofEpochSecond(timestamp);
        if (now.plusSeconds(Magics.TOKEN_SPAN).isBefore(ts)
                || now.minusSeconds(Magics.TOKEN_SPAN).isAfter(ts)) {
            throw new UnauthorizedException(context, UserCode.ACCESS_SPAN_NOT_VALID);
        }

        //Step 3
        Token token = tokenService.findOne(tokenContext.getTokenId());
        if (token == null) {
            throw new UnauthorizedException(context, UserCode.TOKEN__NOT_FOUND, tokenContext.getTokenId());
        }
        if (!Objects.equals(token.getClientId(), tokenContext.getClientId())) {
            throw new UnauthorizedException(context, UserCode.CLIENT_ID__ILLEGAL, tokenContext.getClientId());
        }
        runtimeContext.setClientId(tokenContext.getClientId());

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
        if (!checkPermission(token, classPermission)) {
            throw new UnauthorizedException(context, UserCode.ACCESS_PERMISSION_DENIED);
        }

        tokenContext.setOwnerId(token.getOwnerId());
        runtimeContext.setUserId(token.getOwnerId());
        runtimeContext.setTokenId(token.getId());

        getCache().put(cacheKey, "true");
    }

    private boolean checkPermission(Token token, Permission permission) {
        if (permission == null || permission.value().length == 0) {
            return true;
        }
        Set<Privilege> userPrivileges = getPermissionsByToken(token);
        if (permission.strategy() == PermissionStrategy.MATCH_ANY) {
            return Stream.of(permission.value()).anyMatch(userPrivileges::contains);
        } else {
            return Stream.of(permission.value()).allMatch(userPrivileges::contains);
        }
    }

    private Set<Privilege> getPermissionsByToken(Token token) {
        //TODO ignore clientId and token type
        return identityService.findPrivilegesById(token.getOwnerId());
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
                    .setSessionId(token.getSessionId())
                    .setClientId(token.getClientId());
        }
    }
}
