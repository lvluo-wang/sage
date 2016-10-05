package me.icymint.sage.base.util;

import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.def.PermissionStrategy;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by daniel on 2016/10/4.
 */
public class Permissions {

    public static boolean matchesRole(Permission permission, Predicate<RoleType> predicate) {
        return permission != null
                && matchesRole(permission.value(), permission.strategy(), predicate);
    }

    public static boolean matchesRole(Privilege[] privileges, PermissionStrategy strategy, Predicate<RoleType> predicate) {
        if (privileges == null || privileges.length == 0) {
            return false;
        }
        Stream<RoleType> roleTypeStream = Stream
                .of(privileges)
                .flatMap(privilege -> Stream.of(privilege.getRoleTypes()))
                .sorted()
                .distinct();
        if (strategy == PermissionStrategy.MATCH_ANY) {
            return roleTypeStream.anyMatch(predicate);
        } else {
            return !roleTypeStream.anyMatch(r -> !predicate.test(r));
        }
    }

}
