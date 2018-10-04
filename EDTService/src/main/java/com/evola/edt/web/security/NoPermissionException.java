package com.evola.edt.web.security;

public class NoPermissionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoPermissionException(String string) {

		super(string);
	}

}
