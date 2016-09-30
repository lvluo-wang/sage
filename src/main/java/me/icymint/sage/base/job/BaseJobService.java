package me.icymint.sage.base.job;

import me.icymint.sage.base.core.service.EventServiceImpl;
import me.icymint.sage.base.spec.annotation.IgnoreJobLock;
import me.icymint.sage.base.spec.def.MagicConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by daniel on 2016/9/24.
 */
@Service
@ConditionalOnBean(BaseJobSupporter.class)
public class BaseJobService {
    @Autowired
    BaseJobSupporter supporter;
    @Autowired
    EventServiceImpl eventService;

    @IgnoreJobLock
    @Scheduled(fixedRate = MagicConstants.JOB_LOCK_DURATION)
    public void lockJob() {
        supporter.lockJob(0L);
    }

    @Scheduled(cron = "${" + MagicConstants.PROP_JOB_EVENT_CRON + "}")
    public void eventJob() {
        supporter.executeBatchJob(eventService, 100);
    }
}
