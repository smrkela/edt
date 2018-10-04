package com.evola.edt.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 21.09.2013.
 * 
 */
@XmlRootElement
public class PriceListDTO extends BaseDTO {
	private String description;
	private DrivingSchoolDTO drivingSchoolDTO;
	private List<PriceListPriceDTO> priceListPriceDTOs = new ArrayList<PriceListPriceDTO>();
	private List<PriceListCategoryViewDTO> listCategoryViewDTOs = new ArrayList<PriceListCategoryViewDTO>();

	public PriceListDTO() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DrivingSchoolDTO getDrivingSchoolDTO() {
		return drivingSchoolDTO;
	}

	public void setDrivingSchoolDTO(DrivingSchoolDTO drivingSchoolDTO) {
		this.drivingSchoolDTO = drivingSchoolDTO;
	}

	public List<PriceListPriceDTO> getPriceListPriceDTOs() {
		return priceListPriceDTOs;
	}

	public void setPriceListPriceDTOs(List<PriceListPriceDTO> priceListPriceDTOs) {
		this.priceListPriceDTOs = priceListPriceDTOs;
	}

	public List<PriceListCategoryViewDTO> getListCategoryViewDTOs() {
		return listCategoryViewDTOs;
	}

	public void setListCategoryViewDTOs(
			List<PriceListCategoryViewDTO> listCategoryViewDTOs) {
		this.listCategoryViewDTOs = listCategoryViewDTOs;
	}

}
