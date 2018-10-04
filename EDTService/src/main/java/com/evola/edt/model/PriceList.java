package com.evola.edt.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

/**
 * @author Nikola 21.09.2013.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id", "drivingSchool_id"}))
public class PriceList extends BaseEntity {

    @ManyToOne
    @NotNull
    private DrivingSchool drivingSchool;
    
    @Column(length=5000)
    private String description;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceListPrice> priceListPrices = new ArrayList<PriceListPrice>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DrivingSchool getDrivingSchool() {
        return drivingSchool;
    }

    public void setDrivingSchool(DrivingSchool drivingSchool) {
        this.drivingSchool = drivingSchool;
    }

    public List<PriceListPrice> getPriceListPrices() {
        return priceListPrices;
    }

    public void setPriceListPrices(List<PriceListPrice> priceListPrices) {
        this.priceListPrices = priceListPrices;
    }

    @AssertTrue(message = "priceListPrices must not be null or empty list")
    public Boolean getValidateSubCategoryPrices() {

        if (priceListPrices != null && priceListPrices.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

}
