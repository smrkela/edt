package com.evola.edt.service.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="question-message")
public class QuestionMessageDTO {
	
	private Long id;
	private String messageText;
	private Long userId;
	private String userName;
	private Integer userLevel;
	private Date reportDate;
	
	private Long parentMessageId;
	private Long parentMessageUserId;
	private String parentMessageUserName;
	private Boolean parentMessageUserIsMale;
	private String parentMessageText;
	private Date parentMessageDate;
	private Boolean canDelete;
	
	@XmlAttribute(name="id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@XmlAttribute(name="message-text")
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	
	@XmlAttribute(name="user-id")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@XmlAttribute(name="user-name")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@XmlAttribute(name="report-date")
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	
	@XmlAttribute(name="user-level")
	public Integer getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	
	@XmlAttribute(name="parent-message-id")
	public Long getParentMessageId() {
		return parentMessageId;
	}
	public void setParentMessageId(Long parentMessageId) {
		this.parentMessageId = parentMessageId;
	}
	
	@XmlAttribute(name="parent-message-user-id")
	public Long getParentMessageUserId() {
		return parentMessageUserId;
	}
	public void setParentMessageUserId(Long parentMessageUserId) {
		this.parentMessageUserId = parentMessageUserId;
	}
	
	@XmlAttribute(name="parent-message-user-name")
	public String getParentMessageUserName() {
		return parentMessageUserName;
	}
	public void setParentMessageUserName(String parentMessageUserName) {
		this.parentMessageUserName = parentMessageUserName;
	}
	
	@XmlAttribute(name="parent-message-text")
	public String getParentMessageText() {
		return parentMessageText;
	}
	public void setParentMessageText(String parentMessageText) {
		this.parentMessageText = parentMessageText;
	}
	
	@XmlAttribute(name="parent-message-date")
	public Date getParentMessageDate() {
		return parentMessageDate;
	}
	public void setParentMessageDate(Date parentMessageDate) {
		this.parentMessageDate = parentMessageDate;
	}
	
	@XmlAttribute(name="parent-message-user-is-male")
	public Boolean getParentMessageUserIsMale() {
		return parentMessageUserIsMale;
	}
	public void setParentMessageUserIsMale(Boolean parentMessageUserIsMale) {
		this.parentMessageUserIsMale = parentMessageUserIsMale;
	}
	public Boolean getCanDelete() {
		return canDelete;
	}
	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}
}
