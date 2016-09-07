package me.icymint.sage.base.core.config;

import me.icymint.sage.base.core.service.DefaultSessionService;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.SessionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
