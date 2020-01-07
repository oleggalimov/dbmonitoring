package org.oleggalimov.dbmonitoring.back.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class LoggerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);
    @Pointcut(value = "@annotation(org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent)")
    public void annotationPointCut() {
    }

    @Before("annotationPointCut()")
//    @Before(value = "@annotation(org.oleggalimov.dbmonitoring.back.annotations.LogEvent)")
    public void logEvent(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LogHttpEvent annotation = signature.getMethod().getAnnotation(LogHttpEvent.class);
        LOGGER.info("Event: " + annotation.eventType() + ", message: " + annotation.message());
    }

//    @Around(value = "@annotation(org.oleggalimov.dbmonitoring.back.annotations.LogEvent)")
//    public Object logEvent(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        Object[] args = proceedingJoinPoint.getArgs();
//        for (Object arg : args) {
//            System.out.println("Class is: " + arg.getClass() + "value is: "+arg);
//        }
//        return proceedingJoinPoint.proceed();
//    }
}
