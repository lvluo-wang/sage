package me.icymint.sage.user.rest.resource;

import me.icymint.sage.base.spec.annotation.ToLabel;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.user.spec.def.ClaimType;

import java.time.Instant;

/**
 * Created by daniel on 2016/10/2.
 */
public class ClaimResource {
    private Long id;
    private Instant createTime;
    @ToLabel
    private ClaimType type;
    private String value;
    @ToLabel
    private Bool isVerified;

    public Long getId() {
        return id;
    }

    public ClaimResource setId(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public ClaimResource setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public ClaimType getType() {
        return type;
    }

    public ClaimResource setType(ClaimType type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ClaimResource setValue(String value) {
        this.value = value;
        return this;
    }

    public Bool getIsVerified() {
        return isVerified;
    }

    public ClaimResource setIsVerified(Bool isVerified) {
        this.isVerified = isVerified;
        return this;
    }
}
