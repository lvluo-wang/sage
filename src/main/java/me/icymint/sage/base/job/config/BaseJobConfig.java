package me.icymint.sage.base.job.config;

import me.icymint.sage.base.core.service.EventServiceImpl;
import me.icymint.sage.base.job.service.JobService;
import me.icymint.sage.base.spec.annotation.JobConfiguration;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.internal.api.BatchJob;
import me.icymint.sage.user.spec.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by daniel on 2016/9/24.
 */
@JobConfiguration
@ConditionalOnBean(JobService.class)
public class BaseJobConfig {
    @Autowired
    JobService jobService;
    @Autowired
    EventServiceImpl eventService;

    @Scheduled(fixedRate = Magics.JOB_LOCK_DURATION)
    public void lockJob() {
        jobService.lockJob(0L);
    }

    @Scheduled(cron = "${" + Magics.PROP_JOB_EVENT_CRON + "}")
    public BatchJob<Event> eventJob() {
        return eventService;
    }
}
