package com.evola.edt.web.controller.drivingschool;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.model.dto.DrivingSchoolCarDTO;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class DrivingSchoolAdministrationCarController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	DrivingSchoolService service;

	@Inject
	UserCredentialsManager credentialsManager;
	
	@Inject
	GalleryManager galleryManager;

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/vozni-park")
	public ModelAndView drivingSchoolAdministrationCars(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		List<DrivingSchoolCarDTO> cars = service.findDrivingSchoolCars(schoolId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationCars");
		mav.addObject("school", school);
		mav.addObject("cars", cars);
		mav.addObject("pageInfo", new PageInfo("vozila-admin", "Vozni park", PageCategories.AUTOSKOLE));
		
		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("carsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/vozni-park/dodaj")
	public ModelAndView drivingSchoolAdministrationCarAddNew(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		DrivingSchoolCarDTO car = new DrivingSchoolCarDTO();
		car.setSchool(school);

		String title = "Dodavanje vozila - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationCarAdd");
		mav.addObject("school", school);
		mav.addObject("car", car);
		mav.addObject("pageInfo", new PageInfo("vozila-admin", title, PageCategories.AUTOSKOLE));
		
		galleryManager.addImageUploaderData(mav, car.getImagePath());

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("carsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/vozni-park/izmeni")
	public ModelAndView drivingSchoolAdministrationCarUpdate(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "carId", required = true) Long carId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		DrivingSchoolCarDTO car = service.findDrivingSchoolCar(schoolId, carId);

		String title = "Izmena vozila - " + car.getMake() + " " + car.getModel() + " - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationCarAdd");
		mav.addObject("school", school);
		mav.addObject("car", car);
		mav.addObject("pageInfo", new PageInfo("vozila-admin", title, PageCategories.AUTOSKOLE));
		
		galleryManager.addImageUploaderData(mav, car.getImagePath());

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("carsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/vozni-park/obrisi")
	public ModelAndView drivingSchoolAdministrationCarDelete(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "carId", required = true) Long carId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		service.removeDrivingSchoolCar(schoolId, carId);

		return drivingSchoolAdministrationCars(schoolId);
	}

}
