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

import com.evola.edt.component.store.MarathonStoreData;
import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.MarathonTest;
import com.evola.edt.model.MarathonTestUserResultQuestion;
import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionAnswer;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.MarathonTestUserResultDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.repository.MarathonTestRepository;
import com.evola.edt.repository.QuestionRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.helpers.MarathonTestOverviewDTO;
import com.evola.edt.service.dto.helpers.MarathonTestOverviewTestDTO;
import com.evola.edt.service.dto.transformer.QuestionDTOTransformer;
import com.evola.edt.service.dto.transformer.UserDTOTransformer;
import com.evola.edt.utils.DateUtils;
import com.evola.edt.utils.URLUtils;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

@Component
public class MarathonManager {

	@Inject
	private QuestionRepository rQuestion;

	@Inject
	private QuestionDTOTransformer tQuestionDTO;

	@Inject
	private UserCredentialsManager mUserCredentials;

	@Inject
	private UserRepository rUser;

	@Inject
	private ActivityManager mActivity;

	@Inject
	private SessionStore storeSession;

	@Inject
	private UserDTOTransformer tUser;

	@Inject
	private MarathonTestRepository rMarathon;

	public List<Long> getMarathonQuestionIds(User user) {

		List<Long> questionIds = rQuestion.findRandomIds(null, user.getDrivingCategory(), new PageRequest(0, 5000));

		return questionIds;
	}

	@Transactional
	public QuestionDTO loadQuestion(Long questionId) {

		Question question = rQuestion.findOne(questionId);

		QuestionDTO dto = tQuestionDTO.transformToDTO(question, "questionImages", "questionAnswers", "questionCategories", "drivingCategories");

		// moramo podsetiti d ali je multiselect
		int numOfCorrectAnswers = 0;

		for (QuestionAnswer a : question.getQuestionAnswers()) {

			if (a.getCorrect())
				numOfCorrectAnswers++;
		}

		dto.setMultiSelect(numOfCorrectAnswers > 1);

		return dto;
	}

	@Transactional
	public Map<String, Object> loadInitialData(String marathonId) {

		MarathonStoreData data = storeSession.loadMarathon();

		// mozda je nastavak pa moramo ucitati test

		MarathonTest test = null;

		if (marathonId != null)
			test = rMarathon.findByUid(marathonId);

		Map<String, Object> map = createDoData(data, test);

		return map;
	}

	@Transactional
	public Map<String, Object> skipQuestion(String marathonId) {

		MarathonStoreData data = storeSession.loadMarathon();
		MarathonTest test = rMarathon.findByUid(marathonId);

		if (test == null)
			test = createTest(marathonId);

		Integer currentQuestionIndex = data.getCurrentQuestionIndex();

		Long questionId = data.getQuestionId(currentQuestionIndex);

		saveSubmittedAnswers(test, questionId, null);

		data.setCurrentQuestionIndex(currentQuestionIndex + 1);

		Map<String, Object> map = createDoData(data, test);

		return map;
	}

	@Transactional
	public Map<String, Object> submitQuestion(Map<String, Object> input) {

		String marathonId = (String) input.get("marathonId");
		List answerIds = (List) input.get("answers");

		MarathonStoreData data = storeSession.loadMarathon();
		MarathonTest test = rMarathon.findByUid(marathonId);

		if (test == null)
			test = createTest(marathonId);

		Integer currentQuestionIndex = data.getCurrentQuestionIndex();

		Long questionId = data.getQuestionId(currentQuestionIndex);

		saveSubmittedAnswers(test, questionId, answerIds);

		data.setCurrentQuestionIndex(data.getCurrentQuestionIndex() + 1);

		Map<String, Object> map = createDoData(data, test);

		return map;
	}

