package me.icymint.sage.user.spec.api;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.entity.Identity;

import java.util.List;
import java.util.Set;

/**
 * Created by daniel on 16/9/4.
 */
public interface IdentityService {

    Identity register(Long clientId, String username, String salt, String password);

    Identity createGroup(Long clientId, Long createId, String description, List<RoleType> roleTypes, List<Privilege> privilegeList);

    Identity findOne(Long identityId);

    Identity findClient(Long clientId);

    Identity findGroup(Long groupId);

    Identity findByClaim(String claim, ClaimType type);

    Set<Privilege> findPrivilegesById(Long ownerId);

    Set<RoleType> findRolesById(Long ownerId);

    List<Long> findGroupIds(PageBounds pageBounds);
}
