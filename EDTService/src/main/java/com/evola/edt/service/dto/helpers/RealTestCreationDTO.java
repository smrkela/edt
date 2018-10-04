package com.evola.edt.service.dto.helpers;

import java.util.List;

public class RealTestCreationDTO {
	
	private Long id;
	private String name;
	private Long categoryId;
	private String categoryName;
	private Integer orderIndex;
	private List<Long> questionIds;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
	public List<Long> getQuestionIds() {
		return questionIds;
	}
	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
