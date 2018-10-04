package com.evola.edt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * @author Nikola 23.05.2013.
 * 
 */
@Entity
public class PageCategory extends BaseEntity {

	@Column(length = 100)
	@Size(max = 100)
	private String name;
	
	@Column(length=500)
	@Size(max=500)
	private String note;

	// NEWS|INFORMATION
	@Column(nullable = false)
	private String pageType;

	private Integer orderIndex;

	PageCategory() {
		super();
	}

	public PageCategory(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

}
