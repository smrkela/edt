package com.evola.edt.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.ProblemCategory;
import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionCategory;
import com.evola.edt.model.QuestionMessage;
import com.evola.edt.model.QuestionProblemReport;
import com.evola.edt.model.User;
import com.evola.edt.model.UserQuestionFavorite;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.QuestionAnswerDTO;
import com.evola.edt.model.dto.QuestionCategoryDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.QuestionImageDTO;
import com.evola.edt.model.dto.QuestionProblemReportDTO;
import com.evola.edt.model.dto.UserDiscussionPreviewDTO;
import com.evola.edt.repository.DrivingCategoryRepository;
import com.evola.edt.repository.ProblemCategoryRepository;
import com.evola.edt.repository.QuestionCategoryRepository;
import com.evola.edt.repository.QuestionMessageRepository;
import com.evola.edt.repository.QuestionProblemReportRepository;
import com.evola.edt.repository.QuestionRepository;
import com.evola.edt.repository.UserQuestionFavoriteRepository;
import com.evola.edt.repository.UserQuestionStatLearnRepository;
import com.evola.edt.repository.UserQuestionStatTestRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.AllQuestionMessageDTO;
import com.evola.edt.service.dto.AllQuestionMessagesDTO;
import com.evola.edt.service.dto.AllQuestionsDTO;
import com.evola.edt.service.dto.DoneQuestionsDTO;
import com.evola.edt.service.dto.LearningQuestionsDTO;
import com.evola.edt.service.dto.LessonDTO;
import com.evola.edt.service.dto.MyQuestionMessageDTO;
import com.evola.edt.service.dto.MyQuestionMessagesDTO;
import com.evola.edt.service.dto.PagedResultDTO;
import com.evola.edt.service.dto.QuestionDetailStatDTO;
import com.evola.edt.service.dto.QuestionMessageDTO;
import com.evola.edt.service.dto.QuestionMessagesDTO;
import com.evola.edt.service.dto.QuestionStatDTO;
import com.evola.edt.service.dto.QuestionsStatDTO;
import com.evola.edt.service.dto.helpers.GroupedQuestionProblemDTO;
import com.evola.edt.service.dto.helpers.QuestionViewNavigationWrapper;
import com.evola.edt.service.dto.transformer.DrivingCategoryDTOTransformer;
import com.evola.edt.service.dto.transformer.QuestionCategoryDTOTransformer;
import com.evola.edt.service.dto.transformer.QuestionDTOTransformer;
import com.evola.edt.service.dto.transformer.QuestionProblemReportDTOTransformer;
import com.evola.edt.utils.EDTSettings;
import com.evola.edt.utils.EDTUtils;
import com.evola.edt.utils.URLUtils;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Named
public class QuestionServiceImpl implements QuestionService {

	private static final int QUESTION_MESSAGES_PAGE_SIZE = 20;

	@Inject
	private QuestionRepository questionRepository;

	@Inject
	QuestionCategoryRepository questionCategoryRepository;

	@Inject
	DrivingCategoryRepository drivingCategoryRepository;

	@Inject
	private QuestionDTOTransformer questionTransformer;

	@Inject
	private QuestionCategoryDTOTransformer questionCategoryTransformer;

	@Inject
	private DrivingCategoryDTOTransformer drivingCategoryTransformer;

	@Inject
	private ProblemCategoryRepository problemCategoryRepository;

	@Inject
	private QuestionProblemReportRepository questionProblemRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserQuestionStatLearnRepository userQuestionLearnRepository;

	@Inject
	private UserQuestionStatTestRepository userQuestionTestRepository;

	@Inject
	private UserQuestionFavoriteRepository userQuestionFavoriteRepository;

	@Inject
	private QuestionProblemReportDTOTransformer questionProblemReportTransformer;

	@Inject
	private UserCredentialsManager credentialsManager;

	@Inject
	private QuestionMessageRepository questionMessageRepository;

	@Inject
	private ActivityManager mActivity;

	@Transactional(readOnly = true)
	public AllQuestionsDTO getAllQuestions() {

		AllQuestionsDTO dto = new AllQuestionsDTO();

		// 13.3.2014, Sasa, ne ucitavamo vise sva pitanja, suvisna su
		// Iterable<Question> findAll = questionRepository.findAllFull();
		//
		// List<QuestionDTO> dtos = new LinkedList<QuestionDTO>();
		//
		// for (Question q : findAll) {
		//
		// dtos.add(questionTransformer.transformToDTO(q, "questionImages",
		// "questionAnswers", "questionCategories",
		// "drivingCategories"));
		// }
		//
		// dto.setQuestions(dtos);

		Iterable<QuestionCategory> findAll2 = questionCategoryRepository.findAll();

		List<QuestionCategoryDTO> qcDtos = new LinkedList<QuestionCategoryDTO>();

		for (QuestionCategory qc : findAll2) {

			qcDtos.add(questionCategoryTransformer.transformToDTO(qc));
		}

		dto.setQuestionCategories(qcDtos);

		Iterable<DrivingCategory> findAll3 = drivingCategoryRepository.findAll();

		List<DrivingCategoryDTO> dcDtos = new LinkedList<DrivingCategoryDTO>();

		for (DrivingCategory dc : findAll3) {

			dcDtos.add(drivingCategoryTransformer.transformToDTO(dc));
		}

		dto.setDrivingCategories(dcDtos);

		return dto;
	}

