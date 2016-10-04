package me.icymint.sage.user.spec.entity;

/**
 * Created by daniel on 2016/10/3.
 */
public class IdentityExtension {
    private Long validSeconds;

    public Long getValidSeconds() {
        return validSeconds;
    }

    public IdentityExtension setValidSeconds(Long validSeconds) {
        this.validSeconds = validSeconds;
        return this;
    }

    @Override
    public String toString() {
        return "IdentityExtension{" +
                "validSeconds=" + validSeconds +
                '}';
    }
}
