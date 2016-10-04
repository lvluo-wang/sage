package me.icymint.sage.user.rest.support;

import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.def.PermissionStrategy;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by daniel on 2016/10/4.
 */
public class PermissionCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> permissionMap = metadata.getAnnotationAttributes(Permission.class.getName());
        Privilege[] privileges = (Privilege[]) permissionMap.get("value");
        if (privileges == null || privileges.length == 0) {
            return new ConditionOutcome(true, "No permission limit");
        }
        Stream<RoleType> roleTypeStream = Stream
                .of(privileges)
                .flatMap(privilege -> Stream.of(privilege.getRoleTypes()))
                .sorted()
                .distinct();
        PermissionStrategy strategy = (PermissionStrategy) permissionMap.get("strategy");
        if (strategy == PermissionStrategy.MATCH_ANY) {
            return new ConditionOutcome(roleTypeStream.anyMatch(role -> enable(context, role)), "Any");
        } else {
            return new ConditionOutcome(!roleTypeStream.anyMatch(role -> !enable(context, role)), "ALL");
        }
    }

    private boolean enable(ConditionContext context, RoleType role) {
        return context.getEnvironment().getProperty(Magics.PROP_ENABLE_API_ + role.name(), Boolean.class, true);
    }
}
