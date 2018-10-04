package com.evola.edt.web.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;

import org.joda.time.DateMidnight;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.component.PageMetadataManager;
import com.evola.edt.component.RealTestManager;
import com.evola.edt.model.RealTest;
import com.evola.edt.model.RealTestCategory;
import com.evola.edt.model.RealTestUserResult;
import com.evola.edt.model.RealTestUserResultQuestion;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DailyTestUserResultQuestionDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.RealTestUserResultDTO;
import com.evola.edt.model.dto.RealTestUserResultQuestionDTO;
import com.evola.edt.repository.RealTestUserResultRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.helpers.RealTestCreationDTO;
import com.evola.edt.service.dto.helpers.RealTestOverviewDTO;
import com.evola.edt.service.dto.transformer.RealTestUserResultQuestionDTOTransformer;
import com.evola.edt.utils.FormattingUtils;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.utils.URLUtils;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class RealTestsController {

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	RealTestManager mRealTest;

	@Inject
	PageMetadataManager mPage;

	@Inject
	UserRepository rUser;
	
	@Inject
	RealTestUserResultRepository rUserResult;

	@Inject
	RealTestUserResultQuestionDTOTransformer tUserResultQuestion;
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija/testovi")
	public ModelAndView administration() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		List<RealTest> tests = mRealTest.getAllTests();

		List<RealTestCategory> categories = mRealTest.getAllCategories();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("administrationRealTests");
		mv.addObject("tests", tests);
		mv.addObject("categories", categories);
		mv.addObject("pageInfo", new PageInfo("testsPage", "Administracija testova", PageCategories.ADMINISTRACIJA));

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/testovi/dodaj")
	public ModelAndView administration(@RequestParam("c") Long categoryId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		RealTestCreationDTO dto = mRealTest.getCreationData(categoryId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("administrationCreateRealTest");
		mv.addObject("data", dto);
		mv.addObject("pageInfo", new PageInfo("testsPage", "Administracija testova", PageCategories.ADMINISTRACIJA));

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/testovi/izmeni")
	public ModelAndView update(@RequestParam("id") Long testId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		RealTestCreationDTO dto = mRealTest.getUpdateData(testId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("administrationCreateRealTest");
		mv.addObject("data", dto);
		mv.addObject("pageInfo", new PageInfo("testsPage", "Administracija testova", PageCategories.ADMINISTRACIJA));

		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija/testovi/obrisi")
	public ModelAndView delete(@RequestParam("id") Long testId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		mRealTest.delete(testId);

		return administration();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/administracija/testovi/sacuvaj")
	@Consumes("application/json")
	public ModelAndView administration(RealTestCreationDTO dto) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		// RealTestCreationDTO dto = mRealTest.getCreationData(categoryId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("administrationCreateRealTest");
		mv.addObject("data", dto);
		mv.addObject("pageInfo", new PageInfo("testsPage", "Administracija testova", PageCategories.ADMINISTRACIJA));

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/testovi")
	public ModelAndView tests() {

		// we need: all tests grouped by categories

		RealTestOverviewDTO dto = mRealTest.getOverviewData();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("realTestsOverview");
		mv.addObject("data", dto);
		mv.addObject("selectedCategoryId", 2); //za B kategoriju
		mv.addObject("isUserLoggedIn", SecurityUtils.isLoggedIn());
		mv.addObject("hasUserTakenTests", dto.getTotalTestsByCurrentUser() > 0);
		mv.addObject("pageInfo", new PageInfo("testovi", "Testovi", PageCategories.UCENJE));

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/testovi/polaganje/{testId}")
	@Transactional
	public ModelAndView doTest(@PathVariable("testId") Long testId) {

		ModelAndView mv = new ModelAndView();

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		RealTest test = null;

		test = mRealTest.getTest(testId);

		String title = "Test " + test.getName() + " - polaganje";

		// izvlacimo pitanja
		List<QuestionDTO> dtos = mRealTest.getQuestionDTOs(test.getId());

		mv.setViewName("doRealTest");
		mv.addObject("isUserLoggedIn", isUserLoggedIn);
		mv.addObject("questions", dtos);
		mv.addObject("startTime", System.currentTimeMillis());
		mv.addObject("testId", test.getId());
		mv.addObject("testName", test.getName());
		mv.addObject("title", title);
		mv.addObject("numberOfQuestions", dtos.size());

		mPage.addDoRealTestPage(mv, test.getName());

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/testovi/test/{testId}")
	@Transactional(readOnly = true)
	public ModelAndView dailyTestUsers(@PathVariable("testId") Long testId) {

		ModelAndView mv = new ModelAndView();

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		RealTest test = mRealTest.getTest(testId);

		if (test == null)
			throw new RuntimeException("Ne postoji test za prosleđeni id testa.");

		int numOfTopResults = 20;
		Date fromDate = DateMidnight.now().minusDays(7).toDate();
		
		List<RealTestUserResultDTO> results = mRealTest.getTopResults(test, numOfTopResults, fromDate);
		
		List<RealTestUserResultDTO> userResults = new LinkedList<RealTestUserResultDTO>();
		
		User user = null;
		int orderNumber = 0;
		boolean hasUserTakenTest = false;
		
		if(isUserLoggedIn){
			
			user = rUser.findOne(SecurityUtils.getUserId());
			
			userResults = mRealTest.getUserResults(test, user);
			orderNumber = mRealTest.getUserResultOrder(test, user);
			hasUserTakenTest = userResults.size() > 0;
		}

		RealTestCategory category = test.getCategory();

		String title = "Test - " + test.getName();
		String description = "Ovaj test ima "+category.getNumberOfQuestions()+" pitanja, minimum za prolaz je "+category.getMinimumPoints()+" skupljenih poena za ne duže od "+category.getDuration()+" minuta.";

		mv.setViewName("realTestUsers");
		mv.addObject("testId", test.getId());
		mv.addObject("testName", test.getName());
		mv.addObject("isUserLoggedIn", isUserLoggedIn);
		mv.addObject("hasUserTakenTest", hasUserTakenTest);
		mv.addObject("testIsTaken", results.size() > 0);
		mv.addObject("orderNumber", orderNumber);
		mv.addObject("results", results);
		mv.addObject("userResults", userResults);
		mv.addObject("title", title);
		mv.addObject("description", description);

		mPage.addRealTestUsersPage(mv, test.getName());

		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/testovi/rezultat/{resultId}")
	@Transactional
	public ModelAndView showRealTestResult(@PathVariable("resultId") Long resultId) {

		boolean isUserLoggedIn = credentialsManager.isUserLoggedIn();

		// izvlace se test i korisnik
		RealTestUserResult result = rUserResult.findOne(resultId);

		// ako ne postoji rezultat bacamo gresku
		if (result == null)
			throw new RuntimeException("Ne postoji izabrani rezultat.");

		RealTest test = result.getTest();
		User user = result.getUser();
		
		RealTestUserResultDTO userResult = mRealTest.getResult(resultId);

		int orderNumber = mRealTest.getUserResultOrder(test, user);

		// formatiranje datuma testa
		String dateString = FormattingUtils.formatDateSimple(result.getCreationDate());

		// ako korisnik nije ulogovan ne dozvoljavamo mu da gleda detaljne
		// rezultate ali moze da pogleda sumarne
		if (!isUserLoggedIn) {

			ModelAndView mv = new ModelAndView();

			mv.setViewName("realTestUserResultNotLoggedIn");

			mv.addObject("title", "Rezultat za test - "+test.getName()+"");
			mv.addObject("orderNumber", orderNumber);
			mv.addObject("userResult", userResult);
			mv.addObject("username", user.getUsername());
			mv.addObject("testId", test.getId());
			mv.addObject("testName", test.getName());

			mPage.addRealTestUserResultWhenNotLoggedIn(mv, user.getUsername(), dateString);

			return mv;
		}

		// ako korisnik jeste polagao, idemo dalje i prikazujemo drugacije
		// stranice

		User currentUser = rUser.findOne(SecurityUtils.getUserId());

		// ako korisnik nije polagao test onda prikazujemo stranicu prilagodjenu
		// tom stanju
		boolean hasCurrentUserDoneTest = mRealTest.hasUserTakenTest(currentUser, test);

		if (!hasCurrentUserDoneTest) {

			ModelAndView mv = new ModelAndView();

			mv.setViewName("realTestUserResultNotTakenTest");

			mv.addObject("title", "Rezultat test - " + test.getName());
			mv.addObject("orderNumber", orderNumber);
			mv.addObject("userResult", userResult);
			mv.addObject("username", user.getUsername());
			mv.addObject("testId", test.getId());

			mPage.addRealTestUserResultWhenNotLoggedIn(mv, user.getUsername(), dateString);

			return mv;
		}

		// ako smo ovde onda znamo da je trenutni korisnik ulogovan i da je vec
		// odradio ovaj test tako da mozemo
		// prikazati pune rezultate za izabranog korisnika

		// izvlaci se lista pitanja iz testa
		List<QuestionDTO> questionDtos = mRealTest.getQuestionDTOs(test.getId());

		// dohvataju se rezultati za korisnika

		// u listu se smesataju rezultati odgovaranja zajedno sa pitanjima i oni
		// se vracaju kao odgovor na stranicu
		List<RealTestUserResultQuestionDTO> realTestUserResultQuestion = new LinkedList<RealTestUserResultQuestionDTO>();

		for (RealTestUserResultQuestion resultQuestion : result.getQuestionResults()) {

			// transformacija resultQuestion-a u resultQuestionDTO (odgovor
			// korisnika)
			RealTestUserResultQuestionDTO resultQuestionDTO = tUserResultQuestion.transformToDTO(resultQuestion, "userAnswers");

			// u resultQuestionDTO (odgovor korisnika) dodaje se pravo pitanje
			// (koje pronalazi posebna metoda iz listi)
			QuestionDTO questionDTO = mRealTest.getQuestionDTO(questionDtos, resultQuestion.getQuestion());
			questionDTO.setQuestionUrlTitle(URLUtils.createQuestionTitle(resultQuestion.getQuestion().getText()));
			
			resultQuestionDTO.setQuestion(questionDTO);

			realTestUserResultQuestion.add(resultQuestionDTO);
		}

		// sortiranje odgovora kako bi raspored pitanja odgovarao tesu
		realTestUserResultQuestion = mRealTest.sortResponse(realTestUserResultQuestion, questionDtos);

		// ispisujemo razlicite poruke u zavisnosti od toga da li je rezultat za
		// trenutno ulogovanog usera
		// ili nekog drugog usera

		String message = null;

		if (user.equals(currentUser))
			message = "Trenutno si " + orderNumber + ". na ovom testu!";
		else
			message = user.getUsername() + " je trenutno " + orderNumber + ". na ovom testu!";

		ModelAndView mv = new ModelAndView();

		mv.setViewName("realTestUserResult");

		mv.addObject("title", "Rezultat testa - " + test.getName());
		mv.addObject("orderNumber", orderNumber);
		mv.addObject("userResult", userResult);
		mv.addObject("dailyTestResults", realTestUserResultQuestion);
		mv.addObject("numberOfQuestions", questionDtos.size());
		mv.addObject("message", message);
		mv.addObject("testId", test.getId());
		mv.addObject("testName", test.getName());

		mPage.addRealTestUserResult(mv, user.getUsername(), dateString);

		return mv;
	}
}
