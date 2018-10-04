package com.evola.edt.model.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Daci, 24.04.2013.
 *
 */

@XmlRootElement(name = "problem-category")
public class ProblemCategoryDTO extends BaseDTO {

	private String className = "ProblemCategory";
	private String problemCategoryId;
	private String name;
	private String description;

	@XmlAttribute(name="class-name")
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	@XmlAttribute(name="problem-category-id")
	public String getProblemCategoryId() {
		return problemCategoryId;
	}
	
	public void setProblemCategoryId(String problemCategoryId) {
		this.problemCategoryId = problemCategoryId;
	}
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute(name="description")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}