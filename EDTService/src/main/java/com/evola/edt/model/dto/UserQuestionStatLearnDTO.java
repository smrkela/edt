package com.evola.edt.model.dto;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@XmlRootElement(name = "user-question-stat-learn")
public class UserQuestionStatLearnDTO extends BaseDTO {

	private String className = "UserQuestionStatLearn";
	private Date time;
	private QuestionDTO question;

	@XmlAttribute(name = "class-name")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@XmlAttribute(name = "time")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@XmlElement(name = "question")
	public QuestionDTO getQuestion() {
		return question;
	}

	public void setQuestion(QuestionDTO question) {
		this.question = question;
	}

}
