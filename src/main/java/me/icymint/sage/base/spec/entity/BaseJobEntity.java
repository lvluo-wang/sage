package me.icymint.sage.base.spec.entity;

import java.time.Instant;

/**
 * Created by daniel on 2016/9/24.
 */
public abstract class BaseJobEntity<T extends BaseJobEntity> extends BaseEntity<T> {
    private Instant nextScanTime;

    public Instant getNextScanTime() {
        return nextScanTime;
    }

    public T setNextScanTime(Instant nextScanTime) {
        this.nextScanTime = nextScanTime;
        return getSelf();
    }

    @Override
    public String toString() {
        return "BaseJobEntity{" +
                "nextScanTime=" + nextScanTime +
                "} " + super.toString();
    }
}
