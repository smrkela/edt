package com.evola.edt.component.store;

import java.io.Serializable;

public class LearningStoreData implements Serializable{
	
	private static final long serialVersionUID = -618412020854885313L;
	
	private Integer startIndex;
	private Long drivingCategoryId;
	private Long questionCategoryId;
	
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	public Long getDrivingCategoryId() {
		return drivingCategoryId;
	}
	public void setDrivingCategoryId(Long drivingCategoryId) {
		this.drivingCategoryId = drivingCategoryId;
	}
	public Long getQuestionCategoryId() {
		return questionCategoryId;
	}
	public void setQuestionCategoryId(Long questionCategoryId) {
		this.questionCategoryId = questionCategoryId;
	}

	
}
