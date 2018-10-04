package com.evola.edt.service.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.DailyTestUserResultDTO;
import com.evola.edt.model.dto.DailyTestUserResultQuestionDTO;

@XmlRootElement(name="daily-test-complete-result")
public class MobileDailyTestCompleteResultDTO {

	private int orderNumber;
	private DailyTestUserResultDTO userResult;
	private String username;
	private Long testId;
	private Boolean hasCurrentUserTakenTest;
	private List<DailyTestUserResultQuestionDTO> dailyTestResultQuestions;
	private Date date;
	
	@XmlAttribute(name="order-number")
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	@XmlElement(name="user-result")
	public DailyTestUserResultDTO getUserResult() {
		return userResult;
	}
	public void setUserResult(DailyTestUserResultDTO userResult) {
		this.userResult = userResult;
	}
	
	@XmlAttribute(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@XmlAttribute(name="test-id")
	public Long getTestId() {
		return testId;
	}
	public void setTestId(Long testId) {
		this.testId = testId;
	}
	
	@XmlAttribute(name="has-current-user-taken-test")
	public Boolean getHasCurrentUserTakenTest() {
		return hasCurrentUserTakenTest;
	}
	public void setHasCurrentUserTakenTest(Boolean hasCurrentUserTakenTest) {
		this.hasCurrentUserTakenTest = hasCurrentUserTakenTest;
	}
	
	@XmlElement(name="result-question")
	public List<DailyTestUserResultQuestionDTO> getDailyTestResultQuestions() {
		return dailyTestResultQuestions;
	}
	public void setDailyTestResultQuestions(List<DailyTestUserResultQuestionDTO> dailyTestResultQuestions) {
		this.dailyTestResultQuestions = dailyTestResultQuestions;
	}
	
	@XmlAttribute(name="date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
