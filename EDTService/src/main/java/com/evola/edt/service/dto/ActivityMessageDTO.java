package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.QuestionCategoryDTO;
import com.evola.edt.model.dto.QuestionDTO;

@XmlRootElement(name="activity-message")
public class ActivityMessageDTO {

	private String message;
	private String link;
	private String linkText;
	private String timeAgoString;
	private String formattedTime;
	private Long authorId;
	
	@XmlAttribute(name="message")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@XmlAttribute(name="link")
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	@XmlAttribute(name="time-ago-string")
	public String getTimeAgoString() {
		return timeAgoString;
	}
	public void setTimeAgoString(String timeAgoString) {
		this.timeAgoString = timeAgoString;
	}
	
	@XmlAttribute(name="formatted-time")
	public String getFormattedTime() {
		return formattedTime;
	}
	public void setFormattedTime(String formattedTime) {
		this.formattedTime = formattedTime;
	}
	
	@XmlAttribute(name="link-text")
	public String getLinkText() {
		return linkText;
	}
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}
	
	@XmlAttribute(name="author-id")
	public Long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}


}
