package com.evola.edt.service.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lesson")
public class LearningLessonDTO {

	private Integer id;
	private String title;
	
	private Integer numberOfQuestions = 0;
	
	private Integer learn1 = 0;
	private Integer learn2 = 0;
	private Integer learn3 = 0;
	private Integer learn4 = 0;

	private Integer test1 = 0;
	private Integer test2 = 0;
	private Integer test3 = 0;
	private Integer test4 = 0;
	
	private Integer favoritesCount = 0;
	
	@XmlAttribute(name="learn1")
	public Integer getLearn1() {
		return learn1;
	}
	public void setLearn1(Integer learn1) {
		this.learn1 = learn1;
	}
	
	@XmlAttribute(name="learn2")
	public Integer getLearn2() {
		return learn2;
	}
	public void setLearn2(Integer learn2) {
		this.learn2 = learn2;
	}
	
	@XmlAttribute(name="learn3")
	public Integer getLearn3() {
		return learn3;
	}
	public void setLearn3(Integer learn3) {
		this.learn3 = learn3;
	}
	
	@XmlAttribute(name="learn4")
	public Integer getLearn4() {
		return learn4;
	}
	public void setLearn4(Integer learn4) {
		this.learn4 = learn4;
	}
	
	@XmlAttribute(name="test1")
	public Integer getTest1() {
		return test1;
	}
	public void setTest1(Integer test1) {
		this.test1 = test1;
	}
	
	@XmlAttribute(name="test2")
	public Integer getTest2() {
		return test2;
	}
	public void setTest2(Integer test2) {
		this.test2 = test2;
	}
	
	@XmlAttribute(name="test3")
	public Integer getTest3() {
		return test3;
	}
	public void setTest3(Integer test3) {
		this.test3 = test3;
	}
	
	@XmlAttribute(name="test4")
	public Integer getTest4() {
		return test4;
	}
	public void setTest4(Integer test4) {
		this.test4 = test4;
	}
	
	@XmlAttribute(name="number-of-questions")
	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}
	public void setNumberOfQuestions(Integer numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}
	
	@XmlAttribute(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@XmlAttribute(name="title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@XmlAttribute(name="favorites-count")
	public Integer getFavoritesCount() {
		return favoritesCount;
	}
	public void setFavoritesCount(Integer favoritesCount) {
		this.favoritesCount = favoritesCount;
	}


}
