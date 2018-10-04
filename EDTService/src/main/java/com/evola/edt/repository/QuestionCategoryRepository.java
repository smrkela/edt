package com.evola.edt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.QuestionCategory;

/**
 * @author Nikola 03.04.2013.
 * 
 */
public interface QuestionCategoryRepository extends
		CrudRepository<QuestionCategory, Long> {

	@Query(value = "FROM QuestionCategory q WHERE q.categoryId = :categoryId")
	public QuestionCategory findByCategoryId(
			@Param("categoryId") String categoryId);

}
