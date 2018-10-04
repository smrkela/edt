package com.evola.edt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Entity
public class UserQuestionStatLearn extends BaseEntity {

	@ManyToOne
	private LearningSession session;
	@ManyToOne(fetch=FetchType.LAZY)
	private Question question;
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	public UserQuestionStatLearn() {
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public LearningSession getSession() {
		return session;
	}

	public void setSession(LearningSession session) {
		this.session = session;
	}


}
