package me.icymint.sage.base.rest.config;

import me.icymint.sage.base.rest.support.PageBoundsArgumentResolver;
import me.icymint.sage.base.rest.support.RuntimeContextHandlerInterceptor;
import me.icymint.sage.base.spec.def.Magics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by daniel on 16/9/3.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Value("${" + Magics.PROP_DEV_MODE + "}")
    boolean isDev;

    @Autowired
    RuntimeContextHandlerInterceptor runtimeContextHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(runtimeContextHandlerInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageBoundsArgumentResolver());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (isDev) {
            registry.addMapping("/**").allowedOrigins("http://localhost:8080").allowCredentials(true);
        }
    }
}
