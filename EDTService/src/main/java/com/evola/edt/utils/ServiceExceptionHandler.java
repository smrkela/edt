package com.evola.edt.utils;

import javax.inject.Named;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Aspect
@Named
public class ServiceExceptionHandler {
	Logger log = Logger.getLogger(getClass());

	@AfterThrowing(pointcut = "execution(* com.evola.edt.service.*.*(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error)
			throws Throwable {
		log.error(error, error);
	}
}
