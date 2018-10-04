package com.evola.edt.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * @author Nikola 22.09.2013.
 * 
 */
@Entity
public class Price extends BaseEntity {
	@Enumerated(EnumType.STRING)
    @NotNull
	private Currency currency;
    @NotNull
    private Double value;

	Price() {
	}

	public Price(Currency currency, Double value) {
		super();
		this.currency = currency;
		this.value = value;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
