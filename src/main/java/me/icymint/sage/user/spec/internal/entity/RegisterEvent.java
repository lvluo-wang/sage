package me.icymint.sage.user.spec.internal.entity;

import me.icymint.sage.base.spec.entity.BaseEvent;

/**
 * Created by daniel on 2016/9/23.
 */
public class RegisterEvent extends BaseEvent {
    private Long identityId;

    public Long getIdentityId() {
        return identityId;
    }

    public RegisterEvent setIdentityId(Long identityId) {
        this.identityId = identityId;
        return this;
    }

    @Override
    public String toString() {
        return "RegisterEvent{" +
                "identityId=" + identityId +
                "} " + super.toString();
    }
}
