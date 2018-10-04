package com.evola.edt.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.RealTest;
import com.evola.edt.model.RealTestUserResult;
import com.evola.edt.model.User;

public interface RealTestUserResultRepository extends CrudRepository<RealTestUserResult, Long> {

	@Query("SELECT e FROM RealTestUserResult e WHERE e.test = :test AND e.user = :user")
	List<RealTestUserResult> findMultipleByUserAndTest(@Param("user") User user, @Param("test") RealTest test);

	@Query("SELECT e FROM RealTestUserResult e WHERE e.test = :test ORDER BY e.points DESC, e.correctAnswers DESC, e.timeTaken ASC")
	List<RealTestUserResult> findByTest(@Param("test") RealTest test);
	
	@Query("SELECT e FROM RealTestUserResult e WHERE e.test = :test AND e.creationDate >= :dateFrom ORDER BY e.points DESC, e.correctAnswers DESC, e.timeTaken ASC")
	List<RealTestUserResult> findByTest(@Param("test") RealTest test, @Param("dateFrom") Date dateFrom, Pageable pageable);

	@Query("SELECT e FROM RealTestUserResult e WHERE e.test = :test AND e.user = :user ORDER BY e.points DESC, e.correctAnswers DESC, e.timeTaken ASC")
	List<RealTestUserResult> findByUserAndTest(@Param("test") RealTest test, @Param("user") User user);
	
	@Query("SELECT new map(e.user as user, COUNT(e) as numberOfTests, SUM(e.points) as points, SUM(e.timeTaken) as timeTaken, SUM(e.correctAnswers) as correctAnswers, SUM(e.wrongAswers) as incorrectAnswers) FROM RealTestUserResult e GROUP BY e.user ORDER BY SUM(e.points) DESC")
	List<Map<String, Object>> findTopResults(Pageable pageable);

	@Query("SELECT new map(e.user as user, COUNT(e) as numberOfTests, SUM(e.points) as points, SUM(e.timeTaken) as timeTaken, SUM(e.correctAnswers) as correctAnswers, SUM(e.wrongAswers) as incorrectAnswers) FROM RealTestUserResult e WHERE e.creationDate > :dateFrom GROUP BY e.user ORDER BY SUM(e.points) DESC")
	List<Map<String, Object>> findTopResultsByDate(@Param("dateFrom") Date dateFrom, Pageable pageable);

	@Query("SELECT count(e) FROM RealTestUserResult e WHERE e.user= :user")
	Long countByUser(@Param("user") User user);
	
	@Query("SELECT count(e) FROM RealTestUserResult e WHERE e.user= :user AND e.test = :test")
	Long countByUserAndTest(@Param("user") User user, @Param("test") RealTest test);

	@Query("SELECT SUM(e.points) FROM RealTestUserResult e WHERE e.user= :user")
	Long countPointsByUser(@Param("user") User user);

	@Query("SELECT COUNT(DISTINCT e.user) FROM RealTestUserResult e")
	Long countUsers();

	@Query("SELECT COUNT(DISTINCT e.user) FROM RealTestUserResult e WHERE e.creationDate > :dateFrom AND e.creationDate < :dateTo")
	Long countUsersOnDateRange(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

	@Query("SELECT COUNT(e) FROM RealTestUserResult e WHERE e.creationDate > :dateFrom AND e.creationDate < :dateTo")
	Long countTestResultsOnDateRange(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

}
