package me.icymint.sage.user.rest.resource;

import me.icymint.sage.base.spec.annotation.ToLabel;
import me.icymint.sage.base.spec.def.Bool;

import java.time.Instant;

/**
 * Created by daniel on 2016/10/1.
 */
public class IdentityResource {
    private Long id;
    private Instant createTime;
    private String salt;
    @ToLabel
    private Bool isBlocked;

    public Long getId() {
        return id;
    }

    public IdentityResource setId(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public IdentityResource setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public IdentityResource setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public Bool getIsBlocked() {
        return isBlocked;
    }

    public IdentityResource setIsBlocked(Bool isBlocked) {
        this.isBlocked = isBlocked;
        return this;
    }
}
