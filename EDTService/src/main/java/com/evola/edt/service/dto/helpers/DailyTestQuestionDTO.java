package com.evola.edt.service.dto.helpers;

import java.util.List;

public class DailyTestQuestionDTO {

	private Long id;
	private List<Long> answers;
	
	public DailyTestQuestionDTO(){
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Long> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Long> answers) {
		this.answers = answers;
	}

}
