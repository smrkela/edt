package com.evola.edt.component;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionAnswer;
import com.evola.edt.model.RealTest;
import com.evola.edt.model.RealTestCategory;
import com.evola.edt.model.RealTestUserResult;
import com.evola.edt.model.RealTestUserResultQuestion;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DailyTestUserResultDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.RealTestUserResultDTO;
import com.evola.edt.model.dto.RealTestUserResultQuestionDTO;
import com.evola.edt.repository.QuestionRepository;
import com.evola.edt.repository.RealTestCategoryRepository;
import com.evola.edt.repository.RealTestRepository;
import com.evola.edt.repository.RealTestUserResultRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.helpers.DailyTestDTO;
import com.evola.edt.service.dto.helpers.DailyTestQuestionDTO;
import com.evola.edt.service.dto.helpers.RealTestCreationDTO;
import com.evola.edt.service.dto.helpers.RealTestOverviewDTO;
import com.evola.edt.service.dto.helpers.RealTestOverviewGroupDTO;
import com.evola.edt.service.dto.helpers.RealTestOverviewTestDTO;
import com.evola.edt.service.dto.transformer.QuestionDTOTransformer;
import com.evola.edt.service.dto.transformer.RealTestUserResultDTOTransformer;
import com.evola.edt.service.dto.transformer.UserDTOTransformer;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

@Component
public class RealTestManager {

	@Inject
	private RealTestRepository rRealTest;

	@Inject
	private RealTestCategoryRepository rRealTestCategory;

	@Inject
	private QuestionRepository rQuestion;

	@Inject
	private QuestionDTOTransformer tQuestionDTO;

	@Inject
	private UserCredentialsManager mUserCredentials;

	@Inject
	private UserRepository rUser;

	@Inject
	private RealTestUserResultRepository rRealTestUserResult;

	@Inject
	private ActivityManager mActivity;

	@Inject
	private RealTestUserResultDTOTransformer tUserResult;
	
	@Inject
	private UserDTOTransformer tUser;

	public List<RealTest> getAllTests() {

		List<RealTest> tests = (List<RealTest>) rRealTest.findAll();

		return tests;
	}

	public List<RealTestCategory> getAllCategories() {

		return (List<RealTestCategory>) rRealTestCategory.findAll();
	}

	public RealTestCreationDTO getCreationData(Long categoryId) {

		RealTestCategory category = rRealTestCategory.findOne(categoryId);

		RealTestCreationDTO dto = new RealTestCreationDTO();
		dto.setCategoryId(category.getId());
		dto.setCategoryName(category.getName());

		List<Long> questionIds = new LinkedList<Long>();

		int counter = category.getNumberOfQuestions() - 1;

		while (counter >= 0) {

			questionIds.add(0l);

			counter--;
		}

		dto.setQuestionIds(questionIds);

		return dto;
	}

	@Transactional
	public RealTestCreationDTO getUpdateData(Long testId) {

		RealTest test = rRealTest.findOne(testId);
		RealTestCategory category = test.getCategory();

		RealTestCreationDTO dto = new RealTestCreationDTO();
		dto.setId(test.getId());
		dto.setName(test.getName());
		dto.setOrderIndex(test.getOrderIndex());
		dto.setCategoryId(category.getId());
		dto.setCategoryName(category.getName());

		List<Long> questionIds = new LinkedList<Long>();

		for (int i = 0; i < category.getNumberOfQuestions(); i++) {

			Long questionId = null;

			if (test.getQuestions().size() > i) {

				Question question = test.getQuestions().get(i);

				if (question != null)
					questionId = question.getId();
			}

			questionIds.add(questionId);
		}

		dto.setQuestionIds(questionIds);

		return dto;
	}

	public void save(RealTestCreationDTO dto) {

		Long testId = dto.getId();
		Long categoryId = dto.getCategoryId();

		RealTest test = null;

		if (testId == null) {
			test = new RealTest();
			test.setCreationDate(new Date());
			test.setCategory(rRealTestCategory.findOne(categoryId));
		} else {
			test = rRealTest.findOne(testId);
		}

		test.setName(dto.getName());
		test.setOrderIndex(dto.getOrderIndex());

		List<Long> questionIds = dto.getQuestionIds();

		test.setQuestions(new LinkedList<Question>());

		for (Long questionId : questionIds) {

			Question q = rQuestion.findOne(questionId);

			test.getQuestions().add(q);
		}

		rRealTest.save(test);
	}

