package com.evola.edt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Daci, 23.04.2013.
 *
 */

@Entity
public class DrivingSchoolNotificationComment extends BaseEntity {

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private DrivingSchoolNotification notification;
	
	@Column(nullable = false, length = 2000)
	private String comment;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public DrivingSchoolNotification getNotification() {
		return notification;
	}

	public void setNotification(DrivingSchoolNotification notification) {
		this.notification = notification;
	}
	
}