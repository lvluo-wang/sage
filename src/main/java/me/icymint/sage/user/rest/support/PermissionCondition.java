package me.icymint.sage.user.rest.support;

import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.util.Permissions;
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.def.PermissionStrategy;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * Created by daniel on 2016/10/4.
 */
public class PermissionCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> permissionMap = metadata.getAnnotationAttributes(Permission.class.getName());
        Privilege[] privileges = (Privilege[]) permissionMap.get("value");
        PermissionStrategy strategy = (PermissionStrategy) permissionMap.get("strategy");
        return new ConditionOutcome(Permissions.matchesRole(privileges, strategy, role -> enable(context, role)), "Permission limit");
    }

    private boolean enable(ConditionContext context, RoleType role) {
        return context.getEnvironment().getProperty(Magics.PROP_ENABLE_API_ + role.name(), Boolean.class, true);
    }
}
