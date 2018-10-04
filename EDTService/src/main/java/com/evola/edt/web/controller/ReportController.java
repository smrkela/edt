package com.evola.edt.web.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.formula.functions.Today;
import org.joda.time.DateMidnight;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.component.PageMetadataManager;
import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.PageDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.PageService;
import com.evola.edt.service.StatisticsService;
import com.evola.edt.service.UserService;
import com.evola.edt.service.dto.DrivingSchoolLoginDTO;
import com.evola.edt.service.util.EdtSignInUtils;
import com.evola.edt.utils.SessionCounter;
import com.evola.edt.utils.social.SocialMediaService;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * Kontroler za izvestaje.
 * 
 */
@Controller
public class ReportController {

	@Inject
	private UserService userService;

	@Inject
	private PageMetadataManager pageManager;

	@Inject
	private DrivingSchoolService drivingSchoolService;

	@Inject
	private HttpServletRequest request;
	
	@Inject
	private UserCredentialsManager credentialsManager;
	
	@Inject
	private ActivityManager mActivity;

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/reports")
	public ModelAndView reportsPage(
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "finishDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();
		
		// stranica vraca informacije:
		// koliko se korisnika danas ulogovalo
		// koliko se koristnika danas registrovalo
		// koliko je korisnika koristilo aplikaciju

		DatesWrapper wrapper = new DatesWrapper(startDate, finishDate);

		int numOfLoggedInUsers = userService.getNumOfLoggedInUsers(wrapper.startDate, wrapper.finishDate);
		int numOfRegisteredUsers = userService.getNumOfRegisteredUsers(wrapper.startDate, wrapper.finishDate);
		int numOfAppUsers = userService.getNumOfAppUsers(wrapper.startDate, wrapper.finishDate);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("reportsPage");
		mv.addObject("numOfLoggedInUsers", numOfLoggedInUsers);
		mv.addObject("numOfRegisteredUsers", numOfRegisteredUsers);
		mv.addObject("numOfAppUsers", numOfAppUsers);
		mv.addObject("startDate", wrapper.startDate);
		mv.addObject("finishDate", wrapper.finishDate);

		pageManager.addReportsPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/reports/ulogovani-korisnici")
	public ModelAndView loggedInUsers(
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "finishDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate) {
		
		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DatesWrapper wrapper = new DatesWrapper(startDate, finishDate);

		List<UserDTO> users = userService.getLoggedInUsers(wrapper.startDate, wrapper.finishDate);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("reportsLoggedInUsersPage");
		mv.addObject("users", users);
		mv.addObject("startDate", wrapper.startDate);
		mv.addObject("finishDate", wrapper.finishDate);

		pageManager.addReportsLoggedInUsersPage(mv);

		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija/reports/registrovani-korisnici")
	public ModelAndView registeredUsers(
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "finishDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate) {
		
		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DatesWrapper wrapper = new DatesWrapper(startDate, finishDate);

		List<UserDTO> users = userService.getRegisteredUsers(wrapper.startDate, wrapper.finishDate);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("reportsRegisteredUsersPage");
		mv.addObject("users", users);
		mv.addObject("startDate", wrapper.startDate);
		mv.addObject("finishDate", wrapper.finishDate);

		pageManager.addReportsRegisteredUsersPage(mv);

		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija/reports/korisnici-aplikacije")
	public ModelAndView applicationUsers(
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "finishDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate) {
		
		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DatesWrapper wrapper = new DatesWrapper(startDate, finishDate);

		List<UserDTO> users = userService.getApplicationUsers(wrapper.startDate, wrapper.finishDate);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("reportsApplicationUsersPage");
		mv.addObject("users", users);
		mv.addObject("startDate", wrapper.startDate);
		mv.addObject("finishDate", wrapper.finishDate);

		pageManager.addReportsApplicationUsersPage(mv);

		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija/reports/licencirane-auto-skole")
	public ModelAndView licencedDrivingSchools(
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "finishDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate) {
		
		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DatesWrapper wrapper = new DatesWrapper(startDate, finishDate);

		List<DrivingSchoolDTO> schools = drivingSchoolService.getLicencedDrivingSchools();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("reportsLicencedDrivingSchoolsPage");
		mv.addObject("schools", schools);
		mv.addObject("startDate", wrapper.startDate);
		mv.addObject("finishDate", wrapper.finishDate);

		pageManager.addReportsLicensedDrivingSchoolsPage(mv);

		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija/reports/login-auto-skola")
	public ModelAndView drivingSchoolsLogin(
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "finishDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate) {
		
		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DatesWrapper wrapper = new DatesWrapper(startDate, finishDate);

		Collection<DrivingSchoolLoginDTO> schools = drivingSchoolService.getDrivingSchoolsLoginData();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("reportsDrivingSchoolsLoginPage");
		mv.addObject("schools", schools);
		mv.addObject("startDate", wrapper.startDate);
		mv.addObject("finishDate", wrapper.finishDate);

		pageManager.addReportsDrivingSchoolsLoginPage(mv);

		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija/reports/aktivnost")
	public ModelAndView activity(
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "finishDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate) {
		
		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DatesWrapper wrapper = new DatesWrapper(startDate, finishDate);

		List list = mActivity.getAdministrationActivity(wrapper.startDate, wrapper.finishDate);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("reportsActivityPage");
		mv.addObject("messages", list);
		mv.addObject("startDate", wrapper.startDate);
		mv.addObject("finishDate", wrapper.finishDate);

		pageManager.addReportsActivityPage(mv);

		return mv;
	}


}

class DatesWrapper{
	
	public Date startDate;
	public Date finishDate;
	
	public DatesWrapper(Date startDate, Date finishDate){
		
		if (startDate == null)
			startDate = DateMidnight.now().toDate();
		else
			startDate = new DateMidnight(startDate.getTime()).toDate();

		if (finishDate == null)
			finishDate = DateMidnight.now().plusDays(1).toDate();
		else
			finishDate = new DateMidnight(finishDate.getTime()).toDate();
		
		this.startDate = startDate;
		this.finishDate = finishDate;
	}

}
