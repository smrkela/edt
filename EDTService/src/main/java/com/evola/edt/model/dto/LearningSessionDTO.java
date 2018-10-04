package com.evola.edt.model.dto;

import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@XmlRootElement(name = "learning-session")
public class LearningSessionDTO extends BaseDTO {

	private String className = "LearningSession";

	private String start;
	private String uid;
	private UserDTO user; 
	private Set<UserQuestionStatLearnDTO> learns;
	private Integer numberOfQuestions;
	
	@XmlAttribute(name="start")
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	
	@XmlAttribute(name="uid")
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@XmlElement(name="user")
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	@XmlElement(name="question-learn")
	public Set<UserQuestionStatLearnDTO> getLearns() {
		return learns;
	}
	public void setLearns(Set<UserQuestionStatLearnDTO> learns) {
		this.learns = learns;
	}
	
	@XmlAttribute(name="class-name")
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@XmlAttribute(name="number-of-questions")
	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}
	public void setNumberOfQuestions(Integer numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}
	

}
