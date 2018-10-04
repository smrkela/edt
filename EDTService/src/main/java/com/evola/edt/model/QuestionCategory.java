package com.evola.edt.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Entity
public class QuestionCategory extends BaseEntity {

	@Column(unique = true)
	private String categoryId;
	@Column(nullable=false)
	private Integer orderIndex;
	private String name;

	public QuestionCategory() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

}
