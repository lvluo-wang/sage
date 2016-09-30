package me.icymint.sage.base.job.aspect;

import me.icymint.sage.base.job.BaseJobSupporter;
import me.icymint.sage.base.spec.annotation.IgnoreJobLock;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.base.spec.exception.Exceptions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by daniel on 2016/9/30.
 */
@Aspect
@Component
@Order(MagicConstants.AOP_ORDER_JOB)
public class JobAspect {
    private final Logger logger = LoggerFactory.getLogger(JobAspect.class);
    @Autowired
    Environment environment;
    @Autowired
    BaseJobSupporter baseJobSupporter;

    @Around("@annotation(scheduled)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Scheduled scheduled) {
        if (!environment.getProperty(MagicConstants.PROP_ENABLE_JOB, Boolean.class, false)) {
            logger.warn("Job is disabled!");
            return null;
        }
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        boolean ignoreLock = method.isAnnotationPresent(IgnoreJobLock.class);
        if (!ignoreLock && !baseJobSupporter.hasJobLock()) {
            logger.info("Job lock not get, ignore execute");
            return null;
        }
        if (!ignoreLock) {
            logger.info("Start Job " + proceedingJoinPoint.getSignature().toShortString());
        }
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            Exceptions.catching(e);
            return null;
        } finally {
            if (!ignoreLock) {
                logger.info("End Job " + proceedingJoinPoint.toShortString());
            }
        }
    }
}
