package me.icymint.sage.base.rest.aspect;

import com.google.common.reflect.TypeToken;
import me.icymint.sage.base.rest.entity.PaginatorResponse;
import me.icymint.sage.base.rest.support.RestConverter;
import me.icymint.sage.base.spec.annotation.ResponseView;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.base.util.Exceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.function.Function;

/**
 * Created by daniel on 2016/10/2.
 */
@ControllerAdvice
@SuppressWarnings("unchecked")
public class DefaultResourceHandler implements ResponseBodyAdvice<Object> {
    @Autowired
    RestConverter restConverter;
    @Value("${" + MagicConstants.PROP_ENABLE_RESPONSE_BASE_PACKAGE + ":me.icymint.sage}")
    String basePackage;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().getDeclaringClass().getPackage().getName()
                .startsWith(basePackage);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ResponseView responseView = returnType.getMethodAnnotation(ResponseView.class);
        if (responseView == null) {
            return wrapBody(body, true);
        }
        Class<? extends Function<?, ?>> handlerClass = responseView.converter();
        boolean needConvert = responseView.convertList();
        Class<?> toClass = responseView.value();
        Class<?> fromClass = returnType.getMethod().getReturnType();
        try {
            if (body instanceof List) {
                fromClass = TypeToken
                        .of(returnType.getMethod().getGenericReturnType())
                        .resolveType(fromClass.getTypeParameters()[0])
                        .getRawType();
                Function<Object, Object> handler = getConverter(handlerClass, fromClass, toClass);
                if (handler != null) {
                    modifyBody((List) body, handler);
                }
                return wrapBody(body, needConvert);
            } else {
                Function<Object, Object> handler = getConverter(handlerClass, fromClass, toClass);
                if (handler != null) {
                    return wrapBody(handler.apply(body), needConvert);
                }
            }
        } catch (Exception e) {
            Exceptions.catching(e);
        }
        return wrapBody(body, needConvert);
    }

    private Function<Object, Object> getConverter(Class<? extends Function<?, ?>> handlerClass, Class<?> fromClass, Class<?> toClass) throws IllegalAccessException, InstantiationException {
        if (handlerClass == ResponseView.NullFunction.class) {
            return restConverter.getConverter(fromClass, toClass);
        }
        return (Function<Object, Object>) handlerClass.newInstance();
    }

    private Object wrapBody(Object body, boolean needConvert) {
        if (needConvert
                && body instanceof List) {
            return PaginatorResponse.of((List) body);
        }
        return body;
    }


    private void modifyBody(List body, Function<Object, Object> handler) {
        int length = body.size();
        for (int i = 0; i < length; i++) {
            Object v = body.get(i);
            if (v != null) {
                v = handler.apply(v);
                body.set(i, v);
            }
        }
    }

}
