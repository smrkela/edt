package com.evola.edt.service.exception;

/**
 * @author Nikola 09.04.2013.
 *
 */
public class EDTServiceException extends RuntimeException {

	/**
	 * @author Nikola 09.04.2013.
	 */
	private static final long serialVersionUID = 1L;

	public EDTServiceException(Throwable originalException) {
		super(originalException);
	}

	public EDTServiceException(String message) {
		super(message);
	}
}
