package me.icymint.sage.base.rest.config;

import me.icymint.sage.base.rest.support.RuntimeContextHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by daniel on 16/9/3.
 */
@Component
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    RuntimeContextHandlerInterceptor runtimeContextHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(runtimeContextHandlerInterceptor);
    }
}
