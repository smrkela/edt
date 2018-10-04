package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.QuestionCategoryDTO;
import com.evola.edt.model.dto.QuestionDTO;

@XmlRootElement(name="all-questions")
public class AllQuestionsDTO {
	
	private List<QuestionDTO> questions;
	private List<DrivingCategoryDTO> drivingCategories;
	private List<QuestionCategoryDTO> questionCategories;

	@XmlElement(name="question")
	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}

	@XmlElement(name="driving-category")
	public List<DrivingCategoryDTO> getDrivingCategories() {
		return drivingCategories;
	}

	public void setDrivingCategories(List<DrivingCategoryDTO> drivingCategories) {
		this.drivingCategories = drivingCategories;
	}

	@XmlElement(name="question-category")
	public List<QuestionCategoryDTO> getQuestionCategories() {
		return questionCategories;
	}

	public void setQuestionCategories(List<QuestionCategoryDTO> questionCategories) {
		this.questionCategories = questionCategories;
	}

}
