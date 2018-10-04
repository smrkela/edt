package com.evola.edt.model;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class AbstractTokenAction extends BaseEntity {
	private String token;
	@ManyToOne
	private User user;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	private Boolean confirmed;

	AbstractTokenAction() {
	}

	public AbstractTokenAction(String token, User user, Date creationDate) {
		super();
		this.token = token;
		this.user = user;
		this.creationDate = creationDate;
		this.confirmed = false;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}
}
