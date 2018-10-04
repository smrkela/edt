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
import com.evola.edt.model.dto.UserDrivingSchoolMembershipRequestDTO;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.UserDrivingSchoolMembershipRequestService;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * @author Daci, Jan 25, 2015
 *
 */

@Controller
public class DrivingSchoolAdministrationMembershipController {
	
	Logger log = Logger.getLogger(getClass());
	
	@Inject
	UserCredentialsManager credentialsManager;
	
	@Inject
	UserDrivingSchoolMembershipRequestService userDrivingSchoolMembershipRequestService;
	
	@Inject
	DrivingSchoolService drivingSchoolService;
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/zahtevi-za-clanstvo")
	public ModelAndView getAllMembershipRequest(@RequestParam(value = "id", required = true) Long schoolId){
		
		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();
		
		List<UserDrivingSchoolMembershipRequestDTO> membershipRequests = userDrivingSchoolMembershipRequestService.findAllRequestsForSchool(schoolId);
		
		DrivingSchoolDTO school = drivingSchoolService.findDrivingSchoolById(schoolId);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationMembershipRequests");
		mav.addObject("membershipRequests", membershipRequests);
		mav.addObject("school", school);
		mav.addObject("pageInfo", new PageInfo("zahtevi-za-clanstvo", "Zahtevi za članstvo", PageCategories.AUTOSKOLE));
		
		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("membershipAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();
		
		return mav;
	}

}
