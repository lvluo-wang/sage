package me.icymint.sage.base.spec.repository;

import me.icymint.sage.base.spec.internal.entity.Job;

import java.time.Instant;

/**
 * Created by daniel on 2016/10/1.
 */
public interface JobRepository {

    Job findOne(Long jobId);

    boolean lockJob(Long jobId, String runnerId, Instant expireTime);

}
