package com.evola.edt.model.dto;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Nikola 09.04.2013.
 *
 */
public class DrivingCategoryDTO extends BaseDTO {

	private String className = "DrivingCategory";
	private String categoryId;
	private String name;
	private Integer orderIndex;

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

}
