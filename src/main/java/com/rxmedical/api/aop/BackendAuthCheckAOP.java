package com.rxmedical.api.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class BackendAuthCheckAOP {

    // 切點
    @Pointcut(value = "execution(* com.rxmedical.api.controller.ProductController.getMaterialList(Integer))")
    public void getMaterialList(){};

    // 前置通知
    @Before(value = "getMaterialList()")
    public void beforeCheckAuth(JoinPoint joinPoint) {
        System.out.println("讓我看看！！");
//        Object[] args = joinPoint.getArgs();
//        System.out.println("userId equals:" + args[0]);
    }

}
