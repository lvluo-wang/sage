package me.icymint.sage.base.rest.support;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import me.icymint.sage.base.spec.annotation.EntityConverter;
import me.icymint.sage.base.spec.api.EntityConverterInterface;
import me.icymint.sage.base.util.Exceptions;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.primitives.Primitives.wrap;
import static me.icymint.sage.base.util.Classes.isPojoClass;

/**
 * Created by daniel on 2016/10/2.
 */
@Component
public class EntityConverters {

    private final Table<Class<?>, Class<?>, Function<Object, Object>> converterTable = HashBasedTable.create();
    @Autowired(required = false)
    EntityConverterInterface[] interfaces;
    @Autowired
    ApplicationContext context;


    @PostConstruct
    public void init() {
        Set<Class<?>> set = Sets.newHashSet();
        for (Object obj : interfaces) {
            Class<?> objClz = obj.getClass();
            if (objClz != null && objClz.getInterfaces() != null) {
                for (Class<?> objInt : objClz.getInterfaces()) {
                    if (set.contains(objInt)
                            || !objInt.getPackage().getName().startsWith("me.icymint.sage")) {
                        continue;
                    }
                    set.add(objInt);
                    for (Method method : objInt.getMethods()) {
                        if (method.isAnnotationPresent(EntityConverter.class)) {
                            if (method.getParameterCount() != 1) {
                                throw new BeanCreationException("Unexception");
                            }
                            Class<?> fromClass = wrap(method.getParameterTypes()[0]);
                            if (!isPojoClass(fromClass)) {
                                throw new BeanCreationException("Unexception");
                            }
                            Class<?> toClass = wrap(method.getReturnType());
                            Function<Object, Object> handler = from -> {
                                try {
                                    return method.invoke(obj, from);
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

    public Set<Table.Cell<Class<?>, Class<?>, Function<Object, Object>>> getConverterSets() {
        return converterTable.cellSet();
    }
}
