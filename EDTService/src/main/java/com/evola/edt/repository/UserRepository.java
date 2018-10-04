package com.evola.edt.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.User;

/**
 * @author Nikola 18.05.2013.
 * 
 */
public interface UserRepository extends JpaRepository<User, Long> {

	public abstract User findByUsernameAndEnabled(String username, Boolean enabled);

	public abstract User findByUsername(String username);

	@Query("select count(u) from User u where u.email=:email")
	public abstract Long countByEmail(@Param("email") String email);

	@Query("select count(u) from User u where u.username=:username")
	public abstract Long countByUsername(@Param("username") String username);

	public abstract User findByEmail(String email);

	public abstract User findById(Long id);

	// broji sve korisnike bez obzira na status (enabled/disabled, tj.
	// active/inactive)
	@Query("select count(u) from User u")
	public abstract Long countNumberOfUsers();

	@Query("select u from User u where u.enabled = 1")
	public abstract List<User> findAllEnabledUsers();

	// lista korisnika sa navise poena tj. naucenih i uspesno testiranih pitanja
	// u odredjenom periodu
	@Query(value = "SELECT t.id, t.username, SUM(t.learns) + SUM(t.tests) AS points FROM "
			+ "(SELECT u.id as id, u.username as username, COUNT(l.id) as learns, 0 as tests FROM edtdb.User u "
			+ "LEFT OUTER JOIN edtdb.userquestionstatlearn l ON l.user_id = u.id WHERE l.time > :date GROUP BY u.id "
			+ "UNION "
			+ "SELECT u.id as id, u.username as username, 0 as learns, COUNT(t.id) as tests FROM edtdb.User u "
			+ "LEFT OUTER JOIN edtdb.userquestionstattest t ON t.user_id = u.id WHERE t.correct = true AND t.time > :date GROUP BY u.id) AS t "
			+ "GROUP BY t.id, t.username ORDER BY SUM(t.learns) + SUM(t.tests) DESC LIMIT 5", nativeQuery = true)
	public abstract List findBestUsers(@Param("date") Date date);

	@Query(value = "SELECT e FROM User e WHERE UPPER(e.username) LIKE UPPER(:username+'%')")
	public abstract List<User> findAllWithUsernameStarts(@Param("username") String username);

	@Query(value="SELECT COUNT(e) FROM User e WHERE e.lastLogin >= :startDate AND e.lastLogin <= :finishDate")
	public abstract Long countNumberOfLoggedInUsers(@Param("startDate") Date startDate, @Param("finishDate") Date finishDate);

	@Query(value="SELECT COUNT(e) FROM User e WHERE e.registrationDate >= :startDate AND e.registrationDate <= :finishDate")
	public abstract Long countNumberOfRegisteredUsers(@Param("startDate") Date startDate, @Param("finishDate") Date finishDate);

	//@Query(value="SELECT COUNT(DISTINCT e) FROM User e INNER JOIN e.learnStatistics l INNER JOIN e.testingStatistics t WHERE l.time >= :startDate OR t.time >= :startDate")
	@Query(value="SELECT COUNT(DISTINCT q.user_id) FROM	(select distinct e.user_id as user_id from edtdb.UserQuestionStatLearn e where e.time>=:startDate and e.time<=:finishDate union select distinct t.user_id  as user_id from edtdb.UserQuestionStatTest t where t.time>=:startDate and t.time<=:finishDate) as q", nativeQuery=true)
	public abstract BigInteger contNumberOfApplicationUsers(@Param("startDate") Date startDate, @Param("finishDate") Date finishDate);

	@Query(value="SELECT e FROM User e WHERE e.lastLogin >= :startDate AND e.lastLogin <= :finishDate ORDER BY e.lastLogin DESC")
	public abstract List<User> getLoggedInUsers(@Param("startDate") Date startDate, @Param("finishDate") Date finishDate);
	
	@Query(value="SELECT e FROM User e WHERE e.registrationDate >= :startDate AND e.registrationDate <= :finishDate ORDER BY e.registrationDate DESC")
	public abstract List<User> getRegisteredUsers(@Param("startDate") Date startDate, @Param("finishDate") Date finishDate);
	
	@Query(value="SELECT * FROM edtdb.user u WHERE u.id IN (SELECT DISTINCT q.user_id FROM (select distinct e.user_id as user_id from edtdb.UserQuestionStatLearn e where e.time>=:startDate and e.time<=:finishDate union select distinct t.user_id  as user_id from edtdb.UserQuestionStatTest t where t.time>=:startDate and t.time<=:finishDate) as q)", nativeQuery=true)
	public abstract List<User> getApplicationUsers(@Param("startDate") Date startDate, @Param("finishDate") Date finishDate);
}
