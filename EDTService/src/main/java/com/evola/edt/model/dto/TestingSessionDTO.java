package com.evola.edt.model.dto;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@XmlRootElement(name = "testing-session")
public class TestingSessionDTO extends BaseDTO {

	private String className = "TestingSession";

	private String start;
	private String uid;
	private UserDTO user; 
	private Set<UserQuestionStatTestDTO> tests;
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
	
	@XmlElement(name="question-test")
	public Set<UserQuestionStatTestDTO> getTests() {
		return tests;
	}
	public void setTests(Set<UserQuestionStatTestDTO> learns) {
		this.tests = learns;
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
