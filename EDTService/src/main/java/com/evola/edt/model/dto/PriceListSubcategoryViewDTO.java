package com.evola.edt.model.dto;

/**
 * @author Nikola 22.09.2013.
 */
public class PriceListSubcategoryViewDTO {
    private Long id;
    private String name;
    private Double price;

    public PriceListSubcategoryViewDTO() {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
