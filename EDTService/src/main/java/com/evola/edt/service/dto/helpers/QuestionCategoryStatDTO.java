package com.evola.edt.service.dto.helpers;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.evola.edt.model.dto.QuestionCategoryDTO;

public class QuestionCategoryStatDTO {
	
	private QuestionCategoryDTO questionCategory;
	private Integer count;
	private Integer totalCount;
	
	@XmlElement(name="question-category")
	public QuestionCategoryDTO getQuestionCategory() {
		return questionCategory;
	}
	public void setQuestionCategory(QuestionCategoryDTO questionCategory) {
		this.questionCategory = questionCategory;
	}
	
	@XmlAttribute(name="count")
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	@XmlAttribute(name="total-count")
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

}
