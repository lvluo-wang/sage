package me.icymint.sage.base.job.aspect;

import me.icymint.sage.base.job.service.JobService;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.internal.api.BatchJob;
import me.icymint.sage.base.util.Exceptions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by daniel on 2016/9/30.
 */
@Aspect
@Component
@Order(Magics.AOP_ORDER_JOB)
@ConditionalOnBean(JobService.class)
public class JobAspect {
    private final Logger logger = LoggerFactory.getLogger(JobAspect.class);
    @Autowired
    JobService jobService;

    @SuppressWarnings("unchecked")
    @Around("@annotation(scheduled) && !execution(* me.icymint.sage.base.job.config.BaseJobConfig.lockJob(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Scheduled scheduled) {
        if (jobService.needQuit()) {
            logger.debug("Job ignore");
            return null;
        }
        logger.info("Start Job " + proceedingJoinPoint.getSignature().toShortString());
        try {
            Object result = proceedingJoinPoint.proceed();
            if (result instanceof BatchJob) {
                jobService.executeBatchJob((BatchJob) result);
                result = null;
            }
            return result;
        } catch (Throwable e) {
            Exceptions.catching(e);
            return null;
        } finally {
            logger.info("End Job " + proceedingJoinPoint.toShortString());
        }
    }
}
