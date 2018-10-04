package com.evola.edt.web.security;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.model.DrivingSchoolLicenseType;
import com.evola.edt.model.dto.DrivingSchoolSiteLicenseDTO;
import com.evola.edt.repository.DrivingSchoolMemberRepository;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;

@Named
public class UserCredentialsManager {

	Logger logger = LoggerFactory.getLogger(UserCredentialsManager.class);

	@Inject
	DrivingSchoolMemberRepository drivingSchoolMemberRepository;

	@Inject
	DrivingSchoolService service;

	@Inject
	GalleryManager galleryManager;

	public boolean canAdministerSchool(Long schoolId) {

		if (canAdministerSystem())
			return true;

		if (!SecurityUtils.isLoggedIn())
			return false;

		if (schoolId == null)
			return false;

		// korisnik moze administrirati autoskolu ako se nalazi u skoli kao njen
		// clan i ima ulogu admina

		Long count = drivingSchoolMemberRepository.countMembers(schoolId,
				SecurityUtils.getUserId(), "admin");

		return count > 0;
	}

	public boolean canAdministerSystem() {

		if (!SecurityUtils.isLoggedIn())
			return false;

		return hasRole("ROLE_ADMIN");
	}

	public ModelAndView getForbiddenMAV() {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("forbidden");
		mav.addObject("pageInfo", new PageInfo("forbidden",
				"Stranica nedostupna", PageCategories.GLAVNA));

		return mav;
	}

	public ModelAndView getNotAvailableMAV() {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("notAvailable");
		mav.addObject("pageInfo", new PageInfo("notAvailable",
				"Stranica nedostupna", PageCategories.GLAVNA));

		return mav;
	}

	public ModelAndView getNonexistingDrivingSchoolMAV(String uniqueName) {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("nonExistingDrivingSchool");
		mav.addObject("name", uniqueName);
		mav.addObject("pageInfo", new PageInfo("nonExistingSchool",
				"Nepostojeća autoškola", PageCategories.GLAVNA));

		return mav;
	}

	public void checkSystemAdministration() {

		if (!canAdministerSystem())
			throw new NoPermissionException("Nemate administratorska prava.");
	}

	public void checkUserLoggedIn() {

		if (!SecurityUtils.isLoggedIn())
			throw new NoPermissionException("Morate biti ulogovani.");
	}

	public void checkDrivingSchoolAdministration(Long schoolId) {

		if (!canAdministerSchool(schoolId))
			throw new NoPermissionException(
					"Nemate administratorska prava nad autoskolom.");
	}

	protected boolean hasRole(String role) {

		// get security context from thread local
		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null)
			return false;

		Authentication authentication = context.getAuthentication();

		if (authentication == null)
			return false;

		for (GrantedAuthority auth : authentication.getAuthorities()) {
			if (role.equals(auth.getAuthority()))
				return true;
		}

