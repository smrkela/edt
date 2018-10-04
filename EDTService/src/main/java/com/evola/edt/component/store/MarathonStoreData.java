package com.evola.edt.component.store;

import java.io.Serializable;
import java.util.List;

public class MarathonStoreData implements Serializable{

	private static final long serialVersionUID = -6800040850916402866L;

	private List<Long> questionIds;
	private String marathonId;
	private Integer currentQuestionIndex;
	
	public List<Long> getQuestionIds() {
		return questionIds;
	}
	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}
	public String getMarathonId() {
		return marathonId;
	}
	public void setMarathonId(String marathonId) {
		this.marathonId = marathonId;
	}
	public Integer getCurrentQuestionIndex() {
		return currentQuestionIndex;
	}
	public void setCurrentQuestionIndex(Integer currentQuestionIndex) {
		this.currentQuestionIndex = currentQuestionIndex;
	}
	
	public Long getQuestionId(Integer index) {

		int size = getQuestionIds().size();
		
		//ako je index veci od broja pitanja, rotiramo
		if(index >= size)
			index = index % size;
		
		Long questionId = getQuestionIds().get(index);
		
		return questionId;
	}

}
