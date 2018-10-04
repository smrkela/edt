package com.evola.edt.web.controller.drivingschool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.FormParam;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.component.PageMetadataManager;
import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.DrivingSchoolNotificationComment;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.DrivingSchoolCarDTO;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolEmployeeDTO;
import com.evola.edt.model.dto.DrivingSchoolMarkDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationCommentDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.PageCommentDTO;
import com.evola.edt.model.dto.PriceListDTO;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.DrivingCategoryService;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.dto.DrivingSchoolNotificationsDTO;
import com.evola.edt.service.dto.DrivingSchoolsDTO;
import com.evola.edt.service.dto.PagesDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.utils.FormattingUtils;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * @author Nikola 18.05.2013.
 */
@Controller
public class DrivingSchoolController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	DrivingSchoolService service;

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	UserRepository userRepository;

	@Inject
	GalleryManager galleryManager;

	@Inject
	DrivingCategoryService drivingCategory;

	@Inject
	PageMetadataManager pageManager;

	/**
	 * Daci, 10.01.2014.
	 * 
	 * @param searchText
	 * @param city
	 * @param categoryId
	 * @param searchMarkFromString
	 * @param searchMarkToString
	 * @param searchPriceFromString
	 * @param searchPriceToString
	 * @param sortTypeString
	 * @param startingIndex
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola")
	public ModelAndView listDrivingSchools(@RequestParam(value = "search-text", required = false) String searchText,
			@RequestParam(value = "search-city", required = false) String city,
			@RequestParam(value = "search-category", required = false) Long categoryId,
			@RequestParam(value = "search-mark-from", required = false) String searchMarkFromString,
			@RequestParam(value = "search-mark-to", required = false) String searchMarkToString,
			@RequestParam(value = "search-price-from", required = false) String searchPriceFromString,
			@RequestParam(value = "search-price-to", required = false) String searchPriceToString,
			@RequestParam(value = "sort-type", required = false) String sortTypeString,
			@RequestParam(value = "startingIndex", required = false) Integer startingIndex) {

		// obrada indexa
		if (startingIndex == null || startingIndex < 0)
			startingIndex = 0;

		int count = 50;

		if (StringUtils.isEmpty(sortTypeString))
			sortTypeString = "Ime uzlazno";

		DrivingCategoryDTO category = categoryId != null ? drivingCategory.findOne(categoryId) : null;
		String categoryName = category != null ? category.getName() : null;

		Double searchMarkFrom = parseDoubleString(searchMarkFromString);
		Double searchMarkTo = parseDoubleString(searchMarkToString);
		Double searchPriceFrom = parseDoubleString(searchPriceFromString);
		Double searchPriceTo = parseDoubleString(searchPriceToString);

		// dohvatanje page-a auto skola koje treba prikazati u skladu sa
		// kriterijumima i sortiranjem
		DrivingSchoolsDTO drivingSchools = service.findBySearchAndSortCriteria(searchText, city, categoryName,
				searchMarkFrom, searchMarkTo, searchPriceFrom, searchPriceTo, sortTypeString, startingIndex, count);

		// sastavljamo listu N elemenata za navigaciju
		// trenutni index pokusavamo da stavimo na sredinu tog niza

		int totalPages = drivingSchools.getTotalPages();

		// treba da sastavimo listu strana za navigaciju
		int firstPageIndex = Math.max(startingIndex - 1, 0);
		int lastPageIndex = Math.min(startingIndex + 1, totalPages);
		int previousIndex = Math.max(startingIndex - 1, 0);
		int nextIndex = Math.min(startingIndex + 1, totalPages - 1);

		List<Integer> indices = FormattingUtils.createPageIndices(totalPages, startingIndex);

		// ucitavamo podatke vezane za pretragu
		List<String> names = service.findCityNamesForActiveSchools();
		List<String> cityNames = new ArrayList<String>();

		// ciscenje liste od praznih gradova
		Iterator<String> iterator = names.iterator();
		while (iterator.hasNext()) {
			String name = (String) iterator.next();
			if (name != null && !name.isEmpty()) {
				cityNames.add(name);
			}
		}

		List<Integer> marks = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });

		List<String> sortTypes = Arrays.asList(new String[] { "Ime uzlazno", "Ime silazno", "Grad uzlazno",
				"Grad silazno", "Cena uzlazno", "Cena silazno", "Ocena uzlazno", "Ocena silazno" });

		List<DrivingCategoryDTO> findAllCategories = drivingCategory.findAll();

		// za drugi broj (do) se racuna koji je manji broj (sledeci uvecan za
		// counter) ili (ukupan broj skola koje zadovoljavaju pretragu)
		String title = "Auto škole " + (startingIndex * count + 1) + " - "
				+ Math.min((startingIndex * count + count), drivingSchools.getTotalSchools());

		ModelAndView mav = new ModelAndView();
		mav.setViewName("listOfDrivingSchools");
		mav.addObject("schools", drivingSchools.getSchools());
		mav.addObject("categories", findAllCategories);
		mav.addObject("cities", cityNames);
		mav.addObject("marks", marks);
		mav.addObject("sortTypes", sortTypes);

		mav.addObject("searchText", searchText);
		mav.addObject("searchCity", city);
		mav.addObject("searchCategory", category);
		mav.addObject("searchCategoryId", categoryId);
		mav.addObject("searchMarkFrom", searchMarkFrom);
		mav.addObject("searchMarkTo", searchMarkTo);
		mav.addObject("searchPriceFrom", searchPriceFrom);
		mav.addObject("searchPriceTo", searchPriceTo);
		mav.addObject("sort", sortTypeString);

		// koristi se za paginaciju
		mav.addObject("isFirstPage", drivingSchools.getIsFirstPage());
		mav.addObject("isLastPage", drivingSchools.getIsLastPage());
		mav.addObject("numberOfSchools", drivingSchools.getTotalSchools());
		mav.addObject("nextIndex", nextIndex);
		mav.addObject("previousIndex", previousIndex);
		mav.addObject("firstIndex", 0);
		mav.addObject("lastIndex", totalPages - 1);
		mav.addObject("indices", indices);
		mav.addObject("currentIndex", startingIndex);

		pageManager.addDrivingSchoolListPage(mav, title);

		return mav;
	}

	private Double parseDoubleString(String searchPriceFromString) {

		Double i = null;

		try {

			i = Double.parseDouble(searchPriceFromString);

		} catch (Exception e) {
		}

		return i;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/profil-auto-skole")
	public ModelAndView drivingSchoolProfile(@PathVariable String uniqueName) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		Boolean canAdminister = credentialsManager.canAdministerSchool(schoolId);

		// zvezice
		List<Integer> positiveMarks = new LinkedList<Integer>();
		List<Integer> negativeMarks = new LinkedList<Integer>();

		Double averageMark = school.getAverageMark();

		if (averageMark != null) {

			int round = (int) Math.round(averageMark);

			round = Math.max(round, 1);
			round = Math.min(round, 10);

			for (int i = 1; i <= 10; i++) {

				if (i <= round)
					positiveMarks.add(1);
				else
					negativeMarks.add(1);
			}
		}
		
		boolean hasLicence = credentialsManager.drivingSchoolHasLicense(schoolId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolProfile");
		mav.addObject("school", school);
		mav.addObject("canAdminister", canAdminister);
		mav.addObject("positiveMarks", positiveMarks);
		mav.addObject("negativeMarks", negativeMarks);
		mav.addObject("hasLicense", hasLicence);

		pageManager.addDrivingSchoolBasicInfoPage(mav, school);

		credentialsManager.addLicenceInformation(mav, schoolId);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola-po-izabranom-gradu/{city}")
	public ModelAndView listDrivingSchoolsForCityPath(@PathVariable("city") String city) {

		List<DrivingSchoolDTO> findAll = service.findDrivingSchoolsByCity(city.toUpperCase());

		ModelAndView mav = new ModelAndView();
		mav.setViewName("listOfDrivingSchoolsForCity");
		mav.addObject("schools", findAll);
		mav.addObject("city", city);
		mav.addObject("pageInfo", new PageInfo("autoskole-po-gradu", "Lista autoskola po gradu",
				PageCategories.AUTOSKOLE));
		
		pageManager.addDrivingSchoolsForCityPage(mav, city);

		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola-po-izabranom-gradu")
	public ModelAndView listDrivingSchoolsForCity(@RequestParam(value = "city", required = true) String city) {

		return listDrivingSchoolsForCityPath(city);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/kontakt-auto-skole")
	public ModelAndView drivingSchoolContact(@PathVariable String uniqueName) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolContact");
		mav.addObject("school", school);

		credentialsManager.addLicenceInformation(mav, schoolId);

		// odustajemo od ove stranice ako skola nema pravo na njen prikaz
		boolean isAllowed = (boolean) mav.getModelMap().get("contactAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		pageManager.addDrivingSchoolContactPage(mav, school);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/tim-auto-skole")
	public ModelAndView drivingSchoolTeam(@PathVariable String uniqueName) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		List<DrivingSchoolEmployeeDTO> employees = service.findDrivingSchoolEmployees(schoolId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolEmployees");
		mav.addObject("school", school);
		mav.addObject("employees", employees);

		credentialsManager.addLicenceInformation(mav, schoolId);

		// odustajemo od ove stranice ako skola nema pravo na njen prikaz
		boolean isAllowed = (boolean) mav.getModelMap().get("teamAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		pageManager.addDrivingSchoolTeamPage(mav, school);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/vozni-park-auto-skole")
	public ModelAndView drivingSchoolCars(@PathVariable String uniqueName) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		List<DrivingSchoolCarDTO> employees = service.findDrivingSchoolCars(schoolId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolCars");
		mav.addObject("school", school);
		mav.addObject("cars", employees);

		credentialsManager.addLicenceInformation(mav, schoolId);

		// odustajemo od ove stranice ako skola nema pravo na njen prikaz
		boolean isAllowed = (boolean) mav.getModelMap().get("carsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		pageManager.addDrivingSchoolCarsPage(mav, school);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/obavestenja-auto-skole")
	public ModelAndView drivingSchoolNotifications(@PathVariable String uniqueName) {

		return drivingSchoolNotificationsPaged(uniqueName, 0);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/obavestenja-auto-skole/{startingPage}")
	public ModelAndView drivingSchoolNotificationsPaged(@PathVariable String uniqueName,
			@PathVariable Integer startingPage) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		if (startingPage == null || startingPage < 0)
			startingPage = 0;

		int count = 10;

		DrivingSchoolNotificationsDTO dto = service.findDrivingSchoolNotifications(schoolId, startingPage, count);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolNotifications");
		mav.addObject("school", school);
		mav.addObject("notifications", dto.getNotifications());
		mav.addObject("isFirstPage", dto.getIsFirstPage());
		mav.addObject("isLastPage", dto.getIsLastPage());
		mav.addObject("nextIndex", startingPage + 1);
		mav.addObject("previousIndex", startingPage - 1);

		credentialsManager.addLicenceInformation(mav, schoolId);

		// odustajemo od ove stranice ako skola nema pravo na njen prikaz
		boolean isAllowed = (boolean) mav.getModelMap().get("notificationsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		pageManager.addDrivingSchoolNotificationsPage(mav, school);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/obavestenja-auto-skole/obavestenje/{notificationId}")
	public ModelAndView page(@PathVariable String uniqueName, @PathVariable Long notificationId) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		DrivingSchoolNotificationDTO page = service.findNotification(notificationId);

		// uzimamo komentare stranice
		List<DrivingSchoolNotificationCommentDTO> comments = service.findNotificationComments(notificationId);

		// osvezavamo broj komentara, ovaj podatak nemamo u stranici automatski
		page.setNumOfComments(comments.size());

		ModelAndView mv = new ModelAndView("drivingSchoolNotification");
		mv.addObject("school", page.getSchool());
		mv.addObject("notification", page);
		mv.addObject("comments", comments);
		mv.addObject("isCommentDeleteAllowed", credentialsManager.canAdministerSchool(school.getId()));

		credentialsManager.addLicenceInformation(mv, page.getSchool().getId());

		// odustajemo od ove stranice ako skola nema pravo na njen prikaz
		boolean isAllowed = (boolean) mv.getModelMap().get("notificationsAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		pageManager.addDrivingSchoolSingleNotificationPage(mv, school, page);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/ocene-auto-skole")
	public ModelAndView drivingSchoolMarks(@PathVariable String uniqueName) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		List<DrivingSchoolMarkDTO> marks = service.findDrivingSchoolMarks(schoolId);
		ModelAndView mav = new ModelAndView();

		if (SecurityUtils.isLoggedIn()) {
			User user = userRepository.findOne(SecurityUtils.getUserId());
			DrivingSchoolMarkDTO drivingMarkEditedByUser = service.findDrivingMarkEditedByUser(user, schoolId);
			if (drivingMarkEditedByUser != null) {
				for (DrivingSchoolMarkDTO drivingSchoolMarkDTO : marks) {
					if (drivingSchoolMarkDTO.getId().equals(drivingMarkEditedByUser.getId())) {
						drivingSchoolMarkDTO.setEditedByLoggedInUser(true);
					}
				}
			}
			mav.addObject("userMarkedSchool", drivingMarkEditedByUser != null);
		}
		Double averageMark = service.findDrivingSchoolAverageMark(schoolId);

		mav.setViewName("drivingSchoolMarks");
		mav.addObject("school", school);
		mav.addObject("marks", marks);
		mav.addObject("averageMark", averageMark);

		credentialsManager.addLicenceInformation(mav, schoolId);

		// odustajemo od ove stranice ako skola nema pravo na njen prikaz
		boolean isAllowed = (boolean) mav.getModelMap().get("marksAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		pageManager.addDrivingSchoolMarksPage(mav, school);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/{uniqueName}")
	public ModelAndView drivingSchoolAdministration(@PathVariable String uniqueName) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministration");
		mav.addObject("school", school);
		mav.addObject("pageInfo", new PageInfo("administracija", "Administracija", PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/galerija")
	public ModelAndView drivingSchoolGallery(@PathVariable String uniqueName) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		List<DocumentDTO> drivingSchoolAlbums = galleryManager.getDrivingSchoolAlbums(schoolId);

		// ne prikazujemo misc album jer njegove slike direktno prikazujemo
		galleryManager.removeMiscAlbum(drivingSchoolAlbums);

		List<DocumentDTO> miscImages = galleryManager.getDrivingSchoolMiscelanousImages(schoolId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolGallery");
		mav.addObject("school", school);
		mav.addObject("albums", drivingSchoolAlbums);
		mav.addObject("images", miscImages);

		credentialsManager.addLicenceInformation(mav, schoolId);

		// odustajemo od ove stranice ako skola nema pravo na njen prikaz
		boolean isAllowed = (boolean) mav.getModelMap().get("galleryAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		pageManager.addDrivingSchoolGalleryPage(mav, school);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/album/{albumName}")
	public ModelAndView drivingSchoolGalleryAlbum(@PathVariable("uniqueName") String uniqueName,
			@PathVariable("albumName") String albumId) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		DocumentDTO album = galleryManager.getDrivingSchoolAlbum(albumId);
		List<DocumentDTO> images = galleryManager.getAlbumImages(albumId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolGalleryAlbum");
		mav.addObject("school", school);
		mav.addObject("album", album);
		mav.addObject("images", images);

		credentialsManager.addLicenceInformation(mav, schoolId);

		// odustajemo od ove stranice ako skola nema pravo na njen prikaz
		boolean isAllowed = (boolean) mav.getModelMap().get("galleryAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		pageManager.addDrivingSchoolAlbumPage(mav, school, album);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/{uniqueName}/upis-i-cene")
	public ModelAndView drivingSchoolPriceList(@PathVariable String uniqueName) {

		DrivingSchoolDTO school = service.findDrivingSchoolByUniqueName(uniqueName);

		if (school == null)
			return credentialsManager.getNonexistingDrivingSchoolMAV(uniqueName);

		Long schoolId = school.getId();

		ModelAndView mav = new ModelAndView("drivingSchoolPriceList");

		PriceListDTO priceList = service.findPriceListForDrivingSchool(schoolId);

		mav.addObject("school", school);
		mav.addObject("priceList", priceList);
		mav.addObject("priceListExist", priceList != null);

		credentialsManager.addLicenceInformation(mav, schoolId);

		// odustajemo od ove stranice ako skola nema pravo na njen prikaz
		boolean isAllowed = (boolean) mav.getModelMap().get("pricesAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		pageManager.addDrivingSchoolPricesPage(mav, school);

		return mav;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveNotificationComment")
	public ModelAndView saveNotificationComment(@FormParam("notificationId") Long notificationId,
			@FormParam("comment") String comment) {

		service.saveDrivingSchoolNotificationComment(notificationId, comment);

		DrivingSchoolNotificationDTO page = service.findNotification(notificationId);

		return page(page.getSchool().getUniqueName(), notificationId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/obrisi-komentar-obavestenja")
	@Transactional
	public Object drivingSchoolAdministrationDeleteNotificationComment(
			@RequestParam(value = "commentId", required = true) Long commentId){
		
		DrivingSchoolNotification notification = service.findNotificationByCommentId(commentId);
		DrivingSchool school = notification.getSchool();
		
		if(!credentialsManager.canAdministerSchool(school.getId()))
			return credentialsManager.getForbiddenMAV();
		
		service.removeDrivingSchoolNotificationComment(commentId);
		
		return "redirect:"+"/spisak-auto-skola/"+school.getUniqueName()+"/obavestenja-auto-skole/obavestenje/"+notification.getId();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/mapa")
	public ModelAndView drivingSchoolsMap() {

		List<DrivingSchoolDTO> all = service.findAll();
		
		//sastavljamo izraz koji smestamo u html kao hidden element kojeg ce javascript procesirati
		
		//za svaku skolu koja ima podatke o lokaciji stavljamo sledece:
		//naziv#$#latLen*$*
		
		StringBuffer buffer = new StringBuffer();
		
		int schoolsWithPosition = 0;
		int totalSchools = 0;
		
		String partSeparator = "#$#";
		String lineSeparator = "*$*";
		
		for(DrivingSchoolDTO school : all){
			
			//ako je skola skrivena onda je ne prikazujemo
			if(school.getIsHidden() != null && school.getIsHidden() == true)
				continue;
			
			totalSchools++;
			
			if(StringUtils.isNotBlank(school.getGoogleMapsURL())){
				
				if(buffer.length() > 0)
					buffer.append(lineSeparator);
				
				buffer.append(school.getName());
				buffer.append(partSeparator);
				buffer.append(school.getUniqueName());
				buffer.append(partSeparator);
				buffer.append(school.getAddress());
				buffer.append(partSeparator);
				buffer.append(school.getCity());
				buffer.append(partSeparator);
				buffer.append(school.getGoogleMapsURL());
				
				schoolsWithPosition++;
			}
		}
		
		String parameter = buffer.toString();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolsMap");
		mav.addObject("positionParameter", parameter);
		mav.addObject("schoolsWithPosition", schoolsWithPosition);
		mav.addObject("totalSchools", totalSchools);

		pageManager.addDrivingSchoolsMapPage(mav);

		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/spisak-auto-skola/obavestenja")
	public ModelAndView drivingSchoolsNotifications(@RequestParam(value = "startingIndex", required = false) Integer startingPage) {

		if (startingPage == null || startingPage < 0)
			startingPage = 0;

		int count = 10;

		// uzimamo stranicene vesti
		DrivingSchoolNotificationsDTO dto = service.findDrivingSchoolsNotifications(startingPage, count);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolsNotifications");
		mav.addObject("notifications", dto.getNotifications());
		mav.addObject("isFirstPage", dto.getIsFirstPage());
		mav.addObject("isLastPage", dto.getIsLastPage());
		mav.addObject("nextIndex", startingPage + 1);
		mav.addObject("previousIndex", startingPage - 1);
		
		pageManager.addDrivingSchoolsNotificationsPage(mav);

		return mav;
	}

}
