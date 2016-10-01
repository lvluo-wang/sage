package me.icymint.sage.base.spec.internal.api;

import me.icymint.sage.base.spec.entity.BaseEvent;

import java.util.function.Consumer;

/**
 * Created by daniel on 2016/9/23.
 */
@FunctionalInterface
public interface EventHandler<E extends BaseEvent> extends Consumer<E> {
}
