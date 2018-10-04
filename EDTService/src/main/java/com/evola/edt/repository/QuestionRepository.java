package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionCategory;

/**
 * @author Nikola 03.04.2013.
 * 
 */
public interface QuestionRepository extends CrudRepository<Question, Long> {

	@Query(value = "FROM Question q WHERE q.questionId = :questionId")
	public Question findByQuestionId(@Param("questionId") String questionId);

	@Query(value = "SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.questionImages LEFT JOIN FETCH q.questionAnswers LEFT JOIN FETCH q.questionCategories LEFT JOIN FETCH q.drivingCategories")
	public Iterable<Question> findAllFull();

	@Query(value = "SELECT count(q) FROM Question q WHERE (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY)")
	public abstract long countForDrivingCategory(@Param("drivingCategory") DrivingCategory drivingCategory);

	@Query(value = "SELECT count(q) FROM Question q WHERE :questionCategory MEMBER OF q.questionCategories AND (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY)")
	public abstract long countForQuestionAndDrivingCategory(@Param("questionCategory") QuestionCategory questionCategory, @Param("drivingCategory") DrivingCategory drivingCategory);

	@Query(value = "SELECT q FROM Question q WHERE (:questionCategory MEMBER OF q.questionCategories OR :questionCategory IS NULL) AND (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY)")
	public Page<Question> findAll(@Param("questionCategory") QuestionCategory questionCategory, @Param("drivingCategory") DrivingCategory drivingCategory, Pageable pageable);

	@Query(value = "SELECT q FROM Question q WHERE (:questionCategory MEMBER OF q.questionCategories OR :questionCategory IS NULL) AND (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) ORDER BY q.id")
	public List<Question> findAll(@Param("questionCategory") QuestionCategory questionCategory, @Param("drivingCategory") DrivingCategory drivingCategory);

	@Query(value = "SELECT DISTINCT q FROM Question q WHERE (:questionCategory MEMBER OF q.questionCategories) AND (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) ORDER BY rand()")
	public List<Question> findRandom(@Param("questionCategory") QuestionCategory questionCategory, @Param("drivingCategory") DrivingCategory drivingCategory, Pageable pageable);

	@Query(value = "SELECT DISTINCT q.id FROM Question q WHERE (:questionCategory MEMBER OF q.questionCategories OR :questionCategory = null) AND (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) ORDER BY rand()")
	public List<Long> findRandomIds(@Param("questionCategory") QuestionCategory questionCategory, @Param("drivingCategory") DrivingCategory drivingCategory, Pageable pageable);
	
	@Query(value = "SELECT q FROM Question q WHERE q.id > :questionId ORDER BY q.id ASC")
	public Page<Question> findNext(@Param("questionId") Long questionId, Pageable pageRequest);

	@Query(value = "SELECT q FROM Question q WHERE q.id < :questionId ORDER BY q.id DESC")
	public Page<Question> findPrevious(@Param("questionId") Long questionId, Pageable pageRequest);

	@Query(value = "SELECT COUNT(q) FROM Question q WHERE q.id < :questionId AND (:questionCategory MEMBER OF q.questionCategories OR :questionCategory IS NULL) AND (:drivingCategory MEMBER OF q.drivingCategories OR :drivingCategory = null OR q.drivingCategories IS EMPTY) ORDER BY q.id ASC")
	public Long findCountBeforeQuestion(@Param("questionCategory") QuestionCategory questionCategory, @Param("drivingCategory") DrivingCategory drivingCategory, @Param("questionId") Long questionId);

	@Query(value = "SELECT e FROM Question e WHERE e.text LIKE :text")
	public List<Question> findQuestionsByText(@Param("text") String text);

	@Query(value = "SELECT DISTINCT q FROM QuestionAnswer e INNER JOIN e.question q WHERE e.text LIKE :text GROUP BY q")
	public List<Question> findQuestionsByAnswerText(@Param("text") String text);
}
