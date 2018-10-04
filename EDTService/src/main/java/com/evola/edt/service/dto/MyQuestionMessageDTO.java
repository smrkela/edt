package com.evola.edt.service.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="my-question-message")
public class MyQuestionMessageDTO {
	
	private Long questionId;
	private String questionText;
	private String messageText;
	private Date messageDate;
	private String answerText;
	private Date answerDate;
	private Long answerUserId;
	private String answerUserName;
	private Integer answerUserLevel;
	private String categoryId;
	private String categoryName;
	private Integer messageCount;
	
	@XmlAttribute(name="question-id")
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	
	@XmlAttribute(name="question-text")
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	@XmlAttribute(name="message-text")
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	
	@XmlAttribute(name="message-date")
	public Date getMessageDate() {
		return messageDate;
	}
	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}
	
	@XmlAttribute(name="answer-text")
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	
	@XmlAttribute(name="answer-date")
	public Date getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(Date answerDate) {
		this.answerDate = answerDate;
	}
	
	@XmlAttribute(name="answer-user-name")
	public String getAnswerUserName() {
		return answerUserName;
	}
	public void setAnswerUserName(String answerUserName) {
		this.answerUserName = answerUserName;
	}
	
	@XmlAttribute(name="answer-user-id")
	public Long getAnswerUserId() {
		return answerUserId;
	}
	public void setAnswerUserId(Long answerUserId) {
		this.answerUserId = answerUserId;
	}
	
	@XmlAttribute(name="category-id")
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	@XmlAttribute(name="message-count")
	public Integer getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}

	@XmlAttribute(name="answer-user-level")
	public Integer getAnswerUserLevel() {
		return answerUserLevel;
	}
	public void setAnswerUserLevel(Integer answerUserLevel) {
		this.answerUserLevel = answerUserLevel;
	}
	
	@XmlAttribute(name="category-name")
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
