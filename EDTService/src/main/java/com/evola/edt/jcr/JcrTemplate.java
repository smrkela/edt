package com.evola.edt.jcr;

import javax.jcr.PathNotFoundException;
import javax.jcr.Session;

import org.apache.log4j.Logger;

/**
 * @author Nikola
 * 
 */
public class JcrTemplate {
	Logger log = Logger.getLogger(getClass());

	private JcrSessionFactory sessionFactory;

	/**
	 * @param jcrCallback
	 * @return
	 */
	public <T> T execute(JcrCallback<T> jcrCallback) {
		Session session = sessionFactory.createSession();
		T doInJcr = null;
		
		try {
			
			doInJcr = jcrCallback.doInJcr(session);
			session.save();
			
		} catch (PathNotFoundException pnfe) {

			throw new ContentRepositoryException(pnfe);
			
		} catch (Exception e) {
			
			log.error(e, e);
			throw new ContentRepositoryException(e);
			
		} finally {
			
			session.logout();
		}
		return doInJcr;
	}

	/**
	 * @author Nikola
	 */
	public JcrSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @author Nikola
	 */
	public void setSessionFactory(JcrSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
