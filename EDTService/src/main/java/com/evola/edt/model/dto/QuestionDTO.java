package com.evola.edt.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@XmlRootElement(name="question")
public class QuestionDTO extends BaseDTO {
	
	private String className = "Question";
	private String questionId;
	private String number;
	private String text;
	private String remark;
	private Integer points;
	private String helpUrl;
	private Boolean multiSelect = false;
	private Boolean multiSelectSet = false;
	private Boolean answered;
	private List<QuestionCategoryDTO> questionCategories = new ArrayList<QuestionCategoryDTO>();
	private List<DrivingCategoryDTO> drivingCategories = new ArrayList<DrivingCategoryDTO>();
	private List<QuestionAnswerDTO> questionAnswers = new ArrayList<QuestionAnswerDTO>();
	private List<QuestionImageDTO> questionImages = new ArrayList<QuestionImageDTO>();
	private String questionUrlTitle;
	
	private int learnCount;
	private int testCorrectCount;
	private int testIncorrectCount;
	
	private int numberOfMessages;
	
	private boolean isFavorite;

	@XmlAttribute(name="class-name")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@XmlAttribute(name="question-id")
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	@XmlAttribute(name="number")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@XmlAttribute(name="text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@XmlAttribute(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlAttribute(name="points")
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@XmlAttribute(name="help-url")
	public String getHelpUrl() {
		return helpUrl;
	}

	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}

	@XmlElement(name="question-category")
	public List<QuestionCategoryDTO> getQuestionCategories() {
		return questionCategories;
	}

	public void setQuestionCategories(
			List<QuestionCategoryDTO> questionCategories) {
		this.questionCategories = questionCategories;
	}

	@XmlElement(name="driving-category")
	public List<DrivingCategoryDTO> getDrivingCategories() {
		return drivingCategories;
	}

	public void setDrivingCategories(List<DrivingCategoryDTO> drivingCategories) {
		this.drivingCategories = drivingCategories;
	}

	@XmlElement(name="question-answer")
	public List<QuestionAnswerDTO> getQuestionAnswers() {
		return questionAnswers;
	}

	public void setQuestionAnswers(List<QuestionAnswerDTO> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

	@XmlElement(name="question-image")
	public List<QuestionImageDTO> getQuestionImages() {
		return questionImages;
	}

	public void setQuestionImages(List<QuestionImageDTO> questionImages) {
		this.questionImages = questionImages;
	}

	@XmlAttribute(name="multiselect")
	public Boolean getMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(Boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	@XmlAttribute(name="multiselect-set")
	public Boolean getMultiSelectSet() {
		return multiSelectSet;
	}

	public void setMultiSelectSet(Boolean multiSelectSet) {
		this.multiSelectSet = multiSelectSet;
	}

	@XmlAttribute(name="answered")
	public Boolean getAnswered() {
		return answered;
	}

	public void setAnswered(Boolean answered) {
		this.answered = answered;
	}

	@XmlAttribute(name="learn-count")
	public int getLearnCount() {
		return learnCount;
	}

	public void setLearnCount(int learnCount) {
		this.learnCount = learnCount;
	}

	@XmlAttribute(name="test-correct-count")
	public int getTestCorrectCount() {
		return testCorrectCount;
	}

	public void setTestCorrectCount(int testCorrectCount) {
		this.testCorrectCount = testCorrectCount;
	}

	@XmlAttribute(name="test-incorrect-count")
	public int getTestIncorrectCount() {
		return testIncorrectCount;
	}

	public void setTestIncorrectCount(int testIncorrectCount) {
		this.testIncorrectCount = testIncorrectCount;
	}

	@XmlAttribute(name="is-favorite")
	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	@XmlAttribute(name="number-of-messages")
	public int getNumberOfMessages() {
		return numberOfMessages;
	}

	public void setNumberOfMessages(int numberOfMessages) {
		this.numberOfMessages = numberOfMessages;
	}

	@XmlAttribute(name="question-url-title")
	public String getQuestionUrlTitle() {
		return questionUrlTitle;
	}

	public void setQuestionUrlTitle(String questionUrlTitle) {
		this.questionUrlTitle = questionUrlTitle;
	}

}
