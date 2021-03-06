package me.icymint.sage.base.spec.annotation;

import me.icymint.sage.base.spec.entity.BaseEvent;
import me.icymint.sage.base.spec.internal.api.EventProducer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by daniel on 2016/9/30.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotifyEvent {
    Class<? extends EventProducer<?, ? extends BaseEvent<?>>> eventProducerClass();
}