	private Map<String, Object> createDoData(MarathonStoreData data, MarathonTest test) {

		// treba da ucitamo prvo pitanje i vratimo ga
		QuestionDTO questionDTO = loadQuestion(data.getQuestionId(data.getCurrentQuestionIndex()));

		Map<String, Object> map = new HashMap<>();
		map.put("question", questionDTO);
		map.put("questionIndex", data.getCurrentQuestionIndex());

		// treba da dodamo i dosadasnje statistike
		int correctQuestions = 0;
		int wrongQuestions = 0;
		String timeTakenString = "";
		int skippedQuestions = 0;
		int totalPoints = 0;

		if (test != null) {
			correctQuestions = test.getCorrectAnswers();
			wrongQuestions = test.getWrongAswers();
			timeTakenString = DateUtils.getTimeTakenString(test.getTimeTaken(), false);
			skippedQuestions = test.getTotalQuestions() - (test.getCorrectAnswers() + test.getWrongAswers());
			totalPoints = test.getPoints();
		}

		map.put("correctQuestions", correctQuestions);
		map.put("incorrectQuestions", wrongQuestions);
		map.put("timeTakenString", timeTakenString);
		map.put("skippedQuestions", skippedQuestions);
		map.put("totalPoints", totalPoints);

		String questionUrlTitle = URLUtils.createQuestionTitle(questionDTO.getText());

		map.put("questionId", questionDTO.getId());
		map.put("questionUrlTitle", questionUrlTitle);

		return map;
	}

	private void saveSubmittedAnswers(MarathonTest test, Long questionId, List answerIds) {

		// for ease of use
		if (answerIds == null)
			answerIds = new LinkedList<>();

		Question question = rQuestion.findOne(questionId);

		// za svako pitanje belezimo sta je korisnik odgovorio
		MarathonTestUserResultQuestion resultQuestion = new MarathonTestUserResultQuestion();
		resultQuestion.setQuestion(question);
		resultQuestion.setTest(test);
		resultQuestion.setUserAnswers(new HashSet<QuestionAnswer>());
		resultQuestion.setHasAnswered(answerIds.size() > 0);

		boolean isCorrectlyAnswered = true;

		for (QuestionAnswer a : question.getQuestionAnswers()) {

			// answerIds gotten from client is a list of integers so for
			// comparison, we must convert answer id into integer
			Integer integerId = a.getId().intValue();

			boolean isAnsweredAsCorrect = answerIds.contains(integerId);

			// ako je korisnik izabrao ovaj odgovor belezimo ga
			if (isAnsweredAsCorrect)
				resultQuestion.getUserAnswers().add(a);

			if (a.getCorrect() != isAnsweredAsCorrect) {

				isCorrectlyAnswered = false;
			}
		}

		resultQuestion.setIsCorrect(isCorrectlyAnswered);

		// ubacujemo pitanje rezultata u rezultat
		test.getQuestionResults().add(resultQuestion);

		int correctlyAnswered = test.getCorrectAnswers();
		int incorrectlyAnswered = test.getWrongAswers();
		int points = test.getPoints();
		int totalQuestions = test.getTotalQuestions();

		if (resultQuestion.getHasAnswered()) {

			if (isCorrectlyAnswered)
				correctlyAnswered++;
			else
				incorrectlyAnswered++;

		} else {

			// nista, ne povecavamo brojace za tacno ili netacno
		}

		if (isCorrectlyAnswered)
			points += question.getPoints();

		totalQuestions++;

		test.setCorrectAnswers(correctlyAnswered);
		test.setWrongAswers(incorrectlyAnswered);
		test.setPoints(points);
		test.setTotalQuestions(totalQuestions);

		float correctPercent = correctlyAnswered * 1f / totalQuestions;

		test.setCorrectPercent(correctPercent);

		Date creationDate = test.getCreationDate();

		Integer timeTaken = (int) ((new Date().getTime() - creationDate.getTime()) / 1000);

		test.setTimeTaken(timeTaken);
	}

	private MarathonTest createTest(String fromString) {

		MarathonTest test = new MarathonTest();
		test.setUser(SecurityUtils.getUser());
		test.setCreationDate(new Date());
		test.setCorrectAnswers(0);
		test.setWrongAswers(0);
		test.setCorrectPercent(0f);
		test.setTotalQuestions(0);
		test.setPoints(0);
		test.setUid(fromString);
		test.setTimeTaken(0);

		test = rMarathon.save(test);

		mActivity.startedMarathonTest(test);

		return test;
	}

