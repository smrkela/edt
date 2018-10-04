package com.evola.edt.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.transaction.annotation.Transactional;

import com.evola.edt.component.UserPointsManager;
import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.LearningSession;
import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionCategory;
import com.evola.edt.model.TestingSession;
import com.evola.edt.model.User;
import com.evola.edt.model.UserQuestionStatLearn;
import com.evola.edt.model.UserQuestionStatTest;
import com.evola.edt.model.dto.LearningSessionDTO;
import com.evola.edt.model.dto.TestingSessionDTO;
import com.evola.edt.repository.DrivingCategoryRepository;
import com.evola.edt.repository.LearningSessionRepository;
import com.evola.edt.repository.QuestionCategoryRepository;
import com.evola.edt.repository.QuestionRepository;
import com.evola.edt.repository.TestingSessionRepository;
import com.evola.edt.repository.UserQuestionFavoriteRepository;
import com.evola.edt.repository.UserQuestionStatLearnRepository;
import com.evola.edt.repository.UserQuestionStatTestRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.LeaderboardDTO;
import com.evola.edt.service.dto.LeaderboardUserDTO;
import com.evola.edt.service.dto.LearningGroupDTO;
import com.evola.edt.service.dto.LearningGroupsDTO;
import com.evola.edt.service.dto.LearningLessonDTO;
import com.evola.edt.service.dto.LearningLessonsDTO;
import com.evola.edt.service.dto.StatisticsDTO;
import com.evola.edt.service.dto.UserExperienceDTO;
import com.evola.edt.service.dto.helpers.ActiveUserDTO;
import com.evola.edt.service.dto.helpers.QuestionCategoryStatDTO;
import com.evola.edt.service.dto.transformer.LearningSessionDTOTransformer;
import com.evola.edt.service.dto.transformer.QuestionCategoryDTOTransformer;
import com.evola.edt.service.dto.transformer.TestingSessionDTOTransformer;
import com.evola.edt.utils.EDTSettings;
import com.evola.edt.utils.EDTUtils;

@Named
public class StatisticsServiceImpl implements StatisticsService {

	@Inject
	private UserQuestionStatLearnRepository userQuestionLearnRepository;

	@Inject
	private UserQuestionStatTestRepository userQuestionTestRepository;

	@Inject
	private QuestionRepository questionRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private QuestionCategoryRepository questionCategoryRepository;

	@Inject
	private DrivingCategoryRepository drivingCategoryRepository;

	@Inject
	private QuestionCategoryDTOTransformer questionCategoryTransformer;

	@Inject
	LearningSessionRepository learningSessionRepository;

	@Inject
	TestingSessionRepository testingSessionRepository;

	@Inject
	LearningSessionDTOTransformer learningSessionTransformer;

	@Inject
	TestingSessionDTOTransformer testingSessionTransformer;

	@Inject
	UserPointsManager pointsManager;

	@Inject
	UserQuestionFavoriteRepository userQuestionFavoriteRepository;
	
	@Inject
	private ActivityManager mActivity;

	@Transactional()
	public StatisticsDTO saveLearning(Long questionId, Long userId, String sessionUid) {

		StatisticsDTO dto = new StatisticsDTO();

		User user = userRepository.findOne(userId);

		LearningSession session = null;

		// trazimo sesiju ucenja, ako ne postoji sada je pravimo
		session = learningSessionRepository.findByUid(sessionUid);

		if (session == null) {

			session = new LearningSession();
			session.setStart(new Timestamp(System.currentTimeMillis()));
			session.setUid(sessionUid);
			session.setUser(user);

			session = learningSessionRepository.save(session);
			
			mActivity.learned(session);
		}

		UserQuestionStatLearn learn = new UserQuestionStatLearn();
		learn.setQuestion(questionRepository.findOne(questionId));
		learn.setUser(user);
		learn.setSession(session);
		learn.setTime(new Date());

		// osvezavamo broj naucenih pitanja i poena
		pointsManager.questionLearned(user);

		userQuestionLearnRepository.save(learn);

		return dto;
	}

