package com.evola.edt.model.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Daci 28.01.2014.
 * 
 */
@XmlRootElement(name = "userSync")
public class UserSyncDTO extends BaseDTO {

	private UserDTO user;
	private String errorMessage;
	private Boolean syncSuccessful;
	private Boolean eMailNotificationSent;
	

	@XmlElement(name = "user")
	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	
	@XmlAttribute(name = "errorMessage")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
	@XmlAttribute(name = "syncSuccessful")
	public Boolean getSyncSuccessful() {
		return syncSuccessful;
	}

	public void setSyncSuccessful(Boolean syncSuccessful) {
		this.syncSuccessful = syncSuccessful;
	}
	
	
	@XmlAttribute(name = "eMailNotificationSent")
	public Boolean geteMailNotificationSent() {
		return eMailNotificationSent;
	}

	public void seteMailNotificationSent(Boolean eMailNotificationSent) {
		this.eMailNotificationSent = eMailNotificationSent;
	}
}
