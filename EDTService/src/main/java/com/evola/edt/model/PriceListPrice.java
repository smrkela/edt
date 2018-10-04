package com.evola.edt.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author Nikola 21.09.2013.
 * 
 */
@Entity
public class PriceListPrice extends BaseEntity {
	@ManyToOne
	@NotNull
	private PriceListSubcategory priceListSubcategory;
	@ManyToOne
	@NotNull
	private PriceListCategory priceListCategory;
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
	private Price price;

	PriceListPrice() {
	}

	public PriceListPrice(PriceListSubcategory priceListSubcategory,
			PriceListCategory priceListCategory, Price price) {
		super();
		this.priceListSubcategory = priceListSubcategory;
		this.priceListCategory = priceListCategory;
		this.price = price;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public PriceListSubcategory getPriceListSubcategory() {
		return priceListSubcategory;
	}

	public void setPriceListSubcategory(
			PriceListSubcategory priceListSubcategory) {
		this.priceListSubcategory = priceListSubcategory;
	}

	public PriceListCategory getPriceListCategory() {
		return priceListCategory;
	}

	public void setPriceListCategory(PriceListCategory priceListCategory) {
		this.priceListCategory = priceListCategory;
	}

}
