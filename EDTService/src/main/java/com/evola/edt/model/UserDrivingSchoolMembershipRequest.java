package com.evola.edt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * @author Daci, Jan 11, 2015
 *
 */
@Entity
public class UserDrivingSchoolMembershipRequest extends BaseEntity {

	@ManyToOne
	@NotNull
	private User user;
	
	@ManyToOne
	@NotNull
	private DrivingSchool drivingSchool;
	
	@Column(nullable = true, length = 2000)
	private String comment;
	
	@NotNull
	private Boolean receiveNotifications;
	
	@NotNull
	@Column(name="creationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private MembershipRequestStatus status;
	
	@Column(name="decisionDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date decisionDate;
	
	@Column(nullable = true, length = 2000)
	private String decisionComment;

	private String eMail;
	
	private String confirmationToken;
	
	@ManyToOne
	private DrivingSchoolStudent drivingSchoolStudent;
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DrivingSchool getDrivingSchool() {
		return drivingSchool;
	}

	public void setDrivingSchool(DrivingSchool drivingSchool) {
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

	public MembershipRequestStatus getStatus() {
		return status;
	}

	public void setStatus(MembershipRequestStatus status) {
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
	
	public DrivingSchoolStudent getDrivingSchoolStudent() {
		return drivingSchoolStudent;
	}

	public void setDrivingSchoolStudent(DrivingSchoolStudent drivingSchoolStudent) {
		this.drivingSchoolStudent = drivingSchoolStudent;
	}
}
