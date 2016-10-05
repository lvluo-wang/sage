package me.icymint.sage.base.rest.aspect;

import me.icymint.sage.base.rest.entity.PageableResponse;
import me.icymint.sage.base.spec.annotation.PageableView;
import org.apache.ibatis.session.RowBounds;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

import static me.icymint.sage.base.util.Classes.hasArg;

/**
 * Created by daniel on 2016/10/2.
 */
@ControllerAdvice
@SuppressWarnings("unchecked")
public class DefaultResourceHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return List.class.isAssignableFrom(returnType.getMethod().getReturnType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return null;
        }

        if (returnType.hasMethodAnnotation(PageableView.class)
                || hasArg(returnType.getMethod(), RowBounds.class)) {
            return PageableResponse.of((List) body);
        }
        return body;
    }
}
