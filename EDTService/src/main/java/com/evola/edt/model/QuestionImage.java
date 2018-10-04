package com.evola.edt.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Entity
public class QuestionImage extends BaseEntity {
	
	private Integer orderIndex;
	private String url;
	private String text;
	@ManyToOne
	private Question question; 

	public QuestionImage() {
		// TODO Auto-generated constructor stub
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

}
