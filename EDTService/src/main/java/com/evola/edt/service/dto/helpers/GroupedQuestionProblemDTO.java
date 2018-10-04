package com.evola.edt.service.dto.helpers;

import java.util.Date;

import com.evola.edt.model.Question;

public class GroupedQuestionProblemDTO {

	private Question question;
	private long count;
	private Date maxDate;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}
