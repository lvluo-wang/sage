package me.icymint.sage.base.job;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.RuntimeContext;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.base.spec.entity.BaseJobEntity;
import me.icymint.sage.base.spec.exception.Exceptions;
import me.icymint.sage.base.spec.internal.api.BatchJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel on 2016/9/24.
 */
@Component
@ConditionalOnProperty(prefix = MagicConstants.PROP_PREFIX, name = "enable.job", havingValue = "true")
public class BaseJobSupporter {
    private final Logger logger = LoggerFactory.getLogger(BaseJobSupporter.class);
    @Autowired
    Clock clock;
    @Autowired
    RuntimeContext runtimeContext;

    @Transactional(propagation = Propagation.NEVER)
    public <E extends BaseJobEntity<E>> void executeBatchJob(BatchJob<E> batchJob, int limit) {
        logger.info("Start Job {}...", AopUtils.getTargetClass(batchJob));
        while (!runtimeContext.needShutdown()
                && executeSingleBatchJob(new PageBounds(0, limit), batchJob)) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
        logger.info("End Job {}...", AopUtils.getTargetClass(batchJob));
    }

    private <E extends BaseJobEntity<E>> boolean executeSingleBatchJob(PageBounds pageBounds, BatchJob<E> batchJob) {
        List<E> records = batchJob.getRecords(pageBounds);
        if (records.isEmpty()) {
            return false;
        }
        for (E record : records) {
            if (runtimeContext.needShutdown()) {
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
}
