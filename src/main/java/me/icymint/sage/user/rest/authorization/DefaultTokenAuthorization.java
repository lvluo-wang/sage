package me.icymint.sage.user.rest.authorization;

import me.icymint.sage.base.core.util.HMacs;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.RuntimeContext;
import me.icymint.sage.base.spec.api.SageValidator;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.base.spec.exception.UnauthorizedException;
import me.icymint.sage.user.rest.context.TokenContext;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.api.ClaimService;
import me.icymint.sage.user.spec.api.TokenService;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.def.UserExceptionCode;
import me.icymint.sage.user.spec.entity.Token;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by daniel on 16/9/4.
 */
@Component
@Aspect
@Order(MagicConstants.AOP_ORDER_TOKEN)
public class DefaultTokenAuthorization {
    private final Logger logger = LoggerFactory.getLogger(DefaultTokenAuthorization.class);

    @Autowired
    ApplicationContext context;
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    TokenService tokenService;
    @Autowired
    ClaimService claimService;
    @Autowired
    Clock clock;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    CacheManager cacheManager;
    @Autowired
    SageValidator sageValidator;


    public void authorize(TokenContext tokenContext, RoleType[] roleTypes) {
        logger.debug("authorize tokenContext passed in :{}", tokenContext);

        sageValidator.validate(tokenContext, "tokenContext");

        String nonce = tokenContext.getNonce();
        Long timestamp = tokenContext.getTimestamp();

        //Step 1
        Instant now = clock.now();
        Instant ts = Instant.ofEpochSecond(timestamp);
        if (now.plusSeconds(MagicConstants.TOKEN_SPAN).isBefore(ts)
                || now.minusSeconds(MagicConstants.TOKEN_SPAN).isAfter(ts)) {
            throw new UnauthorizedException(context, UserExceptionCode.ACCESS_SPAN_NOT_VALID);
        }

        //Step 2
        String hash = tokenContext.getSign();
        String cacheKey = nonce + ":" + timestamp + ":" + hash;
        if (getCache().get(cacheKey) != null) {
            throw new UnauthorizedException(context, UserExceptionCode.ACCESS_TOKEN_ILLEGAL);
        }

        //Step 3
        Token token = tokenService.findOne(tokenContext.getTokenId());
        if (token == null) {
            throw new UnauthorizedException(context, UserExceptionCode.TOKEN__NOT_FOUND, tokenContext.getTokenId());
        }
        if (!Objects.equals(token.getClientId(), tokenContext.getClientId())) {
            throw new UnauthorizedException(context, UserExceptionCode.CLIENT_ID__ILLEGAL, tokenContext.getClientId());
        }
        runtimeContext.setClientId(String.valueOf(tokenContext.getClientId()));

        //Step 4
        if (token.getExpireTime() == null || token.getExpireTime().isBefore(ts)) {
            throw new UnauthorizedException(context, UserExceptionCode.ACCESS_TOKEN_ILLEGAL);
        }
        String calculatedHash = calculateHash(tokenContext.getClientId(),
                tokenContext.getNonce(),
                tokenContext.getTimestamp(),
                tokenContext.getTokenId(),
                token.getAccessSecret());
        if (!Objects.equals(hash, calculatedHash)) {
            logger.warn("auth sign not valid");
            throw new UnauthorizedException(context, UserExceptionCode.ACCESS_TOKEN_ILLEGAL);
        }
        //Step 5
        if (!claimService.hasRoles(token.getOwnerId(), roleTypes)) {
            logger.warn("user {} has no roles {}", token.getOwnerId(), Arrays.toString(roleTypes));
            throw new UnauthorizedException(context, UserExceptionCode.ACCESS_PERMISSION_DENIED);
        }

        tokenContext.setOwnerId(token.getOwnerId());
        runtimeContext.setUserId(String.valueOf(token.getOwnerId()));

        getCache().put(cacheKey, "true");
    }

    public String calculateHash(Long clientId, String nonce, Long timestamp, Long tokenId, String secret) {
        String data = Stream.of(clientId,
                nonce,
                timestamp,
                tokenId)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        return HMacs.encodeToBase64(secret, data);
    }

    @Before(value = "@annotation(checkToken)", argNames = "joinPoint,checkToken")
    public void authorize(JoinPoint joinPoint, CheckToken checkToken) {
        TokenContext tokenContext = (TokenContext) Stream
                .of(joinPoint.getArgs())
                .filter(a -> a != null
                        && TokenContext.class.isAssignableFrom(a.getClass()))
                .findFirst()
                .orElse(null);
        if (tokenContext != null) {
            authorize(tokenContext, checkToken.allowRoles());
        }
    }

    private Cache getCache() {
        return cacheManager.getCache(MagicConstants.CACHE_SIGNATURE);
    }
}
