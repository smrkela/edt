package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingLicenceCategory;

public interface DrivingLicenceCategoryRepository extends CrudRepository<DrivingLicenceCategory, Long> {

	@Query(value = "FROM DrivingLicenceCategory q WHERE q.categoryId = :categoryId")
	public DrivingLicenceCategory findByCategoryId(@Param("categoryId") String categoryId);

	@Query(value = "FROM DrivingLicenceCategory q ORDER BY q.orderIndex ASC")
	public List<DrivingLicenceCategory> findAllSorted();

}
