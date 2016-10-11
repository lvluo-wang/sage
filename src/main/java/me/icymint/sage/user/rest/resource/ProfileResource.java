package me.icymint.sage.user.rest.resource;

import me.icymint.sage.user.spec.entity.Claim;
import me.icymint.sage.user.spec.entity.Identity;

import java.util.List;

/**
 * Created by daniel on 2016/10/11.
 */
public class ProfileResource {
    private Identity identity;
    private List<Claim> claimList;

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
}
