package com.evola.edt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Entity
public class QuestionAnswer extends BaseEntity {

	private Integer orderIndex;
	@ManyToOne
	private Question question;
	@Column(length=1024)
	private String text;
	private Boolean correct;

	public QuestionAnswer() {
		// TODO Auto-generated constructor stub
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getCorrect() {
		return correct;
	}

	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

}
