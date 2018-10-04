package com.evola.edt.model.dto;

import java.util.Date;


/**
 * @author Daci, Jan 11, 2015
 *
 */
public class UserDrivingSchoolMembershipRequestDTO extends BaseDTO {
	
	private UserDTO user;
	private DrivingSchoolDTO drivingSchool;
	private String comment;
	private Boolean receiveNotifications;
	private Date creationDate;
	private String status;
	private Date decisionDate;
	private String decisionComment;
	private String eMail;
	private String confirmationToken;
	private DrivingSchoolStudentDTO drivingSchoolStudent;
	
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public DrivingSchoolDTO getDrivingSchool() {
		return drivingSchool;
	}
	public void setDrivingSchool(DrivingSchoolDTO drivingSchool) {
		this.drivingSchool = drivingSchool;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Boolean getReceiveNotifications() {
		return receiveNotifications;
	}
	public void setReceiveNotifications(Boolean receiveNotifications) {
		this.receiveNotifications = receiveNotifications;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDecisionDate() {
		return decisionDate;
	}
	public void setDecisionDate(Date decisionDate) {
		this.decisionDate = decisionDate;
	}
	public String getDecisionComment() {
		return decisionComment;
	}
	public void setDecisionComment(String decisionComment) {
		this.decisionComment = decisionComment;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getConfirmationToken() {
		return confirmationToken;
	}
	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}
	public DrivingSchoolStudentDTO getDrivingSchoolStudent() {
		return drivingSchoolStudent;
	}
	public void setDrivingSchoolStudent(DrivingSchoolStudentDTO drivingSchoolStudent) {
		this.drivingSchoolStudent = drivingSchoolStudent;
	}
}
