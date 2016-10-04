package me.icymint.sage.user.rest.authorization;

import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.util.Exceptions;
import me.icymint.sage.user.core.service.TokenServiceImpl;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.annotation.Permission;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by daniel on 16/9/4.
 */
@Component
@Aspect
@Order(Magics.AOP_ORDER_TOKEN)
public class DefaultTokenAuthorization {

    @Autowired
    ApplicationContext context;
    @Autowired
    TokenServiceImpl tokenService;


    @Pointcut("execution(* me.icymint.sage.user.rest.controller.TokenController.isExpire())")
    public void specialApi() {
    }

    @Before("@annotation(checkToken) && specialApi()")
    public void authorizeInExpired(CheckToken checkToken) {
        try {
            tokenService.authorize(checkToken, null, false);
        } catch (Exception e) {
            Exceptions.catching(e, false);
        }
    }

    @Before("@annotation(checkToken) && !specialApi()")
    public void authorize(JoinPoint joinPoint, CheckToken checkToken) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Permission permission = signature.getMethod().getAnnotation(Permission.class);
        tokenService.authorize(checkToken, permission, true);
    }

}
