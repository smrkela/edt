package com.evola.edt.service.rest.mobile;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.component.DailyTestManager;
import com.evola.edt.model.DailyTest;
import com.evola.edt.model.DailyTestUserResult;
import com.evola.edt.model.DailyTestUserResultQuestion;
import com.evola.edt.model.Question;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DailyTestUserResultDTO;
import com.evola.edt.model.dto.DailyTestUserResultQuestionDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.repository.DailyTestUserResultRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.AllDailyTestsDTO;
import com.evola.edt.service.dto.MobileDailyTestCompleteResultDTO;
import com.evola.edt.service.dto.MobileDailyTestOverviewDTO;
import com.evola.edt.service.dto.MobileDailyTestTopListDTO;
import com.evola.edt.service.dto.MobileDailyTestUserResultDTO;
import com.evola.edt.service.dto.MobilePerformDailyTestDTO;
import com.evola.edt.service.dto.helpers.DailyTestDTO;
import com.evola.edt.service.dto.transformer.DailyTestUserResultQuestionDTOTransformer;
import com.evola.edt.service.rest.AbstractRestService;
import com.evola.edt.utils.DateUtils;
import com.evola.edt.utils.FormattingUtils;
import com.evola.edt.web.controller.LearningController;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

@Path("/mobile/dailyTest")
@Named
public class DailyTestRestService extends AbstractRestService {

	@Inject
	private DailyTestManager dailyTestManager;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserCredentialsManager credentialsManager;
	
	@Inject
	private DailyTestUserResultRepository dailyTestUserResultRepository;
	
	@Inject
	private DailyTestUserResultQuestionDTOTransformer dailyTestUserResultQuestionDTOTransformer;

	@GET
	@Path("/getDailyTestOverview")
	@Produces("application/xml")
	public MobileDailyTestOverviewDTO getDailyTestOverview() {

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		MobileDailyTestOverviewDTO dto = new MobileDailyTestOverviewDTO();

		if (!isUserLoggedIn)
			return dto;

		User user = userRepository.findOne(SecurityUtils.getUserId());

		DailyTestUserResultDTO userResult = dailyTestManager.getTodayUserResults(user);

		// izvlacimo dnevne rezultate
		DailyTest test = dailyTestManager.getTodayTest();

		List<DailyTestUserResultDTO> results = dailyTestManager.getTodayResults();

		int orderNumber = dailyTestManager.getUserResultOrder(test, user, userResult);

		Date testDate = test != null ? test.getDate() : null;
		String username = user != null ? user.getUsername() : null;

		boolean isTodayTestDone = userResult != null;
		int totalTestsDone = dailyTestManager.getTotalUserTestsCount(user);
		int totalTests = dailyTestManager.getTotalTestsCount();
		int totalPointsEarned = dailyTestManager.getTotalUserPoints(user);
		int todayTestPoints = userResult != null ? userResult.getPoints() : -1;
		int todayTestPosition = orderNumber;
		int currentPosition = dailyTestManager.gotTotalUserPosition(user);
		int totalTestUsers = dailyTestManager.getTotalUserCount();
		int totalTodayTestUsers = results.size();

		dto.setCurrentPosition(currentPosition);
		dto.setTodayTestDone(isTodayTestDone);
		dto.setTodayTestPoints(todayTestPoints);
		dto.setTodayTestPosition(todayTestPosition);
		dto.setTotalPointsEarned(totalPointsEarned);
		dto.setTotalTests(totalTests);
		dto.setTotalTestsDone(totalTestsDone);
		dto.setTotalTestUsers(totalTestUsers);
		dto.setTotalTodayTestUsers(totalTodayTestUsers);
		dto.setTodayTestId(test != null ? test.getId()+"" : null);
		
		List<MobileDailyTestUserResultDTO> mobileResults = new LinkedList<MobileDailyTestUserResultDTO>();
		
		for (int i = 0; i < results.size(); i++) {

			mobileResults.add(transform(results.get(i)));
		}
		
		dto.setTodayResults(mobileResults);
		
		//ubacujemo i listu sa danasnjim rezultatima
		

		return dto;
	}

	@GET
	@Path("/getTestsList")
	@Produces("application/xml")
	public AllDailyTestsDTO getTestsList() {

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		if (!isUserLoggedIn)
			return null;

		User currentUser = userRepository.findOne(SecurityUtils.getUserId());

		AllDailyTestsDTO dto = dailyTestManager.getAllTests(currentUser);

		return dto;
	}

