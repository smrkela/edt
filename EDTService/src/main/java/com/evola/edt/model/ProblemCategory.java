package com.evola.edt.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Daci, 24.04.2013.
 *
 */

@Entity
public class ProblemCategory extends BaseEntity {
	
	public static String INVALID_TEXT = "invalid_question_text";
	public static String INVALID_ANSWER = "invalid_question_answer";
	public static String INVALID_QUESTION_CATEGORY = "invalid_question_category";
	public static String INVALID_DRIVING_CATEGORY = "invalid_driving_category";
	public static String INVALID_IMAGES = "invalid_question_images";
	public static String INVALID_GENERAL = "general_issue";
	
	public static String[] CATEGORIES = new String[]{INVALID_TEXT, INVALID_ANSWER, INVALID_QUESTION_CATEGORY, INVALID_DRIVING_CATEGORY, INVALID_IMAGES, INVALID_GENERAL};
	
	@Column(unique = true)
	private String problemCategoryId;
	private String name;
	@Column(length = 1024)
	private String description;
	
	public String getProblemCategoryId() {
		return problemCategoryId;
	}
	
	public void setProblemCategoryId(String problemCategoryId) {
		this.problemCategoryId = problemCategoryId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}