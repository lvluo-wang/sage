package me.icymint.sage.base.spec.annotation;

import me.icymint.sage.base.spec.def.Magics;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by daniel on 2016/10/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(name = Magics.PROP_ENABLE_JOB, havingValue = "true")
public @interface ConditionalOnJob {
}
