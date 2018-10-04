package com.evola.edt.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Entity
public class LearningSession extends BaseEntity {

	@Temporal(TemporalType.TIMESTAMP)
	private Date start;
	private String uid;
	@ManyToOne(fetch=FetchType.LAZY)
	private User user; 
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="session")
	private Set<UserQuestionStatLearn> learns;

	public LearningSession() {
		// TODO Auto-generated constructor stub
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<UserQuestionStatLearn> getLearns() {
		return learns;
	}

	public void setLearns(Set<UserQuestionStatLearn> learns) {
		this.learns = learns;
	}

}
