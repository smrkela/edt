package com.evola.edt.model.dto;

public class DrivingSchoolEmployeeDTO extends BaseDTO {

	private String firstName;
	private String lastName;
	private String role;
	private String comment;
	private DrivingSchoolDTO school;
	private Integer orderIndex;
	private String experience;
	
	private Long schoolId;
	private String image;

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public DrivingSchoolDTO getSchool() {
		return school;
	}

	public void setSchool(DrivingSchoolDTO school) {
		this.school = school;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getImagePath() {

		return "/drivingSchools/" + school.getId() + "/employees/" + getId();
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}