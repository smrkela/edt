package com.evola.edt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * @author Daci, Jan 5, 2015
 *
 */
@Entity
public class DrivingSchoolNotificationConfirmation extends BaseEntity {

	@ManyToOne
	@NotNull
	private DrivingSchoolNotification notification;
	
	@ManyToOne
	@NotNull
	private DrivingSchoolStudent student;
	
	@ManyToOne
	@NotNull
	private DrivingSchool school;
	
	@NotNull
	@Column(name="creationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@NotNull
	private String token;
	
	private Boolean confirmed;
	
	@Column(name="confirmationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date confirmationDate;
	

	public DrivingSchoolNotification getNotification() {
		return notification;
	}

	public void setNotification(DrivingSchoolNotification notification) {
		this.notification = notification;
	}

	public DrivingSchoolStudent getStudent() {
		return student;
	}

	public void setStudent(DrivingSchoolStudent student) {
		this.student = student;
	}

	public DrivingSchool getSchool() {
		return school;
	}

	public void setSchool(DrivingSchool school) {
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
