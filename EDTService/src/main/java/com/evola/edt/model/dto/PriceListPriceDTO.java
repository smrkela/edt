package com.evola.edt.model.dto;

/**
 * @author Nikola 22.09.2013.
 * 
 */
public class PriceListPriceDTO extends BaseDTO {
	private PriceListSubCategoryDTO priceListSubCategoryDTO;
	private PriceListCategoryDTO priceListCategoryDTO;
	private PriceDTO priceDTO;

	public PriceListPriceDTO() {
	}

	public PriceListPriceDTO(PriceListSubCategoryDTO priceListSubCategoryDTO,
			PriceListCategoryDTO priceListCategoryDTO, PriceDTO priceDTO) {
		super();
		this.priceListSubCategoryDTO = priceListSubCategoryDTO;
		this.priceListCategoryDTO = priceListCategoryDTO;
		this.priceDTO = priceDTO;
	}

	public PriceListSubCategoryDTO getPriceListSubCategoryDTO() {
		return priceListSubCategoryDTO;
	}

	public void setPriceListSubCategoryDTO(
			PriceListSubCategoryDTO priceListSubCategoryDTO) {
		this.priceListSubCategoryDTO = priceListSubCategoryDTO;
	}

	public PriceDTO getPriceDTO() {
		return priceDTO;
	}

	public void setPriceDTO(PriceDTO priceDTO) {
		this.priceDTO = priceDTO;
	}

	public PriceListCategoryDTO getPriceListCategoryDTO() {
		return priceListCategoryDTO;
	}

	public void setPriceListCategoryDTO(
			PriceListCategoryDTO priceListCategoryDTO) {
		this.priceListCategoryDTO = priceListCategoryDTO;
	}

}
