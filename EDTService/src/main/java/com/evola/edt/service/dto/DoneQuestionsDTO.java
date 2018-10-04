package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="done-questions")
public class DoneQuestionsDTO {
	
	private List<Long> learntQuestionsIds;
	private List<Long> testedQuestionsIds;

	@XmlElement(name="learnt-question-id")
	public List<Long> getLearntQuestionsIds() {
		return learntQuestionsIds;
	}

	public void setLearntQuestionsIds(List<Long> doneQuestionsIds) {
		this.learntQuestionsIds = doneQuestionsIds;
	}

	@XmlElement(name="tested-question-id")
	public List<Long> getTestedQuestionsIds() {
		return testedQuestionsIds;
	}

	public void setTestedQuestionsIds(List<Long> testedQuestionsIds) {
		this.testedQuestionsIds = testedQuestionsIds;
	}

}
