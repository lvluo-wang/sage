package me.icymint.sage.base.rest.aspect;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.reflect.TypeToken;
import me.icymint.sage.base.rest.entity.PaginatorResponse;
import me.icymint.sage.base.spec.annotation.ResponseConverter;
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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import static com.google.common.primitives.Primitives.wrap;

/**
 * Created by daniel on 2016/10/2.
 */
@ControllerAdvice
@SuppressWarnings("unchecked")
public class DefaultResourceHandler implements ResponseBodyAdvice<Object> {
    @Autowired(required = false)
    ResponseConverter[] converters;
    @Value("${" + MagicConstants.PROP_ENABLE_RESPONSE_WRAP_LIST + ":false}")
    boolean wrapList;
    @Value("${" + MagicConstants.PROP_ENABLE_RESPONSE_BASE_PACKAGE + ":me.icymint.sage}")
    String basePackage;

    private final Table<Class<?>, Class<?>, Function<Object, Object>> converterTable = HashBasedTable.create();

    @PostConstruct
    protected void init() {
        if (converters != null) {
            for (ResponseConverter converter : converters) {
                for (Method method : converter.getClass().getMethods()) {
                    if (method.getParameterCount() == 1) {
                        Function<Object, Object> handler = from -> {
                            try {
                                return method.invoke(converter, from);
                            } catch (Throwable e) {
                                throw Exceptions.wrap(e);
                            }
                        };
                        Assert.isNull(converterTable.put(
                                wrap(method.getParameterTypes()[0]),
                                wrap(method.getReturnType()),
                                handler),
                                "Converter from " + method.getParameterTypes()[0]
                                        + " to " + method.getReturnType()
                                        + " has already registered");
                    }
                }
            }
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().getDeclaringClass().getPackage().getName()
                .startsWith(basePackage);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ResponseView responseView = returnType.getMethodAnnotation(ResponseView.class);
        if (responseView == null) {
            return wrapBody(body);
        }
        Class<?> toClass = responseView.value();
        Class<?> fromClass = returnType.getMethod().getReturnType();
        try {
            if (body instanceof List) {
                fromClass = TypeToken
                        .of(fromClass)
                        .resolveType(fromClass.getTypeParameters()[0])
                        .getRawType();
                Function<Object, Object> handler = converterTable.get(fromClass, toClass);
                if (handler != null) {
                    modifyBody((List) body, handler);
                }
                return wrapBody(body);
            } else {
                Function<Object, Object> handler = converterTable.get(fromClass, toClass);
                if (handler != null) {
                    return wrapBody(handler.apply(body));
                }
            }
        } catch (Exception e) {
            Exceptions.catching(e);
        }
        return wrapBody(body);
    }

    private Object wrapBody(Object body) {
        if (body instanceof List) {
            if (wrapList || body instanceof PageList) {
                return PaginatorResponse.of((List) body);
            }
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
