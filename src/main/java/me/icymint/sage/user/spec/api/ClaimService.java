package me.icymint.sage.user.spec.api;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.entity.Claim;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Set;

/**
 * Created by daniel on 16/9/6.
 */
public interface ClaimService {
    Claim findOne(Long id, Long ownerId);

    Claim findOneByTypeAndValue(ClaimType type, String value);

    boolean hasRoles(Long ownerId, RoleType[] roleTypes);

    Set<RoleType> findRolesByOwnerId(Long ownerId);

    List<Claim> findByOwnerId(Long userId, PageBounds pageBounds);
}
