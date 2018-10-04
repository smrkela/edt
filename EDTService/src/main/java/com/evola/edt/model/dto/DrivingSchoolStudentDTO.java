package com.evola.edt.model.dto;

import java.util.Date;

import com.evola.edt.model.DrivingLicenceCategory;

public class DrivingSchoolStudentDTO extends BaseDTO {

	private UserDTO user;
	private DrivingSchoolDTO drivingSchool;
	private Date creationDate;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private Boolean isMale;
	private String address;
	private String city;
	private Date registerDate;
	private Boolean isTheoryCompleted;
	private Boolean isTheoryPassed;
	private Date theoryPassedDate;
	private Boolean isPracticeCompleted;
	private Boolean isPracticePassed;
	private Date practicePassedDate;
	private Boolean isFirstAidPassed;
	private Date firstAidPassedDate;
	private Boolean isAllPassed;
	private Date allPassedDate;
	private DrivingLicenceCategoryDTO category;
	private String comment;
	private Boolean sendNotifications;
	private Boolean inviteToJoin;
	private Date invitationSent;
	private Boolean deleted;
	
	private Long schoolId;
	private Long categoryId;
	private String membershipRequestToken;
	private Long userId;
	
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
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
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
	public DrivingLicenceCategoryDTO getCategory() {
		return category;
	}
	public void setCategory(DrivingLicenceCategoryDTO category) {
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
	public Long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	public Date getPracticePassedDate() {
		return practicePassedDate;
	}
	public void setPracticePassedDate(Date practicePassedDate) {
		this.practicePassedDate = practicePassedDate;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
	
	public String getMembershipRequestToken() {
		return membershipRequestToken;
	}
	public void setMembershipRequestToken(String membershipRequestToken) {
		this.membershipRequestToken = membershipRequestToken;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}