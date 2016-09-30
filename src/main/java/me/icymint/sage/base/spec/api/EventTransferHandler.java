package me.icymint.sage.base.spec.api;

import me.icymint.sage.base.spec.entity.BaseEvent;

import java.util.function.Function;

/**
 * Created by daniel on 2016/9/23.
 */
@FunctionalInterface
public interface EventTransferHandler<E extends BaseEvent<E>, R extends BaseEvent<R>> extends Function<E, R> {
}
