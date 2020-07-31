package com.example.apsdemo.utils;

import com.example.apsdemo.domain.TestItemCreateParams;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
public class AspectLogger {

    public static final Logger log = LoggerFactory.getLogger(AspectLogger.class);

    @Pointcut("execution(public * com.example.apsdemo.controller.TestItemController.createTestItem(com.example.apsdemo.domain.TestItemCreateParams))&&args(params))")
    public void createTestItem(TestItemCreateParams params) {

    }

    @Around(value = "createTestItem(params)", argNames = "pjp,params")
    public void logCreateTestItem(ProceedingJoinPoint pjp, TestItemCreateParams params) {
        try {
            log.info("开始创建测试明细。");
            pjp.proceed();
            log.info("创建测试明细完成，版号" + params.getWaferNr() + "。");
        } catch (Throwable e) {
            log.info("创建测试明细失败！");
        }
    }
}
