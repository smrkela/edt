package com.evola.edt.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Entity
@XmlRootElement
public class Question extends BaseEntity {
	@Column(unique = true)
	private String questionId;
	private String number;
	@Column(length = 1024)
	private String text;
	private String remark;
	private Integer points;
	private String helpUrl;
	@ManyToMany
	private Set<QuestionCategory> questionCategories = new HashSet<QuestionCategory>();
	@ManyToMany
	private Set<DrivingCategory> drivingCategories = new HashSet<DrivingCategory>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "question")
	private Set<QuestionAnswer> questionAnswers = new HashSet<QuestionAnswer>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "question")
	private Set<QuestionImage> questionImages = new HashSet<QuestionImage>();

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "question")
	private Set<UserQuestionFavorite> questionFavorites = new HashSet<UserQuestionFavorite>();

	public Question() {
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getHelpUrl() {
		return helpUrl;
	}

	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}

	public Set<QuestionCategory> getQuestionCategories() {
		return questionCategories;
	}

	public void setQuestionCategories(Set<QuestionCategory> questionCategories) {
		this.questionCategories = questionCategories;
	}

	public Set<DrivingCategory> getDrivingCategories() {
		return drivingCategories;
	}

	public void setDrivingCategories(Set<DrivingCategory> drivingCategories) {
		this.drivingCategories = drivingCategories;
	}

	public Set<QuestionAnswer> getQuestionAnswers() {
		return questionAnswers;
	}

	public void setQuestionAnswers(Set<QuestionAnswer> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

	public Set<QuestionImage> getQuestionImages() {
		return questionImages;
	}

	public void setQuestionImages(Set<QuestionImage> questionImages) {
		this.questionImages = questionImages;
	}

}
