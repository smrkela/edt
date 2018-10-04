package com.evola.edt.web.controller.drivingschool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.model.dto.DrivingLicenceCategoryDTO;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolStudentDTO;
import com.evola.edt.model.dto.UserDrivingSchoolMembershipRequestDTO;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.UserDrivingSchoolMembershipRequestService;
import com.evola.edt.service.dto.transformer.DrivingLicenceCategoryDTOTransformer;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class DrivingSchoolAdministrationStudentController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	DrivingSchoolService service;

	@Inject
	UserCredentialsManager credentialsManager;
	
	@Inject
	DrivingLicenceCategoryDTOTransformer tDrivingLicenceCategory;
	
	@Inject
	UserDrivingSchoolMembershipRequestService userDrivingSchoolMembershipRequestService;
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/ucenici")
	public ModelAndView drivingSchoolAdministrationStudents(@RequestParam(value = "id", required = true) Long schoolId,
												@RequestParam(value = "select-type", required = false) String selectType) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		List<DrivingSchoolStudentDTO> students = service.findDrivingSchoolStudents(schoolId, selectType);
		
		Map<String, String> selectTypes = new HashMap<String, String>();
		selectTypes.put("1", "Položena obuka prve pomoći");
		selectTypes.put("2", "Položen teorijski deo");
		selectTypes.put("3", "Položen praktični deo");
		selectTypes.put("4", "Položeno sve");
		
		//sortiranje mape
		Map<String, String> selectTypesMap = new TreeMap<String, String>(selectTypes);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationStudents");
		mav.addObject("school", school);
		mav.addObject("students", students);
		mav.addObject("select", selectType);
		mav.addObject("selectTypes", selectTypesMap);
		mav.addObject("pageInfo", new PageInfo("ucenici-admin", "Učenici", PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("studentsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/ucenici/dodaj")
	public ModelAndView drivingSchoolAdministrationStudentAddNew(@RequestParam(value = "id", required = true) Long schoolId, 
																 @RequestParam(value = "membership", required = true) Boolean membership,
																 @RequestParam(value = "token", required = false) String token) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		DrivingSchoolStudentDTO student = new DrivingSchoolStudentDTO();
		student.setDrivingSchool(school);
		student.setIsMale(true);
		student.setSendNotifications(true);
		student.setInviteToJoin(true);
		
		String membershipRequestToken = null;
		
		// ukoliko se korisnik registruje iz membership request-a potrebno je ukljuciti njegove podatke
		// ukoliko se korisnik ne registruje iz membership request onda treba vratiti samo formu
		if (membership) {
			UserDrivingSchoolMembershipRequestDTO request = userDrivingSchoolMembershipRequestService.findRequestForToken(token);
			student.setUser(request.getUser());
			student.setFirstName(request.getUser().getFirstName());
			student.setLastName(request.getUser().getLastName());
			student.setEmail(request.getUser().getEmail());
			student.setIsMale(request.getUser().getIsMale());
			membershipRequestToken = request.getConfirmationToken();
		}

		String title = "Dodavanje novog učenika - " + school.getName();

		List<DrivingLicenceCategoryDTO> categories = service.getDrivingLincenceCategories();
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationStudentAdd");
		mav.addObject("school", school);
		mav.addObject("student", student);
		//ukoliko se kreira korisnik na osnovu membership request-a njemu ne treba slati mail kao poziv za prikljucenje sajtu, to ovaj marker pokazuje
		mav.addObject("membershipRequestToken", membershipRequestToken); 
		mav.addObject("pageInfo", new PageInfo("ucenici-admin", title, PageCategories.AUTOSKOLE));
		mav.addObject("categories", categories);
		
		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("studentsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/ucenici/izmeni")
	public ModelAndView drivingSchoolAdministrationStudentUpdate(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "studentId", required = true) Long studentId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		DrivingSchoolStudentDTO student = service.findDrivingSchoolStudent(schoolId, studentId);
		student.setCategoryId(student.getCategory() != null ? student.getCategory().getId() : null);

		String title = "Izmena učenika - " + student.getFirstName() + " " + student.getLastName();

		List<DrivingLicenceCategoryDTO> categories = service.getDrivingLincenceCategories();
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationStudentAdd");
		mav.addObject("school", school);
		mav.addObject("student", student);
		mav.addObject("pageInfo", new PageInfo("ucenici-admin", title, PageCategories.AUTOSKOLE));
		mav.addObject("categories", categories);
		
		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("studentsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/ucenici/obrisi")
	public ModelAndView drivingSchoolAdministrationStudentDelete(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "studentId", required = true) Long studentId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		service.removeDrivingSchoolStudent(schoolId, studentId);

		return drivingSchoolAdministrationStudents(schoolId, null);
	}
		

	/**
	 * vraca sve "nepovezane" studente, odnosno sve DrivingSchoolStudentDTO koji nisu nastali na osnovu nekog MembershipRequest-a
	 * @param schoolId
	 * @param membership
	 * @param token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/ucenici/povezi-sa-postojecim")
	public ModelAndView getAllDrivingSchoolAdministrationStudents(	@RequestParam(value = "id", required = true) Long schoolId,
																	@RequestParam(value = "token", required = true) String token) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);
		List<DrivingSchoolStudentDTO> students = service.findDrivingSchoolStudentsWithoutMembershipRequest(schoolId);
		UserDrivingSchoolMembershipRequestDTO membershipRequest = userDrivingSchoolMembershipRequestService.findRequestForToken(token);
		
		String title = "Povezivanje sa postojećim učenikom - " + school.getName();
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationStudentConnectToExisting");
		mav.addObject("school", school);
		mav.addObject("students", students);
		mav.addObject("membershipRequestId", membershipRequest.getId());
		mav.addObject("pageInfo", new PageInfo("ucenici-admin", title, PageCategories.AUTOSKOLE));
		
		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("studentsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

}
