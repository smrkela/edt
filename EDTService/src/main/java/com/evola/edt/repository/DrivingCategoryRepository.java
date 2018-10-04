package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingCategory;

/**
 * @author Nikola 03.04.2013.
 * 
 */
public interface DrivingCategoryRepository extends
		CrudRepository<DrivingCategory, Long> {

	@Query(value = "FROM DrivingCategory q WHERE q.categoryId = :categoryId")
	public DrivingCategory findByCategoryId(
			@Param("categoryId") String categoryId);

	@Query(value = "FROM DrivingCategory q ORDER BY q.orderIndex ASC")
	public List<DrivingCategory> findAllSorted();
	
}
