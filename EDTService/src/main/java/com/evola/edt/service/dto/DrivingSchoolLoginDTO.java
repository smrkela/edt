package com.evola.edt.service.dto;

import java.util.Date;

public class DrivingSchoolLoginDTO {

	private Long drivingSchoolId;
	private String drivingSchoolName;
	private Long userId;
	private String userName;
	private Date loginDate;
	private Date registrationDate;
	private Integer totalMembers = 0;
	
	public Long getDrivingSchoolId() {
		return drivingSchoolId;
	}
	public void setDrivingSchoolId(Long drivingSchoolId) {
		this.drivingSchoolId = drivingSchoolId;
	}
	public String getDrivingSchoolName() {
		return drivingSchoolName;
	}
	public void setDrivingSchoolName(String drivingSchoolName) {
		this.drivingSchoolName = drivingSchoolName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	public Integer getTotalMembers() {
		return totalMembers;
	}
	public void setTotalMembers(Integer totalMembers) {
		this.totalMembers = totalMembers;
	}

}
