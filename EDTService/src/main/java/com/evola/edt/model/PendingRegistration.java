package com.evola.edt.model;

import java.util.Date;

import javax.persistence.Entity;

/**
 * @author Nikola 27.04.2013.
 * 
 */
@Entity
public class PendingRegistration extends AbstractTokenAction {


	PendingRegistration() {
	}

	public PendingRegistration(String token, User user, Date creationDate) {
		super(token, user, creationDate);
	}


}
