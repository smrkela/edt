package com.evola.edt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class DrivingSchoolMember extends BaseEntity {

	@NotNull
	@ManyToOne
	private User user;

	@NotNull
	@ManyToOne
	private DrivingSchool drivingSchool;
	
	@NotNull
	@Size(min = 3, max = 10)
	private String role;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@ManyToOne
	private User author;

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

}
