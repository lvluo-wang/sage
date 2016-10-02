package me.icymint.sage.base.rest.resource;

import me.icymint.sage.user.spec.entity.Claim;

import java.util.List;
import java.util.Set;

/**
 * Created by daniel on 2016/10/2.
 */
public class ExampleResource {
    private Set<Claim> claimSet;
    private List<Claim> claimList;
    private Claim[] claims;

    public Set<Claim> getClaimSet() {
        return claimSet;
    }

    public ExampleResource setClaimSet(Set<Claim> claimSet) {
        this.claimSet = claimSet;
        return this;
    }

    public List<Claim> getClaimList() {
        return claimList;
    }

    public ExampleResource setClaimList(List<Claim> claimList) {
        this.claimList = claimList;
        return this;
    }

    public Claim[] getClaims() {
        return claims;
    }

    public ExampleResource setClaims(Claim[] claims) {
        this.claims = claims;
        return this;
    }
}
