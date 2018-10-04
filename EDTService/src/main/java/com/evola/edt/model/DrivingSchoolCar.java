package com.evola.edt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class DrivingSchoolCar extends BaseEntity {

	@Column(nullable = false, length = 200)
	private String make;
	
	@Column(nullable = false, length = 200)
	private String model;

	@Column(length = 200)
	private String type;
	
	@Column(length = 2000)
	private String description;
	
	@Column
	private Integer year;
	
	@Column
	private Integer orderIndex;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private DrivingSchool school;

	public DrivingSchool getSchool() {
		return school;
	}

	public void setSchool(DrivingSchool school) {
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

	
}