package me.icymint.sage.base.core.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.google.common.collect.Lists;
import me.icymint.sage.base.spec.annotation.NotifyEvent;
import me.icymint.sage.base.spec.annotation.NotifyInTransactionEvent;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.EventHandler;
import me.icymint.sage.base.spec.api.EventProducer;
import me.icymint.sage.base.spec.api.EventTransferHandler;
import me.icymint.sage.base.spec.api.RuntimeContext;
import me.icymint.sage.base.spec.entity.BaseEvent;
import me.icymint.sage.base.spec.exception.Exceptions;
import me.icymint.sage.base.spec.internal.api.BatchJob;
import me.icymint.sage.base.spec.internal.api.EventRepository;
import me.icymint.sage.user.spec.def.EventStatus;
import me.icymint.sage.user.spec.entity.Event;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static me.icymint.sage.base.core.util.Jsons.fromJson;
import static me.icymint.sage.base.core.util.Jsons.toJson;

/**
 * Created by daniel on 2016/9/23.
 */
@Aspect
@Service
public class EventServiceImpl implements BatchJob<Event> {
    private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private final List<HandlerContainer> eventHandlerList = Lists.newArrayList();
    @Autowired(required = false)
    EventTransferHandler[] eventTransferHandlers;
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

    @PostConstruct
    protected void init() {
        if (eventTransferHandlers != null) {
            Stream.of(eventTransferHandlers).forEach(this::addHandler);
        }
        if (eventHandlers != null) {
            Stream.of(eventHandlers).forEach(this::addHandler);
        }

    }

    @SuppressWarnings("unchecked")
    private void addHandler(EventHandler eventHandler) {
        EventTransferHandler transferHandler = event -> {
            eventHandler.accept(event);
            return null;
        };
        addHandler(transferHandler);
    }

    @SuppressWarnings("unchecked")
    private void addHandler(EventTransferHandler handler) {
        Method handlerMethod = null;
        for (Method method : AopUtils.getTargetClass(handler).getMethods()) {
            if (method.getName().equals("handler") && method.getParameterTypes().length == 1
                    && method.getParameterTypes()[0].isAssignableFrom(BaseEvent.class)) {
                handlerMethod = method;
                break;
            }
        }
        if (handlerMethod == null) {
            return;
        }
        Class<? extends BaseEvent> eventClass = (Class<? extends BaseEvent>) handlerMethod.getParameterTypes()[0];
        eventHandlerList.add(new HandlerContainer(handler, eventClass));
    }

    @Transactional
    public <E extends BaseEvent> void post(E event) {
        if (event == null) {
            return;
        }
        event.setEventId(UUID.randomUUID().toString());
        if (event.getAsync() != null && !event.getAsync()) {
            executeEventHandlers(event);
        } else {
            logger.info("Saving async event {}", event);
            eventRepository.save(toDbEvent(event));
        }
    }

    private void executeEventHandlers(BaseEvent event) {
        logger.info("Do handle event {}", event);
        eventHandlerList
                .stream()
                .filter(handlerContainer -> handlerContainer.eventClass.isInstance(event))
                .forEach(handler -> doExecuteEvent(handler.handler, event));
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
        executeEventHandlers(fromDbEvent(dbEvent));
    }

    private <E extends BaseEvent<E>, R extends BaseEvent<R>> void doExecuteEvent(EventTransferHandler<E, R> handler, E event) {
        R result = handler.apply(event);
        if (result == null) {
            return;
        }
        result.setParentEventId(event.getEventId());
        if (result.getAsync() == null) {
            result.setAsync(false);
        }
        result.setClientId(event.getClientId());
        result.setCorrelationId(event.getCorrelationId());
        result.setIpAddress(event.getIpAddress());
        result.setSessionId(event.getSessionId());
        eventService.post(result);
    }

    @Order(Ordered.LOWEST_PRECEDENCE - 10)
    @AfterReturning(pointcut = "@annotation(notifyEvent)", returning = "result")
    public void async(JoinPoint joinPoint, NotifyEvent notifyEvent, Object result) {
        sendEventByAop(notifyEvent.eventProducerClass(), joinPoint, result, false);
    }

    @Order(Ordered.LOWEST_PRECEDENCE)
    @AfterReturning(pointcut = "@annotation(notifyInTransactionEvent)", returning = "result")
    public void sync(JoinPoint joinPoint, NotifyInTransactionEvent notifyInTransactionEvent, Object result) {
        sendEventByAop(notifyInTransactionEvent.eventProducerClass(), joinPoint, result, true);
    }


    private void sendEventByAop(Class<? extends EventProducer<?, ? extends BaseEvent<?>>> clazz, JoinPoint joinPoint, Object result, boolean isIntransaction) {
        try {
            EventProducer<?, ? extends BaseEvent<?>> producer = clazz.newInstance();
            if (!doSendEventByAop(producer, result, isIntransaction)) {
                logger.error("Notify event {} at {} missing", clazz, joinPoint.getKind());
            }
        } catch (Exception e) {
            logger.error("Send event fatal");
            Exceptions.catching(e);
        }
    }


    private <O, E extends BaseEvent<E>> boolean doSendEventByAop(EventProducer<O, E> producer, Object result, boolean isInTransaction) {
        if (producer.resultClass().isInstance(result)) {
            try {
                E event = producer.apply(producer.resultClass().cast(result));
                if (event != null) {
                    if (event.getAsync() == null) {
                        event.setAsync(true);
                    }
                    if (!event.getAsync() &&
                            (isInTransaction || TransactionSynchronizationManager.isActualTransactionActive())) {
                        logger.warn("Post sync event {} in transaction is not recommended", event);
                    }
                    if (event.getClientId() != null) {
                        event.setClientId(runtimeContext.getClientId());
                    }
                    if (event.getCorrelationId() != null) {
                        event.setCorrelationId(runtimeContext.getCorrelationId());
                    }
                    if (event.getIpAddress() != null) {
                        event.setIpAddress(runtimeContext.getUserAddress());
                    }
                    if (event.getSessionId() != null) {
                        event.setSessionId(runtimeContext.getSessionId());
                    }
                    eventService.post(event);
                }
            } catch (Exception e) {
                Exceptions.catching(e);
            }
            return true;
        }
        return false;
    }

    private class HandlerContainer<E extends BaseEvent<E>> {
        private final Class<E> eventClass;
        private final EventTransferHandler<E, ?> handler;

        HandlerContainer(EventTransferHandler<E, ?> handler, Class<E> eventClass) {
            this.handler = handler;
            this.eventClass = eventClass;
        }
    }

}
