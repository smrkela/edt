package com.evola.edt.jcr;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * @author Nikola
 * 
 */
public interface JcrCallback<T> {

	/**
	 * @param session
	 * @return
	 * @throws RepositoryException
	 * @author Nikola
	 */
	public T doInJcr(Session session) throws RepositoryException;;

}
