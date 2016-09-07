package me.icymint.sage.base.rest.support;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by daniel on 16/9/3.
 */
@Component
public class JacksonI18nModule extends SimpleModule {
    @Autowired
    ApplicationContext context;

    @PostConstruct
    public void init() {
        this.setSerializerModifier(new JacksonI18nEnumSerializerModifier(context));
        this.addSerializer(PageList.class, new JacksonPageListSerializer());
    }
}
