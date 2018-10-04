package com.evola.edt.model.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userDiscussionPreview")
public class UserDiscussionPreviewDTO extends BaseDTO {

	private int numOfMessages;
	private int numOfUserMessages;
	private String lastUserMessageText;
	private Date lastUserMessageDate;
	private String lastMessageText;
	private long lastMessageUserId;
	private String lastMessageUserName;
	private Date lastMessageDate;

	public int getNumOfMessages() {
		return numOfMessages;
	}
	public void setNumOfMessages(int numOfMessages) {
		this.numOfMessages = numOfMessages;
	}
	public String getLastUserMessageText() {
		return lastUserMessageText;
	}
	public void setLastUserMessageText(String lastUserMessageText) {
		this.lastUserMessageText = lastUserMessageText;
	}
	public Date getLastUserMessageDate() {
		return lastUserMessageDate;
	}
	public void setLastUserMessageDate(Date lastUserMessageDate) {
		this.lastUserMessageDate = lastUserMessageDate;
	}
	public String getLastMessageText() {
		return lastMessageText;
	}
	public void setLastMessageText(String lastMessageText) {
		this.lastMessageText = lastMessageText;
	}
	public long getLastMessageUserId() {
		return lastMessageUserId;
	}
	public void setLastMessageUserId(long lastMessageUserId) {
		this.lastMessageUserId = lastMessageUserId;
	}
	public String getLastMessageUserName() {
		return lastMessageUserName;
	}
	public void setLastMessageUserName(String lastMessageUserName) {
		this.lastMessageUserName = lastMessageUserName;
	}
	public Date getLastMessageDate() {
		return lastMessageDate;
	}
	public void setLastMessageDate(Date lastMessageDate) {
		this.lastMessageDate = lastMessageDate;
	}
	public int getNumOfUserMessages() {
		return numOfUserMessages;
	}
	public void setNumOfUserMessages(int numberOfUserMessages) {
		this.numOfUserMessages = numberOfUserMessages;
	}
	
	
}
