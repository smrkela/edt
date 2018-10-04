package com.evola.edt.service.dto.helpers;

import javax.xml.bind.annotation.XmlAttribute;

public class ActiveUserDTO {

	private Long userId;
	private String firstName;
	private String lastName;
	private String username;
	private Float completionPercent;
	private Integer points;
	private Integer learningPoints;
	private Integer testingPoints;

	@XmlAttribute(name = "first-name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlAttribute(name = "last-name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlAttribute(name = "completion-percent")
	public Float getCompletionPercent() {
		return completionPercent;
	}

	public void setCompletionPercent(Float completionPercent) {
		this.completionPercent = completionPercent;
	}

	@XmlAttribute(name = "user-id")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSmallProfileImagePath() {

		return "/users/" + getUserId() + "/smallImage";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlAttribute(name="points")
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getLearningPoints() {
		return learningPoints;
	}

	public void setLearningPoints(Integer learningPoints) {
		this.learningPoints = learningPoints;
	}

	public Integer getTestingPoints() {
		return testingPoints;
	}

	public void setTestingPoints(Integer testingPoints) {
		this.testingPoints = testingPoints;
	}

}
