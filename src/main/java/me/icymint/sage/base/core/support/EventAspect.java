package me.icymint.sage.base.core.support;

import me.icymint.sage.base.core.service.EventServiceImpl;
import me.icymint.sage.base.spec.annotation.NotifyEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by daniel on 2016/9/30.
 */
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 2)
public class EventAspect {
    @Autowired
    EventServiceImpl eventService;

    @AfterReturning(pointcut = "@annotation(notifyEvent)", returning = "result")
    public void async(JoinPoint joinPoint, NotifyEvent notifyEvent, Object result) {
        eventService.sendEventByAop(notifyEvent.eventProducerClass(), joinPoint, result, false);
    }
}
