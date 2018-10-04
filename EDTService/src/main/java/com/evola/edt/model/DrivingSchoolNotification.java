package com.evola.edt.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Nikola 14.05.2013.
 * 
 */
@Entity
public class DrivingSchoolNotification extends BaseEntity {
	
	@Column(length = 256)
	@Size(max = 256)
	private String title;
	
	@Column(columnDefinition = "text")
	private String content;
	
	@Column(name="creationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@ManyToOne
	@NotNull
	private User author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private DrivingSchool school;
	
	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy="notification")
	private Set<DrivingSchoolNotificationComment> comments = new HashSet<DrivingSchoolNotificationComment>();
	
	private Boolean sendNotification;
	private Boolean requestNotificationReadConfirmation;
	
	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy="notification")
	private Set<DrivingSchoolNotificationConfirmation> confirmations = new HashSet<DrivingSchoolNotificationConfirmation>();

	
	public DrivingSchoolNotification() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Set<DrivingSchoolNotificationComment> getComments() {
		return comments;
	}

	public void setComments(Set<DrivingSchoolNotificationComment> comments) {
		this.comments = comments;
	}

	public DrivingSchool getSchool() {
		return school;
	}

	public void setSchool(DrivingSchool school) {
		this.school = school;
	}

	public Boolean getSendNotification() {
		return sendNotification;
	}

	public void setSendNotification(Boolean sendNotification) {
		this.sendNotification = sendNotification;
	}

	public Boolean getRequestNotificationReadConfirmation() {
		return requestNotificationReadConfirmation;
	}

	public void setRequestNotificationReadConfirmation(Boolean requestNotificationReadConfirmation) {
		this.requestNotificationReadConfirmation = requestNotificationReadConfirmation;
	}

	public Set<DrivingSchoolNotificationConfirmation> getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(Set<DrivingSchoolNotificationConfirmation> confirmations) {
		this.confirmations = confirmations;
	}	
}