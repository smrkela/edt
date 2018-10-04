package com.evola.edt.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DailyTest;
import com.evola.edt.model.DailyTestUserResult;
import com.evola.edt.model.User;

public interface DailyTestUserResultRepository extends CrudRepository<DailyTestUserResult, Long> {

	@Query("SELECT e FROM DailyTestUserResult e WHERE e.test = :test AND e.user = :user")
	List<DailyTestUserResult> findMultipleByUserAndTest(@Param("user") User user, @Param("test") DailyTest test);

	DailyTestUserResult findByUserAndTest(User user, DailyTest test);

	@Query("SELECT e FROM DailyTestUserResult e WHERE e.test = :test ORDER BY e.points DESC, e.correctAnswers DESC, e.timeTaken ASC")
	List<DailyTestUserResult> findByTest(@Param("test") DailyTest test);

	@Query("SELECT new map(e.user as user, COUNT(e) as numberOfTests, SUM(e.points) as points, SUM(e.timeTaken) as timeTaken, SUM(e.correctAnswers) as correctAnswers, SUM(e.wrongAswers) as incorrectAnswers) FROM DailyTestUserResult e GROUP BY e.user ORDER BY SUM(e.points) DESC")
	List<Map<String, Object>> findTopResults(Pageable pageable);

	@Query("SELECT new map(e.user as user, COUNT(e) as numberOfTests, SUM(e.points) as points, SUM(e.timeTaken) as timeTaken, SUM(e.correctAnswers) as correctAnswers, SUM(e.wrongAswers) as incorrectAnswers) FROM DailyTestUserResult e WHERE e.creationDate > :dateFrom GROUP BY e.user ORDER BY SUM(e.points) DESC")
	List<Map<String, Object>> findTopResultsByDate(@Param("dateFrom") Date dateFrom, Pageable pageable);

	@Query("SELECT count(e) FROM DailyTestUserResult e WHERE e.test.date = :date")
	Long countByDate(@Param("date") Date date);

	@Query("SELECT count(e) FROM DailyTestUserResult e WHERE e.user= :user")
	Long countByUser(@Param("user") User user);

	@Query("SELECT SUM(e.points) FROM DailyTestUserResult e WHERE e.user= :user")
	Long countPointsByUser(@Param("user") User user);

	@Query("SELECT COUNT(DISTINCT e.user) FROM DailyTestUserResult e")
	Long countUsers();

	@Query("SELECT COUNT(DISTINCT e.user) FROM DailyTestUserResult e WHERE e.creationDate > :dateFrom AND e.creationDate < :dateTo")
	Long countUsersOnDateRange(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

	@Query("SELECT COUNT(e) FROM DailyTestUserResult e WHERE e.creationDate > :dateFrom AND e.creationDate < :dateTo")
	Long countTestResultsOnDateRange(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

}
