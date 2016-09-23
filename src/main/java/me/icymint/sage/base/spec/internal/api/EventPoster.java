package me.icymint.sage.base.spec.internal.api;

import me.icymint.sage.base.spec.api.EventService;

/**
 * Created by daniel on 2016/9/23.
 */
public interface EventPoster<E> {

    void post(EventService eventService, Object[] args, E result);
}
