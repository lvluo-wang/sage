package me.icymint.sage.user.spec.annotation;

import me.icymint.sage.user.spec.def.PermissionStrategy;
import me.icymint.sage.user.spec.def.Privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by daniel on 2016/10/4.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    Privilege[] value() default {};

    PermissionStrategy strategy() default PermissionStrategy.MATCH_ANY;
}
