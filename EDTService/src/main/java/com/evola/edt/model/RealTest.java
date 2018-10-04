package com.evola.edt.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class RealTest extends BaseEntity {

	@Temporal(TemporalType.DATE)
	@NotNull
	private Date creationDate;
	
	@NotNull
	@Column(unique=true)
	private String name;
	
	@Column(name="order_index")
	private Integer orderIndex;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "realtest_question", joinColumns = { @JoinColumn(name = "realtest_id") }, inverseJoinColumns = { @JoinColumn(name = "question_id") })
	private List<Question> questions;

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "test")
	private List<RealTestUserResult> results;
	
	@ManyToOne
	private RealTestCategory category;

	public RealTest() {
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<RealTestUserResult> getResults() {
		return results;
	}

	public void setResults(List<RealTestUserResult> results) {
		this.results = results;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RealTestCategory getCategory() {
		return category;
	}

	public void setCategory(RealTestCategory category) {
		this.category = category;
	}

}
