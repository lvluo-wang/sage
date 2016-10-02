package me.icymint.sage.user.spec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.icymint.sage.base.spec.annotation.ToLabel;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.entity.BaseEntity;
import me.icymint.sage.user.spec.def.ClaimType;

/**
 * Created by daniel on 16/9/5.
 */
public class Claim extends BaseEntity<Claim> {
    @ToLabel
    private ClaimType type;
    private String value;
    @JsonIgnore
    private String primaryKey;
    @ToLabel
    private Bool isVerified;

    public Bool getIsVerified() {
        return isVerified;
    }

    public Claim setIsVerified(Bool isVerified) {
        this.isVerified = isVerified;
        return this;
    }

    public ClaimType getType() {
        return type;
    }

    public Claim setType(ClaimType type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Claim setValue(String value) {
        this.value = value;
        return this;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public Claim setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    @Override
    protected Claim getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", isVerified=" + isVerified +
                "} " + super.toString();
    }
}
