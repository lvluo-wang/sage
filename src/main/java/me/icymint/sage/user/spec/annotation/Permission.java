package me.icymint.sage.user.spec.annotation;

import me.icymint.sage.user.rest.support.PermissionCondition;
import me.icymint.sage.user.spec.def.PermissionStrategy;
import me.icymint.sage.user.spec.def.Privilege;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by daniel on 2016/10/4.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(PermissionCondition.class)
public @interface Permission {

    Privilege[] value() default {};

    PermissionStrategy strategy() default PermissionStrategy.MATCH_ANY;
}
