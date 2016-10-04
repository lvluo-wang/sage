package me.icymint.sage.user.spec.entity;

import me.icymint.sage.base.spec.entity.BaseLogEntity;

/**
 * Created by daniel on 2016/10/4.
 */
public class Grant extends BaseLogEntity<Grant> {
    private Long groupId;


    public Long getGroupId() {
        return groupId;
    }

    public Grant setGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    @Override
    protected Grant getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "Grant{" +
                "groupId=" + groupId +
                "} " + super.toString();
    }
}
