package com.evola.edt.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class DailyTest extends BaseEntity {

	@Temporal(TemporalType.DATE)
	@NotNull
	private Date date;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "dailytest_question", joinColumns = { @JoinColumn(name = "dailytest_id") }, inverseJoinColumns = { @JoinColumn(name = "question_id") })
	private List<Question> questions;

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "test")
	private List<DailyTestUserResult> results;

	public DailyTest() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<DailyTestUserResult> getResults() {
		return results;
	}

	public void setResults(List<DailyTestUserResult> results) {
		this.results = results;
	}

}
