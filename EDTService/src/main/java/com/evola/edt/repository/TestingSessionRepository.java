package com.evola.edt.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.TestingSession;
import com.evola.edt.model.User;

public interface TestingSessionRepository extends CrudRepository<TestingSession, Long> {

	public abstract TestingSession findByUid(String uid);

	@Query("SELECT s FROM TestingSession s WHERE s.user = :user ORDER BY s.start DESC LIMIT 5")
	public abstract Iterable<TestingSession> findRecent(@Param("user") User user);

	@Query("SELECT s FROM TestingSession s WHERE s.user = :user ORDER BY s.start DESC")
	public abstract Iterable<TestingSession> findAllForUser(@Param("user") User user);

	@Query("SELECT DISTINCT s.user.id FROM TestingSession s WHERE s.start >= :dateFrom")
	public abstract List<Long> getRecentTestingUserIds(@Param("dateFrom") Date dateFrom);

}
