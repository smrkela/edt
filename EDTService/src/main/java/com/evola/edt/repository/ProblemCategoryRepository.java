package com.evola.edt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.evola.edt.model.ProblemCategory;

/**
 * @author Daci, 24.04.2013.
 *
 */
public interface ProblemCategoryRepository extends CrudRepository<ProblemCategory, Long> {

	@Query (value = "SELECT pc FROM ProblemCategory pc")
	public Iterable<ProblemCategory> findAllProblemCategories();
	
	public ProblemCategory findByProblemCategoryId(String problemCategoryId);
	
}