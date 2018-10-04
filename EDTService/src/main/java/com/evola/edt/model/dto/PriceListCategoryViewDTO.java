package com.evola.edt.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola 22.09.2013.
 */
public class PriceListCategoryViewDTO {
	
    private Long id;
    private String name;
    private List<PriceListSubcategoryViewDTO> priceListSubcategoryViewDTOs = new ArrayList<>();
    
    //total
    private Double totalPrice;

    public PriceListCategoryViewDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PriceListSubcategoryViewDTO> getPriceListSubcategoryViewDTOs() {
        return priceListSubcategoryViewDTOs;
    }

    public void setPriceListSubcategoryViewDTOs(
            List<PriceListSubcategoryViewDTO> priceListSubcategoryViewDTOs) {
        this.priceListSubcategoryViewDTOs = priceListSubcategoryViewDTOs;
    }

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