	public RealTestOverviewDTO getOverviewData() {

		RealTestOverviewDTO dto = new RealTestOverviewDTO();
		dto.setGroups(new LinkedList<RealTestOverviewGroupDTO>());

		// we must get all the tests
		Iterable<RealTestCategory> categories = rRealTestCategory.findAll();

		List<Map<String, Object>> userTests = null;
		List<Map<String, Object>> userPassedTests = null;

		User user = null;

		if (SecurityUtils.isLoggedIn()) {

			user = rUser.findOne(SecurityUtils.getUserId());

			userTests = rRealTest.findUserTests(user);
			userPassedTests = rRealTest.findUserPassedTests(user);
		}

		int totalTestByCurrentUser = 0;
		int totalTestsPassedByCurrentUser = 0;
		int totalTests = 0;

		for (RealTestCategory cat : categories) {

			List<Map<String, Object>> findAllForCategory = rRealTest.findAllForCategory(cat);

			RealTestOverviewGroupDTO group = new RealTestOverviewGroupDTO();
			group.setCategoryId(cat.getId());
			group.setCategoryName(cat.getName());
			group.setTests(new LinkedList<RealTestOverviewTestDTO>());

			int totalTestsDone = 0;

			for (Map<String, Object> map : findAllForCategory) {

				Long id = (Long) map.get("id");
				String name = (String) map.get("name");
				Long count = (Long) map.get("count");

				RealTestOverviewTestDTO test = new RealTestOverviewTestDTO();
				test.setId(id);
				test.setName(name);
				test.setTotalResults(count);

				Long userTestsCount = getCountForUser(userTests, user, id);
				Long userPassedTestsCount = getCountForUser(userPassedTests, user, id);

				test.setTotalUserTests(userTestsCount);
				test.setTotalUserPassedTests(userPassedTestsCount);
				test.setHasUserTakenTest(userTestsCount > 0);

				totalTestsDone += count;
				totalTestByCurrentUser += userTestsCount;
				totalTestsPassedByCurrentUser += userPassedTestsCount;
				totalTests += count;

				group.getTests().add(test);
			}

			group.setTotalTestsDone(totalTestsDone);

			dto.getGroups().add(group);
		}

		dto.setTotalTestsDone(totalTests);
		dto.setTotalTestsByCurrentUser(totalTestByCurrentUser);
		dto.setTotalPassedTestsByCurrentUser(totalTestsPassedByCurrentUser);

		// get total number of users that have taken tests
		Long totalUsersCount = rRealTest.findTotalUsersCount();

		dto.setTotalUsers(totalUsersCount.intValue());

		return dto;
	}

	private Long getCountForUser(List<Map<String, Object>> results, User user, Long testId) {

		if (user == null || results == null)
			return 0l;

		for (Map<String, Object> result : results) {

			Long resultTestId = (Long) result.get("id");

			if (resultTestId != null && resultTestId.equals(testId)) {

				Long count = (Long) result.get("count");

				return count;
			}
		}

		return 0l;
	}

	@Transactional
	public RealTest getTest(Long testId) {

		return rRealTest.findOne(testId);
	}

	@Transactional
	public List<QuestionDTO> getQuestionDTOs(Long testId) {

		RealTest test = rRealTest.findOne(testId);

		List<Question> questions = test.getQuestions();

		List<QuestionDTO> dtos = new LinkedList<QuestionDTO>();

		for (Question q : questions) {

			QuestionDTO dto = tQuestionDTO.transformToDTO(q, "questionImages", "questionAnswers", "questionCategories", "drivingCategories");

			// moramo podsetiti d ali je multiselect
			int numOfCorrectAnswers = 0;

			for (QuestionAnswer a : q.getQuestionAnswers()) {

				if (a.getCorrect())
					numOfCorrectAnswers++;
			}

			dto.setMultiSelect(numOfCorrectAnswers > 1);

			dtos.add(dto);
		}

		return dtos;
	}

