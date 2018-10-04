package com.evola.edt.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.LearningSession;
import com.evola.edt.model.User;

public interface LearningSessionRepository extends
		CrudRepository<LearningSession, Long> {

	public abstract LearningSession findByUid(String uid);

	@Query("SELECT s FROM LearningSession s WHERE s.user = :user ORDER BY s.start DESC LIMIT 5")
	public abstract Iterable<LearningSession> findRecent(@Param("user") User user);

	@Query("SELECT s FROM LearningSession s WHERE s.user = :user ORDER BY s.start DESC")
	public abstract Iterable<LearningSession> findAllForUser(@Param("user") User user);

	@Query("SELECT DISTINCT s.user.id FROM LearningSession s WHERE s.start >= :dateFrom")
	public abstract List<Long> getRecentLearningUserIds(@Param("dateFrom") Date dateFrom);

}
