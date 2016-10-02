package me.icymint.sage.base.spec.entity;

import me.icymint.sage.base.spec.annotation.ToLabel;
import me.icymint.sage.base.spec.def.Bool;

import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Created by daniel on 16/9/5.
 */
public abstract class BaseEntity<T extends BaseEntity> extends BaseLogEntity<T> {
    @NotNull
    @ToLabel
    private Bool isDeleted;
    private Instant updateTime;

    public Bool getIsDeleted() {
        return isDeleted;
    }

    public T setIsDeleted(Bool isDeleted) {
        this.isDeleted = isDeleted;
        return getSelf();
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public T setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return getSelf();
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "isDeleted=" + isDeleted +
                ", updateTime=" + updateTime +
                "} " + super.toString();
    }
}
