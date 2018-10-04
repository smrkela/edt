package com.evola.edt.component;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.DailyTest;
import com.evola.edt.model.DailyTestUserResult;
import com.evola.edt.model.DailyTestUserResultQuestion;
import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionAnswer;
import com.evola.edt.model.QuestionCategory;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DailyTestUserResultDTO;
import com.evola.edt.model.dto.DailyTestUserResultQuestionDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.repository.DailyTestRepository;
import com.evola.edt.repository.DailyTestUserResultRepository;
import com.evola.edt.repository.DrivingCategoryRepository;
import com.evola.edt.repository.QuestionCategoryRepository;
import com.evola.edt.repository.QuestionRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.AllDailyTestsDTO;
import com.evola.edt.service.dto.AllDailyTestsTestDTO;
import com.evola.edt.service.dto.helpers.DailyTestDTO;
import com.evola.edt.service.dto.helpers.DailyTestQuestionDTO;
import com.evola.edt.service.dto.transformer.DailyTestUserResultDTOTransformer;
import com.evola.edt.service.dto.transformer.QuestionDTOTransformer;
import com.evola.edt.service.dto.transformer.UserDTOTransformer;
import com.evola.edt.utils.FormattingUtils;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

@Component
public class DailyTestManager {

	// broj pitanja po testu
	private Ratio[] ratios = new Ratio[] { new Ratio(1, 5), new Ratio(2, 5), new Ratio(3, 2), new Ratio(4, 2), new Ratio(5, 2),
			new Ratio(6, 2), new Ratio(7, 2) };

	@Inject
	QuestionRepository questionRepository;

	@Inject
	QuestionCategoryRepository questionCategoryRepository;

	@Inject
	DrivingCategoryRepository drivingCategoryRepository;

	@Inject
	DailyTestRepository dailyTestRepository;

	@Inject
	DailyTestUserResultRepository resultRepository;

	@Inject
	UserRepository userRepository;

	@Inject
	DailyTestUserResultRepository userResultRepository;

	@Inject
	QuestionDTOTransformer questionDTOTransformer;

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	DailyTestUserResultDTOTransformer resultDTOTransformer;

	@Inject
	UserDTOTransformer userDTOTransformer;

	@Inject
	ActivityManager mActivity;

	/**
	 * Metoda pravi dnevni test tj. formira pitanja za dnevni test. Dnevni test
	 * obuhvata sva pitanja iz kategorije vozila B i iz svih kategorija pitanja
	 * ali u razlicitom odnosu.
	 * 
	 * @return - true ako je napravljen novi test, false ako je osvezen
	 *         postojeci
	 */
	@Transactional
	public boolean createTest() {

		// treba da izaberemo N pitanja i to odredjeni broj pitanja iz svake
		// kategorije

		List<Question> questions = new LinkedList<Question>();

		DrivingCategory drivingCategory = drivingCategoryRepository.findByCategoryId("b");

		for (Ratio ratio : ratios) {

			QuestionCategory questionCategory = questionCategoryRepository.findOne(ratio.id);

			List<Question> list = questionRepository
					.findRandom(questionCategory, drivingCategory, new PageRequest(0, ratio.numOfQuestions));

			questions.addAll(list);
		}

		boolean isNewTest = false;

		DailyTest test = null;

		// prvo gledamo da li vec postoji test za danas
		test = dailyTestRepository.findByDate(new Date());

		if (test == null) {

			test = new DailyTest();
			test.setDate(new Date());

			isNewTest = true;
		}

		test.setQuestions(questions);

		dailyTestRepository.save(test);

		return isNewTest;
	}

	public DailyTestUserResultDTO getUserResults(User user, DailyTest test) {

		// prvo proveravamo da nemamo duplikata
		List<DailyTestUserResult> results = userResultRepository.findMultipleByUserAndTest(user, test);

		DailyTestUserResult userAndTest = null;

		if (results != null && results.size() > 1) {

			// moramo obrisati sve osim jednog, ova greske je moguca ako
			// korisnik posalje istovremeno vise zahteva za cuvanje na server
			for (int i = 0; i < results.size() - 1; i++)
				userResultRepository.delete(results.get(i));

			// sada je ostao samo jedan test
			userAndTest = results.get(0);
		}

		if (userAndTest == null)
			userAndTest = userResultRepository.findByUserAndTest(user, test);

		return resultDTOTransformer.transformToDTO(userAndTest, "user");
	}

	public DailyTestUserResultDTO getTodayUserResults(User user) {

		DailyTest test = dailyTestRepository.findByDate(new Date());

		return getUserResults(user, test);
	}

