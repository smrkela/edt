package com.evola.edt.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.QuestionCategory;
import com.evola.edt.model.User;
import com.evola.edt.model.UserQuestionFavorite;

public interface UserQuestionFavoriteRepository extends CrudRepository<UserQuestionFavorite, Long> {

	@Query(value = "SELECT count(q) FROM UserQuestionFavorite uqsl INNER JOIN uqsl.question q WHERE uqsl.user = :user")
	public abstract long countForUser(@Param("user") User user);

	@Query(value = "SELECT DISTINCT q.id FROM UserQuestionFavorite uqsl INNER JOIN uqsl.question q WHERE uqsl.user = :user GROUP BY uqsl.question.id HAVING count(uqsl.question.id) > :countLimit")
	public abstract Iterable<Long> getFavoriteQuestionsIds(@Param("user") User user, @Param("countLimit") Long countLimit);

	@Query(value = "SELECT count(uqsl) FROM UserQuestionFavorite uqsl WHERE uqsl.user = :user")
	public abstract long countAllFavorites(@Param("user") User user);

	@Query(value = "SELECT uqsl FROM UserQuestionFavorite uqsl WHERE uqsl.user.id = :userId AND uqsl.question.id = :questionId")
	public abstract UserQuestionFavorite getForQuestionAndUser(@Param("questionId") Long questionId, @Param("userId") Long userId);

	@Query(value = "SELECT new map(q.id as id, count(uqsl) as count) FROM UserQuestionFavorite uqsl INNER JOIN uqsl.question q WHERE (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) AND :questionCategory MEMBER OF q.questionCategories AND uqsl.user = :user GROUP BY q")
	public abstract List<Map<String, Long>> groupFavoriteExactForQuestionAndDrivingCategory(
			@Param("drivingCategory") DrivingCategory drivingCategory, @Param("questionCategory") QuestionCategory questionCategory,
			@Param("user") User user);

}
