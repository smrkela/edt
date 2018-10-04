package com.evola.edt.web.controller.drivingschool;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class DrivingSchoolAdministrationBasicInfoController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	DrivingSchoolService service;

	@Inject
	UserCredentialsManager credentialsManager;
	
	@Inject
	GalleryManager galleryManager;

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/osnovni-podaci")
	public ModelAndView drivingSchoolAdministrationTeam(
			@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationBasicInfo");
		mav.addObject("school", school);
		mav.addObject("pageInfo", new PageInfo("osnovni-podaci-admin",
				"Osnovni podaci", PageCategories.AUTOSKOLE));
		
		galleryManager.addImageUploaderData(mav, school.getLogoPath());
		
		credentialsManager.addLicenceInformation(mav, schoolId);

		return mav;
	}

}
