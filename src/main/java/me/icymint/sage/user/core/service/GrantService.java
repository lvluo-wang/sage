package me.icymint.sage.user.core.service;

import me.icymint.sage.base.spec.annotation.LogInvokeMethod;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.user.data.mapper.GrantMapper;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.def.UserCode;
import me.icymint.sage.user.spec.entity.Grant;
import me.icymint.sage.user.spec.entity.Identity;
import me.icymint.sage.user.spec.exception.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by daniel on 2016/10/4.
 */
@Service
public class GrantService {

    @Autowired
    ApplicationContext context;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    GrantMapper grantMapper;
    @Autowired
    IdentityServiceImpl identityService;

    @Cacheable(value = Magics.CACHE_GRANT, key = "#ownerId")
    public List<Long> findGroupIdsByOwnerId(Long ownerId) {
        return grantMapper.findGroupIdsByOwnerId(ownerId);
    }

    @Transactional
    @LogInvokeMethod
    @CacheEvict(value = Magics.CACHE_GRANT, key = "#ownerId")
    public Long grant(Long ownerId, Long groupId) {
        if (identityService.findOne(groupId, IdentityType.GROUP) == null) {
            throw new UserServiceException(context, UserCode.GROUP__NOT_FOUND, groupId);
        }
        Identity identity = identityService.findOne(ownerId, IdentityType.MEMBER);
        if (identity == null) {
            throw new UserServiceException(context, UserCode.ONLY_MEMBER_AND_GROUP_CAN_GRANT);
        }
        Grant grant = new Grant()
                .setOwnerId(ownerId)
                .setGroupId(groupId);
        if (grantMapper.create(grant) != 1) {
            throw new UserServiceException(context, UserCode.GRANT_FAILED);
        }
        return grant.getId();
    }

    @Transactional
    @LogInvokeMethod
    @CacheEvict(value = Magics.CACHE_GRANT, key = "#ownerId")
    public void revoke(Long ownerId, Long groupId) {
        grantMapper.deleteByOwnerIdAndGroupId(ownerId, groupId);
    }

}