	/**
	 * Metoda dohvata sve rezultate danasnjeg testa.
	 * 
	 * @return
	 */
	public List<DailyTestUserResultDTO> getTodayResults() {

		DailyTest test = dailyTestRepository.findByDate(new Date());

		return getResults(test);
	}

	/**
	 * Metoda dohvata sve rezultate nekog testa.
	 * 
	 * @param test
	 * @return
	 */
	public List<DailyTestUserResultDTO> getResults(DailyTest test) {

		List<DailyTestUserResult> results = userResultRepository.findByTest(test);

		List<DailyTestUserResultDTO> dtos = new LinkedList<DailyTestUserResultDTO>();

		int count = 1;

		for (DailyTestUserResult result : results) {

			DailyTestUserResultDTO dto = resultDTOTransformer.transformToDTO(result, "user");
			dto.setOrderNumber(count++);

			dtos.add(dto);
		}

		return dtos;
	}

	/**
	 * Metoda vraca broj korisnika koji se danas testirao.
	 * 
	 * @return
	 */
	public int getTodayResultsCount() {

		Long count = userResultRepository.countByDate(new Date());

		if (count == null)
			count = 0l;

		return count.intValue();
	}

	public List<DailyTestUserResultDTO> getTopResults(int numOfResults) {

		List<Map<String, Object>> topResults = userResultRepository.findTopResults(new PageRequest(0, numOfResults));

		List<DailyTestUserResultDTO> dtos = createTopListDTOs(topResults);

		return dtos;
	}

	public List<DailyTestUserResultDTO> getLast7DaysTopResults(int numOfResults) {

		DateTime now = DateTime.now();

		Date date = now.minusDays(7).toDate();

		List<Map<String, Object>> topResults = userResultRepository.findTopResultsByDate(date, new PageRequest(0, numOfResults));

		List<DailyTestUserResultDTO> dtos = createTopListDTOs(topResults);

		return dtos;
	}

	private List<DailyTestUserResultDTO> createTopListDTOs(List<Map<String, Object>> topResults) {

		List<DailyTestUserResultDTO> dtos = new LinkedList<DailyTestUserResultDTO>();

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

			DailyTestUserResultDTO dto = new DailyTestUserResultDTO();
			dto.setPoints(points.intValue());
			dto.setTimeTaken(timeTaken.intValue());
			dto.setCorrectAnswers(correctAnswers.intValue());
			dto.setWrongAswers(incorrectAnswers.intValue());
			dto.setCorrectPercent(totalPercent.floatValue());
			dto.setNumberOfTests(numberOfTests.intValue());

			dto.setUser(userDTOTransformer.transformToDTO(user));

			dto.setOrderNumber(counter++);

			dtos.add(dto);
		}

