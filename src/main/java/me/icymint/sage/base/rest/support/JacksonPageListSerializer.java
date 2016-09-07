package me.icymint.sage.base.rest.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import me.icymint.sage.base.rest.entity.PaginatorResponse;

import java.io.IOException;

/**
 * Created by daniel on 16/9/4.
 */
@SuppressWarnings("unchecked")
public class JacksonPageListSerializer extends JsonSerializer<PageList> {
    @Override
    public void serialize(PageList value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeObject(PaginatorResponse.of(value));
    }
}
