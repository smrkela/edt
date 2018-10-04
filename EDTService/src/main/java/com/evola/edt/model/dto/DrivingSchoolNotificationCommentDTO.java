package com.evola.edt.model.dto;

import java.util.Date;

public class DrivingSchoolNotificationCommentDTO extends BaseDTO {

	private Date date;
	private UserDTO author;
	private DrivingSchoolNotificationDTO notification;
	private String comment;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public UserDTO getAuthor() {
		return author;
	}

	public void setAuthor(UserDTO author) {
		this.author = author;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public DrivingSchoolNotificationDTO getNotification() {
		return notification;
	}

	public void setNotification(DrivingSchoolNotificationDTO notification) {
		this.notification = notification;
	}

}