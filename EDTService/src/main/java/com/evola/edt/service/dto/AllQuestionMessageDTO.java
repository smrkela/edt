package com.evola.edt.service.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="my-question-message")
public class AllQuestionMessageDTO {
	
	private Long questionId;
	private String questionText;
	private String messageText;
	private Date messageDate;
	private String messageUserName;
	private Long messageUserId;
	private Integer messageUserLevel;
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

	@XmlAttribute(name="message-user-name")
	public String getMessageUserName() {
		return messageUserName;
	}
	public void setMessageUserName(String messageUserName) {
		this.messageUserName = messageUserName;
	}

	@XmlAttribute(name="message-user-id")
	public Long getMessageUserId() {
		return messageUserId;
	}
	public void setMessageUserId(Long messageUserId) {
		this.messageUserId = messageUserId;
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

	@XmlAttribute(name="message-user-level")
	public Integer getMessageUserLevel() {
		return messageUserLevel;
	}
	public void setMessageUserLevel(Integer messageUserLevel) {
		this.messageUserLevel = messageUserLevel;
	}
	
	@XmlAttribute(name="category-name")
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
