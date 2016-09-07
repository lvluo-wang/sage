package me.icymint.sage.user.spec.entity;

import me.icymint.sage.base.spec.entity.BaseEntity;

import java.time.Instant;

/**
 * Created by daniel on 16/9/6.
 */
public class Session extends BaseEntity<Session> {
    private String sessionId;
    private String clientId;
    private String ip;
    private String timeZone;
    private Instant expireTime;

    public Instant getExpireTime() {
        return expireTime;
    }

    public Session setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Session setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    protected Session getSelf() {
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public Session setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Session setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public Session setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }
}
