package me.icymint.sage.user.rest.resource;

import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.entity.Claim;
import me.icymint.sage.user.spec.entity.Identity;

import java.util.List;
import java.util.Set;

/**
 * Created by daniel on 2016/10/11.
 */
public class ProfileResource {
    private Identity identity;
    private List<Claim> claimList;
    private Set<Privilege> privileges;
    private Set<RoleType> roles;

    public Identity getIdentity() {
        return identity;
    }

    public ProfileResource setIdentity(Identity identity) {
        this.identity = identity;
        return this;
    }

    public List<Claim> getClaimList() {
        return claimList;
    }

    public ProfileResource setClaimList(List<Claim> claimList) {
        this.claimList = claimList;
        return this;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public ProfileResource setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
        return this;
    }

    public Set<RoleType> getRoles() {
        return roles;
    }

    public ProfileResource setRoles(Set<RoleType> roles) {
        this.roles = roles;
        return this;
    }
}
