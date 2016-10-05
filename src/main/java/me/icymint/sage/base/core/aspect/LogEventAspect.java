package me.icymint.sage.base.core.aspect;

import com.google.common.collect.Lists;
import me.icymint.sage.base.core.service.EventServiceImpl;
import me.icymint.sage.base.spec.annotation.LogInvokeMethod;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.base.spec.internal.entity.LogEvent;
import me.icymint.sage.base.util.Exceptions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

/**
 * Created by daniel on 2016/9/30.
 */
@Aspect
@Component
@Order(Magics.AOP_ORDER_EVENT_LOG)
public class LogEventAspect {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    @Autowired
    EventServiceImpl eventService;
    @Autowired
    RuntimeContext runtimeContext;

    @Around("@annotation(logInvokeMethod)")
    public Object async(ProceedingJoinPoint joinPoint, LogInvokeMethod logInvokeMethod) throws Throwable {
        LogEvent logEvent = new LogEvent();
        try {
            logEvent.setMethod(joinPoint.getSignature().toShortString());
            logEvent.setArguments(Lists.newArrayList());
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] names = parameterNameDiscoverer.getParameterNames(signature.getMethod());
            Object[] args = joinPoint.getArgs();
            Class<?>[] argClasses = signature.getMethod().getParameterTypes();
            IntStream.range(0, joinPoint.getArgs().length)
                    .forEach(i -> {
                        logEvent.getArguments()
                                .add(new LogEvent.Argument()
                                        .setClassName(argClasses[i].getName())
                                        .setArgument(args[i])
                                        .setArgumentName(names != null ? names[i] : ("arg-" + i)));
                    });
        } catch (Exception e) {
            Exceptions.catching(e);
        }
        try {
            Object result = joinPoint.proceed();
            logEvent.setResult(result);
            return result;
        } finally {
            try {
                eventService.post(logEvent);
            } catch (Exception e) {
                Exceptions.catching(e);
            }
        }
    }
}
