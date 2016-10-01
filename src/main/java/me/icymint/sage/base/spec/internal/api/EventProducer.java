package me.icymint.sage.base.spec.internal.api;

import me.icymint.sage.base.spec.entity.BaseEvent;

import java.util.function.Function;

/**
 * Created by daniel on 2016/9/23.
 */
public interface EventProducer<O, E extends BaseEvent<E>> extends Function<O, E> {
    Class<O> resultClass();
}
