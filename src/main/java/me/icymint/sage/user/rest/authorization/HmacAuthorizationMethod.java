package me.icymint.sage.user.rest.authorization;

import com.google.common.base.Splitter;
import me.icymint.sage.user.rest.context.TokenContext;
import me.icymint.sage.user.spec.internal.api.AuthorizationMethod;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by daniel on 2016/10/3.
 */
@Component
public class HmacAuthorizationMethod implements AuthorizationMethod {

    private static final int TOKEN_SIGN_LENGTH = 44;

    @Override
    public String methodHeader() {
        return "HMAC-1";
    }

    @Override
    public TokenContext parse(String authString) {
        TokenContext context = new TokenContext();
        if (authString.length() < TOKEN_SIGN_LENGTH) {
            return null;
        }
        context.setSign(authString.substring(0, TOKEN_SIGN_LENGTH));
        List<String> parameterList = Splitter.on("|")
                .omitEmptyStrings()
                .trimResults()
                .splitToList(authString.substring(TOKEN_SIGN_LENGTH));
        if (parameterList.size() != 4) {
            return null;
        }
        context.setClientId(Long.valueOf(parameterList.get(0)));
        context.setTokenId(Long.valueOf(parameterList.get(1)));
        context.setTimestamp(Long.valueOf(parameterList.get(2)));
        context.setNonce(parameterList.get(3));
        return context;
    }
}