	@Transactional()
	public StatisticsDTO saveTesting(Long questionId, Long userId, Boolean isCorrect, Boolean isUpdate, String sessionUid) {

		StatisticsDTO dto = new StatisticsDTO();

		User user = userRepository.findOne(userId);

		if (isUpdate) {

			Iterable<UserQuestionStatTest> test = userQuestionTestRepository.findLastForQuestion(questionId, userId);

			for (UserQuestionStatTest t : test) {

				// osvezavamo broj naucenih pitanja i poena
				pointsManager.questionTestedAndUpdated(user, isCorrect, t.getCorrect());

				t.setTime(new Date());
				t.setCorrect(isCorrect);

				userQuestionTestRepository.save(test);

				break;
			}

		} else {

			TestingSession session = null;

			// trazimo sesiju ucenja, ako ne postoji sada je pravimo
			session = testingSessionRepository.findByUid(sessionUid);

			if (session == null) {

				session = new TestingSession();
				session.setStart(new Timestamp(System.currentTimeMillis()));
				session.setUid(sessionUid);
				session.setUser(user);

				session = testingSessionRepository.save(session);
				
				mActivity.tested(session);
			}

			UserQuestionStatTest test = new UserQuestionStatTest();
			test.setQuestion(questionRepository.findOne(questionId));
			test.setUser(userRepository.findOne(userId));
			test.setTime(new Date());
			test.setCorrect(isCorrect);
			test.setSession(session);

			// osvezavamo broj naucenih pitanja i poena
			pointsManager.questionTested(user, isCorrect);

			userQuestionTestRepository.save(test);
		}

		return dto;
	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadProgress(Long userId) {

		User user = userRepository.findOne(userId);

		StatisticsDTO dto = populateProgressForUser(user);

		return dto;
	}
	
	@Transactional(readOnly = true)
	@Override
	public LearningGroupsDTO loadPlainGroups(Long userId) {

		//User user = userRepository.findOne(userId);

		LearningGroupsDTO dto = new LearningGroupsDTO();

		// za svaku grupu pitanja treba da vidimo koliko posto pitanja je uceno
		// jednom, dva puta, tri i cetiri
		// istu statistiku vadimo i za testiranje s tim sto racunamo samo
		// odgovore koji su tacni

		Iterable<QuestionCategory> findAll = questionCategoryRepository.findAll();

		List<LearningGroupDTO> groups = new LinkedList<LearningGroupDTO>();

		// prolazimo kroz svaku kategoriju i gledamo koliko ukupno ima pitanja
		// za usera a koliko je ucio

		for (QuestionCategory qc : findAll) {

			LearningGroupDTO group = new LearningGroupDTO();
			group.setId(qc.getId());
			group.setTitle(qc.getName());

			groups.add(group);
		}

		dto.setGroups(groups);

		return dto;
	}

	@Transactional(readOnly = true)
	public LearningGroupsDTO loadGroups(Long userId) {

		User user = userRepository.findOne(userId);

		LearningGroupsDTO dto = populateGroupsForUser(user);

		return dto;
	}
	
	@Transactional(readOnly = true)
	public LearningGroupsDTO loadGuestGroups(Long drivingCategoryId) {

		LearningGroupsDTO dto = new LearningGroupsDTO();

		Iterable<QuestionCategory> findAll = questionCategoryRepository.findAll();
		DrivingCategory drivingCategory = drivingCategoryRepository.findOne(drivingCategoryId);

		List<LearningGroupDTO> groups = new LinkedList<LearningGroupDTO>();

		for (QuestionCategory qc : findAll) {

			Long questionCountLong = questionRepository.countForQuestionAndDrivingCategory(qc, drivingCategory);

			int questionCount = questionCountLong != null ? questionCountLong.intValue() : 0;

			LearningGroupDTO group = new LearningGroupDTO();
			group.setId(qc.getId());
			group.setTitle(qc.getName());
			group.setNumberOfQuestions(questionCount);

			groups.add(group);
		}

		dto.setGroups(groups);

		return dto;
	}

	@Transactional(readOnly = true)
	public LearningLessonsDTO loadLessons(Long userId, Long categoryId) {

		User user = userRepository.findOne(userId);

		LearningLessonsDTO dto = populateLessonsForUser(user, categoryId);

		return dto;
	}

	@Transactional(readOnly = true)
	public LearningLessonsDTO loadGuestLessons(Long drivingCategoryId, Long categoryId) {

		LearningLessonsDTO dto = new LearningLessonsDTO();

		QuestionCategory questionCategory = questionCategoryRepository.findOne(categoryId);
		DrivingCategory drivingCategory = drivingCategoryRepository.findOne(drivingCategoryId);

		dto.setCategoryId(categoryId);
		dto.setCategoryName(questionCategory.getName());

		// ucitavamo sva pitanja iz za izabranu grupu i kategoriju
		List<Question> questions = questionRepository.findAll(questionCategory, drivingCategory);

		List<LearningLessonDTO> lessons = new LinkedList<LearningLessonDTO>();

		int currentLessonSize = 0; // brojimo broj pitanja po lekciji

		LearningLessonDTO currentLesson = null;

		for (int i = 0; i < questions.size(); i++) {

			// prvo vadimo podatke za trenutno pitanje
			Question q = questions.get(i);

			if (currentLessonSize == 0) {

				currentLesson = new LearningLessonDTO();
				currentLesson.setId(lessons.size() + 1);
				currentLesson.setTitle(getLessonName(questionCategory, lessons.size() + 1));
				lessons.add(currentLesson);
			}

			currentLesson.setNumberOfQuestions(currentLesson.getNumberOfQuestions() + 1);

			currentLessonSize++;

			// ako prelazimo broj limit, idemo ispocetka
			if (currentLessonSize >= EDTSettings.LESSON_SIZE)
				currentLessonSize = 0;
		}

		dto.setSessions(lessons);

		return dto;
	}
	
	@Transactional(readOnly = true)
	public UserExperienceDTO loadUserExperience(Long userId) {

		User user = userRepository.findOne(userId);

		// gledamo koliko imamo naucenih i uspesno istestiranih pitanja
		Integer learnsL = user.getLearnedQuestions();
		Integer testsL = user.getTestedQuestions();

		int learns = learnsL != null ? learnsL.intValue() : 0;
		int tests = testsL != null ? testsL.intValue() : 0;

		int experiencePoints = learns + tests;
		int level = EDTUtils.calculateUserLevel(user);
		
		int pointsToNextLevel = level * EDTSettings.POINTS_PER_LEVEL;

		int nextLevelExperiencePoints = EDTSettings.POINTS_PER_LEVEL;
		int currentLevelExperiencePoints = nextLevelExperiencePoints - (pointsToNextLevel - experiencePoints);

		// currentProgress nam je procentualna vrednost uradjenosti tekudjeg
		// nivoa
		float currentProgress = currentLevelExperiencePoints * 1.0f / EDTSettings.POINTS_PER_LEVEL;

		UserExperienceDTO dto = new UserExperienceDTO();
		dto.setCurrentLevel(level);
		dto.setExperiencePoints(experiencePoints);
		dto.setNextLevelExperiencePoints(nextLevelExperiencePoints);
		dto.setCurrentLevelExperiencePoints(currentLevelExperiencePoints);
		dto.setLearnedQuestions(learns);
		dto.setTestedQuestions(tests);
		dto.setCurrentProgress(currentProgress);

		return dto;
	}

	@Transactional(readOnly = true)
	public LeaderboardDTO loadLeaderboard(Long userId, Integer typeId) {

		LeaderboardDTO dto = new LeaderboardDTO();
		dto.setUsers(new LinkedList<LeaderboardUserDTO>());

		Date limitDate = getLimitDate(typeId);

		// izvlacimo N najboljih takmicara koji su sakupili navise poena
		// u poslednjih X dana
		List bestUsers = userRepository.findBestUsers(limitDate);

		for (Object o : bestUsers) {

			Object[] arr = (Object[]) o;

			BigInteger id = (BigInteger) arr[0];
			String username = (String) arr[1];
			BigDecimal points = (BigDecimal) arr[2];
			
			Integer totalPoints = userRepository.findOne(id.longValue()).getPoints();
			int level = totalPoints / EDTSettings.POINTS_PER_LEVEL + 1;

			LeaderboardUserDTO userDto = new LeaderboardUserDTO();
			userDto.setUserId(id.longValue());
			userDto.setUsername(username);
			userDto.setPoints(points.intValue());
			userDto.setLevel(level);

			dto.getUsers().add(userDto);
		}

		return dto;
	}

	private Date getLimitDate(Integer typeId) {

		DateTime date = null;

		if (typeId == 1) {

			// ove nedelje
			DateTime now = DateTime.now();

			date = new DateTime(now.year().get(), now.monthOfYear().get(), now.dayOfMonth().get(), 0, 0);
			date = date.withDayOfWeek(DateTimeConstants.MONDAY);

		} else if (typeId == 2) {

			// ovog meseca
			DateTime now = DateTime.now();

			date = new DateTime(now.year().get(), now.monthOfYear().get(), 1, 0, 0);

		} else if (typeId == 3) {

			// oduvek
			date = new DateTime(2010, 1, 1, 0, 0);
			
		} else if(typeId == 4){
			
			//poslednjih 7 dana
			DateTime now = DateTime.now();
			
			date = now.minusDays(7);
		}

		return date.toDate();
	}

	private StatisticsDTO populateProgressForUser(User user) {

		StatisticsDTO dto = new StatisticsDTO();
		long count = questionRepository.countForDrivingCategory(user.getDrivingCategory());
		int learnedQuestionsCount = user.getLearnedQuestions();
		int testedQuestionsCount = user.getTestedQuestions();
		int points = user.getPoints();

		float progress = count > 0 ? points * 100f / (2 * count) : 0;

		progress = Math.round(progress * 100) / 100f;
		
		//ako je progres veci od 100, limitiraj ga
		progress = Math.min(progress, 100);

		dto.setTotalQuestions((int) count);
		dto.setLearnedQuestions((int) learnedQuestionsCount);
		dto.setTestedQuestions((int) testedQuestionsCount);

		dto.setTotalProgress(progress);
		return dto;
	}

	private LearningGroupsDTO populateGroupsForUser(User user) {

		LearningGroupsDTO dto = new LearningGroupsDTO();

		// za svaku grupu pitanja treba da vidimo koliko posto pitanja je uceno
		// jednom, dva puta, tri i cetiri
		// istu statistiku vadimo i za testiranje s tim sto racunamo samo
		// odgovore koji su tacni

		Iterable<QuestionCategory> findAll = questionCategoryRepository.findAll();

		List<LearningGroupDTO> groups = new LinkedList<LearningGroupDTO>();

		// prolazimo kroz svaku kategoriju i gledamo koliko ukupno ima pitanja
		// za usera a koliko je ucio

		for (QuestionCategory qc : findAll) {

			Long questionCountLong = questionRepository.countForQuestionAndDrivingCategory(qc, user.getDrivingCategory());

			int questionCount = questionCountLong != null ? questionCountLong.intValue() : 0;

			List<Map<String, Long>> list1 = userQuestionLearnRepository.groupLearnedExactForQuestionAndDrivingCategory(
					user.getDrivingCategory(), qc, user, 1);
			List<Map<String, Long>> list2 = userQuestionLearnRepository.groupLearnedExactForQuestionAndDrivingCategory(
					user.getDrivingCategory(), qc, user, 2);
			List<Map<String, Long>> list3 = userQuestionLearnRepository.groupLearnedExactForQuestionAndDrivingCategory(
					user.getDrivingCategory(), qc, user, 3);
			// List<Map<String, Long>> list4 =
			// userQuestionLearnRepository.groupLearnedExactForQuestionAndDrivingCategory(
			// user.getDrivingCategory(), qc, user, 4);

			int learn1 = list1 != null ? list1.size() : 0;
			int learn2 = list2 != null ? list2.size() : 0;
			int learn3 = list3 != null ? list3.size() : 0;
			// int learn4 = list4 != null ? list4.size() : 0;

			list1 = userQuestionTestRepository.groupTestedExactForQuestionAndDrivingCategory(user.getDrivingCategory(), qc, user, 1);
			list2 = userQuestionTestRepository.groupTestedExactForQuestionAndDrivingCategory(user.getDrivingCategory(), qc, user, 2);
			list3 = userQuestionTestRepository.groupTestedExactForQuestionAndDrivingCategory(user.getDrivingCategory(), qc, user, 3);
			// list4 =
			// userQuestionTestRepository.groupTestedExactForQuestionAndDrivingCategory(user.getDrivingCategory(),
			// qc, user, 4);

			int test1 = list1 != null ? list1.size() : 0;
			int test2 = list2 != null ? list2.size() : 0;
			int test3 = list3 != null ? list3.size() : 0;
			// int test4 = list4 != null ? list4.size() : 0;

			LearningGroupDTO group = new LearningGroupDTO();
			group.setId(qc.getId());
			group.setTitle(qc.getName());
			group.setNumberOfQuestions(questionCount);

			group.setLearn1(learn1);
			group.setLearn2(learn2);
			group.setLearn3(learn3);
			// group.setLearn4(learn4);

			group.setTest1(test1);
			group.setTest2(test2);
			group.setTest3(test3);
			// group.setTest4(test4);

			groups.add(group);
		}

		dto.setGroups(groups);

		return dto;
	}

	private LearningLessonsDTO populateLessonsForUser(User user, Long categoryId) {

		LearningLessonsDTO dto = new LearningLessonsDTO();

		// za svaku grupu pitanja treba da vidimo koliko posto pitanja je uceno
		// jednom, dva puta, tri i cetiri
		// istu statistiku vadimo i za testiranje s tim sto racunamo samo
		// odgovore koji su tacni

		QuestionCategory questionCategory = questionCategoryRepository.findOne(categoryId);
		DrivingCategory drivingCategory = user.getDrivingCategory();

		dto.setCategoryId(categoryId);
		dto.setCategoryName(questionCategory.getName());

		// ucitavamo sva pitanja iz za izabranu grupu i kategoriju
		List<Question> questions = questionRepository.findAll(questionCategory, drivingCategory);

		// ucitavamo sva ucenja i testiranja po broju citanja
		List<Map<String, Long>> learnList1 = userQuestionLearnRepository.groupLearnedExactForQuestionAndDrivingCategory(drivingCategory,
				questionCategory, user, 1);
		List<Map<String, Long>> learnList2 = userQuestionLearnRepository.groupLearnedExactForQuestionAndDrivingCategory(drivingCategory,
				questionCategory, user, 2);
		List<Map<String, Long>> learnList3 = userQuestionLearnRepository.groupLearnedExactForQuestionAndDrivingCategory(drivingCategory,
				questionCategory, user, 3);
		List<Map<String, Long>> learnList4 = userQuestionLearnRepository.groupLearnedExactForQuestionAndDrivingCategory(drivingCategory,
				questionCategory, user, 4);

		List<Map<String, Long>> testList1 = userQuestionTestRepository.groupTestedExactForQuestionAndDrivingCategory(drivingCategory,
				questionCategory, user, 1);
		List<Map<String, Long>> testList2 = userQuestionTestRepository.groupTestedExactForQuestionAndDrivingCategory(drivingCategory,
				questionCategory, user, 2);
		List<Map<String, Long>> testList3 = userQuestionTestRepository.groupTestedExactForQuestionAndDrivingCategory(drivingCategory,
				questionCategory, user, 3);
		List<Map<String, Long>> testList4 = userQuestionTestRepository.groupTestedExactForQuestionAndDrivingCategory(drivingCategory,
				questionCategory, user, 4);

		// ucitavamo i favorites statistike
		List<Map<String, Long>> questionFavorites = userQuestionFavoriteRepository.groupFavoriteExactForQuestionAndDrivingCategory(
				drivingCategory, questionCategory, user);

		// sada imamo ucitana sva pitanja i koja nam trebaju i sve ucenja koja
		// nam trebaju da bismo formirali grupe

		List<LearningLessonDTO> lessons = new LinkedList<LearningLessonDTO>();

		int currentLessonSize = 0; // brojimo broj pitanja po lekciji

		LearningLessonDTO currentLesson = null;

		for (int i = 0; i < questions.size(); i++) {

			// prvo vadimo podatke za trenutno pitanje
			Question q = questions.get(i);

			// sada za ovo pitanje vadimo broj ucenja i testiranja
			int learns1 = getNumberOfLearns(learnList1, q);
			int learns2 = getNumberOfLearns(learnList2, q);
			int learns3 = getNumberOfLearns(learnList3, q);
			int learns4 = getNumberOfLearns(learnList4, q);

			int tests1 = getNumberOfLearns(testList1, q);
			int tests2 = getNumberOfLearns(testList2, q);
			int tests3 = getNumberOfLearns(testList3, q);
			int tests4 = getNumberOfLearns(testList4, q);

			int favorite = getNumberOfFavorites(questionFavorites, q);

			// sada imamo sve informacije o tekucem pitanju

			// ako smo presli limit broja pitanja po lekciji, prelazimo na
			// sledecu lekciju
			if (currentLessonSize == 0) {

				currentLesson = new LearningLessonDTO();
				currentLesson.setId(lessons.size() + 1);
				currentLesson.setTitle(getLessonName(questionCategory, lessons.size() + 1));
				lessons.add(currentLesson);
			}

			currentLesson.setNumberOfQuestions(currentLesson.getNumberOfQuestions() + 1);

			currentLesson.setLearn1(currentLesson.getLearn1() + learns1);
			currentLesson.setLearn2(currentLesson.getLearn2() + learns2);
			currentLesson.setLearn3(currentLesson.getLearn3() + learns3);
			currentLesson.setLearn4(currentLesson.getLearn4() + learns4);

			currentLesson.setTest1(currentLesson.getTest1() + tests1);
			currentLesson.setTest2(currentLesson.getTest2() + tests2);
			currentLesson.setTest3(currentLesson.getTest3() + tests3);
			currentLesson.setTest4(currentLesson.getTest4() + tests4);

			currentLesson.setFavoritesCount(currentLesson.getFavoritesCount() + favorite);

			currentLessonSize++;

			// ako prelazimo broj limit, idemo ispocetka
			if (currentLessonSize >= EDTSettings.LESSON_SIZE)
				currentLessonSize = 0;
		}

		dto.setSessions(lessons);

		return dto;
	}

	private String getLessonName(QuestionCategory questionCategory, int lessonId) {

		String title = questionCategory.getName().substring(0, 4).toUpperCase() + "-" + lessonId;

		return title;

	}

	/**
	 * Metoda vraca 0 ili 1 u zavisnosti od toga da li se pitanje nalazi medju
	 * pitanjima koja su ucena ili testirana barem N puta.
	 * 
	 * @param learnList1
	 * @param q
	 * @return
	 */
	private int getNumberOfLearns(List<Map<String, Long>> learnList1, Question q) {

		if (learnList1 == null)
			return 0;

		Long result = 0l;

		Long id;

		for (Map<String, Long> map : learnList1) {

			id = map.get("id");

			if (q.getId().equals(id)) {

				result = map.get("count");
			}
		}

		if (result == null)
			result = 0l;

		return result > 0 ? 1 : 0;
	}

	/**
	 * Metoda vraca 0 ili 1 u zavisnosti od toga da li se pitanje nalazi medju
	 * pitanjima koja su oznacena kao omiljena.
	 * 
	 * @param learnList1
	 * @param q
	 * @return
	 */
	private int getNumberOfFavorites(List<Map<String, Long>> favoritesList, Question q) {

		if (favoritesList == null)
			return 0;

		Long result = 0l;

		Long id;

		for (Map<String, Long> map : favoritesList) {

			id = map.get("id");

			if (q.getId().equals(id)) {

				result = map.get("count");
			}
		}

		if (result == null)
			result = 0l;

		return result > 0 ? 1 : 0;
	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadLearnedPerCategory(Long userId) {

		StatisticsDTO dto = new StatisticsDTO();

		User user = userRepository.findOne(userId);

		Iterable<QuestionCategory> findAll = questionCategoryRepository.findAll();

		List<QuestionCategoryStatDTO> categories = new LinkedList<QuestionCategoryStatDTO>();

		// prolazimo kroz svaku kategoriju i gledamo koliko ukupno ima pitanja
		// za usera a koliko je ucio

		for (QuestionCategory qc : findAll) {

			long questionCountForQuestionCategory = questionRepository.countForQuestionAndDrivingCategory(qc, user.getDrivingCategory());
			long learnedQuestionsCountForQuestionCategory = userQuestionLearnRepository.countForQuestionAndDrivingCategory(
					user.getDrivingCategory(), qc, user);

			QuestionCategoryStatDTO statDto = new QuestionCategoryStatDTO();
			statDto.setQuestionCategory(questionCategoryTransformer.transformToDTO(qc));
			statDto.setCount((int) learnedQuestionsCountForQuestionCategory);
			statDto.setTotalCount((int) questionCountForQuestionCategory);

			categories.add(statDto);
		}

		dto.setCategories(categories);

		return dto;
	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadTestedPerCategory(Long userId) {

		StatisticsDTO dto = new StatisticsDTO();

		User user = userRepository.findOne(userId);

		Iterable<QuestionCategory> findAll = questionCategoryRepository.findAll();

		List<QuestionCategoryStatDTO> categories = new LinkedList<QuestionCategoryStatDTO>();

		// prolazimo kroz svaku kategoriju i gledamo koliko ukupno ima pitanja
		// za usera a koliko je ucio

		for (QuestionCategory qc : findAll) {

			long questionCountForQuestionCategory = questionRepository.countForQuestionAndDrivingCategory(qc, user.getDrivingCategory());
			long learnedQuestionsCountForQuestionCategory = userQuestionTestRepository.countForQuestionAndDrivingCategory(
					user.getDrivingCategory(), qc, user);

			QuestionCategoryStatDTO statDto = new QuestionCategoryStatDTO();
			statDto.setQuestionCategory(questionCategoryTransformer.transformToDTO(qc));
			statDto.setCount((int) learnedQuestionsCountForQuestionCategory);
			statDto.setTotalCount((int) questionCountForQuestionCategory);

			categories.add(statDto);
		}

		dto.setCategories(categories);

		return dto;
	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadCurrentlyActiveUsersCount(Long userId) {

		StatisticsDTO dto = new StatisticsDTO();

		int i = (int) Math.round(Math.random() * 100);

		dto.setLoggedInUsersCount(i);

		return dto;
	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadCurrentlyActiveUsers(Long userId) {

		StatisticsDTO dto = new StatisticsDTO();
		// Set<AuthenticationToken> tokens = tokenStore.getTokens();
		List<ActiveUserDTO> users = new LinkedList<ActiveUserDTO>();
		// for (AuthenticationToken authenticationToken : tokens) {
		// User user = authenticationToken.getUser();
		// ActiveUserDTO au = new ActiveUserDTO();
		// au.setFirstName(user.getFirstName());
		// au.setLastName(user.getLastName());
		// StatisticsDTO statisticsDTO = populateProgressForUser(user);
		// au.setCompletionPercent(statisticsDTO.getTotalProgress());
		// users.add(au);
		// }
		dto.setActiveUsers(users);
		return dto;
	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadRecentLearningSessions(Long userId) {

		StatisticsDTO dto = new StatisticsDTO();

		User user = userRepository.findById(userId);

		Iterable<LearningSession> sessions = learningSessionRepository.findRecent(user);

		List<LearningSessionDTO> dtos = new LinkedList<LearningSessionDTO>();

		if (sessions != null) {

			for (LearningSession session : sessions) {

				LearningSessionDTO lsDto = learningSessionTransformer.transformToDTO(session);
				long count = userQuestionLearnRepository.countForSession(session);

				lsDto.setNumberOfQuestions((int) count);

				dtos.add(lsDto);
			}
		}

		dto.setLearningSessions(dtos);
		return dto;
	}

	public StatisticsDTO loadAllLearningSessions(Long userId) {

		StatisticsDTO dto = new StatisticsDTO();

		User user = userRepository.findById(userId);

		Iterable<LearningSession> sessions = learningSessionRepository.findAllForUser(user);

		List<LearningSessionDTO> dtos = new LinkedList<LearningSessionDTO>();

		if (sessions != null) {

			for (LearningSession session : sessions) {

				LearningSessionDTO lsDto = learningSessionTransformer.transformToDTO(session);
				long count = userQuestionLearnRepository.countForSession(session);

				lsDto.setNumberOfQuestions((int) count);

				dtos.add(lsDto);
			}
		}

		dto.setLearningSessions(dtos);

		return dto;

	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadRecentTestingSessions(Long userId) {

		StatisticsDTO dto = new StatisticsDTO();

		User user = userRepository.findById(userId);

		Iterable<TestingSession> sessions = testingSessionRepository.findRecent(user);

		List<TestingSessionDTO> dtos = new LinkedList<TestingSessionDTO>();

		if (sessions != null) {

			for (TestingSession session : sessions) {

				TestingSessionDTO lsDto = testingSessionTransformer.transformToDTO(session);
				long count = userQuestionTestRepository.countForSession(session);

				lsDto.setNumberOfQuestions((int) count);

				dtos.add(lsDto);
			}
		}

		dto.setTestingSessions(dtos);
		return dto;
	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadAllTestingSessions(Long userId) {

		StatisticsDTO dto = new StatisticsDTO();

		User user = userRepository.findById(userId);

		Iterable<TestingSession> sessions = testingSessionRepository.findAllForUser(user);

		List<TestingSessionDTO> dtos = new LinkedList<TestingSessionDTO>();

		if (sessions != null) {

			for (TestingSession session : sessions) {

				TestingSessionDTO lsDto = testingSessionTransformer.transformToDTO(session);
				long count = userQuestionTestRepository.countForSession(session);

				lsDto.setNumberOfQuestions((int) count);

				dtos.add(lsDto);
			}
		}

		dto.setTestingSessions(dtos);

		return dto;

	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadLearningSessionQuestions(String sessionUid, Long userId) {

		StatisticsDTO dto = new StatisticsDTO();

		LearningSession testingSession = learningSessionRepository.findByUid(sessionUid);

		LearningSessionDTO testingSessionDTO = learningSessionTransformer.transformToDTO(testingSession, "learns");

		List<LearningSessionDTO> learningSessions = new LinkedList<LearningSessionDTO>();

		learningSessions.add(testingSessionDTO);

		dto.setLearningSessions(learningSessions);

		return dto;
	}

	@Transactional(readOnly = true)
	public StatisticsDTO loadTestingSessionQuestions(String sessionUid, Long userId) {

		StatisticsDTO dto = new StatisticsDTO();

		TestingSession testingSession = testingSessionRepository.findByUid(sessionUid);

		TestingSessionDTO testingSessionDTO = testingSessionTransformer.transformToDTO(testingSession, "learns");

		List<TestingSessionDTO> testingSessions = new LinkedList<TestingSessionDTO>();

		testingSessions.add(testingSessionDTO);

		dto.setTestingSessions(testingSessions);

		return dto;
	}

	/**
	 * Metoda izvlaci broj aktivnih ucenika zadnjih N dana koji su ucili ili
	 * testirali preko aplikacije.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public int getNumberOfActiveStudentsRecently(int numOfRecentDays) {

		DateTime date = new DateTime();
		date = date.minusDays(numOfRecentDays);

		List<Long> userIds = testingSessionRepository.getRecentTestingUserIds(date.toDate());
		List<Long> learningUserIds = learningSessionRepository.getRecentLearningUserIds(date.toDate());

		Set<Long> ids = new HashSet<Long>();
		ids.addAll(userIds);
		ids.addAll(learningUserIds);

		return ids.size();
	}

	/**
	 * Metoda izvlaci broj aktivnih ucenika zadnjih N dana koji su ucili ili
	 * testirali preko aplikacije.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public StatisticsDTO getRecentActiveStudents(int numOfRecentDays) {

		DateTime date = new DateTime();
		date = date.minusDays(numOfRecentDays);

		List<Long> userIds = testingSessionRepository.getRecentTestingUserIds(date.toDate());
		List<Long> learningUserIds = learningSessionRepository.getRecentLearningUserIds(date.toDate());

		Set<Long> ids = new HashSet<Long>();
		ids.addAll(userIds);
		ids.addAll(learningUserIds);

		StatisticsDTO dto = new StatisticsDTO();

		List<ActiveUserDTO> users = new LinkedList<ActiveUserDTO>();

		for (Long userId : ids) {

			User user = userRepository.findOne(userId);

			ActiveUserDTO au = new ActiveUserDTO();

			au.setFirstName(user.getFirstName());
			au.setLastName(user.getLastName());
			au.setUserId(user.getId());
			au.setUsername(user.getUsername());
			au.setPoints(user.getPoints());
			au.setLearningPoints(user.getLearnedQuestions());
			au.setTestingPoints(user.getTestedQuestions());

			StatisticsDTO statisticsDTO = populateProgressForUser(user);

			au.setCompletionPercent(statisticsDTO.getTotalProgress());

			users.add(au);
		}

		// sortiramo u upadajucem redosledu
		Collections.sort(users, new Comparator<ActiveUserDTO>() {

			@Override
			public int compare(ActiveUserDTO o1, ActiveUserDTO o2) {

				Float float1 = o1.getCompletionPercent();
				Float float2 = o2.getCompletionPercent();

				if (float1 == null)
					float1 = 0f;

				if (float2 == null)
					float2 = 0f;

				if (float2 > float1)
					return 1;

				if (float1 > float2)
					return -1;

				return 0;
			}
		});

		dto.setActiveUsers(users);

		return dto;
	}

}