		return false;
	}

	public boolean isUserLoggedIn() {

		return SecurityUtils.isLoggedIn();
	}

	public void addLicenceInformation(ModelAndView mav, Long schoolId) {

		// izvlacimo i licencu
		DrivingSchoolSiteLicenseDTO license = service
				.findLincenseForSchool(schoolId);

		boolean contactAllowed = true; // 15.3.2014, osnovne informacije nisu
										// dovoljne
		boolean carsAllowed = false;
		boolean notificationsAllowed = false;
		boolean teamAllowed = false;
		boolean galleryAllowed = false;
		boolean marksAllowed = true; // ocene su dozvoljene za sve skole
		boolean pricesAllowed = false;
		boolean studentsAllowed = false;
		boolean membershipAllowed = false;

		if (license != null && license.isActive()) {

			DrivingSchoolLicenseType lType = DrivingSchoolLicenseType
					.valueOf(license.getLicenseType());

			if (DrivingSchoolLicenseType.BASIC.equals(lType)) {

				contactAllowed = true;
				galleryAllowed = true;
				marksAllowed = true;

			} else if (DrivingSchoolLicenseType.LIMITED.equals(lType)) {

				contactAllowed = true;
				carsAllowed = true;
				teamAllowed = true;
				galleryAllowed = true;
				marksAllowed = true;
				pricesAllowed = true;

			} else if (DrivingSchoolLicenseType.FULL.equals(lType)) {

				contactAllowed = true;
				carsAllowed = true;
				notificationsAllowed = true;
				teamAllowed = true;
				galleryAllowed = true;
				marksAllowed = true;
				pricesAllowed = true;
				studentsAllowed = true;
				membershipAllowed = true;

			} else {

				logger.warn("Driving school " + schoolId
						+ " has licence but of unreckognized type.");
			}
		}

		mav.addObject("contactAllowed", contactAllowed);
		mav.addObject("carsAllowed", carsAllowed);
		mav.addObject("notificationsAllowed", notificationsAllowed);
		mav.addObject("teamAllowed", teamAllowed);
		mav.addObject("galleryAllowed", galleryAllowed);
		mav.addObject("marksAllowed", marksAllowed);
		mav.addObject("pricesAllowed", pricesAllowed);
		mav.addObject("studentsAllowed", studentsAllowed);
		mav.addObject("membershipAllowed", membershipAllowed);
	}

	public String getDrivingSchoolGalleryLimitMessage(Long schoolId) {

		int BASIC_LIMIT = 0;
		int LIMITED_LIMIT = 5;
		int FULL_LIMIT = 10;

		// mozda ne mozemo dodavati vise albuma
		List<DocumentDTO> drivingSchoolAlbums = galleryManager
				.getDrivingSchoolAlbums(schoolId);
		int numOfAlbums = drivingSchoolAlbums.size() - 1; // -1 jer uvek imamo
															// defaultni

		// ako imamo basic licencu ne mozemo imati vise od jednog albuma
		// (default album)
		DrivingSchoolSiteLicenseDTO licence = service
				.findLincenseForSchool(schoolId);

		String message = null;

		if (licence == null || !licence.isActive()) {

			message = "Ne možete dodavati album u galeriju, nemate licencu sajta.";

		} else if (DrivingSchoolLicenseType.BASIC.name().equals(
				licence.getLicenseType())
				&& numOfAlbums >= BASIC_LIMIT) {

			message = "Ne možete dodavati još albuma. Za vašu licencu, osnovnu licencu, limit za dodatne albume je "
					+ BASIC_LIMIT + ".";

		} else if (DrivingSchoolLicenseType.LIMITED.name().equals(
				licence.getLicenseType())
				&& numOfAlbums >= LIMITED_LIMIT) {

			message = "Ne možete dodavati još albuma. Za vašu licencu limit za dodatne albume je "
					+ LIMITED_LIMIT + ".";

		} else if (DrivingSchoolLicenseType.FULL.name().equals(
				licence.getLicenseType())
				&& numOfAlbums >= FULL_LIMIT) {

			message = "Ne možete dodavati još albuma. Za vašu licencu limit za dodatne albume je "
					+ FULL_LIMIT + ".";

		}

		return message;
	}

	public void checkDrivingSchoolGalleryLimitMessage(Long schoolId) {

		String message = getDrivingSchoolGalleryLimitMessage(schoolId);

		if (message != null)
			throw new NoPermissionException(message);
	}

	public int getDrivingSchoolImageLimit(Long schoolId) {

		int BASIC_LIMIT = 10;
		int LIMITED_LIMIT = 50;
		int FULL_LIMIT = 200;

		DrivingSchoolSiteLicenseDTO licence = service
				.findLincenseForSchool(schoolId);

		int limit = 0;

		if (licence == null || !licence.isActive()) {

			limit = 0;

		} else if (DrivingSchoolLicenseType.BASIC.name().equals(
				licence.getLicenseType())) {

			limit = BASIC_LIMIT;

		} else if (DrivingSchoolLicenseType.LIMITED.name().equals(
				licence.getLicenseType())) {

			limit = LIMITED_LIMIT;

		} else if (DrivingSchoolLicenseType.FULL.name().equals(
				licence.getLicenseType())) {

			limit = FULL_LIMIT;
		}

		return limit;
	}

	public boolean drivingSchoolHasLicense(Long schoolId) {

		boolean hasLicense = false;

		// izvlacimo i licencu
		DrivingSchoolSiteLicenseDTO license = service
				.findLincenseForSchool(schoolId);

		if (license != null && license.isActive()) {

			hasLicense = true;
		}

		return hasLicense;
	}

}
