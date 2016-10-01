package me.icymint.sage.base.core.config;

import com.google.common.collect.Lists;
import me.icymint.sage.base.core.service.DefaultSessionService;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.SessionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

/**
 * Created by daniel on 16/9/7.
 */
@Configuration
public class BaseCoreConfig {
    @Bean
    @ConditionalOnMissingBean
    public SessionService sessionService() {
        return new DefaultSessionService();
    }

    @Bean
    @ConditionalOnMissingBean
    public Clock clock() {
        return Instant::now;
    }

    @Bean
    @Scope("prototype")
    public RestTemplate restTemplate(ClientHttpRequestInterceptor[] clientHttpRequestInterceptors) {
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        restTemplate.setInterceptors(Lists.newArrayList(clientHttpRequestInterceptors));
        return restTemplate;
    }
}
