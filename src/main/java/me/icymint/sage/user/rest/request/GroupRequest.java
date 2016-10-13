package me.icymint.sage.user.rest.request;

import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by daniel on 2016/10/4.
 */
public class GroupRequest {
    @NotNull
    @Size(min = 2, max = 30)
    private String name;
    private List<Privilege> privilegeList;
    private List<RoleType> roleTypeList;

    public String getName() {
        return name;
    }

    public GroupRequest setName(String name) {
        this.name = name;
        return this;
    }

    public List<Privilege> getPrivilegeList() {
        return privilegeList;
    }

    public GroupRequest setPrivilegeList(List<Privilege> privilegeList) {
        this.privilegeList = privilegeList;
        return this;
    }

    public List<RoleType> getRoleTypeList() {
        return roleTypeList;
    }

    public GroupRequest setRoleTypeList(List<RoleType> roleTypeList) {
        this.roleTypeList = roleTypeList;
        return this;
    }

    @Override
    public String toString() {
        return "GroupRequest{" +
                "name='" + name + '\'' +
                ", privilegeList=" + privilegeList +
                ", roleTypeList=" + roleTypeList +
                '}';
    }
}
