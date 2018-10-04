package com.evola.edt.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.evola.edt.model.UserDrivingSchoolMembershipRequest;

@Entity
public class DrivingSchoolStudent extends BaseEntity {
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private DrivingSchool drivingSchool;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Column(length = 500)
	@Size(max = 500)
	private String firstName;

	@Column(length = 500)
	@Size(max = 500)
	private String lastName;
	
	@Column(length = 500)
	@Size(max = 500)
	@Email
	private String email;
	
	@Column(length = 500)
	@Size(max = 500)
	private String phone;

	@NotNull
	private Boolean isMale;
	
	@Column(length = 500)
	@Size(max = 500)
	private String address;

	@Column(length = 500)
	@Size(max = 500)
	private String city;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerDate;
	
	@NotNull
	private Boolean isTheoryCompleted;
	
	@NotNull
	private Boolean isTheoryPassed;

	@Temporal(TemporalType.TIMESTAMP)
	private Date theoryPassedDate;

	@NotNull
	private Boolean isPracticeCompleted;
	
	@NotNull
	private Boolean isPracticePassed;

	@Temporal(TemporalType.TIMESTAMP)
	private Date practicePassedDate;

	@NotNull
	private Boolean isFirstAidPassed;

	@Temporal(TemporalType.TIMESTAMP)
	private Date firstAidPassedDate;

	@NotNull
	private Boolean isAllPassed;

	@Temporal(TemporalType.TIMESTAMP)
	private Date allPassedDate;

	@ManyToOne
	private DrivingLicenceCategory category;
	
	@Column(length = 10000)
	@Size(max = 10000)
	private String comment;

	@NotNull
	private Boolean sendNotifications;
	
	@NotNull
	private Boolean inviteToJoin;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date invitationSent;
	
	@NotNull
	private Boolean deleted;
	
	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "drivingSchoolStudent")
	private Set<UserDrivingSchoolMembershipRequest> membershipRequest = new HashSet<UserDrivingSchoolMembershipRequest>();
	
	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getIsMale() {
		return isMale;
	}

	public void setIsMale(Boolean isMale) {
		this.isMale = isMale;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Boolean getIsTheoryCompleted() {
		return isTheoryCompleted;
	}

	public void setIsTheoryCompleted(Boolean isTheoryCompleted) {
		this.isTheoryCompleted = isTheoryCompleted;
	}

	public Boolean getIsTheoryPassed() {
		return isTheoryPassed;
	}

	public void setIsTheoryPassed(Boolean isTheoryPassed) {
		this.isTheoryPassed = isTheoryPassed;
	}

	public Date getTheoryPassedDate() {
		return theoryPassedDate;
	}

	public void setTheoryPassedDate(Date theoryPassedDate) {
		this.theoryPassedDate = theoryPassedDate;
	}

	public Boolean getIsPracticeCompleted() {
		return isPracticeCompleted;
	}

	public void setIsPracticeCompleted(Boolean isPracticeCompleted) {
		this.isPracticeCompleted = isPracticeCompleted;
	}

	public Boolean getIsPracticePassed() {
		return isPracticePassed;
	}

	public void setIsPracticePassed(Boolean isPracticePassed) {
		this.isPracticePassed = isPracticePassed;
	}

	public Date getPracticePassedDate() {
		return practicePassedDate;
	}

	public void setPracticePassedDate(Date practicePassedDate) {
		this.practicePassedDate = practicePassedDate;
	}

	public Boolean getIsFirstAidPassed() {
		return isFirstAidPassed;
	}

	public void setIsFirstAidPassed(Boolean isFirstAidPassed) {
		this.isFirstAidPassed = isFirstAidPassed;
	}

	public Date getFirstAidPassedDate() {
		return firstAidPassedDate;
	}

	public void setFirstAidPassedDate(Date firstAidPassedDate) {
		this.firstAidPassedDate = firstAidPassedDate;
	}

	public Boolean getIsAllPassed() {
		return isAllPassed;
	}

	public void setIsAllPassed(Boolean isAllPassed) {
		this.isAllPassed = isAllPassed;
	}

	public Date getAllPassedDate() {
		return allPassedDate;
	}

	public void setAllPassedDate(Date allPassedDate) {
		this.allPassedDate = allPassedDate;
	}

	public DrivingLicenceCategory getCategory() {
		return category;
	}

	public void setCategory(DrivingLicenceCategory category) {
		this.category = category;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getSendNotifications() {
		return sendNotifications;
	}

	public void setSendNotifications(Boolean sendNotifications) {
		this.sendNotifications = sendNotifications;
	}

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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public Boolean getInviteToJoin() {
		return inviteToJoin;
	}

	public void setInviteToJoin(Boolean inviteToJoin) {
		this.inviteToJoin = inviteToJoin;
	}
	
	public Date getInvitationSent() {
		return invitationSent;
	}

	public void setInvitationSent(Date invitationSent) {
		this.invitationSent = invitationSent;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
