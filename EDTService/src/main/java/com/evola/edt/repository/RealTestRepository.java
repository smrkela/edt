package com.evola.edt.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.RealTest;
import com.evola.edt.model.RealTestCategory;
import com.evola.edt.model.User;

public interface RealTestRepository extends CrudRepository<RealTest, Long> {

	@Query("SELECT new map(e.id as id, e.name as name, COUNT(r) as count) FROM RealTest e LEFT OUTER JOIN e.results r GROUP BY e ORDER BY e.orderIndex DESC")
	List<Map<String, Object>> findAllTests();
	
	@Query("SELECT new map(e.id as id, e.name as name, COUNT(r) as count) FROM RealTest e LEFT OUTER JOIN e.results r WHERE e.category = :category GROUP BY e ORDER BY e.orderIndex ASC")
	List<Map<String, Object>> findAllForCategory(@Param("category") RealTestCategory category);
	
	@Query("SELECT COUNT(e) FROM RealTest e")
	Long findAllTestsCount();

	@Query("SELECT new map(e.id as id, SUM(r.points) as points, COUNT(r) as count) FROM RealTest e INNER JOIN e.results r WHERE r.user = :user GROUP BY e")
	List<Map<String, Object>> findUserTests(@Param("user") User user);
	
	@Query("SELECT new map(e.id as id, SUM(r.points) as points, COUNT(r) as count) FROM RealTest e INNER JOIN e.results r WHERE r.user = :user AND r.hasPassedTest = true GROUP BY e")
	List<Map<String, Object>> findUserPassedTests(@Param("user") User user);

	@Query(nativeQuery=true, value="SELECT w.rank FROM (SELECT e.id, e.testid, @rank \\:= @rank + 1 as rank	FROM (SELECT u.id, t.id as testid, SUM(r.points) as points	FROM user u	INNER JOIN realtestuserresult r ON r.user_id = u.id	INNER JOIN realtest t ON r.test_id = t.id WHERE t.id = :testId GROUP BY u.id, u.username ORDER BY SUM(r.points) DESC) as e, (SELECT @rank \\:= 0) r) AS w WHERE w.id = :userId")
	Double getTotalUserPosition(@Param("testId") Long testId, @Param("userId") Long id);

	@Query("SELECT COUNT(DISTINCT r.user) FROM RealTest e INNER JOIN e.results r")
	Long findTotalUsersCount();

}
