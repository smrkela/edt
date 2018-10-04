package com.evola.edt.model.dto;

public class DrivingSchoolCarDTO extends BaseDTO {

	private DrivingSchoolDTO school;
	private Integer orderIndex;
	private String make;
	private String model;
	private String type;
	private String description;
	private Integer year;
	
	private Long schoolId;
	private String image;

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

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
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
	
	public String getImagePath() {

		return "/drivingSchools/" + school.getId() + "/cars/" + getId();
	}

}