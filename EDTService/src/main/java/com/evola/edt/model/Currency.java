package com.evola.edt.model;

/**
 * @author Nikola 22.09.2013.
 * 
 */
public enum Currency {
	EUR("EUR"), RSD("RSD");
	private String label;

	private Currency(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
