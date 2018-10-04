package com.evola.edt.service.dto.helpers;

import com.evola.edt.model.Question;

public class QuestionViewNavigationWrapper {
	
	private Question previousQuestion;
	private Question nextQuestion;
	private Integer currentPageIndex;
	private Integer totalPages;
	private Long totalQuestions;
	
	public Question getPreviousQuestion() {
		return previousQuestion;
	}
	public void setPreviousQuestion(Question previousQuestion) {
		this.previousQuestion = previousQuestion;
	}
	public Question getNextQuestion() {
		return nextQuestion;
	}
	public void setNextQuestion(Question nextQuestion) {
		this.nextQuestion = nextQuestion;
	}
	public Integer getCurrentPageIndex() {
		return currentPageIndex;
	}
	public void setCurrentPageIndex(Integer currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public Long getTotalQuestions() {
		return totalQuestions;
	}
	public void setTotalQuestions(Long totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	
}
