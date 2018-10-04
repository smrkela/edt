package com.evola.edt.service.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Nikola
 * 
 */
public class WebApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		WebApplicationContextHolder.applicationContext = applicationContext;
	}

}
