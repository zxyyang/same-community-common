package com.same.community.common.util.aop.asp;

import com.same.community.common.meta.context.UserContext;
import com.same.community.common.util.aop.CheckUserContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author Zixuan.Yang
 * @date 2024/7/22 18:27
 */
@Aspect
@Component
public class CheckUserContextAspect {

    @Before("@annotation(checkUserContext)")
    public void processSensitiveFields(JoinPoint joinPoint, CheckUserContext checkUserContext) throws Throwable {
        UserContext.checkLogin();
    }
}
