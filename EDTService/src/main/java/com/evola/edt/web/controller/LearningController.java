package com.evola.edt.web.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.FormParam;

import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.component.DailyTestManager;
import com.evola.edt.component.MarathonManager;
import com.evola.edt.component.PageMetadataManager;
import com.evola.edt.component.RealTestManager;
import com.evola.edt.component.SessionStore;
import com.evola.edt.component.store.LearningStoreData;
import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.DailyTest;
import com.evola.edt.model.DailyTestUserResult;
import com.evola.edt.model.DailyTestUserResultQuestion;
import com.evola.edt.model.Question;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DailyTestUserResultDTO;
import com.evola.edt.model.dto.DailyTestUserResultQuestionDTO;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.MarathonTestUserResultDTO;
import com.evola.edt.model.dto.QuestionCategoryDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.RealTestUserResultDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.repository.DailyTestUserResultRepository;
import com.evola.edt.repository.DrivingCategoryRepository;
import com.evola.edt.repository.QuestionCategoryRepository;
import com.evola.edt.repository.QuestionMessageRepository;
import com.evola.edt.repository.QuestionRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.QuestionService;
import com.evola.edt.service.StatisticsService;
import com.evola.edt.service.dto.ActivityMessageDTO;
import com.evola.edt.service.dto.AllDailyTestsDTO;
import com.evola.edt.service.dto.AllQuestionsDTO;
import com.evola.edt.service.dto.LeaderboardDTO;
import com.evola.edt.service.dto.LearningQuestionsDTO;
import com.evola.edt.service.dto.QuestionDetailStatDTO;
import com.evola.edt.service.dto.QuestionMessagesDTO;
import com.evola.edt.service.dto.StatisticsDTO;
import com.evola.edt.service.dto.helpers.QuestionViewNavigationWrapper;
import com.evola.edt.service.dto.transformer.DailyTestUserResultQuestionDTOTransformer;
import com.evola.edt.service.dto.transformer.QuestionDTOTransformer;
import com.evola.edt.service.util.EdtTranslations;
import com.evola.edt.utils.DateUtils;
import com.evola.edt.utils.FormattingUtils;
import com.evola.edt.utils.URLUtils;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * @author Nikola 18.05.2013.
 * 
 */
