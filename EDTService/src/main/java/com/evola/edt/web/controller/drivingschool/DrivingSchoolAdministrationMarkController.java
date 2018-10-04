package com.evola.edt.web.controller.drivingschool;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolMarkDTO;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class DrivingSchoolAdministrationMarkController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	DrivingSchoolService service;
	
	@Inject
	UserCredentialsManager credentialsManager;
	
	@Inject 
	DrivingSchoolController standardController;

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/utisci")
	public ModelAndView drivingSchoolMarks(
			@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();
		
		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);

		List<DrivingSchoolMarkDTO> marks = service.findDrivingSchoolMarks(schoolId);

		Double averageMark = service.findDrivingSchoolAverageMark(schoolId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationMarks");
		mav.addObject("school", school);
		mav.addObject("marks", marks);
		mav.addObject("averageMark", averageMark);
		mav.addObject("pageInfo", new PageInfo("ocene", "Utisci učenika",PageCategories.AUTOSKOLE));
		
		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("marksAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

}
