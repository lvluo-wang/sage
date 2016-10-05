package me.icymint.sage.base.data.config;

import com.github.miemiedev.mybatis.paginator.CleanupMybatisPaginatorListener;
import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor;
import com.github.miemiedev.mybatis.paginator.dialect.PostgreSQLDialect;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import me.icymint.sage.base.spec.entity.Pageable;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by daniel on 16/9/3.
 */
@Configuration
public class MybatisInterceptorConfig {

    private final Logger logger = LoggerFactory.getLogger(MybatisInterceptorConfig.class);

    @Bean
    public PageableInterceptor pageableInterceptor() {
        PageableInterceptor pageableInterceptor = new PageableInterceptor();
        pageableInterceptor.setDialectClass(PostgreSQLDialect.class.getName());
        return pageableInterceptor;
    }

    @Bean
    CleanupMybatisPaginatorListener cleanupMybatisPaginatorListener() {
        return new CleanupMybatisPaginatorListener();
    }


    @Intercepts({@Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
    public static class PageableInterceptor extends OffsetLimitInterceptor {

        @SuppressWarnings("unchecked")
        public Object intercept(final Invocation invocation) throws Throwable {
            final RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
            Object object = super.intercept(invocation);
            if (rowBounds instanceof Pageable
                    && !(object instanceof PageList)
                    && (object instanceof List)) {
                Pageable pageable = (Pageable) rowBounds;
                Paginator paginator = new Paginator(pageable.getPage(),
                        pageable.getLimit(),
                        pageable.getTotalCount());
                return new PageList((List) object, paginator);
            }
            return object;
        }

    }
}
