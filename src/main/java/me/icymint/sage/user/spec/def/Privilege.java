package me.icymint.sage.user.spec.def;

import me.icymint.sage.base.spec.annotation.I18nEnum;

/**
 * Created by daniel on 2016/10/4.
 */
@I18nEnum("privilege")
public enum Privilege {
    ADMIN(RoleType.ROLE_ADMIN),
    GRANT_QUERY(RoleType.ROLE_ADMIN),
    GRANT_MODIFY(RoleType.ROLE_ADMIN),
    GROUP_QUERY(RoleType.ROLE_ADMIN),
    GROUP_MODIFY(RoleType.ROLE_ADMIN),
    USER,
    DEV_API(RoleType.ROLE_DEV);

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
