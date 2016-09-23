package me.icymint.sage.base.core.service;

import me.icymint.sage.base.spec.internal.api.EventRepository;

/**
 * Created by daniel on 2016/9/23.
 */
public class DefaultEventRepository implements EventRepository {

    @Override
    public boolean allowAsync() {
        return false;
    }

}
