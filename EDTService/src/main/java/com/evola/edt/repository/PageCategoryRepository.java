package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evola.edt.model.PageCategory;

/**
 * @author Nikola 24.05.2013.
 *
 */
public interface PageCategoryRepository extends
		JpaRepository<PageCategory, Long> {

	List<PageCategory> findByPageType(String string);

}
