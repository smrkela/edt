package com.evola.edt.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.evola.edt.model.PriceListSubcategory;

/**
 * @author Nikola 21.09.2013.
 * 
 */
public interface PriceListSubcategoryRepository extends
		JpaRepository<PriceListSubcategory, Long> {
}
