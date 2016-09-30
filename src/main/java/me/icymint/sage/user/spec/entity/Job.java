package me.icymint.sage.user.spec.entity;

import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.entity.BaseEntity;

import java.time.Instant;

/**
 * Created by daniel on 2016/10/1.
 */
public class Job extends BaseEntity<Job> {

    private String runnerId;
    private Bool isActive;
    private Instant expireTime;

    public String getRunnerId() {
        return runnerId;
    }

    public Job setRunnerId(String runnerId) {
        this.runnerId = runnerId;
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
                "runnerId='" + runnerId + '\'' +
                ", isActive=" + isActive +
                ", expireTime=" + expireTime +
                "} " + super.toString();
    }
}
