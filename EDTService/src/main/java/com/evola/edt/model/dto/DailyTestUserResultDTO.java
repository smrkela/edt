package com.evola.edt.model.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.utils.DateUtils;

@XmlRootElement(name="user-result")
public class DailyTestUserResultDTO extends BaseDTO {

	private UserDTO user;
	private Integer correctAnswers;
	private Integer wrongAswers;
	private Float correctPercent;
	private Integer timeTaken; // vreme polaganja u sekundama
	private Date creationDate;
	private Integer points;

	private Integer orderNumber;
	private Integer numberOfTests;
	
	public DailyTestUserResultDTO() {
	}

	@XmlElement(name="user")
	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
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

	@XmlAttribute(name="time-taken-string")
	public String getTimeTakenString() {

		return getTimeTakenString(false);
	}

	public String getTimeTakenShortString() {

		return getTimeTakenString(true);
	}

	public String getTimeTakenString(boolean isShort) {

		return DateUtils.getTimeTakenString(timeTaken, isShort);
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

}
