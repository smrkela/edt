package com.evola.edt.utils.social;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import com.evola.edt.managers.RegistrationManager;
import com.evola.edt.model.User;

public class SocialConnectionSignUp implements ConnectionSignUp {

	private RegistrationManager registrationManager;

	@Override
	public String execute(Connection<?> connection) {

		User user = registrationManager.createFromSocialConnection(connection);
		
		return user != null ? user.getUsername() : null;
	}

	public RegistrationManager getRegistrationManager() {
		return registrationManager;
	}

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

}
