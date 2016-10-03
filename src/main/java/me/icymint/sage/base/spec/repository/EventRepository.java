package me.icymint.sage.base.spec.repository;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.google.common.collect.Lists;
import me.icymint.sage.user.spec.entity.Event;

import java.util.List;

/**
 * Created by daniel on 2016/9/23.
 */
public interface EventRepository {

    default void create(Event event) {
    }

    default List<Event> findAllWithNewCreated(PageBounds pageBounds) {
        return Lists.newArrayList();
    }

    default void update(Event event) {
    }

    default Event findOneForUpdate(Long id) {
        return null;
    }
}
