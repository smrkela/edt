package com.evola.edt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class DrivingLicenceCategory extends BaseEntity {
	
	@Column(unique = true)
	private String categoryId;
	
	private String name;
	
	@ManyToOne
	private DrivingCategory drivingCategory;
	
	private Integer orderIndex;

	public DrivingLicenceCategory() {
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

	public DrivingCategory getDrivingCategory() {
		return drivingCategory;
	}

	public void setDrivingCategory(DrivingCategory drivingCategory) {
		this.drivingCategory = drivingCategory;
	}

}
