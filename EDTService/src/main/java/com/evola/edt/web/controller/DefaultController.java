package com.evola.edt.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.component.DailyTestManager;
import com.evola.edt.component.MarathonManager;
import com.evola.edt.component.PageMetadataManager;
import com.evola.edt.component.RealTestManager;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.PageDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.service.DrivingCategoryService;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.PageService;
import com.evola.edt.service.StatisticsService;
import com.evola.edt.service.UserService;
import com.evola.edt.service.util.EdtSignInUtils;
import com.evola.edt.utils.SessionCounter;
import com.evola.edt.utils.social.SocialMediaService;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * Default controller that contains all mappings
 * 
 * @author Nikola 10.05.2013.
 * 
 */
@Controller
public class DefaultController {

	@Inject
	private PageService pageService;

	@Inject
	private UserService userService;

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private PageMetadataManager pageManager;

	@Inject
	private DrivingSchoolService drivingSchoolService;

	@Inject
	private HttpServletRequest request;

	@Inject
	private UserCredentialsManager credentialsManager;
	
	@Inject
	DrivingCategoryService drivingCategoryService;
	
	@Inject
	DailyTestManager mDailyTest;
	
	@Inject
	RealTestManager mRealTest;
	
	@Inject
	MarathonManager mMarathonTest;

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ModelAndView homePage() {

		int maxPages = 4;

		List<PageDTO> pages = pageService.findLatestPages(maxPages);
		Long regUsers = userService.countNumberOfUsers();
		Long comments = 12l;// forumRepository.getNumberOfPreviousPosts();
		// int i = tokenStore.getTokens().size() - 1;
		int i = SessionCounter.getActiveSessions();
		int numberOfActiveStudentsRecently = statisticsService.getNumberOfActiveStudentsRecently(7);
		
		Date sevenDaysAgo = DateMidnight.now().minusDays(7).toDate();
		
		int realTestResultsCount = mRealTest.getTotalResultsRecently(sevenDaysAgo);
		int marathonTestResultsCount = mMarathonTest.getTotalResultsRecently(sevenDaysAgo);
		int dailyTestResultsCount = mDailyTest.getTotalResultsRecently(sevenDaysAgo);

		List<DrivingSchoolNotificationDTO> recentNotifications = drivingSchoolService.findRecentNotifications(4);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("homePage");
		mv.addObject("pages", pages);
		mv.addObject("regUsers", regUsers);
		mv.addObject("activeStudents", numberOfActiveStudentsRecently);
		mv.addObject("realTestResultsCount", realTestResultsCount);
		mv.addObject("marathonTestResultsCount", marathonTestResultsCount);
		mv.addObject("dailyTestResultsCount", dailyTestResultsCount);
		
		mv.addObject("onlineUsers", i); // (i < 10) ? "9" : i mora da se napravi
										// nova tabela u kojoj ce se se smestati
										// logid, userid, login time, logout
										// time,
		// log IP, browser, i jos neki podaci po potrebi
		// ako je nastala greska pri citanju podataka postavi da bude 11, malo
		// je glupo da nema ni jedne poruke

		mv.addObject("comments", (comments == -1) ? "11" : comments);
		mv.addObject("notifications", recentNotifications);

		pageManager.addHomePage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/uloguj-se")
	public ModelAndView loginPage(@RequestParam(value = "error", required = false) String errorCode) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("loginPage");

		if ("provider".equals(errorCode))
			errorCode = "Neuspešna registracija preko društvene mreže. Možda već imate nalog na našem sajtu. Ukoliko nemate, molimo registrujte se bez korišćenja društvene mreže.";
		else if ("credentials".equals(errorCode))
			errorCode = "Ne postoji korisnik sa unetim podacima.";

		mv.addObject("errorMessage", errorCode);

		pageManager.addLoginPage(mv);

		// ako nemamo gresku odnosno prvi put dolazimo na login stranicu onda
		// cuvamo referer url u sesiji
		if (StringUtils.isBlank(errorCode)) {

			EdtSignInUtils.saveReferer(request);
		}

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/registruj-se")
	public ModelAndView registerPage(WebRequest request) {

		Connection<?> connection = ProviderSignInUtils.getConnection(request);

		UserDTO registration = createRegistrationDTO(connection);
		
		List<DrivingCategoryDTO> drivingCategories = drivingCategoryService.findAll();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("registerPage");
		mv.addObject("user", registration);
		mv.addObject("drivingCategories", drivingCategories);

		pageManager.addRegisterPage(mv);

		return mv;
	}

	private UserDTO createRegistrationDTO(Connection<?> connection) {

		UserDTO dto = new UserDTO();

		if (connection != null) {

			UserProfile socialMediaProfile = connection.fetchUserProfile();

			dto.setEmail(socialMediaProfile.getEmail());
			dto.setFirstName(socialMediaProfile.getFirstName());
			dto.setLastName(socialMediaProfile.getLastName());
			dto.setUsername(socialMediaProfile.getUsername());

			try {

				Object o = connection.getApi();
				Facebook fbi = (Facebook) o;

				FacebookProfile userProfile = fbi.userOperations().getUserProfile();

				dto.setIsMale("male".equals(userProfile.getGender()));

			} catch (Exception e) {
			}

			ConnectionKey providerKey = connection.getKey();

			dto.setSignInProvider(SocialMediaService.valueOf(providerKey.getProviderId().toUpperCase()));
		}

		return dto;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/registracija-uspesna")
	public ModelAndView registerSuccessPage() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("registerSuccessPage");

		pageManager.addRegisterSuccessPage(mv);

		return mv;
	}

	// @RequestMapping(method = RequestMethod.GET, value = "/forum")
	public ModelAndView showForum() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("forumPage");

		pageManager.addForumPage(mv);

		return mv;
	}

