package me.icymint.sage.base.job.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.entity.BaseJobEntity;
import me.icymint.sage.base.spec.internal.api.BatchJob;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.base.spec.internal.entity.Job;
import me.icymint.sage.base.spec.repository.JobRepository;
import me.icymint.sage.base.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnBean(JobRepository.class)
public class JobService {
    private final Logger logger = LoggerFactory.getLogger(JobService.class);
    private final String instanceId = UUID.randomUUID().toString();
    @Autowired
    Clock clock;
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    JobRepository jobRepository;
    private volatile boolean hasJobLock = false;

    @Transactional(propagation = Propagation.NEVER)
    public <E extends BaseJobEntity<E>> void executeBatchJob(BatchJob<E> batchJob) {
        while (!needQuit()
                && executeSingleBatchJob(new PageBounds(0, batchJob.batchSize()), batchJob)) {
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
            if (needQuit()) {
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

    public boolean needQuit() {
        return !hasJobLock || runtimeContext.needShutdown();
    }

    @Transactional
    public void lockJob(Long jobId) {
        Job job = jobRepository.findOne(jobId);
        if (job == null) {
            hasJobLock = false;
            return;
        }
        Instant now = clock.now();

        if (Objects.equals(instanceId, job.getInstanceId())) {
            if (job.getExpireTime()
                    .isAfter(now.plusMillis(Magics.JOB_LOCK_REFRESH_DURATION))) {
                hasJobLock = true;
                return;
            }
        } else {
            if (job.getInstanceId() != null && job.getExpireTime().isAfter(now)) {
                hasJobLock = false;
                return;
            }
        }
        logger.info("Try take job lock...");
        hasJobLock = jobRepository.lockJob(jobId, instanceId, now.plusMillis(Magics.JOB_LOCK_EXPIRE_DURATION));
        if (hasJobLock) {
            logger.info("Take job lock OK!");
        } else {
            logger.debug("Take job lock Failed...");
        }
    }

}
