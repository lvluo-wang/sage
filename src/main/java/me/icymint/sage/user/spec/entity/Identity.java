package me.icymint.sage.user.spec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.icymint.sage.base.spec.annotation.ToLabel;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.entity.BaseEntity;
import me.icymint.sage.user.spec.def.IdentityType;

/**
 * Created by daniel on 16/9/3.
 */
public class Identity extends BaseEntity<Identity> {
    private String salt;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private Long createBy;
    @JsonIgnore
    private IdentityType type;
    @ToLabel
    private Bool isBlocked;
    @JsonIgnore
    private Long validSeconds;

    public IdentityType getType() {
        return type;
    }

    public Identity setType(IdentityType type) {
        this.type = type;
        return this;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public Identity setCreateBy(Long createBy) {
        this.createBy = createBy;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public Identity setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Identity setPassword(String password) {
        this.password = password;
        return this;
    }

    public Bool getIsBlocked() {
        return isBlocked;
    }

    public Identity setIsBlocked(Bool isBlocked) {
        this.isBlocked = isBlocked;
        return this;
    }

    public Long getValidSeconds() {
        return validSeconds;
    }

    public Identity setValidSeconds(Long validSeconds) {
        this.validSeconds = validSeconds;
        return this;
    }

    @Override
    protected Identity getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "salt='" + salt + '\'' +
                ", password='" + password + '\'' +
                ", createBy=" + createBy +
                ", type=" + type +
                ", isBlocked=" + isBlocked +
                ", validSeconds=" + validSeconds +
                "} " + super.toString();
    }
}
