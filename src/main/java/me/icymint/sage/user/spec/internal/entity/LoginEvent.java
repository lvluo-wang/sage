package me.icymint.sage.user.spec.internal.entity;

import me.icymint.sage.base.spec.entity.BaseEvent;

/**
 * Created by daniel on 2016/9/23.
 */
public class LoginEvent extends BaseEvent<LoginEvent> {
    private Long tokenId;
    private String sessionId;

    public Long getTokenId() {
        return tokenId;
    }

    public LoginEvent setTokenId(Long tokenId) {
        this.tokenId = tokenId;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public LoginEvent setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    protected LoginEvent getSelf() {
        return this;
    }


    @Override
    public String toString() {
        return "LoginEvent{" +
                "tokenId=" + tokenId +
                ", sessionId='" + sessionId + '\'' +
                "} " + super.toString();
    }
}
