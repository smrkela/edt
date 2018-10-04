package com.evola.edt.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class MarathonTestUserResultQuestion extends BaseEntity {

	@ManyToOne
	@NotNull
	private MarathonTest test;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<QuestionAnswer> userAnswers = new HashSet<QuestionAnswer>();

	@ManyToOne
	private Question question;

	private Boolean isCorrect;

	private Boolean hasAnswered;

	public Set<QuestionAnswer> getUserAnswers() {
		return userAnswers;
	}

	public void setUserAnswers(Set<QuestionAnswer> userAnswers) {
		this.userAnswers = userAnswers;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Boolean getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public Boolean getHasAnswered() {
		return hasAnswered;
	}

	public void setHasAnswered(Boolean hasAnswered) {
		this.hasAnswered = hasAnswered;
	}

	public MarathonTest getTest() {
		return test;
	}

	public void setTest(MarathonTest test) {
		this.test = test;
	}

}
