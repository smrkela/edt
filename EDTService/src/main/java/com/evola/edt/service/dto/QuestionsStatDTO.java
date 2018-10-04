package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="questionsStat")
public class QuestionsStatDTO {
	
	private List<QuestionStatDTO> questionStats;

	@XmlElement(name="questionStat")
	public List<QuestionStatDTO> getQuestionStats() {
		return questionStats;
	}

	public void setQuestionStats(List<QuestionStatDTO> questionStats) {
		this.questionStats = questionStats;
	}

}