	@Transactional
	public Map<String, Object> submitTest(DailyTestDTO dto) {

		mUserCredentials.checkUserLoggedIn();

		Long userId = SecurityUtils.getUserId();

		Long startTime = dto.getStartTime();
		Long testId = dto.getTestId();

		Assert.notNull(startTime);
		Assert.notNull(testId);

		RealTest test = rRealTest.findOne(testId);
		User user = rUser.findOne(userId);

		int correctlyAnswered = 0;
		int incorrectlyAnswered = 0;
		int totalQuestions = 0;

		RealTestUserResult result = new RealTestUserResult();
		result.setQuestionResults(new HashSet<RealTestUserResultQuestion>());

		int pointWon = 0;

		// sada odredjujemo tacnost resenja
		for (Question q : test.getQuestions()) {

			DailyTestQuestionDTO qDTO = dto.getQuestion(q.getId());

			// sada odredjujemo tacnost odgovora

			// za svako pitanje belezimo sta je korisnik odgovorio
			RealTestUserResultQuestion resultQuestion = new RealTestUserResultQuestion();
			resultQuestion.setQuestion(q);
			resultQuestion.setResult(result);
			resultQuestion.setUserAnswers(new HashSet<QuestionAnswer>());
			resultQuestion.setHasAnswered(qDTO.getAnswers().size() > 0);

			boolean isCorrectlyAnswered = true;

			for (QuestionAnswer a : q.getQuestionAnswers()) {

				boolean isAnsweredAsCorrect = qDTO.getAnswers().contains(a.getId());

				// ako je korisnik izabrao ovaj odgovor belezimo ga
				if (isAnsweredAsCorrect)
					resultQuestion.getUserAnswers().add(a);

				if (a.getCorrect() != isAnsweredAsCorrect) {

					isCorrectlyAnswered = false;

					// petlja mora da nastavi da se vrti da bi zabelezili sve
					// odgovore koje je korisnik dao
				}
			}

			resultQuestion.setIsCorrect(isCorrectlyAnswered);

			// ubacujemo pitanje rezultata u rezultat
			result.getQuestionResults().add(resultQuestion);

			if (isCorrectlyAnswered)
				correctlyAnswered++;
			else
				incorrectlyAnswered++;

			if (isCorrectlyAnswered)
				pointWon += q.getPoints();

			totalQuestions++;
		}

		Float correctPercent = correctlyAnswered * 100f / totalQuestions;
		int timeTaken = (int) ((System.currentTimeMillis() - startTime) / 1000);

		// sada racunamo da li je korisnik polozio test
		RealTestCategory category = test.getCategory();

		Integer minimumPoints = category.getMinimumPoints();
		Integer maxDuration = category.getDuration();

		boolean hasPassedTest = pointWon >= minimumPoints && timeTaken / 60 <= maxDuration;

		// radimo cuvanje rezultata za trenutnog korisnika
		result.setUser(user);
		result.setTest(test);
		result.setCorrectAnswers(correctlyAnswered);
		result.setWrongAswers(incorrectlyAnswered);
		result.setCorrectPercent(correctPercent);
		result.setTimeTaken(timeTaken);
		result.setCreationDate(new Date());
		result.setPoints(pointWon);
		result.setHasPassedTest(hasPassedTest);

		RealTestUserResult savedResult = rRealTestUserResult.save(result);

		mActivity.doneRealTest(savedResult);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", result.getId());

		return map;
	}

	@Transactional
	public boolean hasUserTakenTest(User user, RealTest test) {

		Long count = rRealTestUserResult.countByUserAndTest(user, test);

		return count != null ? count > 0 : false;
	}

	public List<RealTestUserResultDTO> getResults(RealTest test) {

		List<RealTestUserResult> results = rRealTestUserResult.findByTest(test);

		List<RealTestUserResultDTO> dtos = tranformUserResults(results);

		return dtos;
	}

	public List<RealTestUserResultDTO> getTopResults(RealTest test, int numResults, Date fromDate) {

		List<RealTestUserResult> results = rRealTestUserResult.findByTest(test, fromDate, new PageRequest(0, numResults));

		List<RealTestUserResultDTO> dtos = tranformUserResults(results);

		return dtos;
	}

	public List<RealTestUserResultDTO> getUserResults(RealTest test, User user) {

		List<RealTestUserResult> results = rRealTestUserResult.findByUserAndTest(test, user);

		List<RealTestUserResultDTO> dtos = tranformUserResults(results);

		return dtos;
	}

