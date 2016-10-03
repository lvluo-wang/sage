package me.icymint.sage.base.spec.internal.entity;

import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.entity.BaseJobEntity;

import java.time.Instant;

/**
 * Created by daniel on 2016/10/1.
 */
public class Job extends BaseJobEntity<Job> {

    private String instanceId;
    private Bool isActive;
    private Instant expireTime;

    public String getInstanceId() {
        return instanceId;
    }

    public Job setInstanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public Bool getIsActive() {
        return isActive;
    }

    public Job setIsActive(Bool isActive) {
        this.isActive = isActive;
        return this;
    }

    public Instant getExpireTime() {
        return expireTime;
    }

    public Job setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    @Override
    protected Job getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "Job{" +
                "instanceId='" + instanceId + '\'' +
                ", isActive=" + isActive +
                ", expireTime=" + expireTime +
                "} " + super.toString();
    }
}
