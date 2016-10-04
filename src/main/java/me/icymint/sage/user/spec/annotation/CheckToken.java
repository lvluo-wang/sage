package me.icymint.sage.user.spec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by daniel on 16/9/4.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckToken {

    /**
     * Allow no token sign header
     *
     * @return
     */
    boolean allowNone() default false;
}
