package me.icymint.sage.user.rest.support;

import me.icymint.sage.user.core.service.TokenServiceImpl;
import me.icymint.sage.user.spec.entity.Token;

/**
 * Created by daniel on 2016/10/4.
 */
public class TokenObject {
    private final Token token;
    private final TokenServiceImpl tokenService;

    public TokenObject(Token token, TokenServiceImpl tokenService) {
        this.token = token;
        this.tokenService = tokenService;
    }
}
