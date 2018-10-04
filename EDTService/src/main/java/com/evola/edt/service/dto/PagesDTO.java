package com.evola.edt.service.dto;

import java.util.List;

import com.evola.edt.model.dto.PageDTO;

public class PagesDTO {
	
	private List<PageDTO> pages;
	private Boolean isFirstPage;
	private Boolean isLastPage;

	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public Boolean getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public List<PageDTO> getPages() {
		return pages;
	}

	public void setPages(List<PageDTO> pages) {
		this.pages = pages;
	}

}
