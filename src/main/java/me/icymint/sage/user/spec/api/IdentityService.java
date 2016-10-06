package me.icymint.sage.user.spec.api;

import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.entity.Identity;

/**
 * Created by daniel on 16/9/4.
 */
public interface IdentityService {

    Identity register(Long clientId, String username, String salt, String password);

    Identity findOne(Long identityId, IdentityType type);

    Identity findByClaim(IdentityType type, String claim, ClaimType claimType);
}
