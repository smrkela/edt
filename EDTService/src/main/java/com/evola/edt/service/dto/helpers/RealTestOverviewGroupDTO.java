package com.evola.edt.service.dto.helpers;

import java.util.List;

public class RealTestOverviewGroupDTO {

	private Long categoryId;
	private String categoryName;
	private List<RealTestOverviewTestDTO> tests;
	private Integer totalTestsDone = 0;

	public List<RealTestOverviewTestDTO> getTests() {
		return tests;
	}

	public void setTests(List<RealTestOverviewTestDTO> tests) {
		this.tests = tests;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getTotalTestsDone() {
		return totalTestsDone;
	}

	public void setTotalTestsDone(Integer totalTestsDone) {
		this.totalTestsDone = totalTestsDone;
	}
	
}
