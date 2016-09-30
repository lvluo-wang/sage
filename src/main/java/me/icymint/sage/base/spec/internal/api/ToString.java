package me.icymint.sage.base.spec.internal.api;

/**
 * Created by daniel on 2016/9/30.
 */
public interface ToString<T> {

    Class<T> supportObject();

    String toString(T object);
}
