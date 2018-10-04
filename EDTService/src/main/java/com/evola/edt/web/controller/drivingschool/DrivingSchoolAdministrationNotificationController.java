package com.evola.edt.web.controller.drivingschool;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.DrivingSchoolNotificationComment;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationConfirmationDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.service.DrivingSchoolAdministrationService;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class DrivingSchoolAdministrationNotificationController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	DrivingSchoolService service;

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	DrivingSchoolController standardController;
	
	@Inject
	private DrivingSchoolAdministrationService drivingSchoolAdministrationService;

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/obavestenja")
	public ModelAndView drivingSchoolAdministrationNotifications(
			@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		List<DrivingSchoolNotificationDTO> notifications = service.findDrivingSchoolNotifications(schoolId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationNotifications");
		mav.addObject("school", school);
		mav.addObject("notifications", notifications);
		mav.addObject("pageInfo", new PageInfo("obavestenja-admin", "Obaveštenja", PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("notificationsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/obavestenja/dodaj")
	public ModelAndView drivingSchoolAdministrationNotificationAddNew(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		DrivingSchoolNotificationDTO notification = new DrivingSchoolNotificationDTO();
		notification.setSchool(school);

		String title = "Dodavanje obaveštenja - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationNotificationAdd");
		mav.addObject("school", school);
		mav.addObject("notification", notification);
		mav.addObject("pageInfo", new PageInfo("obavestenja-admin", title, PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("notificationsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/obavestenja/izmeni")
	public ModelAndView drivingSchoolAdministrationNotificationUpdate(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "notificationId", required = true) Long notificationId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		DrivingSchoolNotificationDTO notification = service.findDrivingSchoolNotification(schoolId, notificationId);

		String title = "Izmena obaveštenja - " + notification.getTitle() + " - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationNotificationAdd");
		mav.addObject("school", school);
		mav.addObject("notification", notification);
		mav.addObject("pageInfo", new PageInfo("obavestenja-admin", title, PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("notificationsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/obavestenja/obrisi")
	public ModelAndView drivingSchoolAdministrationNotificationDelete(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "notificationId", required = true) Long notificationId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		service.removeDrivingSchoolNotification(schoolId, notificationId);

		return drivingSchoolAdministrationNotifications(schoolId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/obavestenja/potvrde")
	public ModelAndView drivingSchoolAdministrationNotificationShowConfirmations(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "notId", required = true) Long notificationId) {

		if (!credentialsManager.canAdministerSchool(schoolId)) {
			return credentialsManager.getForbiddenMAV();
		}

		DrivingSchoolNotificationDTO notification = service.findDrivingSchoolNotification(schoolId, notificationId);
		
		List<DrivingSchoolNotificationConfirmationDTO> notificationConfirmations = service.findAllDrivingSchoolNotificationConfirmations(schoolId, notificationId);
		
		int noOfConfirmations = 0;
		
		for (DrivingSchoolNotificationConfirmationDTO confirmation : notificationConfirmations) {
			if (confirmation.getConfirmed() != null) {
				noOfConfirmations++;
			}
		}
		
		String title = "Lista potvrda obaveštenja - " + notification.getTitle();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolNotificationConfirmations");
		mav.addObject("notification", notification);
		mav.addObject("school", notification.getSchool());
		mav.addObject("confirmations", notificationConfirmations);
		mav.addObject("noOfConfirmations", noOfConfirmations);
		mav.addObject("pageInfo", new PageInfo("obavestenja-admin", title, PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);
		
		boolean isAllowed = (boolean) mav.getModelMap().get("notificationsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();
		
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/confirmNotification")
	public ModelAndView drivingSchoolAdministrationNotificationConfirmation(@RequestParam(value = "token", required = true) String token) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolNotificationConfirmation");
		
		try {
			Assert.notNull(token);
			
			if (token != ""){
				DrivingSchoolNotificationConfirmationDTO confirmationDTO = service.findDrivingSchoolNotificationConfirmation(token);
				String title = "Potvrda obaveštenja - " + confirmationDTO.getNotification().getTitle() + " - " + confirmationDTO.getSchool().getName();
				
				if (confirmationDTO.getConfirmed() != null){
					mav.addObject("confirmed", false);
					mav.addObject("error", true);
					mav.addObject("errorDescription", "Već ste potvrdili Vaše obaveštenje.");
					mav.addObject("pageInfo", new PageInfo("potvrda-obavestenja", title, PageCategories.AUTOSKOLE));
					
					return mav;
				}
				
				drivingSchoolAdministrationService.drivingSchoolNotificationConfirmed(confirmationDTO);
				
				mav.addObject("confirmed", true);
				mav.addObject("notificationTitle", confirmationDTO.getNotification().getTitle());
				mav.addObject("schoolName", confirmationDTO.getSchool().getName());
				mav.addObject("pageInfo", new PageInfo("obavestenja-admin", title, PageCategories.AUTOSKOLE));
			}
			
			if (token == "") {
				String title = "Potvrda obaveštenja neuspešna!";
				
				mav.addObject("confirmed", false);
				mav.addObject("error", true);
				mav.addObject("errorDescription", "Token za potvrdu ne može biti prazan.");
				mav.addObject("pageInfo", new PageInfo("obavestenja-admin", title, PageCategories.AUTOSKOLE));
			}
			
		} catch (Exception e) {
			String title = "Potvrda obaveštenja neuspešna!";
			
			mav.addObject("confirmed", false);
			mav.addObject("error", false);
			mav.addObject("pageInfo", new PageInfo("obavestenja-admin", title, PageCategories.AUTOSKOLE));
		}
		
		
		return mav;
	}
	
}
