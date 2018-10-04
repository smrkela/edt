package com.evola.edt.service.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

public class MobileDailyTestUserResultDTO {

	private String username;
	private Long userId;
	private Integer correctAnswers;
	private Integer wrongAswers;
	private Float correctPercent;
	private Integer timeTaken; // vreme polaganja u sekundama
	private Date creationDate;
	private Integer points;
	private String timeTakenString;
	private String timeTakenShortString;

	private Integer orderNumber;
	private Integer numberOfTests;

	public MobileDailyTestUserResultDTO() {
	}

	@XmlAttribute(name="correct-answers")
	public Integer getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(Integer correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	@XmlAttribute(name="wrong-answers")
	public Integer getWrongAswers() {
		return wrongAswers;
	}

	public void setWrongAswers(Integer wrongAswers) {
		this.wrongAswers = wrongAswers;
	}

	@XmlAttribute(name="correct-percent")
	public Float getCorrectPercent() {
		return correctPercent;
	}

	public void setCorrectPercent(Float correctPercent) {
		this.correctPercent = correctPercent;
	}

	@XmlAttribute(name="time-taken")
	public Integer getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(Integer timeTaken) {
		this.timeTaken = timeTaken;
	}

	@XmlAttribute(name="creation-date")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@XmlAttribute(name="points")
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@XmlAttribute(name="order-number")
	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	@XmlAttribute(name="number-of-tests")
	public Integer getNumberOfTests() {
		return numberOfTests;
	}

	public void setNumberOfTests(Integer numberOfTests) {
		this.numberOfTests = numberOfTests;
	}

	@XmlAttribute(name="username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlAttribute(name="user-id")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@XmlAttribute(name="time-taken-string")
	public String getTimeTakenString() {
		return timeTakenString;
	}

	public void setTimeTakenString(String timeTakenString) {
		this.timeTakenString = timeTakenString;
	}

	@XmlAttribute(name="time-taken-short-string")
	public String getTimeTakenShortString() {
		return timeTakenShortString;
	}

	public void setTimeTakenShortString(String timeTakenShortString) {
		this.timeTakenShortString = timeTakenShortString;
	}
	
}