	public MarathonTestOverviewDTO getOverviewData() {

		MarathonTestOverviewDTO dto = new MarathonTestOverviewDTO();
		dto.setTests(new LinkedList<MarathonTestOverviewTestDTO>());

		User user = null;

		int totalTestsByCurrentUser = 0;
		int totalPrecisionByCurrentUser = 0;

		if (SecurityUtils.isLoggedIn()) {

			user = rUser.findOne(SecurityUtils.getUserId());

			// ukupan broj testova
			Long countForUser = rMarathon.countForUser(user);

			totalTestsByCurrentUser = countForUser != null ? countForUser.intValue() : 0;

			// ukupna tacnost
			Number precision = rMarathon.getUserPrecision(user);

			totalPrecisionByCurrentUser = precision != null ? precision.intValue() : 0;
		}

		Long totalTestsLong = rMarathon.count();
		int totalTests = totalTestsLong != null ? totalTestsLong.intValue() : 0;

		Long totalUsersLong = rMarathon.countUsers();
		int totalUsers = totalUsersLong != null ? totalUsersLong.intValue() : 0;

		Number totalPrecisionNumber = rMarathon.getPrecision();
		int totalPrecision = totalPrecisionNumber != null ? totalPrecisionNumber.intValue() : 0;

		dto.setTotalCorrectness(totalPrecision);
		dto.setTotalCorrectnessByCurrentUser(totalPrecisionByCurrentUser);
		dto.setTotalTestsByCurrentUser(totalTestsByCurrentUser);
		dto.setTotalTestsDone(totalTests);
		dto.setTotalUsers(totalUsers);

		// sada pravimo pregled skorasnjih N testova
		DateTime now = DateTime.now();
		Date date = now.minusDays(7).toDate();

		List<MarathonTest> bestTests = rMarathon.getBestRecentTests(date, new PageRequest(0, 50));

		for (MarathonTest test : bestTests) {

			MarathonTestOverviewTestDTO testDto = new MarathonTestOverviewTestDTO();
			testDto.setId(test.getId());
			testDto.setCorrectQuestions(test.getCorrectAnswers());
			testDto.setWrongQuestions(test.getWrongAswers());
			testDto.setTotalQuestions(test.getTotalQuestions());
			testDto.setTimeTaken(test.getTimeTaken());
			testDto.setUserId(test.getUser().getId());
			testDto.setUserName(test.getUser().getUsername());
			testDto.setProfileImagePath(test.getUser().getSmallProfileImagePath());
			testDto.setPoints(test.getPoints());

			dto.getTests().add(testDto);
		}

		return dto;
	}

	public List<MarathonTestUserResultDTO> getLast7DaysTopResults(int numOfResults) {

		DateTime now = DateTime.now();

		Date date = now.minusDays(7).toDate();

		List<Map<String, Object>> topResults = rMarathon.findTopResultsByDate(date, new PageRequest(0, numOfResults));

		List<MarathonTestUserResultDTO> dtos = createTopListDTOs(topResults);

		return dtos;
	}

	// TODO Ova funkcija je prepisana na 3 razlicita mesta
	private List<MarathonTestUserResultDTO> createTopListDTOs(List<Map<String, Object>> topResults) {

		List<MarathonTestUserResultDTO> dtos = new LinkedList<MarathonTestUserResultDTO>();

		int counter = 1;

		for (Map<String, Object> map : topResults) {

			User user = (User) map.get("user");
			Long points = (Long) map.get("points");
			Long correctAnswers = (Long) map.get("correctAnswers");
			Long incorrectAnswers = (Long) map.get("incorrectAnswers");
			Long timeTaken = (Long) map.get("timeTaken");

			if (points == null)
				points = 0l;

			if (correctAnswers == null)
				correctAnswers = 0l;

			if (incorrectAnswers == null)
				incorrectAnswers = 0l;

			if (timeTaken == null)
				timeTaken = 0l;

			Long totalAnswers = correctAnswers + incorrectAnswers;

			Long totalPercent = 0l;

			if (totalAnswers > 0)
				totalPercent = correctAnswers * 100 / totalAnswers;

			MarathonTestUserResultDTO dto = new MarathonTestUserResultDTO();
			dto.setPoints(points.intValue());
			dto.setTimeTaken(timeTaken.intValue());
			dto.setCorrectAnswers(correctAnswers.intValue());
			dto.setWrongAswers(incorrectAnswers.intValue());
			dto.setCorrectPercent(totalPercent.floatValue());

			dto.setUser(tUser.transformToDTO(user));

			dto.setOrderNumber(counter++);

			dtos.add(dto);
		}

		return dtos;
	}

	public int getTotalResultsRecently(Date date) {

		Long countLong = rMarathon.countTestResultsOnDateRange(date, new Date());

		int count = countLong != null ? countLong.intValue() : 0;

		return count;
	}
}
