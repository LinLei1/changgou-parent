package com.changgou.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
//@Component
public class TimeAspect {

    @Around("execution(* com.changgou.controller.BrandController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable{
        System.out.println("time aspect start");

        long start = new Date().getTime();

        Object object = pjp.proceed();  //相当于调用被拦截的方法

        Object[] args = pjp.getArgs();  //获取当前代理方法参数

        for (Object arg: args){
            System.out.println("arg is :"+arg);
        }

        System.out.println("time aspect 耗时:" +(new Date().getTime()-start));

        System.out.println("time aspect end");

        return object;  //代理方法执行的结果
    }
}
