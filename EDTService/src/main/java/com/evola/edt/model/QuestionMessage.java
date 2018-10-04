package com.evola.edt.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class QuestionMessage extends BaseEntity {

	@Temporal(TemporalType.TIMESTAMP)
	private Date reportingDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private Question question;
	
	@Column(nullable = false, length = 2000)
	private String message;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private QuestionMessage parentMessage;
	
	@OneToMany(mappedBy="parentMessage")
	private List<QuestionMessage> childMessages;
	
	public Date getReportingDate() {
		return reportingDate;
	}
	
	public void setReportingDate(Date reportingDate) {
		this.reportingDate = reportingDate;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public QuestionMessage getParentMessage() {
		return parentMessage;
	}

	public void setParentMessage(QuestionMessage parentMessage) {
		this.parentMessage = parentMessage;
	}

	public List<QuestionMessage> getChildMessages() {
		return childMessages;
	}

	public void setChildMessages(List<QuestionMessage> childMessages) {
		this.childMessages = childMessages;
	}
	
}