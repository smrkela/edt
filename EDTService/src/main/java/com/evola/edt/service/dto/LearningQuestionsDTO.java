package com.evola.edt.service.dto;

import java.util.List;

import com.evola.edt.model.dto.QuestionDTO;

public class LearningQuestionsDTO {
	
	private List<QuestionDTO> questions;
	private Boolean isFirstPage;
	private Boolean isLastPage;
	private long totalQuestions;
	private int totalPages;

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}

	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public Boolean getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public long getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(long totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
