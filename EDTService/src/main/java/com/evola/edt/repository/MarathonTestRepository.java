package com.evola.edt.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.MarathonTest;
import com.evola.edt.model.User;

public interface MarathonTestRepository extends CrudRepository<MarathonTest, Long> {

	@Query("SELECT COUNT(e) FROM MarathonTest e")
	Long findAllTestsCount();

	MarathonTest findByUid(String uid);

	@Query("SELECT COUNT(e) FROM MarathonTest e WHERE e.user = :user")
	Long countForUser(@Param("user") User user);

	@Query("SELECT SUM(e.correctAnswers) * 100 / SUM(e.totalQuestions) FROM MarathonTest e WHERE e.user = :user")
	Number getUserPrecision(@Param("user") User user);

	@Query("SELECT COUNT(DISTINCT e.user) FROM MarathonTest e")
	Long countUsers();

	@Query("SELECT SUM(e.correctAnswers) * 100 / SUM(e.totalQuestions) FROM MarathonTest e")
	Number getPrecision();

	@Query("SELECT e FROM MarathonTest e WHERE e.creationDate > :dateFrom ORDER BY e.points DESC")
	List<MarathonTest> getBestRecentTests(@Param("dateFrom") Date dateFrom, Pageable pageRequest);
	
	@Query("SELECT new map(e.user as user, SUM(e.points) as points, SUM(e.timeTaken) as timeTaken, SUM(e.correctAnswers) as correctAnswers, SUM(e.wrongAswers) as incorrectAnswers) FROM MarathonTest e WHERE e.creationDate > :dateFrom GROUP BY e.user ORDER BY SUM(e.points) DESC")
	List<Map<String, Object>> findTopResultsByDate(@Param("dateFrom") Date dateFrom, Pageable pageable);

	@Query("SELECT COUNT(e) FROM MarathonTest e WHERE e.creationDate > :dateFrom AND e.creationDate < :dateTo")
	Long countTestResultsOnDateRange(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);


}
