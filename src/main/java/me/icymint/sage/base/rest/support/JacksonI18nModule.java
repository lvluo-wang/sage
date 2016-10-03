package me.icymint.sage.base.rest.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.function.Function;

/**
 * Created by daniel on 16/9/3.
 */
@Component
public class JacksonI18nModule extends SimpleModule {
    @Autowired
    ApplicationContext context;
    @Autowired
    EntityConverters converter;

    @PostConstruct
    public void init() {
        this.setSerializerModifier(new JacksonI18nEnumSerializerModifier(context));
        SimpleSerializers serializers = new SimpleSerializers();
        converter.getConverterSets().forEach(cell -> {
            Function<Object, Object> handler = cell.getValue();
            if (handler == null) {
                return;
            }
            serializers
                    .addSerializer(cell.getRowKey(), new JsonSerializer<Object>() {
                        @Override
                        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                            gen.writeObject(handler.apply(value));
                        }
                    });
        });
        this.setSerializers(serializers);
    }
}
