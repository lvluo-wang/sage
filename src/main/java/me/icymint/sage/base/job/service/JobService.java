package me.icymint.sage.base.job.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.RuntimeContext;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.base.spec.entity.BaseJobEntity;
import me.icymint.sage.base.spec.exception.Exceptions;
import me.icymint.sage.base.spec.internal.api.BatchJob;
import me.icymint.sage.user.data.mapper.JobMapper;
import me.icymint.sage.user.spec.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel on 2016/9/24.
 */
@Component
@ConditionalOnProperty(name = MagicConstants.PROP_ENABLE_JOB, havingValue = "true")
public class JobService {
    private final Logger logger = LoggerFactory.getLogger(JobService.class);
    private final String runnerId = UUID.randomUUID().toString();
    private volatile boolean hasJobLock = false;

    @Autowired
    Clock clock;
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    JobMapper jobMapper;


    @Transactional(propagation = Propagation.NEVER)
    public <E extends BaseJobEntity<E>> void executeBatchJob(BatchJob<E> batchJob, int limit) {
        while (hasJobLock
                && !runtimeContext.needShutdown()
                && executeSingleBatchJob(new PageBounds(0, limit), batchJob)) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    private <E extends BaseJobEntity<E>> boolean executeSingleBatchJob(PageBounds pageBounds, BatchJob<E> batchJob) {
        List<E> records = batchJob.getRecords(pageBounds);
        if (records.isEmpty()) {
            return false;
        }
        for (E record : records) {
            if (!hasJobLock || runtimeContext.needShutdown()) {
                return false;
            }
            logger.info("Handle record {}", record);
            try {
                batchJob.updateNextScanTime(record.getId());
                batchJob.handle(record.getId());
            } catch (Exception e) {
                Exceptions.catching(e);
            }
        }
        return true;
    }

    @Transactional
    public void lockJob(Long jobId) {
        Job job = jobMapper.findOne(jobId);
        if (job == null) {
            hasJobLock = false;
            return;
        }
        Instant now = clock.now();

        if (Objects.equals(runnerId, job.getRunnerId())) {
            if (job.getExpireTime()
                    .isAfter(now.plusMillis(MagicConstants.JOB_LOCK_REFRESH_DURATION))) {
                hasJobLock = true;
                return;
            }
        } else {
            if (job.getRunnerId() != null && job.getExpireTime().isAfter(now)) {
                hasJobLock = false;
                return;
            }
        }
        logger.info("Try take job lock...");
        hasJobLock = jobMapper.lockJob(jobId, runnerId, now.plusMillis(MagicConstants.JOB_LOCK_EXPIRE_DURATION)) == 1;
        if (hasJobLock) {
            logger.info("Take job lock OK!");
        } else {
            logger.info("Take job lock Failed...");
        }
    }

    public boolean hasJobLock() {
        return hasJobLock;
    }

}
