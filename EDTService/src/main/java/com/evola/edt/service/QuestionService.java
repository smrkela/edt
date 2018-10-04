package com.evola.edt.service;

import java.util.List;

import com.evola.edt.model.Question;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.QuestionCategoryDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.QuestionProblemReportDTO;
import com.evola.edt.model.dto.UserDiscussionPreviewDTO;
import com.evola.edt.service.dto.AllQuestionMessagesDTO;
import com.evola.edt.service.dto.AllQuestionsDTO;
import com.evola.edt.service.dto.DoneQuestionsDTO;
import com.evola.edt.service.dto.LearningQuestionsDTO;
import com.evola.edt.service.dto.LessonDTO;
import com.evola.edt.service.dto.MyQuestionMessagesDTO;
import com.evola.edt.service.dto.PagedResultDTO;
import com.evola.edt.service.dto.QuestionDetailStatDTO;
import com.evola.edt.service.dto.QuestionMessageDTO;
import com.evola.edt.service.dto.QuestionMessagesDTO;
import com.evola.edt.service.dto.QuestionsStatDTO;
import com.evola.edt.service.dto.helpers.GroupedQuestionProblemDTO;
import com.evola.edt.service.dto.helpers.QuestionViewNavigationWrapper;

public interface QuestionService {

	/**
	 * @author Nikola 03.04.2013.
	 * @return
	 */
	public abstract AllQuestionsDTO getAllQuestions();

	public abstract void reportQuestionProblem(Long userId, Long questionId,
			String problemCategoryId, String userComment);

	public abstract DoneQuestionsDTO getLearntQuestionsIds(Long userId,
			Long numOfLearnedQuestionsLimit);

	public abstract DoneQuestionsDTO getTestedAndLearntQuestionsIds(Long userId);

	public abstract AllQuestionsDTO getDrivingCategories();

	public abstract QuestionsStatDTO getAllQuestionsStats(Long userId);

	public abstract LearningQuestionsDTO getQuestions(Long questionsCategoryId,
			Long drivingCategoryId, Integer startingIndex, int count);

	public abstract AllQuestionsDTO getQuestionCategories();

	public abstract QuestionDTO getQuestion(Long id);

	public abstract void addNew(QuestionDTO dto);

	public abstract void update(QuestionDTO dto);

	public abstract void deleteById(Long id);

	public abstract PagedResultDTO getQuestionProblems(Integer startingPage,
			int count);

	public abstract List<GroupedQuestionProblemDTO> getGroupedUnfixedQuestionProblems();

	public abstract List<QuestionProblemReportDTO> getUnfixedQuestionProblems(
			long id);

	public abstract QuestionProblemReportDTO getQuestionProblem(
			long questionProblemId);

	public abstract void deleteQuestionProblemById(Long id);

	public abstract void fixQuestionProblemById(Long id);

	public abstract DrivingCategoryDTO getDrivingCategory(Long drivingCategoryId);

	public abstract QuestionCategoryDTO getQuestionCategory(
			Long questionCategoryId);

	public abstract LessonDTO getLessonQuestions(Long userId, Long groupId, Integer lessonId);

	public abstract LessonDTO getRemainingLessonQuestions(Long userId, Long groupId, Integer lessonId, Boolean isTesting,
			Integer repeatCount);

	public abstract void toggleFavorite(Long userId, Long questionId);

	public abstract LessonDTO getFavoriteLessonQuestions(Long userId, Long groupId, Integer lessonId);

	public abstract QuestionMessageDTO saveQuestionMessage(Long userId, Long questionId, String messageText, Long parentMessageId);

	public abstract QuestionMessagesDTO getQuestionsMessages(Long questionId, Integer pageIndex);

	public abstract void deleteQuestionMessage(Long userId, Long questionMessageId);

	public abstract MyQuestionMessagesDTO getMyQuestionsMessages(Long userId, Integer startPage, String searchText);

	public abstract AllQuestionMessagesDTO getAllQuestionsMessages(Long userId, Integer startPage, String searchText);

	public abstract UserDiscussionPreviewDTO getUserQuestionsPreview(Long userId);

	public abstract QuestionDetailStatDTO getQuestionDetailsStat(Long questionId);

	public abstract Question getQuestionByQuestionMessage(Long questionMessageId);

	public abstract QuestionViewNavigationWrapper getPreviousAndNextQuestions(Long currentQuestionId, Long questionsCategoryId, Long drivingCategoryId, Integer startingIndex, int count);

	public abstract List<QuestionDTO> findQuestions(String q1, String q2, String q3, String a1, String a2, String a3);

}