package com.evola.edt.service.dto.helpers;

import java.util.List;

public class DailyTestDTO {

	private Long testId;
	private Long startTime;
	private List<DailyTestQuestionDTO> questions;

	public DailyTestDTO() {

	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public List<DailyTestQuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<DailyTestQuestionDTO> questions) {
		this.questions = questions;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public DailyTestQuestionDTO getQuestion(Long id) {

		for (DailyTestQuestionDTO dto : questions) {

			if (id.equals(dto.getId()))
				return dto;
		}

		return null;
	}

}
