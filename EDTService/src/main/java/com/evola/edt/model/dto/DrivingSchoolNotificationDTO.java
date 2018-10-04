package com.evola.edt.model.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 14.05.2013.
 * 
 */
@XmlRootElement
public class DrivingSchoolNotificationDTO extends BaseDTO {
	
	private String title;
	private String content;
	private UserDTO authorDTO;
	private Date creationDate;
	private DrivingSchoolDTO school;
	
	private int numOfComments = 0;
	
	private Long schoolId;
	private String schoolUniqueName;
	
	private Boolean sendNotification;
	private Boolean requestNotificationReadConfirmation;

	
	public DrivingSchoolNotificationDTO() {
		super();
	}

	@XmlElement
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@XmlElement
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement
	public UserDTO getAuthorDTO() {
		return authorDTO;
	}

	public void setAuthorDTO(UserDTO author) {
		this.authorDTO = author;
	}

	@XmlElement
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@XmlElement
	public int getNumOfComments() {
		return numOfComments;
	}

	public void setNumOfComments(int numOfComments) {
		this.numOfComments = numOfComments;
	}

	public DrivingSchoolDTO getSchool() {
		return school;
	}

	public void setSchool(DrivingSchoolDTO school) {
		this.school = school;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolUniqueName() {
		return schoolUniqueName;
	}

	public void setSchoolUniqueName(String schoolUniqueName) {
		this.schoolUniqueName = schoolUniqueName;
	}
	
	public Boolean getSendNotification() {
		return sendNotification;
	}

	public void setSendNotification(Boolean sendNotification) {
		this.sendNotification = sendNotification;
	}

	public Boolean getRequestNotificationReadConfirmation() {
		return requestNotificationReadConfirmation;
	}

	public void setRequestNotificationReadConfirmation(Boolean requestNotificationReadConfirmation) {
		this.requestNotificationReadConfirmation = requestNotificationReadConfirmation;
	}

}