@Controller
public class LearningController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	QuestionService service;

	@Inject
	StatisticsService statisticsService;

	@Inject
	PageMetadataManager pageManager;

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	DailyTestManager dailyTestManager;

	@Inject
	RealTestManager mRealTest;
	
	@Inject
	MarathonManager mMarathon;
	
	@Inject
	UserRepository userRepository;

	@Inject
	QuestionRepository rQuestion;

	@Inject
	QuestionDTOTransformer tQuestion;

	@Inject
	QuestionMessageRepository rQuestionMessage;

	@Inject
	DailyTestUserResultRepository dailyTestUserResultRepository;

	@Inject
	DailyTestUserResultQuestionDTOTransformer dailyTestUserResultQuestionDTOTransformer;

	@Inject
	ActivityManager mActivity;

	@Inject
	QuestionService sQuestion;

	@Inject
	SessionStore sessionStore;
	
	@Inject
	QuestionCategoryRepository rQuestionCategory;
	
	@Inject
	DrivingCategoryRepository rDrivingCategory;

	@RequestMapping(method = RequestMethod.GET, value = "/ucenje")
	public ModelAndView drivingSchoolProfile(@RequestParam(value = "startingIndex", required = false) Integer startingIndex, @RequestParam(value = "qcId", required = true) Long questionCategoryId, @RequestParam(value = "dcId", required = true) Long drivingCategoryId) {

		if (startingIndex == null || startingIndex < 0)
			startingIndex = 0;

		int count = 20;

		LearningQuestionsDTO dto = service.getQuestions(questionCategoryId, drivingCategoryId, startingIndex, count);

		QuestionCategoryDTO questionCategoryDTO = service.getQuestionCategory(questionCategoryId);
		DrivingCategoryDTO drivingCategoryDTO = service.getDrivingCategory(drivingCategoryId);

		String title = "Pitanja " + (startingIndex * count + 1) + " - " + (startingIndex * count + count);

		// sastavljamo listu N elemenata za navigaciju
		// trenutni index pokusavamo da stavimo na sredinu tog niza

		int totalPages = dto.getTotalPages();

		// treba da sastavimo listu strana za navigaciju
		int firstPageIndex = Math.max(startingIndex - 1, 0);
		int lastPageIndex = Math.min(startingIndex + 1, totalPages);
		int previousIndex = Math.max(startingIndex - 1, 0);
		int nextIndex = Math.min(startingIndex + 1, totalPages - 1);

		List<Integer> indices = createPageIndices(totalPages, startingIndex, 12);

		// cuvamo u sesiji, mozda nam zatreba na ostalim formama za ucenje
		sessionStore.saveLearning(startingIndex, questionCategoryId, drivingCategoryId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("listOfQuestionsForLearning");
		mav.addObject("questions", dto.getQuestions());
		mav.addObject("numberOfQuestions", dto.getTotalQuestions());
		mav.addObject("numberOfPages", totalPages);
		mav.addObject("isFirstPage", dto.getIsFirstPage());
		mav.addObject("isLastPage", dto.getIsLastPage());
		mav.addObject("qcId", questionCategoryId);
		mav.addObject("dcId", drivingCategoryId);
		mav.addObject("nextIndex", nextIndex);
		mav.addObject("previousIndex", previousIndex);
		mav.addObject("firstIndex", firstPageIndex);
		mav.addObject("lastIndex", lastPageIndex);
		mav.addObject("indices", indices);
		mav.addObject("currentIndex", startingIndex);
		mav.addObject("questionCategory", questionCategoryDTO != null ? questionCategoryDTO.getName() : "Sve");
		mav.addObject("drivingCategory", drivingCategoryDTO != null ? drivingCategoryDTO.getName() : "Sve");

		String fbTitle = title;

		if (questionCategoryDTO != null) {
			fbTitle += " - Kategorija pitanja: " + questionCategoryDTO.getName();
		} else {
			fbTitle += " - Kategorija pitanja: Sve";
		}

		if (drivingCategoryDTO != null) {
			fbTitle += " - Kategorija vozila: " + drivingCategoryDTO.getName();
		} else {
			fbTitle += " - Kategorija vozila: Sva";
		}
		pageManager.addLearningPage(mav, title, fbTitle);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/priprema-za-vozacki-ispit-testovi-aplikacija")
	public ModelAndView learningPage() {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("learningPage");

		pageManager.addAboutApplication(mav);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/priprema-za-vozacki-ispit-testovi")
	public ModelAndView prepareForLearning() {

		// vadimo kategorije vozila i pitanja
		AllQuestionsDTO drivingCategories = service.getDrivingCategories();
		AllQuestionsDTO questionCategories = service.getQuestionCategories();

		List<DrivingCategoryDTO> drivingCategoryDTOs = drivingCategories.getDrivingCategories();
		List<QuestionCategoryDTO> questionCategoryDTOs = questionCategories.getQuestionCategories();

		// dodajemo broj korisnika koji je danas radio test
		int todayResults = dailyTestManager.getTotalTestResultsOnDate(new Date());

		//dodajemo broj uradjenih testova posladnjih 7 dana
		int realTestResults = mRealTest.getTotalResultsRecently(DateMidnight.now().minusDays(7).toDate());
		
		//dodajemo broj uradjenih maratona poslednjih 7 dana
		int marathonTestResults = mMarathon.getTotalResultsRecently(DateMidnight.now().minusDays(7).toDate());
		
		// dodajemo broj korisnika koji je zadnjih 7 dana koristio aplikaciju
		int activeStudentsRecently = statisticsService.getNumberOfActiveStudentsRecently(7);

		List<ActivityMessageDTO> publicActivity = mActivity.getPublicActivity();

		LeaderboardDTO bestLearners = statisticsService.loadLeaderboard(SecurityUtils.getUserId(), 4);
		List<DailyTestUserResultDTO> bestCompetitors = dailyTestManager.getLast7DaysTopResults(5);
		List<RealTestUserResultDTO> bestRealTestCompetitors = mRealTest.getLast7DaysTopResults(5);
		List<MarathonTestUserResultDTO> bestMarathonCompetitors = mMarathon.getLast7DaysTopResults(5);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("prepareForLearning");
		mv.addObject("questionCategories", questionCategoryDTOs);
		mv.addObject("drivingCategories", drivingCategoryDTOs);
		mv.addObject("numberOfTodayDailyTestResults", todayResults);
		mv.addObject("numberOfActiveStudents", activeStudentsRecently);
		mv.addObject("activity", publicActivity);
		mv.addObject("bestLearners", bestLearners.getUsers());
		mv.addObject("bestCompetitors", bestCompetitors);
		mv.addObject("realTestResults", realTestResults);
		mv.addObject("realTestsBestCompetitors", bestRealTestCompetitors);
		mv.addObject("marathonTestsBestCompetitors", bestMarathonCompetitors);
		mv.addObject("marathonTestResults", marathonTestResults);

		pageManager.addPrepareForLearningPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/aktivni-ucenici")
	public ModelAndView recentActiveStudents() {

		StatisticsDTO recentActiveStudents = statisticsService.getRecentActiveStudents(7);

		ModelAndView mv = new ModelAndView();

		mv.setViewName("recentActiveStudents");

		mv.addObject("students", recentActiveStudents.getActiveUsers());
		mv.addObject("numOfStudents", recentActiveStudents.getActiveUsers().size());

		pageManager.addRecentlyActiveUsersPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/test-dana")
	@Transactional(readOnly = true)
	public ModelAndView dailyTest() {

		ModelAndView mv = new ModelAndView();

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();
		DailyTestUserResultDTO userResult = null;

		// izvlacimo podatke za trenutnog korisnika

		User user = null;

		if (isUserLoggedIn) {

			user = userRepository.findOne(SecurityUtils.getUserId());

			userResult = dailyTestManager.getTodayUserResults(user);
		}

		// izvlacimo dnevne rezultate
		DailyTest test = dailyTestManager.getTodayTest();

		List<DailyTestUserResultDTO> results = dailyTestManager.getTodayResults();

		// gledamo koji je po redu trenutni korisnik

		int orderNumber = dailyTestManager.getUserResultOrder(test, user, userResult);

		// izvlacimo i listu top 5 najboljih igraca ikad
		List<DailyTestUserResultDTO> topResults = dailyTestManager.getTopResults(5);

		// uzimamo ukupan broj takmicara koji se danas takmicio
		int totalUsersToday = dailyTestManager.getTotalUserCountOnDate(new Date());

		// uzimamo ukupan broj testova koji je danas uradjen
		int totalTestResultsToday = dailyTestManager.getTotalTestResultsOnDate(new Date());

		String todaysMessage = EdtTranslations.getDailyTestMessage(totalUsersToday, totalTestResultsToday);

		Date testDate = test != null ? test.getDate() : null;
		String username = user != null ? user.getUsername() : null;

		mv.setViewName("dailyTest");
		mv.addObject("isUserLoggedIn", isUserLoggedIn);
		mv.addObject("hasUserTakenTest", userResult != null);
		mv.addObject("userResult", userResult);
		mv.addObject("testIsTaken", results.size() > 0);
		mv.addObject("orderNumber", orderNumber);
		mv.addObject("results", results);
		mv.addObject("topResults", topResults);
		mv.addObject("testDate", testDate);
		mv.addObject("username", username);
		mv.addObject("totalUsersToday", totalUsersToday);
		mv.addObject("totalTestResultsToday", totalTestResultsToday);
		mv.addObject("todaysMessage", todaysMessage);

		pageManager.addDailyTestPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/test-dana/korisnici/{date}")
	@Transactional(readOnly = true)
	public ModelAndView dailyTestUsers(@PathVariable("date") @DateTimeFormat(iso = ISO.DATE) Date date) {

		ModelAndView mv = new ModelAndView();

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		DailyTestUserResultDTO userResult = null;

		DailyTest test = dailyTestManager.getTestByDate(date);

		if (test == null)
			throw new RuntimeException("Ne postoji test za prosledjeni datum.");

		User user = null;

		if (isUserLoggedIn) {

			user = userRepository.findOne(SecurityUtils.getUserId());

			userResult = dailyTestManager.getUserResults(user, test);
		}

		List<DailyTestUserResultDTO> results = dailyTestManager.getResults(test);

		// gledamo koji je po redu trenutni korisnik

		int orderNumber = dailyTestManager.getUserResultOrder(test, user, userResult);

		Date testDate = test != null ? test.getDate() : null;
		String username = user != null ? user.getUsername() : null;

		// formatiranje datuma testa
		String dateString = FormattingUtils.formatDateSimple(test.getDate());

		String title = "Rezultati za test dana - " + dateString;

		mv.setViewName("dailyTestUsers");
		mv.addObject("isUserLoggedIn", isUserLoggedIn);
		mv.addObject("hasUserTakenTest", userResult != null);
		mv.addObject("userResult", userResult);
		mv.addObject("testIsTaken", results.size() > 0);
		mv.addObject("orderNumber", orderNumber);
		mv.addObject("results", results);
		mv.addObject("testDate", testDate);
		mv.addObject("username", username);
		mv.addObject("title", title);

		pageManager.addDailyTestUsersPage(mv, dateString);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/test-dana/top-lista")
	@Transactional(readOnly = true)
	public ModelAndView dailyTestTopList() {

		ModelAndView mv = new ModelAndView();

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		// ukupan broj korisnika za prikaz
		int numOfResults = 500;

		// izvlacimo i listu top 500 najboljih igraca ikad
		List<DailyTestUserResultDTO> topResults = dailyTestManager.getTopResults(numOfResults);

		// gledamo koji je po redu trenutni korisnik

		int orderNumber = 0;

		if (isUserLoggedIn) {

			Long userId = SecurityUtils.getUserId();

			for (int i = 0; i < topResults.size(); i++) {

				UserDTO user = topResults.get(i).getUser();

				if (user != null && userId.equals(user.getId())) {

					orderNumber = i + 1;
					break;
				}
			}
		}

		mv.setViewName("dailyTestTopList");
		mv.addObject("isUserLoggedIn", isUserLoggedIn);
		mv.addObject("orderNumber", orderNumber);
		mv.addObject("results", topResults);
		mv.addObject("numberOfResults", numOfResults);
		mv.addObject("isUserInTopList", orderNumber != 0);
		mv.addObject("orderNumber", orderNumber);

		pageManager.addDailyTestTopListPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/test-dana/polaganje")
	@Transactional
	public ModelAndView doDailyTestWithDate() {

		// ova metoda se koristi za danasnji test i podrazumeva se danasnji
		// datum

		return doDailyTestWithDate(new Date());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/test-dana/polaganje/{date}")
	@Transactional
	public ModelAndView doDailyTestWithDate(@PathVariable("date") @DateTimeFormat(iso = ISO.DATE) Date testDate) {

		// za ovu metodu znamo da dobija datum, treba da nadjemo test i da ga
		// obradimo
		// ako test ne postoji, vracamo stranicu na kojoj dobija tu informaciju

		DailyTest test = dailyTestManager.getTestByDate(testDate);

		// ako je test null a datum je danasnji onda pustamo jer ce biti
		// napravljen novi test

		if (test == null && !DateUtils.isToday(testDate))
			throw new RuntimeException("Ne postoji test za datum " + testDate.toString());

		return doDailyTest(test != null ? test.getId() : null);
	}

	private ModelAndView doDailyTest(Long testId) {

		ModelAndView mv = new ModelAndView();

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();
		DailyTestUserResultDTO userResult = null;

		// izvlacimo podatke za trenutnog korisnika

		DailyTest test = null;

		if (testId != null)
			test = dailyTestManager.getTest(testId);

		// ako nismo pronasli test idemo na danasnji
		if (test == null)
			test = dailyTestManager.getTodayTest();

		// ako i dalje nema testa onda ga pravimo
		if (test == null) {

			dailyTestManager.createTest();

			test = dailyTestManager.getTodayTest();
		}

		if (isUserLoggedIn) {

			User user = userRepository.findOne(SecurityUtils.getUserId());

			if (test != null)
				userResult = dailyTestManager.getUserResults(user, test);
			else
				userResult = dailyTestManager.getTodayUserResults(user);
		}

		Date date = test.getDate();
		String title = null;

		if (DateUtils.isToday(date)) {

			title = "Današnji test";
		} else {

			title = "Test za datum " + FormattingUtils.formatDateSimple(date);
		}

		// izvlacimo pitanja
		List<QuestionDTO> dtos = dailyTestManager.getQuestionDTOs(test.getId());

		mv.setViewName("doDailyTest");
		mv.addObject("isUserLoggedIn", isUserLoggedIn);
		mv.addObject("hasUserTakenTest", userResult != null);
		mv.addObject("questions", dtos);
		mv.addObject("startTime", System.currentTimeMillis());
		mv.addObject("testId", test.getId());
		mv.addObject("title", title);
		mv.addObject("numberOfQuestions", dtos.size());

		pageManager.addDoDailyTestPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/test-dana/svi-testovi")
	@Transactional
	public ModelAndView allDailyTests() {

		ModelAndView mv = new ModelAndView();

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		User currentUser = null;

		// izvlacimo podatke za trenutnog korisnika
		if (isUserLoggedIn)
			currentUser = userRepository.findOne(SecurityUtils.getUserId());

		AllDailyTestsDTO dto = dailyTestManager.getAllTests(currentUser);

		mv.setViewName("allDailyTests");
		mv.addObject("isUserLoggedIn", isUserLoggedIn);
		mv.addObject("result", dto);
		mv.addObject("hasUserTakenTests", dto.getTotalTestsByCurrentUser() > 0);
		mv.addObject("username", currentUser != null ? currentUser.getUsername() : null);

		pageManager.addAllDailyTestsPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/test-dana/rezultat/{date}/{username}")
	@Transactional
	public ModelAndView showDailyTestResult(@PathVariable("date") @DateTimeFormat(iso = ISO.DATE) Date date, @PathVariable("username") String username) {

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		// izvlace se test i korisnik
		DailyTest test = dailyTestManager.getTestByDate(date);
		User user = userRepository.findByUsername(username);

		// ako ne postoji test bacamo gresku
		if (test == null)
			throw new RuntimeException("Ne postoji test za izabrani datum");

		DailyTestUserResultDTO userResult = dailyTestManager.getUserResults(user, test);

		int orderNumber = dailyTestManager.getUserResultOrder(test, user, userResult);

		// formatiranje datuma testa
		String dateString = FormattingUtils.formatDateSimple(test.getDate());

		// ako korisnik nije ulogovan ne dozvoljavamo mu da gleda detaljne
		// rezultate ali moze da pogleda sumarne
		if (!isUserLoggedIn) {

			ModelAndView mv = new ModelAndView();

			mv.setViewName("dailyTestUserResultNotLoggedIn");

			mv.addObject("title", "Rezultat za test dana - " + dateString);
			mv.addObject("orderNumber", orderNumber);
			mv.addObject("userResult", userResult);
			mv.addObject("username", user.getUsername());

			pageManager.addDailyTestUserResultWhenNotLoggedIn(mv, user.getUsername(), dateString);

			return mv;
		}

		// ako korisnik jeste polagao, idemo dalje i prikazujemo drugacije
		// stranice

		User currentUser = userRepository.findOne(SecurityUtils.getUserId());

		// ako korisnik nije polagao test onda prikazujemo stranicu prilagodjenu
		// tom stanju
		DailyTestUserResultDTO currentUserResults = dailyTestManager.getUserResults(currentUser, test);

		if (currentUserResults == null) {

			ModelAndView mv = new ModelAndView();

			mv.setViewName("dailyTestUserResultNotTakenTest");

			mv.addObject("title", "Rezultat za test dana - " + dateString);
			mv.addObject("orderNumber", orderNumber);
			mv.addObject("userResult", userResult);
			mv.addObject("username", user.getUsername());
			mv.addObject("testId", test.getId());

			pageManager.addDailyTestUserResultWhenNotLoggedIn(mv, user.getUsername(), dateString);

			return mv;
		}

		// ako smo ovde onda znamo da je trenutni korisnik ulogovan i da je vec
		// odradio ovaj test tako da mozemo
		// prikazati pune rezultate za izabranog korisnika

		// izvlaci se lista pitanja iz testa
		List<QuestionDTO> questionDtos = dailyTestManager.getQuestionDTOs(test.getId());

		// dohvataju se rezultati za korisnika
		DailyTestUserResult dailyTestUserResult = dailyTestUserResultRepository.findByUserAndTest(user, test);

		// u listu se smesataju rezultati odgovaranja zajedno sa pitanjima i oni
		// se vracaju kao odgovor na stranicu
		List<DailyTestUserResultQuestionDTO> dailyTestUserResultQuestion = new LinkedList<DailyTestUserResultQuestionDTO>();

		for (DailyTestUserResultQuestion resultQuestion : dailyTestUserResult.getQuestionResults()) {

			// transformacija resultQuestion-a u resultQuestionDTO (odgovor
			// korisnika)
			DailyTestUserResultQuestionDTO resultQuestionDTO = dailyTestUserResultQuestionDTOTransformer.transformToDTO(resultQuestion, "userAnswers");

			// u resultQuestionDTO (odgovor korisnika) dodaje se pravo pitanje
			// (koje pronalazi posebna metoda iz listi)
			resultQuestionDTO.setQuestion(dailyTestManager.getQuestionDTO(questionDtos, resultQuestion.getQuestion()));

			dailyTestUserResultQuestion.add(resultQuestionDTO);
		}

		// sortiranje odgovora kako bi raspored pitanja odgovarao tesu
		dailyTestUserResultQuestion = dailyTestManager.sortResponse(dailyTestUserResultQuestion, questionDtos);

		// ispisujemo razlicite poruke u zavisnosti od toga da li je rezultat za
		// trenutno ulogovanog usera
		// ili nekog drugog usera

		String message = null;

		if (user.equals(currentUser))
			message = "Trenutno si " + orderNumber + ". na ovom testu!";
		else
			message = user.getUsername() + " je trenutno " + orderNumber + " na ovom testu!";

		ModelAndView mv = new ModelAndView();

		mv.setViewName("dailyTestUserResult");

		mv.addObject("title", "Rezultat za test dana - " + dateString);
		mv.addObject("orderNumber", orderNumber);
		mv.addObject("userResult", userResult);
		mv.addObject("dailyTestResults", dailyTestUserResultQuestion);
		mv.addObject("numberOfQuestions", questionDtos.size());
		mv.addObject("message", message);

		pageManager.addDailyTestUserResult(mv, user.getUsername(), dateString);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/online-ucenje")
	@Transactional
	public String learningApp() {

		return "learningAppPage";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ucenje/pitanja/{questionId}")
	@Transactional
	public Object questionView(@PathVariable("questionId") Long questionId) {

		return questionView(questionId, null, 1);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ucenje/pitanja/{questionId}/{questionTitle}")
	@Transactional
	public Object questionView(@PathVariable("questionId") Long questionId, @PathVariable("questionTitle") String questionTitle) {

		return questionView(questionId, questionTitle, 1);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ucenje/pitanja/{questionId}/{questionTitle}/{pageNumber}")
	@Transactional
	public Object questionView(@PathVariable("questionId") Long questionId, @PathVariable("questionTitle") String questionTitle, @PathVariable("pageNumber") Integer pageNumber) {

		// id pitanja mora da se slaze sa naslovom pitanja,
		// ukoliko se ne slaze, radimo redirekciju

		if (pageNumber == null || pageNumber < 1)
			pageNumber = 1;

		QuestionDTO question = sQuestion.getQuestion(questionId);

		String correctQuestionTitle = URLUtils.createQuestionTitle(question.getText());

		if (correctQuestionTitle == null || !correctQuestionTitle.equals(questionTitle)) {

			String redirect = "redirect:/ucenje/pitanja/" + questionId + "/" + correctQuestionTitle;

			if (pageNumber > 1)
				redirect += "/" + pageNumber;

			return redirect;
		}

		LearningStoreData loadLearning = sessionStore.loadLearning();

		// za gui treba nam pitanje i njegove diskusije

		QuestionMessagesDTO questionsMessages = sQuestion.getQuestionsMessages(questionId, pageNumber - 1);

		List<Integer> indices = FormattingUtils.createPageIndices(questionsMessages.getTotalPages(), pageNumber - 1);

		// treba da sastavimo listu strana za navigaciju
		int previousIndex = Math.max(pageNumber - 1, 0);
		int nextIndex = Math.min(pageNumber + 1, questionsMessages.getTotalPages());

		// izvlacimo i broj ucenja ovog pitanja, broj testiranja i njegovu
		// tezinu
		QuestionDetailStatDTO statDTO = sQuestion.getQuestionDetailsStat(questionId);

		Long questionsCategoryId = 0l;
		Long drivingCategoryId = 0l;
		Integer startingIndex = 0;
		int count = 20;

		if (loadLearning != null) {

			questionsCategoryId = loadLearning.getQuestionCategoryId();
			drivingCategoryId = loadLearning.getDrivingCategoryId();
			startingIndex = loadLearning.getStartIndex();
		}

		QuestionViewNavigationWrapper wrapper = sQuestion.getPreviousAndNextQuestions(questionId, questionsCategoryId, drivingCategoryId, startingIndex, count);

		Question previousQuestion = wrapper.getPreviousQuestion();
		Question nextQuestion = wrapper.getNextQuestion();

		String questionCategoryName = "Sve";
		String drivingCategoryName = "Sve";
		
		if(questionsCategoryId > 0)
			questionCategoryName = rQuestionCategory.findOne(questionsCategoryId).getName();
		
		if(drivingCategoryId > 0)
			drivingCategoryName = rDrivingCategory.findOne(drivingCategoryId).getName();
		
		// moguce je da nema prethodnog i narednog pitanja ako smo stigli u
		// pitanje koje nije u trazenim kategorijama
		if (previousQuestion == null || nextQuestion == null) {

			ModelAndView mv = new ModelAndView("invalidQuestionView");
			
			mv.addObject("numberOfQuestions", wrapper.getTotalQuestions());
			mv.addObject("numberOfPages", wrapper.getTotalPages());
			mv.addObject("currentPageIndex", wrapper.getCurrentPageIndex());
			mv.addObject("questionCategory", questionCategoryName);
			mv.addObject("drivingCategory", drivingCategoryName);
			
			mv.addObject("questionCategoryName", questionCategoryName);
			mv.addObject("drivingCategoryName", drivingCategoryName);
			
			pageManager.addInvalidQuestionView(mv);
			
			return mv;
		}

		// ako smo navigirajuci kroz pitanja presli na neku drugu stranicu, to
		// cuvamo ujedno i u sesiji
		if (startingIndex != wrapper.getCurrentPageIndex()) {

			sessionStore.saveLearning(wrapper.getCurrentPageIndex(), questionsCategoryId, drivingCategoryId);
		}

		// pravimo link ka odgovarajucoj celoj lekciji
		String lessonLink = "/ucenje?qcId=" + questionsCategoryId + "&dcId=" + drivingCategoryId;

		String lessonTitle = "Pitanja " + (wrapper.getCurrentPageIndex() * count + 1) + " - " + (wrapper.getCurrentPageIndex() * count + count);

		if (wrapper.getCurrentPageIndex() > 0)
			lessonLink = lessonLink += "&startingIndex=" + wrapper.getCurrentPageIndex();

		String previousQuestionLink = "/ucenje/pitanja/" + previousQuestion.getId() + "/" + URLUtils.createQuestionTitle(previousQuestion.getText());
		String nextQuestionLink = "/ucenje/pitanja/" + nextQuestion.getId() + "/" + URLUtils.createQuestionTitle(nextQuestion.getText());

		ModelAndView mv = new ModelAndView("questionView");
		mv.addObject("question", question);
		mv.addObject("title", "Pregled pitanja br. " + question.getId());
		mv.addObject("messages", questionsMessages);
		mv.addObject("questionTextUrl", correctQuestionTitle);
		mv.addObject("stat", statDTO);
		mv.addObject("previousQuestionLink", previousQuestionLink);
		mv.addObject("nextQuestionLink", nextQuestionLink);
		mv.addObject("lessonLink", lessonLink);
		mv.addObject("lessonTitle", lessonTitle);

		// koristi se za paginaciju
		mv.addObject("isFirstPage", pageNumber == 1);
		mv.addObject("isLastPage", pageNumber == questionsMessages.getTotalPages());
		mv.addObject("numberOfSchools", questionsMessages.getTotalMessages());
		mv.addObject("nextIndex", nextIndex);
		mv.addObject("previousIndex", previousIndex);
		mv.addObject("firstIndex", 0);
		mv.addObject("lastIndex", questionsMessages.getTotalPages());
		mv.addObject("indices", indices);
		mv.addObject("currentIndex", pageNumber);
		
		mv.addObject("numberOfQuestions", wrapper.getTotalQuestions());
		mv.addObject("numberOfPages", wrapper.getTotalPages());
		mv.addObject("currentPageIndex", wrapper.getCurrentPageIndex());
		mv.addObject("questionCategory", questionCategoryName);
		mv.addObject("drivingCategory", drivingCategoryName);

		pageManager.addQuestionView(mv, question.getText());

		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveQuestionMessage")
	public String saveNotificationComment(@FormParam("questionId") Long questionId, @FormParam("message") String message) {

		credentialsManager.checkUserLoggedIn();

		sQuestion.saveQuestionMessage(SecurityUtils.getUserId(), questionId, message, null);

		Question question = rQuestion.findOne(questionId);

		String urlTitle = URLUtils.createQuestionTitle(question.getText());

		QuestionMessagesDTO questionMessagesDTO = sQuestion.getQuestionsMessages(questionId, 0);

		// saljemo korisnika na poslednju stranicu u diskusijama kako bi mogao
		// da vidi svoju poruku
		String redirect = "redirect:/ucenje/pitanja/" + questionId + "/" + urlTitle;

		Integer pageNumber = questionMessagesDTO.getTotalPages();

		if (pageNumber > 1)
			redirect += "/" + pageNumber;

		return redirect;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/deleteQuestionMessage")
	@Transactional
	public String saveNotificat1ionComment(@FormParam("messageId") Long messageId, @FormParam("pageNumber") Integer pageNumber) {

		credentialsManager.checkUserLoggedIn();

		Question question = sQuestion.getQuestionByQuestionMessage(messageId);

		sQuestion.deleteQuestionMessage(SecurityUtils.getUserId(), messageId);

		String urlTitle = URLUtils.createQuestionTitle(question.getText());

		String redirect = "redirect:/ucenje/pitanja/" + question.getId() + "/" + urlTitle;

		if (pageNumber != null && pageNumber > 1)
			redirect += "/" + pageNumber;

		return redirect;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ucenje/izbor")
	@Transactional
	public Object learningSelection() {

		// vadimo kategorije vozila i pitanja
		AllQuestionsDTO drivingCategories = service.getDrivingCategories();
		AllQuestionsDTO questionCategories = service.getQuestionCategories();

		List<DrivingCategoryDTO> drivingCategoryDTOs = drivingCategories.getDrivingCategories();
		List<QuestionCategoryDTO> questionCategoryDTOs = questionCategories.getQuestionCategories();

		// moramo dodati po prazan element za 'sve'
		DrivingCategoryDTO allDCdto = new DrivingCategoryDTO();
		allDCdto.setId(0l);
		allDCdto.setName("Sve");
		drivingCategoryDTOs.add(0, allDCdto);

		QuestionCategoryDTO allQCdto = new QuestionCategoryDTO();
		allQCdto.setId(0l);
		allQCdto.setName("Sve");
		questionCategoryDTOs.add(0, allQCdto);

		// vadimo predefinisane vrednosti
		Long questionCategoryId = 0l;
		Long drivingCategoryId = 0l;

		LearningStoreData loadLearning = sessionStore.loadLearning();

		if (loadLearning != null) {

			questionCategoryId = loadLearning.getQuestionCategoryId();
			drivingCategoryId = loadLearning.getDrivingCategoryId();
		} else if (credentialsManager.isUserLoggedIn()) {

			// mozda je user ulogovan pa mozemo izvuci info iz profila
			User user = userRepository.findOne(SecurityUtils.getUserId());

			if (user.getDrivingCategory() != null) {

				drivingCategoryId = user.getDrivingCategory().getId();
			}
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("learningSelection");
		mv.addObject("questionCategories", questionCategoryDTOs);
		mv.addObject("drivingCategories", drivingCategoryDTOs);
		mv.addObject("selectedQuestionCategoryId", questionCategoryId);
		mv.addObject("selectedDrivingCategoryId", drivingCategoryId);

		pageManager.addLearningSelectionPage(mv);

		return mv;
	}

	private List<Integer> createPageIndices(int totalPages, int currentIndex, int numberOfIndices) {

		List<Integer> indices = new LinkedList<Integer>();

		int totalIndices = Math.min(totalPages, numberOfIndices);

		int startingIndex = Math.max(currentIndex - totalIndices / 2, 0);

		// sada nam je index dobro poravnat u levo ali mozda ne i u desno
		if (startingIndex + totalIndices > totalPages)
			startingIndex = totalPages - totalIndices;

		int i = startingIndex;

		while (totalIndices-- > 0) {

			indices.add(i++);
		}

		return indices;
	}

}
