package me.icymint.sage.user.spec.entity;

import me.icymint.sage.base.spec.entity.BaseEntity;
import me.icymint.sage.user.spec.def.TokenType;

import java.time.Instant;

/**
 * Created by daniel on 16/9/4.
 */
public class Token extends BaseEntity<Token> {
    private Long clientId;
    private TokenType type;
    private String accessSecret;
    private Instant expireTime;
    private String sessionId;
    private String ip;
    private String timeZone;

    public String getIp() {
        return ip;
    }

    public Token setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public Token setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public TokenType getType() {
        return type;
    }

    public Token setType(TokenType type) {
        this.type = type;
        return this;
    }

    public Long getClientId() {
        return clientId;
    }

    public Token setClientId(Long clientId) {
        this.clientId = clientId;
        return this;
    }


    public String getAccessSecret() {
        return accessSecret;
    }

    public Token setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
        return this;
    }

    public Instant getExpireTime() {
        return expireTime;
    }

    public Token setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Token setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    protected Token getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "Token{" +
                "clientId=" + clientId +
                ", accessSecret='" + accessSecret + '\'' +
                ", expireTime=" + expireTime +
                ", sessionId=" + sessionId +
                "} " + super.toString();
    }
}
