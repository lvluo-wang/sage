package me.icymint.sage.base.spec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by daniel on 2016/9/23.
 */
public abstract class BaseEvent {
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private String parentEventId;
    @JsonIgnore
    private String eventId;

    public Long getId() {
        return id;
    }

    public BaseEvent setId(Long id) {
        this.id = id;
        return this;
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public BaseEvent setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
        return this;
    }

    public String getEventId() {
        return eventId;
    }

    public BaseEvent setEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "id=" + id +
                ", parentEventId='" + parentEventId + '\'' +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
