package me.icymint.sage.base.spec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by daniel on 2016/9/23.
 */
public abstract class BaseEvent<T extends BaseEvent<T>> {
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long ownerId;
    @JsonIgnore
    private String parentEventId;
    @JsonIgnore
    private String eventId;
    @JsonIgnore
    private Boolean async;
    private String ipAddress;
    private String correlationId;
    private String clientId;
    private String sessionId;

    public <E extends BaseEvent<E>> Class<E> parentEventClass() {
        return null;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public T setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return getSelf();
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public T setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return getSelf();
    }

    public String getSessionId() {
        return sessionId;
    }

    public T setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return getSelf();
    }

    public String getClientId() {
        return clientId;
    }

    public T setClientId(String clientId) {
        this.clientId = clientId;
        return getSelf();
    }

    public Boolean getAsync() {
        return async;
    }

    public T setAsync(Boolean async) {
        this.async = async;
        return getSelf();
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public T setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return getSelf();
    }

    protected abstract T getSelf();

    public Long getId() {
        return id;
    }

    public T setId(Long id) {
        this.id = id;
        return getSelf();
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public T setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
        return getSelf();
    }

    public String getEventId() {
        return eventId;
    }

    public T setEventId(String eventId) {
        this.eventId = eventId;
        return getSelf();
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
