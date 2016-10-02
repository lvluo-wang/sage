package me.icymint.sage.base.rest.support;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.icymint.sage.base.spec.annotation.ResponseConverter;
import me.icymint.sage.base.util.Exceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.primitives.Primitives.wrap;
import static me.icymint.sage.base.util.Classes.isValueClass;

/**
 * Created by daniel on 2016/10/2.
 */
@Component
public class RestConverter {

    @Autowired(required = false)
    ResponseConverter[] converters;

    private final Table<Class<?>, Class<?>, Function<Object, Object>> converterTable = HashBasedTable.create();

    @PostConstruct
    protected void init() {
        if (converters != null) {
            for (ResponseConverter converter : converters) {
                for (Method method : converter.getClass().getMethods()) {
                    if (method.getParameterCount() == 1) {
                        Class<?> fromClass = wrap(method.getParameterTypes()[0]);
                        if (!isValueClass(fromClass) && fromClass != Object.class) {
                            Class<?> toClass = wrap(method.getReturnType());
                            Function<Object, Object> handler = from -> {
                                try {
                                    return method.invoke(converter, from);
                                } catch (Throwable e) {
                                    throw Exceptions.wrap(e);
                                }
                            };
                            Assert.isNull(converterTable.put(fromClass, toClass, handler),
                                    "Converter from " + method.getParameterTypes()[0]
                                            + " to " + method.getReturnType()
                                            + " has already registered");
                        }
                    }
                }
            }
        }
    }

    public Function<Object, Object> getConverter(Class<?> fromClass, Class<?> toClass) {
        return converterTable.get(fromClass, toClass);
    }

    public Set<Table.Cell<Class<?>, Class<?>, Function<Object, Object>>> getConverterCell() {
        return converterTable.cellSet();
    }
}