	@Transactional
	public void reportQuestionProblem(Long userId, Long questionId, String problemCategoryId, String userComment) {

		if (!Arrays.asList(ProblemCategory.CATEGORIES).contains(problemCategoryId)) {

			System.out.println("INVALID PROBLEM CATEGORY: " + problemCategoryId);
			return;
		}

		ProblemCategory problemCategory = problemCategoryRepository.findByProblemCategoryId(problemCategoryId);

		User user = userRepository.findOne(userId);
		Question question = questionRepository.findOne(questionId);

		QuestionProblemReport problem = new QuestionProblemReport();
		problem.setProblemCategory(problemCategory);
		problem.setQuestion(question);
		problem.setReportingDate(new Timestamp(System.currentTimeMillis()));
		problem.setUser(user);
		problem.setUserComment(userComment);

		questionProblemRepository.save(problem);
	}

	@Transactional
	public DoneQuestionsDTO getLearntQuestionsIds(Long userId, Long numOfLearnedQuestionsLimit) {

		List<Long> ids = new LinkedList<Long>();

		User user = userRepository.findOne(userId);

		Iterable<Long> learntQuestionsIds = userQuestionLearnRepository.getLearntQuestionsIds(user, numOfLearnedQuestionsLimit);

		if (learntQuestionsIds != null) {

			for (Long l : learntQuestionsIds) {

				ids.add(l);
			}
		}

		DoneQuestionsDTO dto = new DoneQuestionsDTO();
		dto.setLearntQuestionsIds(ids);

		return dto;
	}

	@Transactional
	public DoneQuestionsDTO getTestedAndLearntQuestionsIds(Long userId) {

		List<Long> learntIds = new LinkedList<Long>();

		User user = userRepository.findOne(userId);

		Iterable<Long> learntQuestionsIds = userQuestionLearnRepository.getLearntQuestionsIds(user, 1000000L);

		if (learntQuestionsIds != null) {

			for (Long l : learntQuestionsIds) {

				learntIds.add(l);
			}
		}

		List<Long> testedIds = new LinkedList<Long>();

		Iterable<Long> testedQuestionsIds = userQuestionTestRepository.getTestedQuestionsIds(user);

		if (testedQuestionsIds != null) {

			for (Long l : testedQuestionsIds) {

				testedIds.add(l);
			}
		}

		DoneQuestionsDTO dto = new DoneQuestionsDTO();
		dto.setLearntQuestionsIds(learntIds);
		dto.setTestedQuestionsIds(testedIds);

		return dto;
	}

	@Transactional(readOnly = true)
	public AllQuestionsDTO getDrivingCategories() {

		AllQuestionsDTO dto = new AllQuestionsDTO();

		Iterable<DrivingCategory> findAll3 = drivingCategoryRepository.findAll();

		List<DrivingCategoryDTO> dcDtos = new LinkedList<DrivingCategoryDTO>();

		for (DrivingCategory dc : findAll3) {

			dcDtos.add(drivingCategoryTransformer.transformToDTO(dc));
		}

		dto.setDrivingCategories(dcDtos);

		return dto;
	}

	@Transactional(readOnly = true)
	public AllQuestionsDTO getQuestionCategories() {

		AllQuestionsDTO dto = new AllQuestionsDTO();

		Iterable<QuestionCategory> findAll3 = questionCategoryRepository.findAll();

		List<QuestionCategoryDTO> dcDtos = new LinkedList<QuestionCategoryDTO>();

		for (QuestionCategory dc : findAll3) {

			dcDtos.add(questionCategoryTransformer.transformToDTO(dc));
		}

		dto.setQuestionCategories(dcDtos);

		return dto;
	}

