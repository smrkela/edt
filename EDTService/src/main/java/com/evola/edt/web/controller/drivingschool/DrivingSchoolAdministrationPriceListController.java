package com.evola.edt.web.controller.drivingschool;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.PriceListDTO;
import com.evola.edt.service.DrivingSchoolAdministrationService;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class DrivingSchoolAdministrationPriceListController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	DrivingSchoolService service;

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	private DrivingSchoolAdministrationService drivingSchoolService;

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/upis-i-cene")
	public ModelAndView drivingSchoolAdministrationPriceList(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		ModelAndView mav = new ModelAndView("drivingSchoolAdministrationPriceList");
		PriceListDTO priceListForDrivingSchool = drivingSchoolService.findPriceListForDrivingSchoolToEdit(schoolId);
		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		mav.addObject("priceList", priceListForDrivingSchool);
		mav.addObject("school", school);
		mav.addObject("pageInfo", new PageInfo("cenovnik", "Upis i cene", PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("pricesAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

}
