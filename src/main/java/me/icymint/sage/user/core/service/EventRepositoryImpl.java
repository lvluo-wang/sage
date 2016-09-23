package me.icymint.sage.user.core.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.internal.api.EventRepository;
import me.icymint.sage.user.data.mapper.EventMapper;
import me.icymint.sage.user.spec.def.UserExceptionCode;
import me.icymint.sage.user.spec.entity.Event;
import me.icymint.sage.user.spec.exception.UserServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by daniel on 2016/9/23.
 */
@Repository
public class EventRepositoryImpl implements EventRepository {
    private final Logger logger = LoggerFactory.getLogger(EventRepositoryImpl.class);
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    EventMapper eventMapper;
    @Autowired
    ApplicationContext context;
    @Autowired
    Clock clock;


    @Override
    public boolean allowAsync() {
        return true;
    }

    @Override
    @Transactional
    public void save(Event event) {
        if (eventMapper.save(event) != 1) {
            throw new UserServiceException(context, UserExceptionCode.EVENT__SAVE_FAILED, event.getEventId());
        }
    }

    @Override
    public Event findByEventId(String eventId) {
        return eventMapper.findByEventId(eventId);
    }

    @Override
    public List<Event> findAllWithNewCreated(PageBounds pageBounds) {
        return eventMapper.findAllWithNewCreated(pageBounds);
    }

    @Override
    public int update(Event event) {
        return eventMapper.update(event);
    }

    @Override
    @Transactional
    public Event findOneForUpdate(Long id) {
        return eventMapper.findOneForUpdate(id);
    }
}
