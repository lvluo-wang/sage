package me.icymint.sage.base.spec.entity;

import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Created by daniel on 16/9/5.
 */
public abstract class BaseLogEntity<T extends BaseLogEntity> {
    private Long id;
    @NotNull
    private Long ownerId;
    private Instant createTime;

    protected abstract T getSelf();

    public Long getId() {
        return id;
    }

    public T setId(Long id) {
        this.id = id;
        return getSelf();
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public T setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return getSelf();
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public T setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return getSelf();
    }

    @Override
    public String toString() {
        return "BaseLogEntity{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", createTime=" + createTime +
                '}';
    }
}