	@Transactional(readOnly = true)
	public QuestionsStatDTO getAllQuestionsStats(Long userId) {

		QuestionsStatDTO dto = new QuestionsStatDTO();
		dto.setQuestionStats(new LinkedList<QuestionStatDTO>());

		User user = userRepository.findOne(userId);

		Iterable<Object[]> numOfQuestionLearns = userQuestionLearnRepository.getNumOfQuestionLearns(user);

		Map<Long, QuestionStatDTO> set = new HashMap<Long, QuestionStatDTO>();

		for (Object[] learnStat : numOfQuestionLearns) {

			Long questionId = (Long) learnStat[0];
			Long numOfLearns = (Long) learnStat[1];

			QuestionStatDTO qs = new QuestionStatDTO();
			qs.setQuestionId(questionId.toString());
			qs.setNumOfLearns(numOfLearns.intValue());
			qs.setNumOfTests(0);

			dto.getQuestionStats().add(qs);

			set.put(questionId, qs);
		}

		Iterable<Object[]> numOfQuestionTests = userQuestionTestRepository.getNumOfQuestionTests(user);

		for (Object[] learnStat : numOfQuestionTests) {

			Long questionId = (Long) learnStat[0];
			Long numOfTests = (Long) learnStat[1];

			QuestionStatDTO qs = set.get(questionId);

			if (qs == null) {

				qs = new QuestionStatDTO();
				qs.setQuestionId(questionId.toString());
				qs.setNumOfLearns(0);
				dto.getQuestionStats().add(qs);
			}

			qs.setNumOfTests(numOfTests.intValue());
		}

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public LearningQuestionsDTO getQuestions(Long questionsCategoryId, Long drivingCategoryId, Integer startingIndex, int count) {

		QuestionCategory qc = null;

		if (questionsCategoryId != 0)
			qc = questionCategoryRepository.findOne(questionsCategoryId);

		DrivingCategory dc = null;

		if (drivingCategoryId != 0)
			dc = drivingCategoryRepository.findOne(drivingCategoryId);

		Page<Question> findAll = questionRepository.findAll(qc, dc, new PageRequest(startingIndex, count));

		List<QuestionDTO> dtos = new LinkedList<QuestionDTO>();

		for (Question q : findAll) {

			QuestionDTO questionDTO = questionTransformer.transformToDTO(q, "questionImages", "questionAnswers", "questionCategories", "drivingCategories");

			questionDTO.setQuestionUrlTitle(URLUtils.createQuestionTitle(q.getText()));

			sortQuestionData(questionDTO);

			dtos.add(questionDTO);
		}

		LearningQuestionsDTO dto = new LearningQuestionsDTO();
		dto.setQuestions(dtos);
		dto.setIsFirstPage(findAll.isFirstPage());
		dto.setIsLastPage(findAll.isLastPage());
		dto.setTotalQuestions(findAll.getTotalElements());
		dto.setTotalPages(findAll.getTotalPages());

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public QuestionViewNavigationWrapper getPreviousAndNextQuestions(Long currentQuestionId, Long questionsCategoryId, Long drivingCategoryId, Integer startingIndex, int count) {

		Question previousQuestion = null;
		Question nextQuestion = null;

		Page<Question> currentPage = getQuestionsForPage(questionsCategoryId, drivingCategoryId, startingIndex, count);
		Page<Question> previousPage = null;
		Page<Question> nextPage = null;

		Question currentQuestion = questionRepository.findOne(currentQuestionId);

		int totalPages = currentPage.getTotalPages();

		// ovde je moguce da se prethodno ili sledece pitanje nalazi medju
		// trenutnim pitanjima ili u prethodnoj ili narednoj stranici

		int currentIndex = currentPage.getContent().indexOf(currentQuestion);

		// mozda nemamo uopste informaciju o broju stranice pa se u tom slucaju
		// pitanje ne mora nalaziti
		// ni u narednoj ni u prethodnoj stranici, moramo naci tacnu stranicu
		if (currentIndex == -1) {

			Long previousQuestions = getCountForPage(questionsCategoryId, drivingCategoryId, currentQuestionId);

			startingIndex = previousQuestions.intValue() / count;

			currentPage = getQuestionsForPage(questionsCategoryId, drivingCategoryId, startingIndex, count);
			currentIndex = currentPage.getContent().indexOf(currentQuestion);
		}

		if (currentIndex == -1) {

			if (startingIndex > 0) {

				Page<Question> newPage = getQuestionsForPage(questionsCategoryId, drivingCategoryId, startingIndex - 1, count);
				currentIndex = newPage.getContent().indexOf(currentQuestion);

				if (currentIndex > -1) {

					nextPage = currentPage;
					currentPage = newPage;
					startingIndex--;
				}
			} else {

				Page<Question> newPage = getQuestionsForPage(questionsCategoryId, drivingCategoryId, totalPages - 1, count);
				currentIndex = newPage.getContent().indexOf(currentQuestion);

				if (currentIndex > -1) {

					nextPage = currentPage;
					currentPage = newPage;
					startingIndex = totalPages - 1;
				}
			}
		}

		if (currentIndex == -1) {

			if (startingIndex < totalPages - 1) {

				Page<Question> newPage = getQuestionsForPage(questionsCategoryId, drivingCategoryId, startingIndex + 1, count);
				currentIndex = newPage.getContent().indexOf(currentQuestion);

				if (currentIndex > -1) {

					previousPage = currentPage;
					currentPage = newPage;
					startingIndex++;
				}
			} else {

				Page<Question> newPage = getQuestionsForPage(questionsCategoryId, drivingCategoryId, 0, count);
				currentIndex = newPage.getContent().indexOf(currentQuestion);

				if (currentIndex > -1) {

					previousPage = currentPage;
					currentPage = newPage;
					startingIndex = 0;
				}
			}
		}

		if (currentIndex == 0) {

			int localIndex = startingIndex;

			// prethodno pitanje je na prethodnoj stranici
			if (startingIndex == 0) {

				localIndex = totalPages - 1;
			} else {

				localIndex--;
			}

			if (previousPage == null)
				previousPage = getQuestionsForPage(questionsCategoryId, drivingCategoryId, localIndex, count);

			previousQuestion = previousPage.getContent().get(previousPage.getContent().size() - 1);
			nextQuestion = currentPage.getContent().get(currentIndex + 1);

		} else if (currentIndex == (currentPage.getContent().size() - 1)) {

			int localIndex = startingIndex;

			// naredno pitanje nam se nalazi u narednoj stranici
			if (startingIndex == totalPages - 1) {

				localIndex = 0;
			} else {

				localIndex++;
			}

			if (nextPage == null)
				nextPage = getQuestionsForPage(questionsCategoryId, drivingCategoryId, localIndex, count);

			previousQuestion = currentPage.getContent().get(currentIndex - 1);
			nextQuestion = nextPage.getContent().get(0);

		} else {

			if (currentIndex > 0) {
				// jednostavni slucaju kad su nam i prethodno i naredno pitanje
				// na
				// istoj stranici
				previousQuestion = currentPage.getContent().get(currentIndex - 1);
				nextQuestion = currentPage.getContent().get(currentIndex + 1);
			} else {

				// usli smo u pitanje koje nije validno za izabrane kategorije,
				// mozda je link rucno ukucan
			}
		}

		QuestionViewNavigationWrapper result = new QuestionViewNavigationWrapper();
		result.setPreviousQuestion(previousQuestion);
		result.setNextQuestion(nextQuestion);
		result.setCurrentPageIndex(startingIndex);
		result.setTotalPages(currentPage.getTotalPages());
		result.setTotalQuestions(currentPage.getTotalElements());

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public QuestionDTO getQuestion(Long id) {

		Question q = questionRepository.findOne(id);

		QuestionDTO questionDTO = questionTransformer.transformToDTO(q, "questionImages", "questionAnswers", "questionCategories", "drivingCategories");

		return questionDTO;
	}

	@Override
	@Transactional
	public void addNew(QuestionDTO dto) {

		credentialsManager.checkSystemAdministration();

		Question q = questionTransformer.transformToEntity(dto);

		questionRepository.save(q);
	}

	@Override
	@Transactional
	public void update(QuestionDTO dto) {

		credentialsManager.checkSystemAdministration();

		Question oldQuestion = questionRepository.findOne(dto.getId());

		oldQuestion.setHelpUrl(dto.getHelpUrl());
		oldQuestion.setNumber(dto.getNumber());
		oldQuestion.setPoints(dto.getPoints());
		oldQuestion.setQuestionId(dto.getQuestionId());
		oldQuestion.setRemark(dto.getRemark());
		oldQuestion.setText(dto.getText());

		questionRepository.save(oldQuestion);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {

		credentialsManager.checkSystemAdministration();

		Assert.notNull(id);

		questionRepository.delete(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResultDTO getQuestionProblems(Integer startingIndex, int count) {

		Page<QuestionProblemReport> findAll = questionProblemRepository.findAll(new PageRequest(startingIndex, count));

		List<QuestionProblemReportDTO> dtos = new LinkedList<QuestionProblemReportDTO>();

		for (QuestionProblemReport q : findAll) {

			dtos.add(questionProblemReportTransformer.transformToDTO(q, "question", "user", "problemCategory"));
		}

		PagedResultDTO dto = new PagedResultDTO();
		dto.setResults(dtos);
		dto.setIsFirstPage(findAll.isFirstPage());
		dto.setIsLastPage(findAll.isLastPage());

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GroupedQuestionProblemDTO> getGroupedUnfixedQuestionProblems() {

		List<GroupedQuestionProblemDTO> dtos = new LinkedList<GroupedQuestionProblemDTO>();

		List<Object[]> grouped = questionProblemRepository.findAllGroupedUnfixed();

		for (Object group : grouped) {

			Object[] arr = (Object[]) group;

			GroupedQuestionProblemDTO dto = new GroupedQuestionProblemDTO();
			dto.setQuestion((Question) arr[0]);
			dto.setCount((long) arr[1]);
			dto.setMaxDate((Date) arr[2]);

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuestionProblemReportDTO> getUnfixedQuestionProblems(long id) {

		List<QuestionProblemReport> allUnfixedForQuestion = questionProblemRepository.findAllUnfixedForQuestion(id);

		List<QuestionProblemReportDTO> dtos = new LinkedList<QuestionProblemReportDTO>();

		for (QuestionProblemReport q : allUnfixedForQuestion) {

			dtos.add(questionProblemReportTransformer.transformToDTO(q, "question", "user", "problemCategory"));
		}

		return dtos;
	}

	@Override
	@Transactional
	public QuestionProblemReportDTO getQuestionProblem(long questionProblemId) {

		QuestionProblemReport problemReport = questionProblemRepository.findOne(questionProblemId);

		QuestionProblemReportDTO dto = questionProblemReportTransformer.transformToDTO(problemReport, "question", "user", "problemCategory");

		return dto;
	}

	@Override
	@Transactional
	public void deleteQuestionProblemById(Long id) {

		credentialsManager.checkSystemAdministration();

		Assert.notNull(id);

		questionProblemRepository.delete(id);
	}

	@Override
	@Transactional
	public void fixQuestionProblemById(Long questionProblemId) {

		credentialsManager.checkSystemAdministration();

		QuestionProblemReport problemReport = questionProblemRepository.findOne(questionProblemId);

		problemReport.setFixDate(new Date());
		problemReport.setFixed(true);

		questionProblemRepository.save(problemReport);
	}

	@Override
	@Transactional
	public DrivingCategoryDTO getDrivingCategory(Long drivingCategoryId) {

		DrivingCategory findOne = drivingCategoryRepository.findOne(drivingCategoryId);

		if (findOne == null)
			return null;

		DrivingCategoryDTO dto = drivingCategoryTransformer.transformToDTO(findOne);

		return dto;
	}

	@Override
	@Transactional
	public QuestionCategoryDTO getQuestionCategory(Long questionCategoryId) {

		QuestionCategory one = questionCategoryRepository.findOne(questionCategoryId);

		if (one == null)
			return null;

		QuestionCategoryDTO dto = questionCategoryTransformer.transformToDTO(one);

		return dto;
	}

	@Transactional(readOnly = true)
	public LessonDTO getLessonQuestions(Long userId, Long questionCategoryId, Integer lessonId) {

		LessonDTO dto = new LessonDTO();

		User user = userRepository.findOne(userId);

		QuestionCategory questionCategory = questionCategoryRepository.findOne(questionCategoryId);
		DrivingCategory drivingCategory = user.getDrivingCategory();

		// sada imamo ucitana sva pitanja i koja nam trebaju i sve ucenja koja
		// nam trebaju da bismo formirali grupe

		List<QuestionDTO> dtos = getLessonQuestionDTOs(lessonId, questionCategory, drivingCategory, user);

		dto.setQuestions(dtos);
		dto.setGroupId(questionCategoryId);
		dto.setGroupName(questionCategory.getName());
		dto.setLessonId(lessonId);
		dto.setLessonName(getLessonName(questionCategory, lessonId));

		return dto;
	}

	@Transactional(readOnly = true)
	public LessonDTO getRemainingLessonQuestions(Long userId, Long questionCategoryId, Integer lessonId, Boolean isTesting, Integer repeatCount) {

		LessonDTO dto = new LessonDTO();

		User user = userRepository.findOne(userId);

		QuestionCategory questionCategory = questionCategoryRepository.findOne(questionCategoryId);
		DrivingCategory drivingCategory = user.getDrivingCategory();

		List<QuestionDTO> dtos = getLessonQuestionDTOs(lessonId, questionCategory, drivingCategory, user);

		// sada treba da izbacimo pitanja u skladu sa
		// repeatCount i isTesting atributima

		// za svako pitanje gledamo koliko puta je nauceno ili testirano

		int removedQuestionsCount = 0;

		for (int i = dtos.size() - 1; i >= 0; i--) {

			QuestionDTO questionDTO = dtos.get(i);

			long questionRepeat = 0;

			if (isTesting)
				questionRepeat = questionDTO.getTestCorrectCount();
			else
				questionRepeat = questionDTO.getLearnCount();

			// sada znamo koliko puta je pitanje uceno ili tacno provereno a
			// uzimamo samo ona pitanja koja su ucena ili proverena
			// manje od zeljenog broja

			if (questionRepeat < repeatCount) {
				// OK
			} else {

				removedQuestionsCount++;

				dtos.remove(i);
			}
		}

		dto.setQuestions(dtos);
		dto.setGroupId(questionCategoryId);
		dto.setGroupName(questionCategory.getName());
		dto.setLessonId(lessonId);
		dto.setLessonName(getLessonName(questionCategory, lessonId));
		dto.setRemovedQuestionsCount(removedQuestionsCount);

		return dto;
	}

	@Transactional(readOnly = true)
	public LessonDTO getFavoriteLessonQuestions(Long userId, Long questionCategoryId, Integer lessonId) {

		LessonDTO dto = new LessonDTO();

		User user = userRepository.findOne(userId);

		QuestionCategory questionCategory = questionCategoryRepository.findOne(questionCategoryId);
		DrivingCategory drivingCategory = user.getDrivingCategory();

		List<QuestionDTO> dtos = getLessonQuestionDTOs(lessonId, questionCategory, drivingCategory, user);

		// sada treba da izbacimo pitanja koja nisu favorite

		int removedQuestionsCount = 0;

		for (int i = dtos.size() - 1; i >= 0; i--) {

			QuestionDTO questionDTO = dtos.get(i);

			boolean isFavorite = userQuestionFavoriteRepository.getForQuestionAndUser(questionDTO.getId(), userId) != null;

			if (isFavorite) {
				// OK
			} else {

				removedQuestionsCount++;

				dtos.remove(i);
			}
		}

		dto.setQuestions(dtos);
		dto.setGroupId(questionCategoryId);
		dto.setGroupName(questionCategory.getName());
		dto.setLessonId(lessonId);
		dto.setLessonName(getLessonName(questionCategory, lessonId));
		dto.setRemovedQuestionsCount(removedQuestionsCount);

		return dto;
	}

	@Transactional
	public void toggleFavorite(Long userId, Long questionId) {

		// ako nam je pitanje favorite onda ga izbacujemo kao favorite-a a ako
		// nije onda ga ubacujemo

		UserQuestionFavorite questionFavorite = userQuestionFavoriteRepository.getForQuestionAndUser(questionId, userId);

		if (questionFavorite == null) {

			Question question = questionRepository.findOne(questionId);
			User user = userRepository.findOne(userId);

			questionFavorite = new UserQuestionFavorite();
			questionFavorite.setQuestion(question);
			questionFavorite.setUser(user);
			questionFavorite.setTime(new Date());

			userQuestionFavoriteRepository.save(questionFavorite);
		} else {

			userQuestionFavoriteRepository.delete(questionFavorite);
		}

	}

	@Transactional
	public QuestionMessageDTO saveQuestionMessage(Long userId, Long questionId, String messageText, Long parentMessageId) {

		credentialsManager.checkUserLoggedIn();

		Assert.notNull(userId);
		Assert.notNull(questionId);
		Assert.hasLength(messageText);

		// parent message ne mora da bude definisano

		QuestionMessage parent = null;

		if (parentMessageId != null)
			parent = questionMessageRepository.findOne(parentMessageId);

		User user = userRepository.findOne(userId);
		Question question = questionRepository.findOne(questionId);

		QuestionMessage message = new QuestionMessage();
		message.setQuestion(question);
		message.setReportingDate(new Timestamp(System.currentTimeMillis()));
		message.setUser(user);
		message.setMessage(messageText);
		message.setParentMessage(parent);

		message = questionMessageRepository.save(message);

		mActivity.discussedQuestion(message);

		QuestionMessageDTO messageDTO = convertToQuestionMessageDTO(message);

		return messageDTO;
	}

	@Transactional
	public QuestionMessagesDTO getQuestionsMessages(Long questionId, Integer pageIndex) {

		Assert.notNull(questionId);

		if (pageIndex == null)
			pageIndex = 0;

		Page<QuestionMessage> allForQuestion = questionMessageRepository.findAllForQuestion(questionId, new PageRequest(pageIndex, QUESTION_MESSAGES_PAGE_SIZE));

		QuestionMessagesDTO dto = new QuestionMessagesDTO();
		dto.setMessages(new LinkedList<QuestionMessageDTO>());
		dto.setIsAdministrator(credentialsManager.canAdministerSystem());
		dto.setCurrentPageIndex(pageIndex);
		dto.setTotalPages(allForQuestion.getTotalPages());
		dto.setPageSize(QUESTION_MESSAGES_PAGE_SIZE);
		dto.setTotalMessages((int) allForQuestion.getTotalElements());

		for (QuestionMessage message : allForQuestion.getContent()) {

			QuestionMessageDTO d = convertToQuestionMessageDTO(message);

			dto.getMessages().add(d);
		}

		// ubacujemo i podatke o pitanju kako bi ga prikazali u diskusiji
		Question q = questionRepository.findOne(questionId);

		QuestionDTO questionDTO = questionTransformer.transformToDTO(q, "questionImages", "questionAnswers", "questionCategories", "drivingCategories");

		sortQuestionData(questionDTO);

		dto.setQuestion(questionDTO);

		return dto;
	}

	@Transactional
	public void deleteQuestionMessage(Long userId, Long questionMessageId) {

		credentialsManager.checkUserLoggedIn();

		Assert.notNull(questionMessageId);

		QuestionMessage questionMessage = questionMessageRepository.findOne(questionMessageId);

		// dozvoljavamo brisanje ako je korisnik administrator ili ako je on
		// autor poruke

		boolean canDelete = false;

		if (credentialsManager.canAdministerSystem() || questionMessage.getUser().getId().equals(userId))
			canDelete = true;

		if (!canDelete)
			throw new RuntimeException("Nemate pravo da obrišete ovu poruku.");

		// pre brisanja moramo obraditi sve poruke kojima je ova poruka parent
		List<QuestionMessage> childMessages = questionMessage.getChildMessages();

		if (childMessages != null) {

			for (QuestionMessage childMessage : childMessages) {

				childMessage.setParentMessage(null);
			}
		}

		questionMessageRepository.delete(questionMessage);
	}

	@Override
	@Transactional
	public MyQuestionMessagesDTO getMyQuestionsMessages(Long userId, Integer startPageIndex, String searchText) {

		credentialsManager.checkUserLoggedIn();

		if (startPageIndex == null)
			startPageIndex = 0;

		if (StringUtils.isEmpty(searchText))
			searchText = null;
		else
			searchText = "%" + searchText + "%";

		// treba da pokupimo iz baze sve poruke korisnika grupisane po pitanjima
		Page<Question> questions = questionMessageRepository.findUserQuestions(userId, searchText, new PageRequest(startPageIndex, QUESTION_MESSAGES_PAGE_SIZE));

		MyQuestionMessagesDTO result = new MyQuestionMessagesDTO();
		result.setMessages(new LinkedList<MyQuestionMessageDTO>());
		result.setPage(startPageIndex);
		result.setTotalPages(questions.getTotalPages());
		result.setPageSize(QUESTION_MESSAGES_PAGE_SIZE);
		result.setTotalMessages((int) questions.getTotalElements());

		for (Question q : questions.getContent()) {

			// za svako pitanje formiramo dto

			MyQuestionMessageDTO dto = new MyQuestionMessageDTO();

			Page<QuestionMessage> page = questionMessageRepository.findLastUserMessage(userId, q.getId(), new PageRequest(0, 1));
			QuestionMessage message = page.getContent().size() > 0 ? page.getContent().get(0) : null;

			Page<QuestionMessage> page2 = questionMessageRepository.findLastMessage(q.getId(), new PageRequest(0, 1));
			QuestionMessage lastMessage = page2.getContent().size() > 0 ? page2.getContent().get(0) : null;

			Long messageCount = questionMessageRepository.countForQuestion(q.getId());

			dto.setMessageDate(message.getReportingDate());
			dto.setMessageText(message.getMessage());
			dto.setQuestionId(q.getId());
			dto.setQuestionText(q.getText());

			QuestionCategory questionCategory = getQuestionCategory(q);

			if (questionCategory != null) {
				dto.setCategoryId(questionCategory.getCategoryId());
				dto.setCategoryName(questionCategory.getName());
			}

			dto.setMessageCount(messageCount.intValue());

			if (!lastMessage.equals(message)) {

				dto.setAnswerDate(lastMessage.getReportingDate());
				dto.setAnswerText(lastMessage.getMessage());
				dto.setAnswerUserName(lastMessage.getUser().getUsername());
				dto.setAnswerUserId(lastMessage.getUser().getId());
				dto.setAnswerUserLevel(EDTUtils.calculateUserLevel(lastMessage.getUser()));
			}

			result.getMessages().add(dto);
		}

		return result;
	}

	@Override
	@Transactional
	public Question getQuestionByQuestionMessage(Long questionMessageId) {

		Assert.notNull(questionMessageId);

		QuestionMessage message = questionMessageRepository.findOne(questionMessageId);

		Question question = message.getQuestion();

		Hibernate.initialize(question);

		return question;
	}

	private QuestionCategory getQuestionCategory(Question q) {

		QuestionCategory id = null;

		if (q.getQuestionCategories() != null && q.getQuestionCategories().size() > 0) {
			id = q.getQuestionCategories().iterator().next();
		}

		return id;
	}

	@Override
	@Transactional
	public AllQuestionMessagesDTO getAllQuestionsMessages(Long userId, Integer startPageIndex, String searchText) {

		credentialsManager.checkUserLoggedIn();

		if (startPageIndex == null)
			startPageIndex = 0;

		if (StringUtils.isEmpty(searchText))
			searchText = null;
		else
			searchText = "%" + searchText + "%";

		// treba da pokupimo iz baze sve poruke korisnika grupisane po pitanjima
		Page<Question> questions = questionMessageRepository.findQuestions(searchText, new PageRequest(startPageIndex, QUESTION_MESSAGES_PAGE_SIZE));

		AllQuestionMessagesDTO result = new AllQuestionMessagesDTO();
		result.setMessages(new LinkedList<AllQuestionMessageDTO>());
		result.setPage(startPageIndex);
		result.setTotalPages(questions.getTotalPages());
		result.setPageSize(QUESTION_MESSAGES_PAGE_SIZE);
		result.setTotalMessages((int) questions.getTotalElements());

		for (Question q : questions.getContent()) {

			// za svako pitanje formiramo dto

			AllQuestionMessageDTO dto = new AllQuestionMessageDTO();

			Page<QuestionMessage> page = questionMessageRepository.findLastMessage(q.getId(), new PageRequest(0, 1));
			QuestionMessage message = page.getContent().size() > 0 ? page.getContent().get(0) : null;

			Long messageCount = questionMessageRepository.countForQuestion(q.getId());

			dto.setMessageDate(message.getReportingDate());
			dto.setMessageText(message.getMessage());
			dto.setQuestionId(q.getId());
			dto.setQuestionText(q.getText());
			dto.setMessageUserName(message.getUser().getUsername());
			dto.setMessageUserId(message.getUser().getId());

			QuestionCategory questionCategory = getQuestionCategory(q);

			if (questionCategory != null) {
				dto.setCategoryId(questionCategory.getCategoryId());
				dto.setCategoryName(questionCategory.getName());
			}

			dto.setMessageCount(messageCount.intValue());
			dto.setMessageUserLevel(EDTUtils.calculateUserLevel(message.getUser()));

			result.getMessages().add(dto);
		}

		return result;
	}

	private QuestionMessageDTO convertToQuestionMessageDTO(QuestionMessage message) {

		QuestionMessageDTO d = new QuestionMessageDTO();
		d.setId(message.getId());
		d.setMessageText(message.getMessage());
		d.setReportDate(message.getReportingDate());
		d.setUserId(message.getUser().getId());
		d.setUserName(message.getUser().getUsername());
		d.setUserLevel(EDTUtils.calculateUserLevel(message.getUser()));

		// mozda imamo i parent informacije
		if (message.getParentMessage() != null) {

			d.setParentMessageId(message.getParentMessage().getId());
			d.setParentMessageText(message.getParentMessage().getMessage());
			d.setParentMessageUserId(message.getParentMessage().getUser().getId());
			d.setParentMessageUserIsMale(message.getParentMessage().getUser().getIsMale());
			d.setParentMessageUserName(message.getParentMessage().getUser().getUsername());
			d.setParentMessageDate(message.getParentMessage().getReportingDate());
		}

		boolean canDelete = false;

		if (credentialsManager.isUserLoggedIn()) {

			if (credentialsManager.canAdministerSystem())
				canDelete = true;
			else if (message.getUser().equals(SecurityUtils.getUserId()))
				canDelete = true;
		}

		d.setCanDelete(canDelete);

		return d;
	}

	private List<QuestionDTO> getLessonQuestionDTOs(Integer lessonId, QuestionCategory questionCategory, DrivingCategory drivingCategory, User user) {
		List<Question> questions = questionRepository.findAll(questionCategory, drivingCategory);

		// index lekcije krece od jedan
		int lessonIndexFrom = (lessonId - 1) * EDTSettings.LESSON_SIZE;
		int lessonIndexTo = lessonIndexFrom + EDTSettings.LESSON_SIZE;

		List<QuestionDTO> dtos = new LinkedList<QuestionDTO>();

		// sada imamo ucitana sva pitanja i koja nam trebaju i sve ucenja koja
		// nam trebaju da bismo formirali grupe

		for (int i = 0; i < questions.size(); i++) {

			// jos nismo dosli do nase lekcije
			if (i < lessonIndexFrom)
				continue;

			// prosli smo nasu lekciju
			if (i >= lessonIndexTo)
				break;

			// gledamo da li smo na pitanjima za tekucu lekciju a jesmo ako nam
			// je index pitanja izmedju indexa
			// koji cine lekciju

			// prvo vadimo podatke za trenutno pitanje
			Question q = questions.get(i);

			// ako smo presli limit broja pitanja po lekciji, prelazimo na
			// sledecu lekciju

			QuestionDTO questionDTO = questionTransformer.transformToDTO(q, "questionImages", "questionAnswers", "questionCategories", "drivingCategories");

			// ubacujemo i broj ucenja i testiranja

			long learns = userQuestionLearnRepository.countForQuestionAndUser(q.getId(), user.getId());
			long corrects = userQuestionTestRepository.countCorrectForQuestionAndUser(q.getId(), user.getId());
			long incorrects = userQuestionTestRepository.countIncorrectForQuestionAndUser(q.getId(), user.getId());

			// treba da uradimo sortiranje slika i odgovora pitanja
			sortQuestionData(questionDTO);

			questionDTO.setLearnCount((int) learns);
			questionDTO.setTestCorrectCount((int) corrects);
			questionDTO.setTestIncorrectCount((int) incorrects);

			boolean isFavorite = userQuestionFavoriteRepository.getForQuestionAndUser(q.getId(), user.getId()) != null;

			questionDTO.setFavorite(isFavorite);

			long numberOfMessages = questionMessageRepository.countForQuestion(q.getId());

			questionDTO.setNumberOfMessages((int) numberOfMessages);

			dtos.add(questionDTO);
		}
		return dtos;
	}

	/**
	 * Metoda sortira odgovore i slike prema order index-u.
	 * 
	 * @param questionDTO
	 */
	private void sortQuestionData(QuestionDTO questionDTO) {

		if (questionDTO == null)
			return;

		List<QuestionAnswerDTO> questionAnswers = questionDTO.getQuestionAnswers();

		if (questionAnswers != null) {

			Collections.sort(questionAnswers, new Comparator<QuestionAnswerDTO>() {

				@Override
				public int compare(QuestionAnswerDTO o1, QuestionAnswerDTO o2) {

					Integer orderIndex1 = o1.getOrderIndex();
					Integer orderIndex2 = o2.getOrderIndex();

					// NAPOMENA: reverse order ne mnozi sa minus -1 a bez
					// reverse treba obrnuti defaultni rezultat
					// tako da nije greska, flex i java zahtevaju razlicitno
					// sortiranje

					return ObjectUtils.compare(orderIndex1, orderIndex2);
				}
			});
		}

		// sada dodeljujemo slova odgovorima nakon sto su sortirani
		String[] letters = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i" };

		int index = 0;

		for (QuestionAnswerDTO questionAnswerDTO : questionAnswers) {

			questionAnswerDTO.setLetter(letters[index++] + ")");
		}

		List<QuestionImageDTO> questionImages = questionDTO.getQuestionImages();

		if (questionImages != null) {

			Collections.sort(questionImages, new Comparator<QuestionImageDTO>() {

				@Override
				public int compare(QuestionImageDTO o1, QuestionImageDTO o2) {

					Integer orderIndex1 = o1.getOrderIndex();
					Integer orderIndex2 = o2.getOrderIndex();

					return ObjectUtils.compare(orderIndex1, orderIndex2);
				}
			});
		}
	}

	private String getLessonName(QuestionCategory questionCategory, int lessonId) {

		String title = questionCategory.getName().substring(0, 4).toUpperCase() + "-" + lessonId;

		return title;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDiscussionPreviewDTO getUserQuestionsPreview(Long userId) {

		User user = userRepository.findOne(userId);

		UserDiscussionPreviewDTO dto = new UserDiscussionPreviewDTO();

		Long numOfUserMessages = questionMessageRepository.countForUser(user);
		Long numOfMessages = questionMessageRepository.count();

		dto.setNumOfUserMessages(numOfUserMessages != null ? numOfUserMessages.intValue() : null);
		dto.setNumOfMessages(numOfMessages != null ? numOfMessages.intValue() : null);

		List<QuestionMessage> userMessages = questionMessageRepository.findLastMessagesByUser(userId, new PageRequest(0, 1)).getContent();
		List<QuestionMessage> anyMessages = questionMessageRepository.findLastMessagesByAnyone(new PageRequest(0, 1)).getContent();

		if (userMessages != null && userMessages.size() > 0) {

			QuestionMessage questionMessage = userMessages.get(0);

			dto.setLastUserMessageDate(questionMessage.getReportingDate());
			dto.setLastUserMessageText(questionMessage.getMessage());
		}

		if (anyMessages != null && anyMessages.size() > 0) {

			QuestionMessage questionMessage = anyMessages.get(0);

			dto.setLastMessageDate(questionMessage.getReportingDate());
			dto.setLastMessageText(questionMessage.getMessage());
			dto.setLastMessageUserId(questionMessage.getUser().getId());
			dto.setLastMessageUserName(questionMessage.getUser().getUsername());
		}

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public QuestionDetailStatDTO getQuestionDetailsStat(Long questionId) {

		QuestionDetailStatDTO dto = new QuestionDetailStatDTO();

		Long learnCount = userQuestionLearnRepository.countForQuestion(questionId);
		Long correctCount = userQuestionTestRepository.countCorrectForQuestion(questionId);
		Long incorrectCount = userQuestionTestRepository.countIncorrectForQuestion(questionId);
		Long learningUsers = userQuestionLearnRepository.countUsers(questionId);
		Long testingUsers = userQuestionTestRepository.countUsers(questionId);

		double incorrectD = incorrectCount * 100.0 / (incorrectCount + correctCount);

		Long difficulty = Math.round(incorrectD);

		dto.setNumberOfLearns(learnCount.intValue());
		dto.setNumberOfCorrectTests(correctCount.intValue());
		dto.setNumberOfIncorrectTests(incorrectCount.intValue());
		dto.setNumberOfTests(correctCount.intValue() + incorrectCount.intValue());
		dto.setNumberOfTestingUsers(testingUsers.intValue());
		dto.setNumberOfLearningUsers(learningUsers.intValue());
		dto.setDifficulty(difficulty.intValue());

		return dto;
	}

	private Page<Question> getQuestionsForPage(Long questionsCategoryId, Long drivingCategoryId, Integer startingIndex, int count) {

		QuestionCategory qc = null;

		if (questionsCategoryId != 0)
			qc = questionCategoryRepository.findOne(questionsCategoryId);

		DrivingCategory dc = null;

		if (drivingCategoryId != 0)
			dc = drivingCategoryRepository.findOne(drivingCategoryId);

		Page<Question> findAll = questionRepository.findAll(qc, dc, new PageRequest(startingIndex, count));

		return findAll;
	}

	private Long getCountForPage(Long questionsCategoryId, Long drivingCategoryId, Long questionId) {

		QuestionCategory qc = null;

		if (questionsCategoryId != 0)
			qc = questionCategoryRepository.findOne(questionsCategoryId);

		DrivingCategory dc = null;

		if (drivingCategoryId != 0)
			dc = drivingCategoryRepository.findOne(drivingCategoryId);

		Long count = questionRepository.findCountBeforeQuestion(qc, dc, questionId);

		return count;
	}

	@Override
	@Transactional
	public List<QuestionDTO> findQuestions(String q1, String q2, String q3, String a1, String a2, String a3) {

		boolean hasSearch = false;

		hasSearch = hasSearch || StringUtils.isNotBlank(q1);
		hasSearch = hasSearch || StringUtils.isNotBlank(q2);
		hasSearch = hasSearch || StringUtils.isNotBlank(q3);
		hasSearch = hasSearch || StringUtils.isNotBlank(a1);
		hasSearch = hasSearch || StringUtils.isNotBlank(a2);
		hasSearch = hasSearch || StringUtils.isNotBlank(a3);

		if (hasSearch == false)
			return null;

		String questionTextQuery = "%";

		if (StringUtils.isNotBlank(q1))
			questionTextQuery += q1 + "%";

		if (StringUtils.isNotBlank(q2))
			questionTextQuery += q2 + "%";

		if (StringUtils.isNotBlank(q3))
			questionTextQuery += q3 + "%";

		String answerTextQuery = "%";

		if (StringUtils.isNotBlank(a1))
			answerTextQuery += a1 + "%";

		if (StringUtils.isNotBlank(a2))
			answerTextQuery += a2 + "%";

		if (StringUtils.isNotBlank(a3))
			answerTextQuery += a3 + "%";

		List<Question> questions = null;
		
		if(questionTextQuery.length() > 1){
			
			questions = questionRepository.findQuestionsByText(questionTextQuery);
		}
		else{
			
			questions = questionRepository.findQuestionsByAnswerText(answerTextQuery);
		}
		
		List<QuestionDTO> dtos = new LinkedList<QuestionDTO>();
		
		for(Question q:questions){
			
			QuestionDTO dto = questionTransformer.transformToDTO(q);
			
			dtos.add(dto);
		}
		
		return dtos;
	}
}
