package com.evola.edt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class RealTestCategory extends BaseEntity {
	
	@NotNull
	@Size(min = 1)
	@Column(unique = true)
	private String name;
	
	@NotNull
	@Column(name="number_of_questions")
	private Integer numberOfQuestions;
	
	@NotNull
	@Column(name="duration")
	private Integer duration;
	
	@NotNull
	@Column(name="minimum")
	private Integer minimumPoints;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="driving_category")
	private DrivingCategory drivingCategory;

	RealTestCategory() {
	}

	public RealTestCategory(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public void setNumberOfQuestions(Integer numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getMinimumPoints() {
		return minimumPoints;
	}

	public void setMinimumPoints(Integer minimumPoints) {
		this.minimumPoints = minimumPoints;
	}

	public DrivingCategory getDrivingCategory() {
		return drivingCategory;
	}

	public void setDrivingCategory(DrivingCategory drivingCategory) {
		this.drivingCategory = drivingCategory;
	}

}