	// OVO SRANJE PRIKAZUJE CONTACT STRANICU, DEFINISANO KOJA STRANICA U
	// TILES-DEFS.XML
	@RequestMapping(method = RequestMethod.GET, value = "/kontakt-forma")
	public ModelAndView showContactForm() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("contactPage");

		pageManager.addContactPage(mv);

		return mv;
	}

	// KADA SE POSALJE MAIL POZIVA SE OVA METODA KOJA CE PRIKAZATI
	// USPESNO/NEUSPESNO POSLAT MAIL
	@RequestMapping(method = RequestMethod.GET, value = "/email-poslat")
	public ModelAndView showEmailSuccess() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("contactSuccessPage");

		pageManager.addContactEmailSuccess(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/politika-privatnosti")
	public ModelAndView privacyPolicy() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("privacyPolicyPage");

		pageManager.addPrivacyPolicy(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/uslovi-koriscenja")
	public ModelAndView termsOfService() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("termsOfServicePage");

		pageManager.addTermsOfService(mv);

		return mv;
	}
	
	/**
	 * koristi se za Vanilla forum
	 * @return
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/js-connect", produces = "application/json")
	@ResponseBody
	public String jsConnect() {

		String clientID = "123-abc-xyz";
		String secret = "xyz-cba-321";

		User user = SecurityUtils.getUser();

		Map<String, String> userMap = new HashMap<String, String>();

		if (user != null) {
			userMap.put("uniqueid", "" + user.getId());
			userMap.put("name", user.getUsername());
			userMap.put("email", user.getEmail());
			userMap.put("photourl", "/resource?path=/users/" + user.getId() + "/smallImage");
		}

		Boolean secure = true;

		String jsConnectString = com.evola.edt.utils.jsConnect.GetJsConnectString(userMap, request.getParameterMap(), clientID, secret,
				secure);

		return jsConnectString;
	}

	
	/**
	 * koristi se za PHPBB3 forum
	 * @return JSON
	 */
	
	@RequestMapping(method = RequestMethod.POST, value = "/js-connect", produces = "application/json")
	@ResponseBody
	public String jsConnectPost() {

		String response = null;

		if (SecurityUtils.isLoggedIn()) {

			User user = SecurityUtils.getUser();

			Map<String, String> userMap = new HashMap<String, String>();

			userMap.put("username", user.getUsername());
			userMap.put("email", user.getEmail());
			userMap.put("admin", credentialsManager.canAdministerSystem() + "");
			userMap.put("authenticated", true+"");

			response = com.evola.edt.utils.jsConnect.JsonEncode(userMap);
			
		} else {

			response = "{authenticated: False}";
		}

		return response;
	}

}
