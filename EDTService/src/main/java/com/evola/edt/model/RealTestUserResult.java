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

@Entity
public class RealTestUserResult extends BaseEntity {

	@ManyToOne
	private User user;
	
	private String username; //ako nema relacije ka user-u, mora postojati username za ovaj rezultat

	@ManyToOne
	@NotNull
	private RealTest test;

	private Integer correctAnswers;

	private Integer wrongAswers;

	private Float correctPercent;
	
	private Integer points;

	@NotNull
	private Integer timeTaken; // vreme polaganja u sekundama
	
	@Column(name="creationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "result", fetch = FetchType.EAGER)
	private Set<RealTestUserResultQuestion> questionResults = new HashSet<RealTestUserResultQuestion>();
	
	@NotNull
	private Boolean hasPassedTest;

	public RealTestUserResult() {
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public RealTest getTest() {
		return test;
	}

	public void setTest(RealTest test) {
		this.test = test;
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

	public Integer getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(Integer timeTaken) {
		this.timeTaken = timeTaken;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Set<RealTestUserResultQuestion> getQuestionResults() {
		return questionResults;
	}

	public void setQuestionResults(Set<RealTestUserResultQuestion> questionResults) {
		this.questionResults = questionResults;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getHasPassedTest() {
		return hasPassedTest;
	}

	public void setHasPassedTest(Boolean hasPassedTest) {
		this.hasPassedTest = hasPassedTest;
	}

}
