package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.QuestionDTO;

@XmlRootElement(name = "lesson")
public class LessonDTO {
	
	private Long groupId;
	private String groupName;
	private Integer lessonId;
	private String lessonName;
	private Integer removedQuestionsCount;
	
	private List<QuestionDTO> questions;

	@XmlAttribute(name="group-id")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@XmlAttribute(name="lesson-id")
	public Integer getLessonId() {
		return lessonId;
	}

	public void setLessonId(Integer lessonId) {
		this.lessonId = lessonId;
	}

	@XmlElement(name="question")
	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}

	@XmlAttribute(name="group-name")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@XmlAttribute(name="lesson-name")
	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	@XmlAttribute(name="removed-questions-count")
	public Integer getRemovedQuestionsCount() {
		return removedQuestionsCount;
	}

	public void setRemovedQuestionsCount(Integer removedQuestionsCount) {
		this.removedQuestionsCount = removedQuestionsCount;
	}

}
