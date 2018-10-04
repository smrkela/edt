package com.evola.edt.service.dto;

public class QuestionDetailStatDTO {
	
	private Long questionId;
	private Integer numberOfLearns;
	private Integer numberOfTests;
	private Integer numberOfCorrectTests;
	private Integer numberOfIncorrectTests;
	private Integer numberOfLearningUsers;
	private Integer numberOfTestingUsers;
	private Integer difficulty;
	
	public Integer getNumberOfLearns() {
		return numberOfLearns;
	}
	public void setNumberOfLearns(Integer numberOfLearns) {
		this.numberOfLearns = numberOfLearns;
	}
	public Integer getNumberOfTests() {
		return numberOfTests;
	}
	public void setNumberOfTests(Integer numberOfTests) {
		this.numberOfTests = numberOfTests;
	}
	public Integer getNumberOfCorrectTests() {
		return numberOfCorrectTests;
	}
	public void setNumberOfCorrectTests(Integer numberOfCorrectTests) {
		this.numberOfCorrectTests = numberOfCorrectTests;
	}
	public Integer getNumberOfIncorrectTests() {
		return numberOfIncorrectTests;
	}
	public void setNumberOfIncorrectTests(Integer numberOfIncorrectTests) {
		this.numberOfIncorrectTests = numberOfIncorrectTests;
	}
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	public Integer getNumberOfLearningUsers() {
		return numberOfLearningUsers;
	}
	public void setNumberOfLearningUsers(Integer numberOfLearningUsers) {
		this.numberOfLearningUsers = numberOfLearningUsers;
	}
	public Integer getNumberOfTestingUsers() {
		return numberOfTestingUsers;
	}
	public void setNumberOfTestingUsers(Integer numberOfTestingUsers) {
		this.numberOfTestingUsers = numberOfTestingUsers;
	}
	public Integer getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

}
