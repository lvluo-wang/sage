package me.icymint.sage.user.rest.authorization;

import me.icymint.sage.base.spec.def.BaseCode;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.exception.UnauthorizedException;
import me.icymint.sage.user.spec.annotation.Permission;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Denied {@link Permission} without {@link me.icymint.sage.user.spec.annotation.CheckToken}
 * Created by daniel on 16/9/4.
 */
@Component
@Aspect
@Order(Magics.AOP_ORDER_PERMISSION)
public class DefaultPermissionAuthorization {

    @Autowired
    ApplicationContext context;

    @Pointcut("within(@me.icymint.sage.user.spec.annotation.Permission *) && !@annotation(me.icymint.sage.user.spec.annotation.CheckToken)")
    public void classDenied() {
    }

    @Before("classDenied()")
    public void permissionWithoutCheckToken() {
        throw new UnauthorizedException(context, BaseCode.AUTHORIZATION_REQUIRED);
    }

}
