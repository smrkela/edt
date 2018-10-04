package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.LearningSessionDTO;
import com.evola.edt.model.dto.TestingSessionDTO;
import com.evola.edt.service.dto.helpers.ActiveUserDTO;
import com.evola.edt.service.dto.helpers.QuestionCategoryStatDTO;

@XmlRootElement(name = "statistics")
public class StatisticsDTO {

	private Integer totalQuestions;
	private Integer learnedQuestions;
	private Integer testedQuestions;
	private Float totalProgress;
	private List<QuestionCategoryStatDTO> categories;
	private Integer loggedInUsersCount;
	private List<ActiveUserDTO> activeUsers;
	private List<LearningSessionDTO> learningSessions;
	private List<TestingSessionDTO> testingSessions;

	@XmlAttribute(name = "total-questions")
	public Integer getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	@XmlAttribute(name = "learned-questions")
	public Integer getLearnedQuestions() {
		return learnedQuestions;
	}

	public void setLearnedQuestions(Integer learnedQuestions) {
		this.learnedQuestions = learnedQuestions;
	}

	@XmlAttribute(name = "tested-questions")
	public Integer getTestedQuestions() {
		return testedQuestions;
	}

	public void setTestedQuestions(Integer testedQuestions) {
		this.testedQuestions = testedQuestions;
	}

	@XmlAttribute(name = "total-progress")
	public Float getTotalProgress() {
		return totalProgress;
	}

	public void setTotalProgress(Float totalProgress) {
		this.totalProgress = totalProgress;
	}

	@XmlElement(name = "question-category-stat")
	public List<QuestionCategoryStatDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<QuestionCategoryStatDTO> categories) {
		this.categories = categories;
	}

	@XmlAttribute(name = "logged-in-users-count")
	public Integer getLoggedInUsersCount() {
		return loggedInUsersCount;
	}

	public void setLoggedInUsersCount(Integer loggedInUsersCount) {
		this.loggedInUsersCount = loggedInUsersCount;
	}

	@XmlElement(name = "active-user")
	public List<ActiveUserDTO> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<ActiveUserDTO> activeUsers) {
		this.activeUsers = activeUsers;
	}

	@XmlElement(name = "learning-session")
	public List<LearningSessionDTO> getLearningSessions() {
		return learningSessions;
	}

	public void setLearningSessions(List<LearningSessionDTO> learningSessions) {
		this.learningSessions = learningSessions;
	}

	@XmlElement(name = "testing-session")
	public List<TestingSessionDTO> getTestingSessions() {
		return testingSessions;
	}

	public void setTestingSessions(List<TestingSessionDTO> testingSessions) {
		this.testingSessions = testingSessions;
	}

}
