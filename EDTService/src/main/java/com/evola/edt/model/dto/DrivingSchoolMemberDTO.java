package com.evola.edt.model.dto;

import java.util.Date;

/**
 * @author Nikola 09.04.2013.
 * 
 */
public class DrivingSchoolMemberDTO extends BaseDTO {

	private String className = "DrivingSchoolMember";
	private String role;
	private Date creationDate;
	private DrivingSchoolDTO school;
	private UserDTO user;
	
	private Long schoolId;
	private String email;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public DrivingSchoolDTO getSchool() {
		return school;
	}

	public void setSchool(DrivingSchoolDTO school) {
		this.school = school;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
