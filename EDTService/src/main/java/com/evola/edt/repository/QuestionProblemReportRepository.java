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
import com.evola.edt.model.QuestionProblemReport;

public interface QuestionProblemReportRepository extends
		CrudRepository<QuestionProblemReport, Long> {

	@Query(value = "SELECT qpr FROM QuestionProblemReport qpr INNER JOIN FETCH qpr.user u INNER JOIN FETCH qpr.question q INNER JOIN FETCH qpr.problemCategory pc")
	public Iterable<QuestionProblemReport> findAllQuestionProblemReport();

	@Query(value = "SELECT qpr FROM QuestionProblemReport qpr INNER JOIN FETCH qpr.user u INNER JOIN FETCH qpr.question q INNER JOIN FETCH qpr.problemCategory pc WHERE qpr.id = :id")
	public QuestionProblemReport findByQuestionProblemReportId(
			@Param("id") Long questionProblemReportId);

	@Query(value = "SELECT qpr FROM QuestionProblemReport qpr INNER JOIN FETCH qpr.user u INNER JOIN FETCH qpr.question q INNER JOIN FETCH qpr.problemCategory pc WHERE qpr.fixed = 'true'")
	public Iterable<QuestionProblemReport> findAllFixedQuestionProblemReport();

	@Query(value = "SELECT qpr FROM QuestionProblemReport qpr INNER JOIN FETCH qpr.user u INNER JOIN FETCH qpr.question q INNER JOIN FETCH qpr.problemCategory pc WHERE qpr.fixed = 'false'")
	public Iterable<QuestionProblemReport> findAllOpenedQuestionProblemReport();

	public Page<QuestionProblemReport> findAll(Pageable pageable);

	@Query(value = "SELECT e, count(e), max(e.reportingDate) FROM QuestionProblemReport e GROUP BY e.question ORDER BY max(e.reportingDate)")
	public Object[] findAllGrouped();

	@Query(value = "SELECT e.question, count(e), max(e.reportingDate) FROM QuestionProblemReport e WHERE e.fixed!=true GROUP BY e.question ORDER BY max(e.reportingDate)")
	public List<Object[]> findAllGroupedUnfixed();

	@Query(value = "SELECT e FROM QuestionProblemReport e WHERE e.fixed!=true AND e.question.id = :questionId ORDER BY e.reportingDate")
	public List<QuestionProblemReport> findAllUnfixedForQuestion(
			@Param("questionId") long questionId);
	
}
