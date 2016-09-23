package me.icymint.sage.base.spec.api;

import me.icymint.sage.base.spec.entity.BaseEvent;

/**
 * Created by daniel on 2016/9/23.
 */
public interface EventService {
    <E extends BaseEvent> void post(E event);
}
