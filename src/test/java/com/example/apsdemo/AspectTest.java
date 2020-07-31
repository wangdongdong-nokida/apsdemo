package com.example.apsdemo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@Aspect
public class AspectTest {
    @Pointcut("execution(public * com.example.apsdemo.TestController.calcChangeName(String))&&args(name)")
    public void test(String name){}

    @Around("test(name)")
    public void around(ProceedingJoinPoint pjp, String name){
        try {
            pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
