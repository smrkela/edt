package com.evola.edt.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class MarathonTest extends BaseEntity {

	@ManyToOne
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date creationDate;

	private Integer correctAnswers;

	private Integer wrongAswers;

	private Float correctPercent;

	private Integer totalQuestions;

	private Integer points;

	private String uid;

	// vreme polaganja u sekundama
	@NotNull
	private Integer timeTaken;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "test")
	private Set<MarathonTestUserResultQuestion> questionResults = new HashSet<MarathonTestUserResultQuestion>();

	public MarathonTest() {
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(Integer correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public Integer getWrongAswers() {
		return wrongAswers;
	}

	public void setWrongAswers(Integer wrongAswers) {
		this.wrongAswers = wrongAswers;
	}

	public Float getCorrectPercent() {
		return correctPercent;
	}

	public void setCorrectPercent(Float correctPercent) {
		this.correctPercent = correctPercent;
	}

	public Integer getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uniqueId) {
		this.uid = uniqueId;
	}

	public Set<MarathonTestUserResultQuestion> getQuestionResults() {
		return questionResults;
	}

	public void setQuestionResults(Set<MarathonTestUserResultQuestion> questionResults) {
		this.questionResults = questionResults;
	}

	public Integer getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(Integer timeTaken) {
		this.timeTaken = timeTaken;
	}

}
