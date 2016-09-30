package me.icymint.sage.user.core.service;

import me.icymint.sage.base.core.util.HMacs;
import me.icymint.sage.base.spec.annotation.NotifyEvent;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.EventProducer;
import me.icymint.sage.base.spec.api.RuntimeContext;
import me.icymint.sage.base.spec.def.BaseExceptionCode;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.base.spec.exception.InvalidArgumentException;
import me.icymint.sage.base.spec.exception.UnauthorizedException;
import me.icymint.sage.user.data.mapper.TokenMapper;
import me.icymint.sage.user.spec.api.IdentityService;
import me.icymint.sage.user.spec.api.TokenService;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.def.UserExceptionCode;
import me.icymint.sage.user.spec.entity.Identity;
import me.icymint.sage.user.spec.entity.Token;
import me.icymint.sage.user.spec.internal.entity.LoginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by daniel on 16/9/4.
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    ApplicationContext context;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    TokenMapper tokenMapper;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    IdentityService identityService;
    @Autowired
    Clock clock;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    CacheManager cacheManager;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    RuntimeContext runtimeContext;

    @Override
    @Transactional
    @NotifyEvent(eventProducerClass = LoginEventProducer.class)
    public Token login(Long identityId, Long clientId, String nonce, Long timestamp, String hash) {
        if (identityId == null) {
            throw new InvalidArgumentException(context, BaseExceptionCode.PARAM__ILLEGAL, "uid");
        }
        if (clientId == null) {
            throw new InvalidArgumentException(context, BaseExceptionCode.PARAM__ILLEGAL, "cid");
        }
        if (nonce == null) {
            throw new InvalidArgumentException(context, BaseExceptionCode.PARAM__ILLEGAL, "nonce");
        }
        if (timestamp == null) {
            throw new InvalidArgumentException(context, BaseExceptionCode.PARAM__ILLEGAL, "ts");
        }
        if (hash == null) {
            throw new InvalidArgumentException(context, BaseExceptionCode.PARAM__ILLEGAL, "sign");
        }
        //Step 1
        Instant now = clock.now();
        Instant ts = Instant.ofEpochSecond(timestamp);
        if (now.plusSeconds(MagicConstants.TOKEN_SPAN).isBefore(ts)
                || now.minusSeconds(MagicConstants.TOKEN_SPAN).isAfter(ts)) {
            throw new UnauthorizedException(context, UserExceptionCode.ACCESS_SPAN_NOT_VALID);
        }

        //Step 2
        String cacheKey = nonce + ":" + timestamp + ":" + hash;
        if (getCache().get(cacheKey) != null) {
            throw new UnauthorizedException(context, UserExceptionCode.ACCESS_TOKEN_ILLEGAL);
        }

        //Step 3
        Identity identity = checkAndGetIdentity(identityId, IdentityType.USER);
        String data = Stream.of(identityId,
                clientId,
                nonce,
                timestamp)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        String calculatedHash = HMacs.encodeToBase64(identity.getPassword(), data);
        if (!Objects.equals(hash, calculatedHash)) {
            throw new UnauthorizedException(context, UserExceptionCode.ACCESS_TOKEN_ILLEGAL);
        }

        //Step 4
        Identity client = checkAndGetIdentity(clientId, IdentityType.CLIENT);
        if (client.getValidSeconds() == null || client.getValidSeconds() <= 0) {
            throw new UnauthorizedException(context, UserExceptionCode.CLIENT_ID__ILLEGAL, clientId);
        }

        //Step 5
        runtimeContext.setClientId(String.valueOf(clientId));
        runtimeContext.setUserId(String.valueOf(identityId));
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

    private Identity checkAndGetIdentity(Long identityId, IdentityType... types) {
        Identity identity = identityService.findOne(identityId);
        if (identity == null) {
            throw new InvalidArgumentException(context, UserExceptionCode.IDENTITY__NOT_FOUND, identityId);
        }
        if (identity.getIsBlocked() == Bool.Y) {
            throw new UnauthorizedException(context, UserExceptionCode.IDENTITY__IS_BLOCKED, identityId);
        }
        for (IdentityType type : types) {
            if (identity.getType() == type) {
                return identity;
            }
        }
        throw new UnauthorizedException(context, UserExceptionCode.IDENTITY__ILLEGAL, identityId);
    }


    private Cache getCache() {
        return cacheManager.getCache(MagicConstants.CACHE_SIGNATURE);
    }

    @Override
    public Token findOne(Long tokenId) {
        return tokenMapper.findOne(tokenId);
    }

    @Override
    @Transactional
    public boolean isExpire(Long tokenId) {
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
