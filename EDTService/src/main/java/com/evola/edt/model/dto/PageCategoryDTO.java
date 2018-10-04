package com.evola.edt.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 23.05.2013.
 * 
 */
@XmlRootElement
public class PageCategoryDTO extends BaseDTO {
	
	private String name;
	private String note;
	private String pageType;
	private Integer orderIndex;
	
	private List<PageDTO> pages = new ArrayList<PageDTO>();

	public PageCategoryDTO() {
		super();
	}

	public PageCategoryDTO(String name) {
		super();
		this.name = name;
	}

	@XmlElement
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

	public List<PageDTO> getPages() {
		return pages;
	}

	public void setPages(List<PageDTO> pages) {
		this.pages = pages;
	}

}
