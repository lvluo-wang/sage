package me.icymint.sage.user.rest.controller;

import me.icymint.sage.base.spec.exception.UnauthorizedException;
import me.icymint.sage.user.rest.context.TokenContext;
import me.icymint.sage.user.rest.request.TokenRequest;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.api.TokenService;
import me.icymint.sage.user.spec.def.UserExceptionCode;
import me.icymint.sage.user.spec.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Created by daniel on 16/9/4.
 */
@RestController
@RequestMapping("/tokens")
public class TokenController {
    @Autowired
    TokenService tokenService;
    @Autowired
    ApplicationContext context;

    @GetMapping(value = "/{tokenId}/expired", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isExpire(@PathVariable("tokenId") Long tokenId) {
        return tokenService.isExpire(tokenId);
    }

    @CheckToken
    @DeleteMapping(value = "/{tokenId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void expire(@PathVariable("tokenId") Long tokenId,
                       @RequestHeader("Authorization") TokenContext tokenContext) {
        if (!Objects.equals(tokenContext.getTokenId(), tokenId)) {
            throw new UnauthorizedException(context, UserExceptionCode.ACCESS_TOKEN_ILLEGAL);
        }
        tokenService.expire(tokenId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Token login(@Valid @RequestBody TokenRequest request) {
        return tokenService.login(request.getUid(),
                request.getCid(),
                request.getNonce(),
                request.getTs(),
                request.getSign());
    }

}
