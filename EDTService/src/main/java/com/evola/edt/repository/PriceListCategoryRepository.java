package com.evola.edt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evola.edt.model.PriceListCategory;

/**
 * @author Nikola 21.09.2013.
 */
public interface PriceListCategoryRepository extends
		JpaRepository<PriceListCategory, Long> {
}
