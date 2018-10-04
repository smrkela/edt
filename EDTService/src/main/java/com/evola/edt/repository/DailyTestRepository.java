package com.evola.edt.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DailyTest;
import com.evola.edt.model.User;

public interface DailyTestRepository extends CrudRepository<DailyTest, Long> {

	DailyTest findByDate(Date date);

	@Query("SELECT new map(e.id as id, e.date as date, COUNT(r) as count) FROM DailyTest e LEFT OUTER JOIN e.results r GROUP BY e ORDER BY e.date DESC")
	List<Map<String, Object>> findAllTests();
	
	@Query("SELECT COUNT(e) FROM DailyTest e")
	Long findAllTestsCount();

	@Query("SELECT new map(e.id as id, SUM(r.points) as points) FROM DailyTest e INNER JOIN e.results r WHERE r.user = :user GROUP BY e")
	List<Map<String, Object>> findUserTests(@Param("user") User user);

	@Query(nativeQuery=true, value="SELECT w.rank FROM (SELECT e.id, @rank \\:= @rank + 1 as rank	FROM (SELECT u.id, SUM(r.points) as points	FROM user u	INNER JOIN dailytestuserresult r ON r.user_id = u.id	GROUP BY u.id, u.username	ORDER BY SUM(r.points) DESC) as e, (SELECT @rank \\:= 0) r) AS w WHERE w.id = :userId")
	Double getTotalUserPosition(@Param("userId") Long id);

	@Query("SELECT COUNT(DISTINCT r.user) FROM DailyTest e INNER JOIN e.results r")
	Long findTotalUsersCount();

}