		return dtos;
	}

	@Transactional
	public List<QuestionDTO> getQuestionDTOs(Long testId) {

		DailyTest test = dailyTestRepository.findOne(testId);

		List<Question> questions = test.getQuestions();

		List<QuestionDTO> dtos = new LinkedList<QuestionDTO>();

		for (Question q : questions) {

			QuestionDTO dto = questionDTOTransformer.transformToDTO(q, "questionImages", "questionAnswers", "questionCategories",
					"drivingCategories");

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

		credentialsManager.checkUserLoggedIn();

		Long userId = SecurityUtils.getUserId();

		Long startTime = dto.getStartTime();
		Long testId = dto.getTestId();

		Assert.notNull(startTime);
		Assert.notNull(testId);

		DailyTest test = dailyTestRepository.findOne(testId);
		User user = userRepository.findOne(userId);

		// gledamo da korisnik nije vec uradio test
		DailyTestUserResult previousResult = resultRepository.findByUserAndTest(user, test);

		if (previousResult != null) {

			throw new RuntimeException(getDailyTestAllreadySubmittedMessage(previousResult));
		}

		int correctlyAnswered = 0;
		int incorrectlyAnswered = 0;
		int totalQuestions = 0;

		DailyTestUserResult result = new DailyTestUserResult();
		result.setQuestionResults(new HashSet<DailyTestUserResultQuestion>());

		// sada odredjujemo tacnost resenja
		for (Question q : test.getQuestions()) {

			DailyTestQuestionDTO qDTO = dto.getQuestion(q.getId());

			// sada odredjujemo tacnost odgovora

			// za svako pitanje belezimo sta je korisnik odgovorio
			DailyTestUserResultQuestion resultQuestion = new DailyTestUserResultQuestion();
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

			totalQuestions++;
		}

		Float correctPercent = correctlyAnswered * 100f / totalQuestions;
		int timeTaken = (int) ((System.currentTimeMillis() - startTime) / 1000);

		// sada racunamo broj poena
		int points = calculatePoints(timeTaken, totalQuestions, correctlyAnswered);

		// radimo cuvanje rezultata za trenutnog korisnika
		result.setUser(user);
		result.setTest(test);
		result.setCorrectAnswers(correctlyAnswered);
		result.setWrongAswers(incorrectlyAnswered);
		result.setCorrectPercent(correctPercent);
		result.setTimeTaken(timeTaken);
		result.setCreationDate(new Date());
		result.setPoints(points);

		DailyTestUserResult savedResult = resultRepository.save(result);

		mActivity.doneDailyTest(savedResult);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("dateString", FormattingUtils.formatIsoDate(test.getDate()));
		map.put("username", user.getUsername());

		return map;
	}

	public DailyTest getTodayTest() {

		return dailyTestRepository.findByDate(new Date());
	}

	private String getDailyTestAllreadySubmittedMessage(DailyTestUserResult previousResult) {

		Date creationDate = previousResult.getCreationDate();

		String message = "Već ste radili ovaj test.";

		if (creationDate != null) {

			DateTime date = new DateTime(creationDate);
			DateTime now = DateTime.now();

			Interval interval = new Interval(date, now);

			Duration duration = interval.toDuration();

			long hours = duration.getStandardHours();

			if (hours > 1)
				message = "Već ste radili ovaj test pre " + hours + " sati.";
			else if (hours == 1)
				message = "Već ste radili ova test pre sat vremena.";
			else
				message = "Već ste radili ovaj test pre " + duration.getStandardMinutes() + " minuta.";
		}

		return message;
	}

	/**
	 * Metoda racuna broj osvojenih poena preko odredjene formule.
	 * 
	 * @param timeTaken
	 * @param totalQuestions
	 * @param correctlyAnswered
	 * @return
	 */
	private int calculatePoints(int timeTaken, int totalQuestions, int correctlyAnswered) {

		int optimalTime = 5 * 60; // 5 minuta
		int pointsPerQuestion = 5;
		int nominalPoints = pointsPerQuestion * correctlyAnswered;

		int points = optimalTime * nominalPoints * 27 / ((timeTaken + 500) * 10);

		return points;
	}

	/**
	 * Metoda izvlaci sve testove ikad i neke statistike o njima i trenutnom
	 * korisniku.
	 * 
	 * @return
	 */
	public AllDailyTestsDTO getAllTests(User user) {

		List<Map<String, Object>> allTests = dailyTestRepository.findAllTests();
		List<Map<String, Object>> userTests = new LinkedList<Map<String, Object>>();

		if (user != null)
			userTests = dailyTestRepository.findUserTests(user);

		AllDailyTestsDTO dto = new AllDailyTestsDTO();

		int totalPointsByCurrentUser = 0;
		int totalTestsByCurrentUser = 0;
		int totalTestsDone = 0;

		Long totalUsersLong = dailyTestRepository.findTotalUsersCount();

		int totalUsers = totalUsersLong != null ? totalUsersLong.intValue() : 0;

		for (Map<String, Object> test : allTests) {

			Long id = (Long) test.get("id");
			Date date = (Date) test.get("date");
			Long count = (Long) test.get("count");
			Long userPoints = 0l;
			boolean userHasTakenTest = false;

			for (Map<String, Object> userTest : userTests) {

				Long userTestId = (Long) userTest.get("id");
				Long points = (Long) userTest.get("points");

				if (id.equals(userTestId)) {

					userHasTakenTest = true;
					userPoints = points != null ? points : 0;

					totalPointsByCurrentUser += userPoints;
					totalTestsByCurrentUser++;

					break;
				}
			}

			AllDailyTestsTestDTO testDto = new AllDailyTestsTestDTO();
			testDto.setDate(date);
			testDto.setDateString(FormattingUtils.formatDateSimple(date));
			testDto.setHasUserTakenTest(userHasTakenTest);
			testDto.setNumberOfPoints(userPoints.intValue());
			testDto.setNumberOfUsers(count.intValue());
			testDto.setTestId(id);

			dto.addTest(testDto);

			totalTestsDone += count;
		}

		dto.setTotalPointsByCurrentUser(totalPointsByCurrentUser);
		dto.setTotalTestsByCurrentUser(totalTestsByCurrentUser);
		dto.setTotalTestsDone(totalTestsDone);
		dto.setTotalUsers(totalUsers);

		return dto;
	}

	public DailyTest getTest(Long testId) {

		return dailyTestRepository.findOne(testId);
	}

	/**
	 * @param Daci
	 *            , 27.05.2014.
	 * @return - metoda vraca sva pitanja i odgovore
	 */
	public DailyTestUserResultDTO getDailyTestUserResultQuestion(User user) {

		// treba da se vrati sve iz DailyTestUserResultQuestion tabele a to nije
		// jedan rekord vec vise njih

		DailyTest test = dailyTestRepository.findByDate(new Date());

		DailyTestUserResult userAndTest = userResultRepository.findByUserAndTest(user, test);

		return resultDTOTransformer.transformToDTO(userAndTest, "user");
	}

	/**
	 * Metoda vraca test po datumu.
	 * 
	 * @param testDate
	 * @return
	 */
	public DailyTest getTestByDate(Date testDate) {

		return dailyTestRepository.findByDate(testDate);
	}

	public int getTotalTestsCount() {

		Long count = dailyTestRepository.findAllTestsCount();

		if (count == null)
			count = 0l;

		return count.intValue();
	}

	public int getTotalUserTestsCount(User user) {

		Long count = resultRepository.countByUser(user);

		if (count == null)
			count = 0l;

		return count.intValue();
	}

	public int getTotalUserPoints(User user) {

		Long count = resultRepository.countPointsByUser(user);

		if (count == null)
			count = 0l;

		return count.intValue();
	}

	public int getTotalUserCount() {

		Long count = resultRepository.countUsers();

		if (count == null)
			count = 0l;

		return count.intValue();
	}

	/**
	 * Metoda vraca redni broj korisnikovog rezultata na nekom testu.
	 * 
	 * @param test
	 * @param user
	 * @param userResult
	 * @return
	 */
	public int getUserResultOrder(DailyTest test, User user, DailyTestUserResultDTO userResult) {

		// izvlace se dnevni rezultati
		List<DailyTestUserResultDTO> results = getResults(test);

		// koji je po redu trenutni korisnik
		int orderNumber = 1;

		if (userResult != null) {

			for (int i = 0; i < results.size(); i++) {

				UserDTO userDto = results.get(i).getUser();

				if (userDto != null && user.getId().equals(userDto.getId())) {

					orderNumber = i + 1;
					break;
				}
			}
		}

		return orderNumber;
	}

	public int gotTotalUserPosition(User user) {

		Double rank = dailyTestRepository.getTotalUserPosition(user.getId());

		if (rank == null)
			rank = 0d;

		return rank.intValue();
	}

	public List<DailyTestUserResultQuestionDTO> sortResponse(List<DailyTestUserResultQuestionDTO> dailyTestUserResultQuestion,
			List<QuestionDTO> questionDtos) {

		List<DailyTestUserResultQuestionDTO> response = new LinkedList<DailyTestUserResultQuestionDTO>();
		Long questionId;

		for (QuestionDTO question : questionDtos) {
			questionId = question.getId();

			for (DailyTestUserResultQuestionDTO dailyTest : dailyTestUserResultQuestion) {

				if (questionId.equals(dailyTest.getQuestion().getId())) {
					response.add(dailyTest);
					break;
				}
			}

		}

		return response;
	}

	public QuestionDTO getQuestionDTO(List<QuestionDTO> questionDtos, Question question) {

		for (QuestionDTO questionDTO : questionDtos) {

			if (questionDTO.getId().equals(question.getId())) {
				return questionDTO;
			}

		}

		return null;
	}

	public int getTotalUserCountOnDate(Date date) {

		DateMidnight dateTime = new DateMidnight(date);

		Long countLong = resultRepository.countUsersOnDateRange(dateTime.toDate(), dateTime.plusDays(1).toDate());

		int count = countLong != null ? countLong.intValue() : 0;

		return count;
	}

	public int getTotalTestResultsOnDate(Date date) {

		DateMidnight dateTime = new DateMidnight(date);

		Long countLong = resultRepository.countTestResultsOnDateRange(dateTime.toDate(), dateTime.plusDays(1).toDate());

		int count = countLong != null ? countLong.intValue() : 0;

		return count;
	}

	public int getTotalResultsRecently(Date dateFrom) {

		Long countLong = resultRepository.countTestResultsOnDateRange(dateFrom, new Date());

		int count = countLong != null ? countLong.intValue() : 0;

		return count;
	}

}

class Ratio {

	public long id;
	public int numOfQuestions;

	public Ratio(long categoryId, int numOfQuestions) {

		this.id = categoryId;
		this.numOfQuestions = numOfQuestions;
	}
}