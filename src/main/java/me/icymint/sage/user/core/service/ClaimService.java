package me.icymint.sage.user.core.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.user.data.mapper.ClaimMapper;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.def.UserCode;
import me.icymint.sage.user.spec.entity.Claim;
import me.icymint.sage.user.spec.exception.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by daniel on 16/9/6.
 */
@Service
public class ClaimService {
    @Autowired
    ApplicationContext context;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    ClaimMapper claimMapper;
    @Autowired
    ClaimService claimService;

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = Magics.CACHE_CLAIM, key = "#type+'-'+#identityId", condition = "!#type.globalUnique"),
            @CacheEvict(value = Magics.CACHE_CLAIM, key = "T(me.icymint.sage.user.spec.def.ClaimType).PRIVILEGE+'-'+#identityId",
                    condition = "T(me.icymint.sage.user.spec.def.ClaimType).ROLE==#type")
    })
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

    @Cacheable(value = Magics.CACHE_CLAIM, key = "#id", unless = "#result.type.globalUnique")
    public Claim findOne(Long id, Long ownerId) {
        return claimMapper.findOneByOwnerId(id, ownerId);
    }

    public Claim findOneByTypeAndValue(ClaimType type, String value) {
        if (type == null) {
            throw new UserServiceException(context, UserCode.CLAIM_TYPE_NULL);
        }
        if (!type.isGlobalUnique()) {
            throw new UserServiceException(context, UserCode.USE_MULTI_CLAIM_QUERY_API_INSTEAD);
        }
        return claimMapper.findOneByTypeAndValue(type, value);
    }

    @Cacheable(value = Magics.CACHE_CLAIM, key = "T(me.icymint.sage.user.spec.def.ClaimType).ROLE+'-'+#ownerId")
    public Set<RoleType> findRolesByOwnerId(Long ownerId) {
        return claimMapper.findRolesByOwnerId(ownerId);
    }

    @Cacheable(value = Magics.CACHE_CLAIM, key = "T(me.icymint.sage.user.spec.def.ClaimType).PRIVILEGE+'-'+#ownerId")
    public Set<Privilege> findPrivilegesByOwnerId(Long ownerId) {
        return claimMapper.findPrivilegesByOwnerId(ownerId);
    }

    public List<Claim> findByOwnerId(Long userId, PageBounds pageBounds) {
        return claimMapper.findByOwnerId(userId, pageBounds);
    }


    public List<Claim> findAllUniqueByOwnerId(Long userId) {
        return claimMapper.findUniqueByOwnerId(userId);
    }

    @Transactional
    @CacheEvict(value = Magics.CACHE_CLAIM, key = "T(me.icymint.sage.user.spec.def.ClaimType).ROLE+'-'+#ownerId")
    public void deleteRole(Long ownerId, RoleType value) {
        Claim claim = claimMapper.findOneByOwnerIdAndTypeAndValue(ownerId, ClaimType.ROLE, value.name());
        if (claim != null) {
            claimMapper.delete(claim.getId());
        }
    }

    @Transactional
    @CacheEvict(value = Magics.CACHE_CLAIM, key = "T(me.icymint.sage.user.spec.def.ClaimType).PRIVILEGE+'-'+#ownerId")
    public void deletePrivilege(Long ownerId, Privilege value) {
        Claim claim = claimMapper.findOneByOwnerIdAndTypeAndValue(ownerId, ClaimType.PRIVILEGE, value.name());
        if (claim != null) {
            claimMapper.delete(claim.getId());
        }
    }

}
