package com.evola.edt.jcr;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

/**
 * @author Nikola
 * 
 */
public class JcrSessionFactoryImpl implements JcrSessionFactory {
	private Logger log = Logger.getLogger(getClass());
	private Repository repository;
	Credentials credentials;

	/**
	 * @author Nikola
	 */
	public Session createSession() {
		Assert.notNull(repository, "Repository cannot be null.");
		Assert.notNull(repository, "Credentials cannot be null.");
		Session session = null;
		try {
			session = repository.login(credentials);
		} catch (LoginException e) {
			log.error(e, e);
		} catch (RepositoryException e) {
			log.error(e, e);
		}
		return session;
	}

	/**
	 * @author Nikola
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @author Nikola
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	/**
	 * @author Nikola
	 */
	public Credentials getCredentials() {
		return credentials;
	}

	/**
	 * @author Nikola
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
