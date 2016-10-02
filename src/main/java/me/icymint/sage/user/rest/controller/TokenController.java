package me.icymint.sage.user.rest.controller;

import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.rest.converter.UserRestConverter;
import me.icymint.sage.user.rest.request.TokenRequest;
import me.icymint.sage.user.rest.resource.TokenResource;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.api.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    UserRestConverter converter;

    @CheckToken
    @GetMapping(value = "/expired", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isExpire() {
        return tokenService.isExpire(runtimeContext.getTokenId());
    }

    @CheckToken
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void expire() {
        tokenService.expire(runtimeContext.getTokenId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenResource login(@Valid @RequestBody TokenRequest request) {
        return converter.to(tokenService.login(request.getUid(),
                request.getCid(),
                request.getNonce(),
                request.getTs(),
                request.getSign()));
    }

}
