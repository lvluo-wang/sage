package me.icymint.sage.user.spec.def;

/**
 * Created by daniel on 2016/10/4.
 */
public enum Privilege {
    ADMIN(RoleType.ROLE_ADMIN),
    USER,
    DEV_API(RoleType.ROLE_DEV),;

    private final RoleType[] roleTypes;

    Privilege(RoleType... roleTypes) {
        this.roleTypes = roleTypes.length == 0
                ? new RoleType[]{RoleType.ROLE_USER}
                : roleTypes;
    }

    public RoleType[] getRoleTypes() {
        return roleTypes;
    }
}
