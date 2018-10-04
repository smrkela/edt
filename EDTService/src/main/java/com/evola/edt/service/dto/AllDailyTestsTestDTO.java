package com.evola.edt.service.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="test")
public class AllDailyTestsTestDTO {
	
	private Long testId;
	private String dateString;
	private Date date;
	private Integer numberOfUsers;
	private Boolean hasUserTakenTest;
	private Integer numberOfPoints;
	
	@XmlAttribute(name="test-id")
	public Long getTestId() {
		return testId;
	}
	public void setTestId(Long testId) {
		this.testId = testId;
	}
	
	@XmlAttribute(name="date-string")
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String date) {
		this.dateString = date;
	}
	
	@XmlAttribute(name="number-of-users")
	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}
	public void setNumberOfUsers(Integer numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	
	@XmlAttribute(name="has-user-taken-test")
	public Boolean getHasUserTakenTest() {
		return hasUserTakenTest;
	}
	public void setHasUserTakenTest(Boolean hasUserTakenTest) {
		this.hasUserTakenTest = hasUserTakenTest;
	}
	
	@XmlAttribute(name="number-of-points")
	public Integer getNumberOfPoints() {
		return numberOfPoints;
	}
	public void setNumberOfPoints(Integer numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}
	
	@XmlAttribute(name="date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
