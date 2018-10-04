package com.evola.edt.web.controller.drivingschool;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolEmployeeDTO;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class DrivingSchoolAdministrationEmployeeController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	DrivingSchoolService service;

	@Inject
	UserCredentialsManager credentialsManager;
	
	@Inject
	GalleryManager galleryManager;

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/nas-tim")
	public ModelAndView drivingSchoolAdministrationTeam(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		List<DrivingSchoolEmployeeDTO> employees = service.findDrivingSchoolEmployees(schoolId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationTeam");
		mav.addObject("school", school);
		mav.addObject("employees", employees);
		mav.addObject("pageInfo", new PageInfo("tim-admin", "Naš tim", PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("teamAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/nas-tim/dodaj")
	public ModelAndView drivingSchoolAdministrationTeamAddNew(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		DrivingSchoolEmployeeDTO employee = new DrivingSchoolEmployeeDTO();
		employee.setSchool(school);

		String title = "Dodavanje člana tima - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationTeamAdd");
		mav.addObject("school", school);
		mav.addObject("employee", employee);
		mav.addObject("pageInfo", new PageInfo("tim-admin", title, PageCategories.AUTOSKOLE));
		
		galleryManager.addImageUploaderData(mav, employee.getImagePath());

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("teamAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/nas-tim/izmeni")
	public ModelAndView drivingSchoolAdministrationTeamUpdate(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "employeeId", required = true) Long employeeId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		DrivingSchoolEmployeeDTO employee = service.findDrivingSchoolEmployee(schoolId, employeeId);

		String title = "Izmena člana tima - " + employee.getFirstName() + " " + employee.getLastName() + " - "
				+ school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationTeamAdd");
		mav.addObject("school", school);
		mav.addObject("employee", employee);
		mav.addObject("pageInfo", new PageInfo("tim-admin", title, PageCategories.AUTOSKOLE));
		
		galleryManager.addImageUploaderData(mav, employee.getImagePath());

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("teamAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/nas-tim/obrisi")
	public ModelAndView drivingSchoolAdministrationTeamDelete(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "employeeId", required = true) Long employeeId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		service.removeDrivingSchoolEmployee(schoolId, employeeId);

		return drivingSchoolAdministrationTeam(schoolId);
	}

}
