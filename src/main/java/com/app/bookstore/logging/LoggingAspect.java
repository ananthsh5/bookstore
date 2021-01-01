package com.app.bookstore.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Ananth Shanmugam
 * Class that demonstrates aspectj usage for logging - cross cutting concern  
 */
@Component
@Aspect
public class LoggingAspect {
    static Logger log = LoggerFactory.getLogger(LoggingAspect.class.getName());


    @Before("execution(* com.app.bookstore.service.impl.CustomerServiceImpl.*(..)) ")
    public void logBookStoreBeforeMethod(JoinPoint joinpoint) throws Throwable{
        log.info("Before Book Store App Method: " +joinpoint.getSignature());

    }
    @After("execution(* com.app.bookstore.service.impl.CustomerServiceImpl.*(..)) ")
    public void logBookStoreAfterMethod(JoinPoint joinpoint) throws Throwable{
        log.info("After Book Store App Method: " +joinpoint.getSignature());

    }

}
