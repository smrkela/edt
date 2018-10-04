package com.evola.edt.service.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.QuestionDTO;

@XmlRootElement(name="daily-test-questions")
public class MobilePerformDailyTestDTO {

	private Boolean hasUserTakenTest;
	private List<QuestionDTO> questions;
	private Long startTime;
	private Long testId;
	private Integer numberOfQuestions;
	private Date date;
	private Boolean isToday;
	
	@XmlAttribute(name="has-user-taken-test")
	public Boolean getHasUserTakenTest() {
		return hasUserTakenTest;
	}
	public void setHasUserTakenTest(Boolean hasUserTakenTest) {
		this.hasUserTakenTest = hasUserTakenTest;
	}

	@XmlElement(name="question")
	public List<QuestionDTO> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}
	
	@XmlAttribute(name="start-time")
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	
	@XmlAttribute(name="test-id")
	public Long getTestId() {
		return testId;
	}
	public void setTestId(Long testId) {
		this.testId = testId;
	}
	
	@XmlAttribute(name="number-of-questions")
	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}
	public void setNumberOfQuestions(Integer numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}
	
	@XmlAttribute(name="date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@XmlAttribute(name="is-today")
	public Boolean getIsToday() {
		return isToday;
	}
	public void setIsToday(Boolean isToday) {
		this.isToday = isToday;
	}
	
}
