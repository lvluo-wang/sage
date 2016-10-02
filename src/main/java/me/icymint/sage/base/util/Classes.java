package me.icymint.sage.base.util;

import com.google.common.primitives.Primitives;

/**
 * Created by daniel on 2016/10/2.
 */
public class Classes {
    public static boolean isValueClass(Class<?> clazz) {
        return clazz.isPrimitive()
                || Primitives.isWrapperType(clazz)
                || clazz == String.class;
    }
}