	@GET
	@Path("/getTopList")
	@Produces("application/xml")
	public MobileDailyTestTopListDTO getTopList() {

		MobileDailyTestTopListDTO dto = new MobileDailyTestTopListDTO();

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		if (!isUserLoggedIn)
			return dto;

		// ukupan broj korisnika za prikaz
		int numOfResults = 200;

		// izvlacimo i listu top 500 najboljih igraca ikad
		List<DailyTestUserResultDTO> topResults = dailyTestManager.getTopResults(numOfResults);

		List<MobileDailyTestUserResultDTO> results = new LinkedList<MobileDailyTestUserResultDTO>();

		// gledamo koji je po redu trenutni korisnik

		int orderNumber = 0;

		Long userId = SecurityUtils.getUserId();

		for (int i = 0; i < topResults.size(); i++) {

			UserDTO user = topResults.get(i).getUser();

			if (user != null && userId.equals(user.getId())) {

				orderNumber = i + 1;
			}

			results.add(transform(topResults.get(i)));
		}

		dto.setOrderNumber(orderNumber);
		dto.setUsers(results);
		dto.setIsUserInTopList(orderNumber != 0);

		return dto;
	}

	@GET
	@Path("/getQuestions")
	@Produces("application/xml")
	public MobilePerformDailyTestDTO getQuestions(@QueryParam(value = "testId") Long testId) {

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		if (!isUserLoggedIn)
			return null;

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

		User user = userRepository.findOne(SecurityUtils.getUserId());
		DailyTestUserResultDTO userResult = null;

		if (test != null)
			userResult = dailyTestManager.getUserResults(user, test);
		else
			userResult = dailyTestManager.getTodayUserResults(user);

		Date date = test.getDate();

		// izvlacimo pitanja
		List<QuestionDTO> dtos = dailyTestManager.getQuestionDTOs(test.getId());

		MobilePerformDailyTestDTO dto = new MobilePerformDailyTestDTO();

		dto.setHasUserTakenTest(userResult != null);
		dto.setQuestions(dtos);
		dto.setStartTime(System.currentTimeMillis());
		dto.setTestId(test.getId());
		dto.setNumberOfQuestions(dtos.size());
		dto.setDate(date);
		dto.setIsToday(DateUtils.isToday(date));

		return dto;
	}

	@GET
	@Path("/getResults")
	@Produces("application/xml")
	public MobileDailyTestCompleteResultDTO getResults(@QueryParam(value = "testId") Long testId, @QueryParam(value = "userId") Long userId) {

		credentialsManager.checkUserLoggedIn();

		// userid nije neophodno, ako se ne posalje uzima se trenutni user
		if (userId == null)
			userId = SecurityUtils.getUserId();

		// izvlace se test i korisnik
		DailyTest test = dailyTestManager.getTest(testId);
		User user = userRepository.findById(userId);

		// ako ne postoji test bacamo gresku
		if (test == null)
			throw new RuntimeException("Ne postoji test za izabrani datum");

		DailyTestUserResultDTO userResult = dailyTestManager.getUserResults(user, test);

		int orderNumber = dailyTestManager.getUserResultOrder(test, user, userResult);

		// ako korisnik jeste polagao, idemo dalje i prikazujemo drugacije
		// stranice

		User currentUser = userRepository.findOne(SecurityUtils.getUserId());

		// ako korisnik nije polagao test onda prikazujemo stranicu prilagodjenu
		// tom stanju
		DailyTestUserResultDTO currentUserResults = dailyTestManager.getUserResults(currentUser, test);

		if (currentUserResults == null) {
			
			MobileDailyTestCompleteResultDTO dto = new MobileDailyTestCompleteResultDTO();
			dto.setOrderNumber(orderNumber);
			dto.setUserResult(userResult);
			dto.setUsername(user.getUsername());
			dto.setTestId(test.getId());
			dto.setHasCurrentUserTakenTest(false);

			return dto;
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
			DailyTestUserResultQuestionDTO resultQuestionDTO = dailyTestUserResultQuestionDTOTransformer.transformToDTO(resultQuestion,
					"userAnswers");

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

		MobileDailyTestCompleteResultDTO dto = new MobileDailyTestCompleteResultDTO();
		dto.setOrderNumber(orderNumber);
		dto.setUserResult(userResult);
		dto.setHasCurrentUserTakenTest(true);
		dto.setTestId(testId);
		dto.setUsername(user.getUsername());
		dto.setDailyTestResultQuestions(dailyTestUserResultQuestion);
		dto.setDate(test.getDate());

		return dto;
	}
	
	private MobileDailyTestUserResultDTO transform(DailyTestUserResultDTO rDTO) {

		MobileDailyTestUserResultDTO dto = new MobileDailyTestUserResultDTO();
		dto.setCorrectAnswers(rDTO.getCorrectAnswers());
		dto.setCorrectPercent(rDTO.getCorrectPercent());
		dto.setCreationDate(rDTO.getCreationDate());
		dto.setNumberOfTests(rDTO.getNumberOfTests());
		dto.setOrderNumber(rDTO.getOrderNumber());
		dto.setPoints(rDTO.getPoints());
		dto.setTimeTaken(rDTO.getTimeTaken());
		dto.setTimeTakenString(rDTO.getTimeTakenString());
		dto.setUserId(rDTO.getUser().getId());
		dto.setUsername(rDTO.getUser().getUsername());
		dto.setWrongAswers(rDTO.getWrongAswers());
		dto.setTimeTakenShortString(rDTO.getTimeTakenShortString());

		return dto;
	}

}
