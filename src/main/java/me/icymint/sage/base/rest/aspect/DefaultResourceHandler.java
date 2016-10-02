package me.icymint.sage.base.rest.aspect;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Lists;
import me.icymint.sage.base.rest.entity.PaginatorResponse;
import me.icymint.sage.base.spec.annotation.PaginatorView;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

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
            body = Lists.newArrayList();
        }
        if (returnType.hasMethodAnnotation(PaginatorView.class)
                || body instanceof PageList) {
            return PaginatorResponse.of((List) body);
        }
        return body;
    }
}
