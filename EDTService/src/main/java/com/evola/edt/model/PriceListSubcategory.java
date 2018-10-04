package com.evola.edt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Nikola 21.09.2013.
 * 
 */
@Entity
public class PriceListSubcategory extends BaseEntity {

	@NotNull
	@Size(min = 1)
	@Column(unique = true)
	private String name;

	PriceListSubcategory() {
	}

	public PriceListSubcategory(String name) {
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
