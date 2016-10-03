package me.icymint.sage.user.core.service;

import me.icymint.sage.base.spec.annotation.LogInvokeMethod;
import me.icymint.sage.base.spec.annotation.NotifyInTransactionEvent;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.exception.InvalidArgumentException;
import me.icymint.sage.base.spec.internal.api.EventProducer;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.data.mapper.IdentityMapper;
import me.icymint.sage.user.spec.api.IdentityService;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.def.UserCode;
import me.icymint.sage.user.spec.entity.Claim;
import me.icymint.sage.user.spec.entity.Identity;
import me.icymint.sage.user.spec.exception.UserServiceException;
import me.icymint.sage.user.spec.internal.entity.RegisterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by daniel on 16/9/5.
 */
@Service
public class IdentityServiceImpl implements IdentityService {

    public static final Pattern PATTERN_USERNAME = Pattern.compile("^[0-9a-zA-Z][._0-9a-zA-Z]{3,31}$");

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    IdentityMapper identityMapper;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    ClaimServiceImpl claimService;
    @Autowired
    ApplicationContext context;
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    Clock clock;

    @Override
    @Cacheable(value = Magics.CACHE_IDENTITY, key = "#identityId")
    public Identity findOne(Long identityId) {
        return identityMapper.findOne(identityId);
    }

    @Override
    @Transactional
    @LogInvokeMethod
    @NotifyInTransactionEvent(eventProducerClass = RegisterEventProducer.class)
    public Identity register(Long clientId, String username, String salt, String password) {
        if (StringUtils.isEmpty(username)) {
            username = null;
        } else if (!PATTERN_USERNAME.matcher(username).matches()) {
            throw new InvalidArgumentException(context, UserCode.USERNAME__ILLEGAL, username);
        }
        if (StringUtils.isEmpty(salt)) {
            throw new UserServiceException(context, UserCode.SALT_IS_NULL);
        }
        if (StringUtils.isEmpty(password)) {
            throw new UserServiceException(context, UserCode.PASSWORD_IS_NULL);
        }
        Identity client = identityMapper.findOne(clientId);
        if (client == null || client.getType() != IdentityType.CLIENT) {
            throw new UserServiceException(context, UserCode.CLIENT_ID__ILLEGAL, clientId);
        }
        runtimeContext.setClientId(clientId);
        Long createId = runtimeContext.getUserId();
        if (createId == null) {
            createId = clientId;
        }
        Identity identity = new Identity()
                .setOwnerId(clientId)
                .setCreateId(createId)
                .setIsBlocked(Bool.N)
                .setPassword(password)
                .setSalt(salt)
                .setType(IdentityType.USER)
                .setValidSeconds(null);
        if (identityMapper.create(identity) != 1) {
            throw new UserServiceException(context, UserCode.IDENTITY_CREATE_FAILED);
        }
        identity = identityMapper.findOne(identity.getId());
        username = StringUtils.isEmpty(username) ?
                (identity.getType() + "-" + identity.getId())
                : username;
        claimService.createClaim(identity.getId(), ClaimType.USERNAME, username, true);
        claimService.createClaim(identity.getId(), ClaimType.ROLE, RoleType.USER.name(), true);
        return identity;
    }

    @Override
    public Identity findByClaim(String claimValue, ClaimType type) {
        Claim claim = claimService.findOneByTypeAndValue(type, claimValue);
        if (claim == null) {
            return null;
        }
        return findOne(claim.getOwnerId());
    }


    public static class RegisterEventProducer implements EventProducer<Identity, RegisterEvent> {
        @Override
        public Class<Identity> resultClass() {
            return Identity.class;
        }

        @Override
        public RegisterEvent apply(Identity identity) {
            return new RegisterEvent()
                    .setOwnerId(identity.getId())
                    .setIdentityId(identity.getId());
        }
    }
}
