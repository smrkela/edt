package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "learning-lessons")
public class LearningLessonsDTO {

	private Long categoryId;
	private String categoryName;
	
	private List<LearningLessonDTO> lessons;

	@XmlElement(name="lesson")
	public List<LearningLessonDTO> getLessons() {
		return lessons;
	}

	public void setSessions(List<LearningLessonDTO> lessons) {
		this.lessons = lessons;
	}

	@XmlAttribute(name="category-id")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@XmlAttribute(name="category-name")
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
