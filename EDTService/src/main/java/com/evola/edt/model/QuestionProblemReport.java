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
public class QuestionProblemReport extends BaseEntity {

	@Temporal(TemporalType.TIMESTAMP)
	private Date reportingDate;
	@ManyToOne(fetch = FetchType.LAZY)
	private ProblemCategory problemCategory;
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
	private Question question;
	@Column(nullable = false, length = 2000)
	private String userComment;
	@Column(nullable = false)
	private boolean fixed;
	private Date fixDate;
	
	
	public Date getReportingDate() {
		return reportingDate;
	}
	
	public void setReportingDate(Date reportingDate) {
		this.reportingDate = reportingDate;
	}
	
	public ProblemCategory getProblemCategory() {
		return problemCategory;
	}

	public void setProblemCategory(ProblemCategory problemCategory) {
		this.problemCategory = problemCategory;
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
	
	public String getUserComment() {
		return userComment;
	}
	
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	
	public boolean isFixed() {
		return fixed;
	}
	
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public Date getFixDate() {
		return fixDate;
	}

	public void setFixDate(Date fixDate) {
		this.fixDate = fixDate;
	}
}