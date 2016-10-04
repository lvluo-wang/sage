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
    private String description;
    @JsonIgnore
    private IdentityType type;
    @JsonIgnore
    private Long createId;
    @ToLabel
    private Bool isBlocked;
    @JsonIgnore
    private IdentityExtension extension;

    public String getDescription() {
        return description;
    }

    public Identity setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getCreateId() {
        return createId;
    }

    public Identity setCreateId(Long createId) {
        this.createId = createId;
        return this;
    }

    public IdentityType getType() {
        return type;
    }

    public Identity setType(IdentityType type) {
        this.type = type;
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

    public IdentityExtension getExtension() {
        return extension;
    }

    public Identity setExtension(IdentityExtension extension) {
        this.extension = extension;
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
                ", type=" + type +
                ", createId=" + createId +
                ", isBlocked=" + isBlocked +
                ", extension=" + extension +
                "} " + super.toString();
    }
}
