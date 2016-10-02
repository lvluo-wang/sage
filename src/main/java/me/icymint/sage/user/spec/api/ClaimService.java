package me.icymint.sage.user.spec.api;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.entity.Claim;

import java.util.List;

/**
 * Created by daniel on 16/9/6.
 */
public interface ClaimService {
    Claim findOne(Long id);

    Claim findOneByTypeAndValue(ClaimType type, String value);

    List<Claim> findAllByTypeAndValue(ClaimType type, String value);

    boolean hasRoles(Long ownerId, RoleType[] roleTypes);

    List<Claim> findByOwnerId(Long userId, PageBounds pageBounds);
}
