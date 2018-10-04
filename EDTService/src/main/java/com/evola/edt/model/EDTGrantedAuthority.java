package com.evola.edt.model;

import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class EDTGrantedAuthority extends BaseEntity implements GrantedAuthority {

	/**
	 * @author Nikola 22.04.2013.
	 */
	private static final long serialVersionUID = 1L;
	private String authority;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
