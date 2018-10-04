package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionMessage;
import com.evola.edt.model.User;

public interface QuestionMessageRepository extends
		CrudRepository<QuestionMessage, Long> {

	public Page<QuestionMessage> findAll(Pageable pageable);

	@Query(value = "SELECT e, count(e), max(e.reportingDate) FROM QuestionMessage e GROUP BY e.question ORDER BY max(e.reportingDate)")
	public Object[] findAllGrouped();


	@Query(value = "SELECT e FROM QuestionMessage e WHERE e.question.id = :questionId ORDER BY e.reportingDate")
	public Page<QuestionMessage> findAllForQuestion(
			@Param("questionId") long questionId, Pageable pageRequest);

	@Query(value = "SELECT count(e) FROM QuestionMessage e WHERE e.question.id = :questionId")
	public long countForQuestion(@Param("questionId") long questionId);
	
	@Query(value = "SELECT e.question FROM QuestionMessage e WHERE e.user.id = :userId AND (:searchText IS NULL OR UPPER(e.question.text) LIKE UPPER(:searchText) OR CAST(e.question.id AS string) LIKE :searchText) GROUP BY e.question ORDER BY max(e.reportingDate) DESC")
	public Page<Question> findUserQuestions(@Param("userId") Long userId, @Param("searchText") String searchText, Pageable pageRequest);

	@Query(value = "SELECT e FROM QuestionMessage e WHERE e.user.id = :userId AND e.question.id = :questionId ORDER BY e.reportingDate DESC")
	public Page<QuestionMessage> findLastUserMessage(@Param("userId") Long userId, @Param("questionId") Long questionId, Pageable page);

	@Query(value = "SELECT e FROM QuestionMessage e WHERE e.question.id = :questionId ORDER BY e.reportingDate DESC")
	public Page<QuestionMessage> findLastMessage(@Param("questionId") Long questionId, Pageable page);

	@Query(value = "SELECT e.question FROM QuestionMessage e WHERE (:searchText IS NULL OR UPPER(e.question.text) LIKE UPPER(:searchText) OR CAST(e.question.id AS string) LIKE :searchText) GROUP BY e.question ORDER BY max(e.reportingDate) DESC")
	public Page<Question> findQuestions(@Param("searchText") String searchText, Pageable pageRequest);

	@Query(value = "SELECT COUNT(e) FROM QuestionMessage e WHERE e.user = :user")
	public Long countForUser(@Param("user") User user);

	@Query(value = "SELECT e FROM QuestionMessage e WHERE e.user.id = :userId ORDER BY e.reportingDate DESC")
	public Page<QuestionMessage> findLastMessagesByUser(@Param("userId") Long userId, Pageable page);
	
	@Query(value = "SELECT e FROM QuestionMessage e ORDER BY e.reportingDate DESC")
	public Page<QuestionMessage> findLastMessagesByAnyone(Pageable page);

}
