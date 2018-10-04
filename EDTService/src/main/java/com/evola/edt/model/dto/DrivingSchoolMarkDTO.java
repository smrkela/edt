package com.evola.edt.model.dto;

import java.util.Date;

public class DrivingSchoolMarkDTO extends BaseDTO {

	private DrivingSchoolDTO school;
	private Date date;
	private String comment;
	private UserDTO author;
	private Integer mark;
	private Boolean editedByLoggedInUser = false;

	public DrivingSchoolDTO getSchool() {
		return school;
	}

	public void setSchool(DrivingSchoolDTO school) {
		this.school = school;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public UserDTO getAuthor() {
		return author;
	}

	public void setAuthor(UserDTO author) {
		this.author = author;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public Boolean getEditedByLoggedInUser() {
		return editedByLoggedInUser;
	}

	public void setEditedByLoggedInUser(Boolean editedByLoggedInUser) {
		this.editedByLoggedInUser = editedByLoggedInUser;
	}

}