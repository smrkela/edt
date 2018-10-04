package com.evola.edt.model;

import java.util.Date;

import javax.persistence.Entity;

/**
 * @author Nikola 18.05.2013.
 * 
 */
@Entity
public class ResetPassword extends AbstractTokenAction {
	ResetPassword() {
	}

	public ResetPassword(String token, User user, Date creationDate) {
		super(token, user, creationDate);
	}
}
