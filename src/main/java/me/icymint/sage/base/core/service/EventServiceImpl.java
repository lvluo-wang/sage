package me.icymint.sage.base.core.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.google.common.collect.Lists;
import me.icymint.sage.base.spec.api.AsyncEventHandler;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.EventHandler;
import me.icymint.sage.base.spec.api.EventService;
import me.icymint.sage.base.spec.api.RuntimeContext;
import me.icymint.sage.base.spec.entity.BaseEvent;
import me.icymint.sage.base.spec.exception.Exceptions;
import me.icymint.sage.base.spec.internal.api.BatchJob;
import me.icymint.sage.base.spec.internal.api.EventRepository;
import me.icymint.sage.user.spec.def.EventStatus;
import me.icymint.sage.user.spec.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import static me.icymint.sage.base.core.util.Jsons.fromJson;
import static me.icymint.sage.base.core.util.Jsons.toJson;

/**
 * Created by daniel on 2016/9/23.
 */
@Service
public class EventServiceImpl implements EventService, BatchJob<Event> {
    private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private final List<HandlerContainer> asyncEventHandlerList = Lists.newArrayList();
    private final List<HandlerContainer> eventHandlerList = Lists.newArrayList();
    @Autowired(required = false)
    EventHandler[] eventHandlers;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventServiceImpl eventService;
    @Autowired
    Clock clock;
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    Environment environment;

    @SuppressWarnings("unchecked")
    @PostConstruct
    protected void init() {
        if (eventHandlers != null) {
            for (EventHandler handler : eventHandlers) {
                Method handlerMethod = null;
                for (Method method : AopUtils.getTargetClass(handler).getMethods()) {
                    if (method.getName().equals("handler") && method.getParameterTypes().length == 1
                            && method.getParameterTypes()[0].isAssignableFrom(BaseEvent.class)) {
                        handlerMethod = method;
                        break;
                    }
                }
                if (handlerMethod == null) {
                    continue;
                }
                Class<? extends BaseEvent> eventClass = (Class<? extends BaseEvent>) handlerMethod.getParameterTypes()[0];
                if (eventRepository.allowAsync() && handler instanceof AsyncEventHandler) {
                    asyncEventHandlerList.add(new HandlerContainer(handler, handlerMethod, eventClass));
                } else {
                    eventHandlerList.add(new HandlerContainer(handler, handlerMethod, eventClass));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public <E extends BaseEvent> void post(E event) {
        if (event == null) {
            return;
        }
        event.setEventId(UUID.randomUUID().toString());
        if (environment.getProperty("sage.always.save.event", Boolean.class, true)
                || !asyncEventHandlerList.isEmpty()) {
            logger.info("Saving async event {}", event);
            eventRepository.save(toDbEvent(event));
        } else {
            logger.warn("Async Event {} ignored", event);
        }
        eventHandlerList
                .stream()
                .filter(handlerContainer -> handlerContainer.eventClass.isInstance(event))
                .forEach(handler -> handler.handler.handle(event));
    }

    private <E extends BaseEvent> Event toDbEvent(E event) {
        Event dbEvent = new Event()
                .setStatus(EventStatus.CREATED)
                .setBody(toJson(event))
                .setAsyncEventType(event.getClass().getName());
        BeanUtils.copyProperties(event, dbEvent);
        return dbEvent;
    }

    @SuppressWarnings("unchecked")
    private <E extends BaseEvent> E fromDbEvent(Event dbEvent) {
        try {
            Class<E> clazz = (Class<E>) Class.forName(dbEvent.getAsyncEventType());
            E event = fromJson(dbEvent.getBody(), clazz);
            BeanUtils.copyProperties(dbEvent, event);
            return event;
        } catch (ClassNotFoundException e) {
            throw Exceptions.wrap(e);
        }
    }

    @Override
    public List<Event> getRecords(PageBounds pageBounds) {
        return eventRepository.findAllWithNewCreated(pageBounds);
    }

    @Override
    @Transactional
    public void updateNextScanTime(Long id) {
        eventRepository.update(new Event()
                .setNextScanTime(clock.now().plusSeconds(10 * 60L))
                .setId(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void handle(Long record) throws Exception {
        Event dbEvent = eventRepository.findOneForUpdate(record);
        if (dbEvent.getStatus() != EventStatus.CREATED) {
            logger.warn("event {} status error", dbEvent);
            return;
        }
        eventRepository.update(new Event()
                .setStatus(EventStatus.PROCESSED)
                .setId(record));
        BaseEvent event = fromDbEvent(dbEvent);
        logger.info("Do handle event {}", event);
        asyncEventHandlerList
                .stream()
                .filter(handler -> handler.eventClass.isInstance(event))
                .forEach(handler -> handler.handler.handle(event));
    }

    private class HandlerContainer {
        private final Class<? extends BaseEvent> eventClass;
        private final EventHandler handler;
        private final Method method;

        public HandlerContainer(EventHandler handler, Method method, Class<? extends BaseEvent> eventClass) {
            this.handler = handler;
            this.method = method;
            this.eventClass = eventClass;
        }
    }

}
