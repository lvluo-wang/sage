package me.icymint.sage.user.core.repository;

import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.internal.entity.Job;
import me.icymint.sage.base.spec.repository.JobRepository;
import me.icymint.sage.user.data.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Created by daniel on 2016/10/1.
 */
@Repository
@ConditionalOnProperty(name = Magics.PROP_ENABLE_JOB, havingValue = "true")
public class JobRepositoryImpl implements JobRepository {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    JobMapper jobMapper;

    @Override
    public Job findOne(Long jobId) {
        return jobMapper.findOne(jobId);
    }

    @Override
    @Transactional
    public boolean lockJob(Long jobId, String instanceId, Instant expireTime) {
        return jobMapper.lockJob(jobId, instanceId, expireTime) == 1;
    }
}
