package com.evola.edt.model.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class DrivingLicenceCategoryDTO extends BaseDTO {

	private String className = "DrivingLicenceCategory";
	private String categoryId;
	private String name;
	private Integer orderIndex;
	private DrivingCategoryDTO category;

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name="class-name")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@XmlAttribute(name="category-id")
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@XmlAttribute(name="order-index")
	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	@XmlElement(name="category")
	public DrivingCategoryDTO getCategory() {
		return category;
	}

	public void setCategory(DrivingCategoryDTO category) {
		this.category = category;
	}

}
