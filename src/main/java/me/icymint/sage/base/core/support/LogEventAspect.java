package me.icymint.sage.base.core.support;

import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;
import me.icymint.sage.base.core.service.EventServiceImpl;
import me.icymint.sage.base.spec.annotation.LogMethodInvoke;
import me.icymint.sage.base.spec.internal.api.ToString;
import me.icymint.sage.base.spec.internal.entity.LogEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by daniel on 2016/9/30.
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class LogEventAspect {
    private final Map<Class<?>, ToString<?>> toStringMap = Maps.newHashMap();
    @Autowired
    EventServiceImpl eventService;
    @Autowired(required = false)
    ToString[] toStrings;

    @PostConstruct
    protected void init() {
        if (toStrings != null) {
            Stream.of(toStrings).forEach(toString -> toStringMap.put(toString.supportObject(), toString));
        }
    }

    @AfterReturning(pointcut = "@annotation(logMethodInvoke)", returning = "result")
    public void async(JoinPoint joinPoint, LogMethodInvoke logMethodInvoke, Object result) {
        Map<String, Object> logMap = Maps.newTreeMap();
        logMap.put("return", toString(result));
        logMap.put("signature", joinPoint.getSignature().toShortString());
        if (joinPoint.getArgs() != null) {
            int i = 1;
            for (Object arg : joinPoint.getArgs()) {
                logMap.put("arg-" + (i++), toString(arg));
            }
        }
        eventService.post(new LogEvent().setLogMap(logMap));
    }

    private String toString(Object arg) {
        if (arg == null) {
            return "null";
        }
        if (arg.getClass().isPrimitive()
                || Primitives.isWrapperType(arg.getClass())
                || arg.getClass() == String.class) {
            return toPrefix(arg.getClass()) + String.valueOf(arg);
        }
        Class<?> argType = arg.getClass();
        ToString<?> toString = null;
        while (argType != null && (toString = toStringMap.get(argType)) == null) {
            argType = argType.getSuperclass();
        }
        if (toString != null) {
            return toPrefix(arg.getClass()) + doIt(toString, arg);
        }
        return toPrefix(arg.getClass()) + "UnknownObject";
    }

    private String toPrefix(Class<?> aClass) {
        return "<" + aClass.getSimpleName() + ">";
    }

    private <E> String doIt(ToString<E> toString, Object arg) {
        return toString.toString(toString.supportObject().cast(arg));
    }
}
