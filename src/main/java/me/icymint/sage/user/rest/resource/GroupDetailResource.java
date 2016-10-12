package me.icymint.sage.user.rest.resource;

import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;

import java.util.Arrays;

/**
 * Created by daniel on 2016/10/12.
 */
public class GroupDetailResource {
    private Long id;
    private RoleType[] roles;
    private Privilege[] privileges;

    public Long getId() {
        return id;
    }

    public GroupDetailResource setId(Long id) {
        this.id = id;
        return this;
    }

    public RoleType[] getRoles() {
        return roles;
    }

    public GroupDetailResource setRoles(RoleType[] roles) {
        this.roles = roles;
        return this;
    }

    public Privilege[] getPrivileges() {
        return privileges;
    }

    public GroupDetailResource setPrivileges(Privilege[] privileges) {
        this.privileges = privileges;
        return this;
    }

    @Override
    public String toString() {
        return "GroupDetailResource{" +
                "roles=" + Arrays.toString(roles) +
                ", privileges=" + Arrays.toString(privileges) +
                '}';
    }
}
