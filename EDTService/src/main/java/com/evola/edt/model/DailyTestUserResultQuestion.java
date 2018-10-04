package com.evola.edt.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * @author Daci, 13.05.2014.
 */

@Entity
public class DailyTestUserResultQuestion extends BaseEntity {

	@ManyToOne
	@NotNull
	private DailyTestUserResult result;
	
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

	public DailyTestUserResult getResult() {
		return result;
	}

	public void setResult(DailyTestUserResult result) {
		this.result = result;
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

}
