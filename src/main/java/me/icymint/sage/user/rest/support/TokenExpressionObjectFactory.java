package me.icymint.sage.user.rest.support;

import com.google.common.collect.Sets;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.core.service.TokenServiceImpl;
import me.icymint.sage.user.spec.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Created by daniel on 2016/10/4.
 */
@Component
public class TokenExpressionObjectFactory implements IExpressionObjectFactory {
    private final Set<String> OBJECT_SET = Collections.unmodifiableSet(Sets.newHashSet(Magics.TOKEN_OBJECT));
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    TokenServiceImpl tokenService;


    @Override
    public Set<String> getAllExpressionObjectNames() {
        return OBJECT_SET;
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if (Magics.TOKEN_OBJECT.equals(expressionObjectName)) {
            Long tokenId = runtimeContext.getTokenId();
            if (tokenId != null) {
                Token token = tokenService.findOne(tokenId);
                if (token != null) {
                    return new TokenObject(token, tokenService);
                }
            }
        }
        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return false;
    }
}
