package me.icymint.sage.base.util;

import com.google.common.primitives.Primitives;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * Created by daniel on 2016/10/2.
 */
public class Classes {
    public static boolean isValueClass(Class<?> clazz) {
        return clazz.isPrimitive()
                || Primitives.isWrapperType(clazz)
                || clazz == String.class;
    }

    public static boolean hasArg(Method method, Class<?> aClass) {
        if (method.getParameterCount() > 0) {
            return Stream
                    .of(method.getParameterTypes())
                    .anyMatch(aClass::isAssignableFrom);
        }
        return false;
    }
}
