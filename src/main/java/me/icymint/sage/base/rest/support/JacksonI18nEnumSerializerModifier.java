package me.icymint.sage.base.rest.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import me.icymint.sage.base.spec.annotation.ToLabel;
import me.icymint.sage.base.spec.def.EnumMode;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static me.icymint.sage.base.util.Enums.findLabelMethod;
import static me.icymint.sage.base.util.Enums.getI18nKeyPrefix;

/**
 * Created by daniel on 16/9/3.
 */

public class JacksonI18nEnumSerializerModifier extends BeanSerializerModifier {
    private final Map<Class<Enum<?>>, StdScalarSerializer> expendedEnumMap = new ConcurrentHashMap<>();
    private final Map<Class<Enum<?>>, StdScalarSerializer> notExpendedEnumMap = new ConcurrentHashMap<>();
    private final MessageSource messageSource;
    private final RuntimeContext runtimeContext;

    public JacksonI18nEnumSerializerModifier(RuntimeContext runtimeContext, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.runtimeContext = runtimeContext;
    }

    @SuppressWarnings("unchecked")
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        for (BeanPropertyWriter propertyWriter : beanProperties) {
            ToLabel toLabel = propertyWriter.getAnnotation(ToLabel.class);
            if (toLabel != null
                    && (propertyWriter.getType().getRawClass().isAssignableFrom(Enum.class)
                    || propertyWriter.getType().getRawClass().isEnum())) {
                Class<Enum<?>> enumClass = (Class<Enum<?>>) propertyWriter.getType().getRawClass();
                if (enumClass.getTypeName().equals(Enum.class.getName())) {
                    Type type = beanDesc.getBeanClass().getGenericSuperclass();
                    if (type instanceof ParameterizedType) {
                        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                        if (types != null
                                && types.length > 0
                                && types[0] instanceof Class
                                && ((Class) types[0]).isEnum()) {
                            enumClass = (Class<Enum<?>>) types[0];
                        }
                    }
                }
                StdScalarSerializer cer = (toLabel.expend()
                        ? expendedEnumMap
                        : notExpendedEnumMap)
                        .computeIfAbsent(enumClass, clazz -> createEnumSerializer(toLabel, clazz));
                propertyWriter.assignSerializer(cer);
            }
        }
        return beanProperties;
    }


    private StdScalarSerializer createEnumSerializer(final ToLabel toLabel, Class<Enum<?>> enumClass) {
        final Method labelMethod = findLabelMethod(enumClass);
        final String prefix = getI18nKeyPrefix(enumClass);
        return new StdScalarSerializer<Enum<?>>(enumClass, false) {
            @Override
            public void serialize(Enum<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                EnumMode mode = runtimeContext.getEnumMode();
                if (mode == EnumMode.NAME_ONLY && toLabel.expend()) {
                    gen.writeString(value.name());
                    return;
                }
                String label = getLabel(labelMethod, prefix, value);
                if (mode == EnumMode.LABEL_ONLY || !toLabel.expend()) {
                    gen.writeString(label);
                } else {
                    gen.writeStartObject();
                    gen.writeStringField("name", value.name());
                    gen.writeStringField("label", label);
                    gen.writeEndObject();
                }
            }

            private String getLabel(Method labelMethod, String prefix, Enum<?> value) {
                if (labelMethod != null) {
                    try {
                        String label = (String) labelMethod.invoke(value);
                        if (label != null) {
                            return label;
                        }
                    } catch (Exception e) {
                        //Ignore error
                    }
                }
                return messageSource.getMessage(prefix + value.name(), null, value.name(), LocaleContextHolder.getLocale());
            }
        };
    }
}
