package com.evola.edt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class DrivingSchoolEmployee extends BaseEntity {

	@Column(nullable = false, length = 200)
	private String firstName;
	
	@Column(nullable = false, length = 200)
	private String lastName;

	@Column(nullable = false, length = 200)
	private String role;
	
	@Column(length = 2000)
	private String comment;
	
	@Column(length = 500)
	private String experience;
	
	private Integer orderIndex;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private DrivingSchool school;

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

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	
}