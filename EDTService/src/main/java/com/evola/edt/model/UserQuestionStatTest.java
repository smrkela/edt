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
public class UserQuestionStatTest extends BaseEntity {

	@ManyToOne
	private TestingSession session;
	@ManyToOne(fetch=FetchType.LAZY)
	private Question question;
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	private Boolean correct;
	

	public UserQuestionStatTest() {
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

	public Boolean getCorrect() {
		return correct;
	}

	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}

	public TestingSession getSession() {
		return session;
	}

	public void setSession(TestingSession session) {
		this.session = session;
	}


}
