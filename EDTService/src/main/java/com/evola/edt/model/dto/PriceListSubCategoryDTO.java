package com.evola.edt.model.dto;

/**
 * @author Nikola 21.09.2013.
 * 
 */
public class PriceListSubCategoryDTO extends BaseDTO {

	private String name;

	public PriceListSubCategoryDTO() {
	}

	public PriceListSubCategoryDTO(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
