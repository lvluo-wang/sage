package me.icymint.sage.base.data.config;

import com.github.miemiedev.mybatis.paginator.CleanupMybatisPaginatorListener;
import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor;
import com.github.miemiedev.mybatis.paginator.dialect.PostgreSQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by daniel on 16/9/3.
 */
@Configuration
public class MybatisInterceptorConfig {

    private final Logger logger = LoggerFactory.getLogger(MybatisInterceptorConfig.class);

    @Bean
    public OffsetLimitInterceptor offsetLimitInterceptor() {
        OffsetLimitInterceptor offsetLimitInterceptor = new OffsetLimitInterceptor();
        offsetLimitInterceptor.setDialectClass(PostgreSQLDialect.class.getName());
        return offsetLimitInterceptor;
    }

    @Bean
    CleanupMybatisPaginatorListener cleanupMybatisPaginatorListener() {
        return new CleanupMybatisPaginatorListener();
    }
}
