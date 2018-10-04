package com.evola.edt.service.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user-experience")
public class UserExperienceDTO {

	private int currentLevel;
	private float currentProgress;
	private int experiencePoints;
	private int currentLevelExperiencePoints;
	private int nextLevelExperiencePoints;
	private int learnedQuestions;
	private int testedQuestions;

	@XmlAttribute(name = "current-level")
	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	@XmlAttribute(name = "current-progress")
	public float getCurrentProgress() {
		return currentProgress;
	}

	public void setCurrentProgress(float currentProgress) {
		this.currentProgress = currentProgress;
	}

	@XmlAttribute(name = "experience-points")
	public int getExperiencePoints() {
		return experiencePoints;
	}

	public void setExperiencePoints(int experiencePoints) {
		this.experiencePoints = experiencePoints;
	}

	@XmlAttribute(name = "learned-questions")
	public int getLearnedQuestions() {
		return learnedQuestions;
	}

	public void setLearnedQuestions(int learnedQuestions) {
		this.learnedQuestions = learnedQuestions;
	}

	@XmlAttribute(name = "tested-questions")
	public int getTestedQuestions() {
		return testedQuestions;
	}

	public void setTestedQuestions(int testedQuestions) {
		this.testedQuestions = testedQuestions;
	}

	@XmlAttribute(name="next-level-experience-points")
	public int getNextLevelExperiencePoints() {
		return nextLevelExperiencePoints;
	}

	public void setNextLevelExperiencePoints(int nextLevelExperiencePoints) {
		this.nextLevelExperiencePoints = nextLevelExperiencePoints;
	}

	@XmlAttribute(name="current-level-experience-points")
	public int getCurrentLevelExperiencePoints() {
		return currentLevelExperiencePoints;
	}

	public void setCurrentLevelExperiencePoints(int currentLevelExperiencePoints) {
		this.currentLevelExperiencePoints = currentLevelExperiencePoints;
	}

}
