package com.evola.edt.utils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Daci, 23.12.2013.
 *
 */
public class SessionCounter implements HttpSessionListener {

	private static int activeSessions = 0;
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		activeSessions++;
	}

	
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		if(activeSessions > 0){
			activeSessions--;
		}
	}
	
	
	/**
	 * @return int - broj aktivnih sesija 
	 */
	public static int getActiveSessions(){
		return activeSessions;
	}

}
