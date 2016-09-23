package me.icymint.sage.user.spec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.icymint.sage.base.spec.entity.BaseEvent;
import me.icymint.sage.user.spec.def.EventStatus;

import java.time.Instant;

/**
 * Created by daniel on 2016/9/23.
 */
public class Event extends BaseEvent {
    @JsonIgnore
    private Instant createTime;
    @JsonIgnore
    private Instant nextScanTime;
    @JsonIgnore
    private Instant updateTime;
    @JsonIgnore
    private EventStatus status;
    @JsonIgnore
    private String body;
    @JsonIgnore
    private String asyncEventType;

    public Instant getNextScanTime() {
        return nextScanTime;
    }

    public Event setNextScanTime(Instant nextScanTime) {
        this.nextScanTime = nextScanTime;
        return this;
    }

    public String getAsyncEventType() {
        return asyncEventType;
    }

    public Event setAsyncEventType(String asyncEventType) {
        this.asyncEventType = asyncEventType;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public Event setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public Event setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public EventStatus getStatus() {
        return status;
    }

    public Event setStatus(EventStatus status) {
        this.status = status;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Event setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "Event{" +
                "createTime=" + createTime +
                ", nextScanTime=" + nextScanTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                ", body='" + body + '\'' +
                ", asyncEventType='" + asyncEventType + '\'' +
                "} " + super.toString();
    }
}
