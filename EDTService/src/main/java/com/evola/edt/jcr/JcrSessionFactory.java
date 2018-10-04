package com.evola.edt.jcr;

import javax.jcr.Session;

/**
 * @author Nikola
 * 
 */
public interface JcrSessionFactory {

	/**
	 * @return
	 * @author Nikola
	 */
	public Session createSession();
}