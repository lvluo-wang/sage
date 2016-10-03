package me.icymint.sage.user.spec.internal.api;

import me.icymint.sage.user.rest.context.TokenContext;

/**
 * Created by daniel on 2016/10/3.
 */
public interface AuthorizationMethod {

    String methodHeader();

    TokenContext parse(String authString);
}
