package com.evola.edt.service.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.UserDTO;

@XmlRootElement(name="login")
public class LoginDTO {

	private UserDTO user;
	private Boolean loginSuccessful;
	private String loginMessage;

	@XmlElement(name="user")
	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	@XmlAttribute(name="success")
	public Boolean getLoginSuccessful() {
		return loginSuccessful;
	}

	public void setLoginSuccessful(Boolean loginSuccessful) {
		this.loginSuccessful = loginSuccessful;
	}

	@XmlAttribute(name="message")
	public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}

}
