package com.evola.edt.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.QuestionCategory;
import com.evola.edt.model.TestingSession;
import com.evola.edt.model.User;
import com.evola.edt.model.UserQuestionStatTest;

public interface UserQuestionStatTestRepository extends CrudRepository<UserQuestionStatTest, Long> {

	@Query("SELECT t FROM UserQuestionStatTest t WHERE t.question.id = :questionId AND t.user.id = :userId ORDER BY t.time DESC LIMIT 1")
	public abstract Iterable<UserQuestionStatTest> findLastForQuestion(@Param("questionId") Long questionId,
			@Param("userId") Long userId);

	@Query(value = "SELECT count(DISTINCT q) FROM UserQuestionStatTest uqsl INNER JOIN uqsl.question q WHERE uqsl.correct = true AND (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) AND uqsl.user = :user")
	public abstract long countForDrivingCategory(@Param("drivingCategory") DrivingCategory drivingCategory,
			@Param("user") User user);

	@Query(value = "SELECT count(q) FROM UserQuestionStatTest uqsl INNER JOIN uqsl.question q WHERE uqsl.correct = true AND uqsl.user = :user")
	public abstract long countForUser(@Param("user") User user);

	@Query(value = "SELECT count(DISTINCT q) FROM UserQuestionStatTest uqsl INNER JOIN uqsl.question q WHERE uqsl.correct = true AND (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) AND :questionCategory MEMBER OF q.questionCategories AND uqsl.user = :user")
	public abstract long countForQuestionAndDrivingCategory(@Param("drivingCategory") DrivingCategory drivingCategory,
			@Param("questionCategory") QuestionCategory questionCategory, @Param("user") User user);

	@Query(value = "SELECT new map(q.id as id, count(uqsl) as count) FROM UserQuestionStatTest uqsl INNER JOIN uqsl.question q WHERE uqsl.correct = true AND (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) AND :questionCategory MEMBER OF q.questionCategories AND uqsl.user = :user GROUP BY q HAVING count(uqsl) >= :count")
	public abstract List<Map<String, Long>> groupTestedExactForQuestionAndDrivingCategory(
			@Param("drivingCategory") DrivingCategory drivingCategory,
			@Param("questionCategory") QuestionCategory questionCategory, @Param("user") User user,
			@Param("count") long count);

	@Query(value = "SELECT count(DISTINCT q) FROM UserQuestionStatTest uqsl INNER JOIN uqsl.question q WHERE uqsl.session = :session AND uqsl.correct = TRUE")
	public abstract long countForSession(@Param("session") TestingSession session);

	@Query(value = "SELECT DISTINCT q.id FROM UserQuestionStatTest uqsl INNER JOIN uqsl.question q WHERE uqsl.user = :user AND uqsl.correct = true")
	public abstract Iterable<Long> getTestedQuestionsIds(@Param("user") User user);

	@Query(value = "SELECT q.id, count(uqsl) FROM UserQuestionStatTest uqsl INNER JOIN uqsl.question q WHERE uqsl.user = :user AND uqsl.correct = true GROUP BY uqsl.question.id")
	public abstract Iterable<Object[]> getNumOfQuestionTests(@Param("user") User user);
	
	@Query(value = "SELECT count(uqsl) FROM UserQuestionStatTest uqsl WHERE uqsl.correct = true AND uqsl.user = :user")
	public abstract long countAllCorrectTests(@Param("user") User user);
	
	@Query(value = "SELECT count(uqsl) FROM UserQuestionStatTest uqsl WHERE uqsl.correct = true AND uqsl.question.id = :questionId AND uqsl.user.id = :userId")
	public abstract long countCorrectForQuestionAndUser(@Param("questionId") Long questionId, @Param("userId") Long userId);
	
	@Query(value = "SELECT count(uqsl) FROM UserQuestionStatTest uqsl WHERE uqsl.correct = true AND uqsl.question.id = :questionId")
	public abstract long countCorrectForQuestion(@Param("questionId") Long questionId);
	
	@Query(value = "SELECT count(uqsl) FROM UserQuestionStatTest uqsl WHERE uqsl.correct = false AND uqsl.question.id = :questionId AND uqsl.user.id = :userId")
	public abstract long countIncorrectForQuestionAndUser(@Param("questionId") Long questionId, @Param("userId") Long userId);

	@Query(value = "SELECT count(uqsl) FROM UserQuestionStatTest uqsl WHERE uqsl.correct = false AND uqsl.question.id = :questionId")
	public abstract long countIncorrectForQuestion(@Param("questionId") Long questionId);
	
	@Query(value = "SELECT MIN(uqsl.time) FROM UserQuestionStatTest uqsl WHERE uqsl.user.id = :userId")
	public abstract Date getFirstTestDate(@Param("userId") Long userId);
	
	@Query(value = "SELECT COUNT(DISTINCT uqsl.user) FROM UserQuestionStatTest uqsl WHERE uqsl.question.id = :questionId")
	public abstract long countUsers(@Param("questionId") Long questionId);

}
