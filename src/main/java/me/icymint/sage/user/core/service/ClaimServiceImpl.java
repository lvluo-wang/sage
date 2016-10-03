package me.icymint.sage.user.core.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.user.data.mapper.ClaimMapper;
import me.icymint.sage.user.spec.api.ClaimService;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.def.UserCode;
import me.icymint.sage.user.spec.entity.Claim;
import me.icymint.sage.user.spec.exception.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by daniel on 16/9/6.
 */
@Service
public class ClaimServiceImpl implements ClaimService {
    @Autowired
    ApplicationContext context;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    ClaimMapper claimMapper;


    @Transactional
    public Long createClaim(Long identityId, ClaimType type, String value, boolean isVerified) {
        if (type == null) {
            throw new UserServiceException(context, UserCode.CLAIM_TYPE_NULL);
        }
        Claim claim = new Claim()
                .setOwnerId(identityId)
                .setIsVerified(isVerified ? Bool.Y : Bool.N)
                .setType(type)
                .setValue(value)
                .setPrimaryKey(type.isGlobalUnique() ?
                        Magics.CLAIM_GLOBAL_UNIQUE
                        : String.valueOf(identityId));
        if (claimMapper.create(claim) != 1) {
            throw new UserServiceException(context, UserCode.CLAIM_CREATE_FAILED);
        }
        return claim.getId();
    }

    @Override
    @Cacheable(value = Magics.CACHE_CLAIM, key = "#id")
    public Claim findOne(Long id, Long ownerId) {
        return claimMapper.findOneByOwnerId(id, ownerId);
    }

    @Override
    public Claim findOneByTypeAndValue(ClaimType type, String value) {
        if (type == null) {
            throw new UserServiceException(context, UserCode.CLAIM_TYPE_NULL);
        }
        if (!type.isGlobalUnique()) {
            throw new UserServiceException(context, UserCode.USE_MULTI_CLAIM_QUERY_API_INSTEAD);
        }
        return claimMapper.findOneByTypeAndValue(type, value);
    }

    @Override
    public boolean hasRoles(Long ownerId, RoleType[] roleTypes) {
        if (roleTypes == null || roleTypes.length == 0) {
            return true;
        }
        return claimMapper.existRoles(ownerId, roleTypes) > 0;
    }

    @Override
    public List<Claim> findByOwnerId(Long userId, PageBounds pageBounds) {
        return claimMapper.findByOwnerId(userId, pageBounds);
    }
}
