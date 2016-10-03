package me.icymint.sage.user.rest.authorization;

import com.google.common.base.Strings;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.BaseCode;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.exception.UnauthorizedException;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.base.spec.internal.api.SageValidator;
import me.icymint.sage.base.util.Exceptions;
import me.icymint.sage.base.util.HMacs;
import me.icymint.sage.user.core.service.TokenServiceImpl;
import me.icymint.sage.user.rest.context.TokenContext;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.api.ClaimService;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.def.UserCode;
import me.icymint.sage.user.spec.entity.Token;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
@Order(Magics.AOP_ORDER_TOKEN)
public class DefaultTokenAuthorization {
    private final Logger logger = LoggerFactory.getLogger(DefaultTokenAuthorization.class);

    @Autowired
    ApplicationContext context;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    TokenServiceImpl tokenService;
    @Autowired
    ClaimService claimService;
    @Autowired
    Clock clock;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    CacheManager cacheManager;
    @Autowired
    SageValidator sageValidator;


    public void authorize(TokenContext tokenContext, RoleType[] roleTypes, boolean expireTimeCheck) {
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
        Token token = tokenService.findOne(tokenContext.getTokenId());
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
        String calculatedHash = calculateHash(tokenContext.getClientId(),
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

    public String calculateHash(Long clientId, String nonce, Long timestamp, Long tokenId, String secret) {
        String data = Stream.of(clientId,
                nonce,
                timestamp,
                tokenId)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        return HMacs.encodeToBase64(secret, data);
    }

    @Pointcut("execution(* me.icymint.sage.user.rest.controller.TokenController.isExpire())")
    public void specialApi() {
    }

    @Before("@annotation(checkToken) && specialApi()")
    public void authorizeInExpired(CheckToken checkToken) {
        try {
            doAuthorize(checkToken, false);
        } catch (Exception e) {
            Exceptions.catching(e, false);
        }
    }

    @Before("@annotation(checkToken) && !specialApi()")
    public void authorize(CheckToken checkToken) {
        doAuthorize(checkToken, true);
    }

    private void doAuthorize(CheckToken checkToken, boolean expireTimeCheck) {
        try {
            String header = runtimeContext.getHeader(Magics.HEADER_AUTHORIZATION);
            if (checkToken.allowNone() && Strings.isNullOrEmpty(header)) {
                return;
            }
            TokenContext context = tokenService.parseTokenContext(header);
            if (context == null) {
                throw new UnauthorizedException(this.context, BaseCode.AUTHORIZATION_REQUIRED);
            }
            authorize(context, checkToken.allowRoles(), expireTimeCheck);
        } finally {
            if (runtimeContext.getTokenId() == null) {
                runtimeContext.setTokenId(null);
            }
            if (runtimeContext.getUserId() == null) {
                runtimeContext.setUserId(null);
            }
        }
    }

    private Cache getCache() {
        return cacheManager.getCache(Magics.CACHE_SIGNATURE);
    }
}
