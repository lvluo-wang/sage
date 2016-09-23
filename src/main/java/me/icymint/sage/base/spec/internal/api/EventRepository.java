package me.icymint.sage.base.spec.internal.api;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.google.common.collect.Lists;
import me.icymint.sage.user.spec.entity.Event;

import java.util.List;

/**
 * Created by daniel on 2016/9/23.
 */
public interface EventRepository {

    boolean allowAsync();

    default void save(Event event) {
    }

    default Event findByEventId(String eventId) {
        return null;
    }

    default List<Event> findAllWithNewCreated(PageBounds pageBounds) {
        return Lists.newArrayList();
    }

    default int update(Event event) {
        return 0;
    }

    default Event findOneForUpdate(Long id) {
        return null;
    }
}
