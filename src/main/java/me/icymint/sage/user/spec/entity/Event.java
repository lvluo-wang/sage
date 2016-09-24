package me.icymint.sage.user.spec.entity;

import me.icymint.sage.user.spec.def.EventStatus;
import me.icymint.sage.base.spec.entity.BaseJobEntity;

/**
 * Created by daniel on 2016/9/23.
 */
public class Event extends BaseJobEntity<Event> {
    private String eventId;
    private String parentEventId;
    private String body;
    private String asyncEventType;
    private EventStatus status;

    public String getEventId() {
        return eventId;
    }

    public Event setEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public Event setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
        return this;
    }

    @Override
    protected Event getSelf() {
        return this;
    }

    public String getBody() {
        return body;
    }

    public Event setBody(String body) {
        this.body = body;
        return this;
    }

    public String getAsyncEventType() {
        return asyncEventType;
    }

    public Event setAsyncEventType(String asyncEventType) {
        this.asyncEventType = asyncEventType;
        return this;
    }

    public EventStatus getStatus() {
        return status;
    }

    public Event setStatus(EventStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", parentEventId='" + parentEventId + '\'' +
                ", body='" + body + '\'' +
                ", asyncEventType='" + asyncEventType + '\'' +
                ", status=" + status +
                "} " + super.toString();
    }
}
