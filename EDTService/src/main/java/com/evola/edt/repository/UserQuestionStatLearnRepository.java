package com.evola.edt.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.LearningSession;
import com.evola.edt.model.QuestionCategory;
import com.evola.edt.model.User;
import com.evola.edt.model.UserQuestionStatLearn;

public interface UserQuestionStatLearnRepository extends CrudRepository<UserQuestionStatLearn, Long> {

	@Query(value = "SELECT count(DISTINCT q) FROM UserQuestionStatLearn uqsl INNER JOIN uqsl.question q WHERE (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) AND uqsl.user = :user")
	public abstract long countForDrivingCategory(@Param("drivingCategory") DrivingCategory drivingCategory,
			@Param("user") User user);

	@Query(value = "SELECT count(q) FROM UserQuestionStatLearn uqsl INNER JOIN uqsl.question q WHERE uqsl.user = :user")
	public abstract long countForUser(@Param("user") User user);

	@Query(value = "SELECT count(DISTINCT q) FROM UserQuestionStatLearn uqsl INNER JOIN uqsl.question q WHERE (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) AND :questionCategory MEMBER OF q.questionCategories AND uqsl.user = :user")
	public abstract long countForQuestionAndDrivingCategory(@Param("drivingCategory") DrivingCategory drivingCategory,
			@Param("questionCategory") QuestionCategory questionCategory, @Param("user") User user);

	@Query(value = "SELECT new map(q.id as id, count(uqsl) as count) FROM UserQuestionStatLearn uqsl INNER JOIN uqsl.question q WHERE (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) AND :questionCategory MEMBER OF q.questionCategories AND uqsl.user = :user GROUP BY q HAVING count(uqsl) >= :count")
	public abstract List<Map<String, Long>> groupLearnedExactForQuestionAndDrivingCategory(
			@Param("drivingCategory") DrivingCategory drivingCategory,
			@Param("questionCategory") QuestionCategory questionCategory, @Param("user") User user,
			@Param("count") long count);

	@Query(value = "SELECT count(DISTINCT q) FROM UserQuestionStatLearn uqsl INNER JOIN uqsl.question q WHERE uqsl.session = :session")
	public abstract long countForSession(@Param("session") LearningSession session);

	@Query(value = "SELECT DISTINCT q.id FROM UserQuestionStatLearn uqsl INNER JOIN uqsl.question q WHERE uqsl.user = :user GROUP BY uqsl.question.id HAVING count(uqsl.question.id) > :countLimit")
	public abstract Iterable<Long> getLearntQuestionsIds(@Param("user") User user, @Param("countLimit") Long countLimit);

	@Query(value = "SELECT q.id, count(uqsl) FROM UserQuestionStatLearn uqsl INNER JOIN uqsl.question q WHERE uqsl.user = :user GROUP BY uqsl.question.id")
	public abstract Iterable<Object[]> getNumOfQuestionLearns(@Param("user") User user);
	
	@Query(value = "SELECT count(uqsl) FROM UserQuestionStatLearn uqsl WHERE uqsl.user = :user")
	public abstract long countAllLearns(@Param("user") User user);
	
	@Query(value = "SELECT count(uqsl) FROM UserQuestionStatLearn uqsl WHERE uqsl.question.id = :questionId AND uqsl.user.id = :userId")
	public abstract long countForQuestionAndUser(@Param("questionId") Long questionId, @Param("userId") Long userId);
	
	@Query(value = "SELECT count(uqsl) FROM UserQuestionStatLearn uqsl WHERE uqsl.question.id = :questionId")
	public abstract long countForQuestion(@Param("questionId") Long questionId);

	@Query(value = "SELECT COUNT(DISTINCT uqsl.user) FROM UserQuestionStatLearn uqsl WHERE uqsl.question.id = :questionId")
	public abstract long countUsers(@Param("questionId") Long questionId);
	
	@Query(value = "SELECT MIN(uqsl.time) FROM UserQuestionStatLearn uqsl WHERE uqsl.user.id = :userId")
	public abstract Date getFirstLearnDate(@Param("userId") Long userId);
}
