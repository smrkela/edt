package com.evola.edt.model.dto;

/**
 * @author Nikola 22.09.2013.
 * 
 */
public class PriceDTO extends BaseDTO {
	private String currency;
	private Double value;

	public PriceDTO() {
	}

	public String getCurrency() {
		return currency;
	}

	public PriceDTO(String currency, Double value) {
		super();
		this.currency = currency;
		this.value = value;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
