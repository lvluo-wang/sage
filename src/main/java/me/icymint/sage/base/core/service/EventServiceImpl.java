package me.icymint.sage.base.core.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.google.common.collect.Lists;
import me.icymint.sage.base.spec.api.AsyncEventHandler;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.EventHandler;
import me.icymint.sage.base.spec.api.EventService;
import me.icymint.sage.base.spec.entity.BaseEvent;
import me.icymint.sage.base.spec.exception.Exceptions;
import me.icymint.sage.base.spec.internal.api.EventRepository;
import me.icymint.sage.user.spec.def.EventStatus;
import me.icymint.sage.user.spec.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static me.icymint.sage.base.core.util.Jsons.fromJson;
import static me.icymint.sage.base.core.util.Jsons.toJson;

/**
 * Created by daniel on 2016/9/23.
 */
@Service
public class EventServiceImpl implements EventService {
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
        if (eventHandlers == null || eventHandlers.length == 0) {
            logger.warn("Event {} ignored", event);
        }
        event.setEventId(UUID.randomUUID().toString());
        logger.info("Handle event {}", event);
        eventHandlerList
                .stream()
                .filter(handlerContainer -> handlerContainer.eventClass.isInstance(event))
                .forEach(handler -> handler.handler.handle(event));
        Event dbEvent = toDbEvent(event);
        logger.info("Handle event {}", dbEvent);
        eventRepository.save(dbEvent);
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

    @Scheduled(fixedRate = 60L)
    public void executeEvents() {
        while (singleRoundExecuteEvents(new PageBounds(0, 100))) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    private boolean singleRoundExecuteEvents(PageBounds pageBounds) {
        eventRepository.findAllWithNewCreated(pageBounds).forEach(event -> {
            logger.info("Handle event {}", event);
            try {
                eventRepository.update((Event) new Event()
                        .setNextScanTime(clock.now().plusSeconds(10 * 60L))
                        .setId(event.getId()));
                eventService.handleEvent(fromDbEvent(event));
            } catch (Exception e) {
                Exceptions.catching(e);
            }
        });
        return false;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public <E extends BaseEvent> void handleEvent(E event) throws IllegalAccessException, InstantiationException {
        eventRepository.findOneForUpdate(event.getId());
        asyncEventHandlerList
                .stream()
                .filter(handler -> handler.eventClass.isInstance(event))
                .forEach(handler -> handler.handler.handle(event));
        Event updateDbEvent = new Event().setStatus(EventStatus.PROCESSED);
        updateDbEvent.setId(event.getId());
        eventRepository.update(updateDbEvent);
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
