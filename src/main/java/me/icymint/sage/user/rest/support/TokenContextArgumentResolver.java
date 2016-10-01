package me.icymint.sage.user.rest.support;

import me.icymint.sage.base.rest.util.QueryStrings;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.base.util.Exceptions;
import me.icymint.sage.user.rest.context.TokenContext;
import org.springframework.core.MethodParameter;
import org.springframework.format.Formatter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by daniel on 16/9/5.
 */
@ControllerAdvice
public class TokenContextArgumentResolver implements HandlerMethodArgumentResolver, Formatter<TokenContext> {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return TokenContext.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return parse(webRequest.getHeader(MagicConstants.AUTHORIZATION), null);
    }

    @Override
    public TokenContext parse(String text, Locale locale) throws ParseException {
        TokenContext context = new TokenContext();
        if (StringUtils.isEmpty(text)) {
            return context;
        }
        text = text.trim();
        int spaceIdx = text.indexOf(' ');
        if (spaceIdx > 0) {
            text = text.substring(spaceIdx + 1);
        }
        if (StringUtils.isEmpty(text)) {
            return context;
        }
        try {
            MultiValueMap<String, String> map = QueryStrings.parse(text);
            context.setClientId(Long.valueOf(map.getFirst("clientId")));
            context.setTokenId(Long.valueOf(map.getFirst("tokenId")));
            context.setTimestamp(Long.valueOf(map.getFirst("timestamp")));
            context.setNonce(map.getFirst("nonce"));
            context.setSign(map.getFirst("sign"));
        } catch (Exception e) {
            Exceptions.catching(e);
        }
        return context;
    }

    @Override
    public String print(TokenContext object, Locale locale) {
        return null;
    }

}