	private List<RealTestUserResultDTO> tranformUserResults(List<RealTestUserResult> results) {
		List<RealTestUserResultDTO> dtos = new LinkedList<RealTestUserResultDTO>();

		int count = 1;

		for (RealTestUserResult result : results) {

			RealTestUserResultDTO dto = tUserResult.transformToDTO(result, "user");
			dto.setOrderNumber(count++);

			dtos.add(dto);
		}
		return dtos;
	}

	public RealTestUserResultDTO getResult(Long resultId) {

		RealTestUserResult result = rRealTestUserResult.findOne(resultId);

		RealTestUserResultDTO dto = tUserResult.transformToDTO(result, "user");

		return dto;
	}

	public QuestionDTO getQuestionDTO(List<QuestionDTO> questionDtos, Question question) {

		for (QuestionDTO questionDTO : questionDtos) {

			if (questionDTO.getId().equals(question.getId())) {
				return questionDTO;
			}

		}

		return null;
	}

	public List<RealTestUserResultQuestionDTO> sortResponse(List<RealTestUserResultQuestionDTO> dailyTestUserResultQuestion, List<QuestionDTO> questionDtos) {

		List<RealTestUserResultQuestionDTO> response = new LinkedList<RealTestUserResultQuestionDTO>();
		Long questionId;

		for (QuestionDTO question : questionDtos) {
			questionId = question.getId();

			for (RealTestUserResultQuestionDTO dailyTest : dailyTestUserResultQuestion) {

				if (questionId.equals(dailyTest.getQuestion().getId())) {
					response.add(dailyTest);
					break;
				}
			}

		}

		return response;
	}

	public int getUserResultOrder(RealTest test, User user) {

		Double userPosition = rRealTest.getTotalUserPosition(test.getId(), user.getId());

		return userPosition != null ? userPosition.intValue() : 0;
	}

	public int getTotalResultsRecently(Date date) {

		Long countLong = rRealTestUserResult.countTestResultsOnDateRange(date, new Date());

		int count = countLong != null ? countLong.intValue() : 0;

		return count;
	}

	public void delete(Long testId) {

		rRealTest.delete(testId);
	}

	public List<RealTestUserResultDTO> getLast7DaysTopResults(int numOfResults) {

		DateTime now = DateTime.now();

		Date date = now.minusDays(7).toDate();

		List<Map<String, Object>> topResults = rRealTestUserResult.findTopResultsByDate(date, new PageRequest(0, numOfResults));

		List<RealTestUserResultDTO> dtos = createTopListDTOs(topResults);

		return dtos;
	}
	
	private List<RealTestUserResultDTO> createTopListDTOs(List<Map<String, Object>> topResults) {

		List<RealTestUserResultDTO> dtos = new LinkedList<RealTestUserResultDTO>();

		int counter = 1;

		for (Map<String, Object> map : topResults) {

			User user = (User) map.get("user");
			Long points = (Long) map.get("points");
			Long correctAnswers = (Long) map.get("correctAnswers");
			Long incorrectAnswers = (Long) map.get("incorrectAnswers");
			Long timeTaken = (Long) map.get("timeTaken");
			Long numberOfTests = (Long) map.get("numberOfTests");

			if (points == null)
				points = 0l;

			if (correctAnswers == null)
				correctAnswers = 0l;

			if (incorrectAnswers == null)
				incorrectAnswers = 0l;

			if (timeTaken == null)
				timeTaken = 0l;

			if (numberOfTests == null)
				numberOfTests = 0l;

			Long totalPercent = correctAnswers * 100 / (correctAnswers + incorrectAnswers);

			RealTestUserResultDTO dto = new RealTestUserResultDTO();
			dto.setPoints(points.intValue());
			dto.setTimeTaken(timeTaken.intValue());
			dto.setCorrectAnswers(correctAnswers.intValue());
			dto.setWrongAswers(incorrectAnswers.intValue());
			dto.setCorrectPercent(totalPercent.floatValue());
			dto.setNumberOfTests(numberOfTests.intValue());

			dto.setUser(tUser.transformToDTO(user));

			dto.setOrderNumber(counter++);

			dtos.add(dto);
		}

		return dtos;
	}

}
