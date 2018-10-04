package com.evola.edt.service.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="questionStat")
public class QuestionStatDTO {

	private String questionId;
	private Integer numOfLearns;
	private Integer numOfTests;
	
	@XmlAttribute(name="numOfLearns")
	public Integer getNumOfLearns() {
		return numOfLearns;
	}
	public void setNumOfLearns(Integer numOfLearns) {
		this.numOfLearns = numOfLearns;
	}
	
	@XmlAttribute(name="numOfTests")
	public Integer getNumOfTests() {
		return numOfTests;
	}
	public void setNumOfTests(Integer numOfTests) {
		this.numOfTests = numOfTests;
	}
	
	@XmlAttribute(name="questionId")
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

}
