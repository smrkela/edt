package com.evola.edt.model.dto;

import java.util.Date;

/**
 * @author Daci, Jan 5, 2015
 *
 */
public class DrivingSchoolNotificationConfirmationDTO extends BaseDTO {

	private DrivingSchoolNotificationDTO notification;
	private DrivingSchoolStudentDTO student;
	private DrivingSchoolDTO school;
	private Date creationDate;
	private String token;
	private Boolean confirmed;
	private Date confirmationDate;
	
	public DrivingSchoolNotificationConfirmationDTO() {
		super();
	}

	
	public DrivingSchoolNotificationDTO getNotification() {
		return notification;
	}

	public void setNotification(DrivingSchoolNotificationDTO notification) {
		this.notification = notification;
	}

	public DrivingSchoolStudentDTO getStudent() {
		return student;
	}

	public void setStudent(DrivingSchoolStudentDTO student) {
		this.student = student;
	}

	public DrivingSchoolDTO getSchool() {
		return school;
	}


	public void setSchool(DrivingSchoolDTO school) {
		this.school = school;
	}


	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}
	
}
