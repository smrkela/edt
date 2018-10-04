package com.evola.edt.web.controller;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.model.DrivingSchoolLicenseType;
import com.evola.edt.model.MembershipRequestStatus;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolMemberDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationConfirmationDTO;
import com.evola.edt.model.dto.DrivingSchoolSiteLicenseDTO;
import com.evola.edt.model.dto.PageCategoryDTO;
import com.evola.edt.model.dto.PageDTO;
import com.evola.edt.model.dto.PageResultDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.QuestionProblemReportDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.model.dto.UserDrivingSchoolMembershipRequestDTO;
import com.evola.edt.model.dto.UserSyncDTO;
import com.evola.edt.service.AdministrationService;
import com.evola.edt.service.DrivingCategoryService;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.PageCategoryService;
import com.evola.edt.service.PageService;
import com.evola.edt.service.QuestionService;
import com.evola.edt.service.UserDrivingSchoolMembershipRequestService;
import com.evola.edt.service.UserService;
import com.evola.edt.service.dto.LearningQuestionsDTO;
import com.evola.edt.service.dto.PagesDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.service.dto.helpers.GroupedQuestionProblemDTO;
import com.evola.edt.utils.FormattingUtils;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.utils.sorters.PageCategoryPageTypeSorter;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class AdministrationController {

	@Inject
	private PageService pageService;

	@Inject
	private PageCategoryService pageCategoryService;

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	QuestionService questionsService;

	@Inject
	DrivingSchoolService drivingSchoolService;

	@Inject
	UserService userService;

	@Inject
	GalleryManager galleryManager;

	@Inject
	AdministrationService administrationService;
	
	@Inject
	DrivingCategoryService drivingCategoryService;
	
	@Inject
	private UserDrivingSchoolMembershipRequestService userDrivingSchoolMembershipRequestService;

	@RequestMapping(method = RequestMethod.GET, value = "/administracija")
	public ModelAndView administration() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("administration");
		mv.addObject("pageInfo", new PageInfo("admin", "Administracija", PageCategories.ADMINISTRACIJA));

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/vesti")
	public ModelAndView news(@RequestParam(value = "startingIndex", required = false) Integer startingPage) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		if (startingPage == null || startingPage < 0)
			startingPage = 0;

		int count = 50;

		// uzimamo stranicene vesti
		PagesDTO dto = pageService.findNews(startingPage, count);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationPages");
		mav.addObject("pages", dto.getPages());
		mav.addObject("isFirstPage", dto.getIsFirstPage());
		mav.addObject("isLastPage", dto.getIsLastPage());
		mav.addObject("nextIndex", startingPage + 1);
		mav.addObject("previousIndex", startingPage - 1);
		mav.addObject("pageInfo", new PageInfo("vesti", "Vesti", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/informacije")
	public ModelAndView informations(@RequestParam(value = "startingIndex", required = false) Integer startingPage) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		if (startingPage == null || startingPage < 0)
			startingPage = 0;

		int count = 50;

		// uzimamo stranicene vesti
		PagesDTO dto = pageService.findInformations(startingPage, count);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationInformations");
		mav.addObject("pages", dto.getPages());
		mav.addObject("isFirstPage", dto.getIsFirstPage());
		mav.addObject("isLastPage", dto.getIsLastPage());
		mav.addObject("nextIndex", startingPage + 1);
		mav.addObject("previousIndex", startingPage - 1);
		mav.addObject("pageInfo", new PageInfo("informacije", "Informacije", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pomoc")
	public ModelAndView helps(@RequestParam(value = "startingIndex", required = false) Integer startingPage) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		if (startingPage == null || startingPage < 0)
			startingPage = 0;

		int count = 50;

		// uzimamo stranicene vesti
		PagesDTO dto = pageService.findHelps(startingPage, count);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationHelps");
		mav.addObject("pages", dto.getPages());
		mav.addObject("isFirstPage", dto.getIsFirstPage());
		mav.addObject("isLastPage", dto.getIsLastPage());
		mav.addObject("nextIndex", startingPage + 1);
		mav.addObject("previousIndex", startingPage - 1);
		mav.addObject("pageInfo", new PageInfo("pomoc", "Pomoć", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/vesti/dodaj")
	public ModelAndView addNews() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		PageDTO page = new PageDTO();

		String title = "Dodavanje vesti";

		List<PageCategoryDTO> pageCategories = pageCategoryService.findAllForNews();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreatePage");
		mav.addObject("page", page);
		mav.addObject("pageCategories", pageCategories);
		mav.addObject("pageInfo", new PageInfo("vesti-dodavanje", title, PageCategories.ADMINISTRACIJA));

		galleryManager.addImageUploaderData(mav, page.getSmallImagePath());

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/informacije/dodaj")
	public ModelAndView addInformations() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		PageDTO page = new PageDTO();

		String title = "Dodavanje informacija";

		List<PageCategoryDTO> pageCategories = pageCategoryService.findAllForInformations();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreateInformations");
		mav.addObject("page", page);
		mav.addObject("pageCategories", pageCategories);
		mav.addObject("pageInfo", new PageInfo("informacije-dodavanje", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pomoc/dodaj")
	public ModelAndView addHelp() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		PageDTO page = new PageDTO();

		String title = "Dodavanje stranice za pomoć";

		List<PageCategoryDTO> pageCategories = pageCategoryService.findAllForHelp();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreateHelp");
		mav.addObject("page", page);
		mav.addObject("pageCategories", pageCategories);
		mav.addObject("pageInfo", new PageInfo("pomoc-dodavanje", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/vesti/izmeni")
	public ModelAndView addNews(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		PageDTO page = pageService.findPageById(schoolId);
		List<PageCategoryDTO> pageCategories = pageCategoryService.findAllForNews();

		String title = "Izmena vesti - " + page.getTitle();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreatePage");
		mav.addObject("page", page);
		mav.addObject("pageCategories", pageCategories);
		mav.addObject("pageInfo", new PageInfo("vest-izmena", title, PageCategories.ADMINISTRACIJA));

		galleryManager.addImageUploaderData(mav, page.getSmallImagePath());

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/informacije/izmeni")
	public ModelAndView updateNews(@RequestParam(value = "id", required = true) Long pageId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		PageDTO page = pageService.findPageById(pageId);

		List<PageCategoryDTO> pageCategories = pageCategoryService.findAllForInformations();

		String title = "Izmena informacije - " + page.getTitle();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreateInformations");
		mav.addObject("page", page);
		mav.addObject("pageCategories", pageCategories);
		mav.addObject("pageInfo", new PageInfo("informacije-izmena", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pomoc/izmeni")
	public ModelAndView updateHelp(@RequestParam(value = "id", required = true) Long pageId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		PageDTO page = pageService.findPageById(pageId);

		List<PageCategoryDTO> pageCategories = pageCategoryService.findAllForHelp();

		String title = "Izmena pomoći - " + page.getTitle();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreateHelp");
		mav.addObject("page", page);
		mav.addObject("pageCategories", pageCategories);
		mav.addObject("pageInfo", new PageInfo("pomoc-izmena", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/vesti/obrisi")
	public ModelAndView deleteNews(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		pageService.removePage(schoolId);

		return news(0);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/informacije/obrisi")
	public ModelAndView deleteInformations(@RequestParam(value = "id", required = true) Long pageId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		pageService.removePage(pageId);

		return informations(0);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pomoc/obrisi")
	public ModelAndView deleteHelp(@RequestParam(value = "id", required = true) Long pageId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		pageService.removePage(pageId);

		return helps(0);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/kategorije-vesti")
	public ModelAndView newsCategories() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		// uzimamo stranicene vesti
		List<PageCategoryDTO> dots = pageCategoryService.findAll();
		
		// sortiranje liste prema pageType-u
		Collections.sort(dots, new PageCategoryPageTypeSorter());

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationPageCategories");
		mav.addObject("pageCategories", dots);
		mav.addObject("pageInfo", new PageInfo("kategorije-vesti", "Kategorije vesti", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/kategorije-vesti/dodaj")
	public ModelAndView addNewsCategory() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		PageCategoryDTO page = new PageCategoryDTO();

		String title = "Dodavanje kategorije vesti";

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreatePageCategory");
		mav.addObject("pageCategory", page);
		mav.addObject("pageInfo", new PageInfo("kategorije-vesti-dodavanje", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/kategorije-vesti/izmeni")
	public ModelAndView editNewsCategory(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		PageCategoryDTO page = pageCategoryService.findById(schoolId);

		String title = "Izmena kategorije vesti - " + page.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreatePageCategory");
		mav.addObject("pageCategory", page);
		mav.addObject("pageInfo", new PageInfo("kategorije-vesti-izmena", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/kategorije-vesti/obrisi")
	public ModelAndView deleteNewsCategory(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		pageCategoryService.deleteById(schoolId);

		return newsCategories();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pitanja")
	public ModelAndView questions(@RequestParam(value = "startingIndex", required = false) Integer startingPage) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		if (startingPage == null || startingPage < 0)
			startingPage = 0;

		int count = 250;

		LearningQuestionsDTO questions = questionsService.getQuestions(0l, 0l, startingPage, count);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationQuestions");
		mav.addObject("questions", questions.getQuestions());
		mav.addObject("isFirstPage", questions.getIsFirstPage());
		mav.addObject("isLastPage", questions.getIsLastPage());
		mav.addObject("nextIndex", startingPage + 1);
		mav.addObject("previousIndex", startingPage - 1);
		mav.addObject("pageInfo", new PageInfo("pitanja", "Pitanja", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pitanja/pogledaj")
	public ModelAndView viewQuestion(@RequestParam(value = "id", required = true) Long id) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		String title = "Pregled pitanja";

		QuestionDTO dto = questionsService.getQuestion(id);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationQuestionView");
		mav.addObject("question", dto);
		mav.addObject("pageInfo", new PageInfo("pitanja-pregled", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pitanja/izmeni")
	public ModelAndView editQuestion(@RequestParam(value = "id", required = true) Long id) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		QuestionDTO dto = questionsService.getQuestion(id);

		String title = "Izmena pitanja - " + dto.getQuestionId();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreateQuestion");
		mav.addObject("question", dto);
		mav.addObject("pageInfo", new PageInfo("pitanja-izmena", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pitanja/dodaj")
	public ModelAndView addQuestion() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		QuestionDTO dto = new QuestionDTO();

		String title = "Dodavanje pitanja";

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreateQuestion");
		mav.addObject("question", dto);
		mav.addObject("pageInfo", new PageInfo("pitanja-dodavanje", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pitanja/obrisi")
	public ModelAndView deleteQuestion(@RequestParam(value = "id", required = true) Long id) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		questionsService.deleteById(id);

		return questions(null);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/greske-pitanja")
	public ModelAndView questionProblems() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		List<GroupedQuestionProblemDTO> groupedQuestionProblems = questionsService.getGroupedUnfixedQuestionProblems();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationGroupedQuestionProblems");
		mav.addObject("groups", groupedQuestionProblems);
		mav.addObject("pageInfo", new PageInfo("greske-pitanja", "Greške pitanja", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/greske-pitanja/pitanje")
	public ModelAndView questionProblems(@RequestParam(value = "id") long id) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		QuestionDTO question = questionsService.getQuestion(id);

		String title = "Greške pitanja - " + question.getQuestionId();

		List<QuestionProblemReportDTO> questions = questionsService.getUnfixedQuestionProblems(id);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationQuestionProblems");
		mav.addObject("questionProblems", questions);
		mav.addObject("pageInfo", new PageInfo("greske-ordedjenog-pitanja", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/greske-pitanja/obrisi")
	public ModelAndView deleteQuestionProblem(@RequestParam(value = "id") long questionProblemId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		QuestionProblemReportDTO dto = questionsService.getQuestionProblem(questionProblemId);

		QuestionDTO question = questionsService.getQuestion(dto.getQuestionDTO().getId());

		questionsService.deleteQuestionProblemById(dto.getId());

		return questionProblems(question.getId());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/greske-pitanja/resi")
	public ModelAndView fixQuestionProblem(@RequestParam(value = "id") long questionProblemId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		QuestionProblemReportDTO dto = questionsService.getQuestionProblem(questionProblemId);

		QuestionDTO question = questionsService.getQuestion(dto.getQuestionDTO().getId());

		questionsService.fixQuestionProblemById(dto.getId());

		return questionProblems(question.getId());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/auto-skole")
	public ModelAndView drivingSchools(@RequestParam(value = "startingIndex", required = false) Integer startingPage) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		if (startingPage == null || startingPage < 0)
			startingPage = 0;

		int count = 50;

		PageResultDTO<DrivingSchoolDTO> dto = drivingSchoolService.findDrivingSchoolsPaged(startingPage, count);

		// sastavljamo listu N elemenata za navigaciju
		// trenutni index pokusavamo da stavimo na sredinu tog niza

		int totalPages = dto.getTotalPages();

		List<Integer> indices = FormattingUtils.createPageIndices(totalPages, startingPage);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationDrivingSchools");
		mav.addObject("drivingSchools", dto.getDtos());
		mav.addObject("isFirstPage", dto.getFirstPage());
		mav.addObject("isLastPage", dto.getLastPage());
		mav.addObject("nextIndex", startingPage + 1);
		mav.addObject("firstIndex", 0);
		mav.addObject("lastIndex", totalPages - 1);
		mav.addObject("previousIndex", startingPage - 1);
		mav.addObject("indices", indices);
		mav.addObject("currentIndex", startingPage);
		mav.addObject("pageInfo", new PageInfo("autoskole", "Auto škole", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/auto-skole/dodaj")
	public ModelAndView addDrivingSchool() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO dto = new DrivingSchoolDTO();

		String title = "Dodavanje auto škole";

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreateDrivingSchool");
		mav.addObject("drivingSchool", dto);
		mav.addObject("pageInfo", new PageInfo("autoskole-dodavanje", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/auto-skole/izmeni")
	public ModelAndView editDrivingSchool(@RequestParam(value = "id", required = true) Long id) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO dto = drivingSchoolService.findDrivingSchoolById(id);

		String title = "Izmena auto škole - " + dto.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationCreateDrivingSchool");
		mav.addObject("drivingSchool", dto);
		mav.addObject("pageInfo", new PageInfo("autoskole-dodavanje", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/auto-skole/prikazi")
	public ModelAndView showDrivingSchool(@RequestParam(value = "id", required = true) Long id) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO dto = drivingSchoolService.findDrivingSchoolById(id);
		List<DrivingSchoolMemberDTO> members = drivingSchoolService.findDrivingSchoolMembersById(id);

		String title = "Auto škola - " + dto.getName();

		DrivingSchoolSiteLicenseDTO licenseDTO = drivingSchoolService.findLincenseForSchool(id);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationShowDrivingSchool");
		mav.addObject("drivingSchool", dto);
		mav.addObject("members", members);
		mav.addObject("license", licenseDTO);
		mav.addObject("pageInfo", new PageInfo("autoskole-prikaz", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/auto-skole/licenca")
	public ModelAndView updateDrivingSchoolLicense(@RequestParam(value = "id", required = true) Long id) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO schoolDTO = drivingSchoolService.findDrivingSchoolById(id);

		String title = "Licenca auto škole - " + schoolDTO.getName();

		DrivingSchoolSiteLicenseDTO licenseDTO = drivingSchoolService.findLincenseForSchool(id);

		// ako ne postoji pravimo novi dto
		if (licenseDTO == null) {
			licenseDTO = new DrivingSchoolSiteLicenseDTO();
			licenseDTO.setValidFrom(new Date());
			licenseDTO.setValidTo(new Date());
		}

		// stavljamo i listu dostupnih licenci
		List<DrivingSchoolLicenseType> licenseTypes = Arrays.asList(DrivingSchoolLicenseType.values());

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationUpdateDrivingSchoolLicense");
		mav.addObject("drivingSchool", schoolDTO);
		mav.addObject("license", licenseDTO);
		mav.addObject("licenseTypes", licenseTypes);
		mav.addObject("pageInfo", new PageInfo("autoskole-prikaz-licence", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/auto-skole/obrisi")
	public ModelAndView deleteDrivingSchool(@RequestParam(value = "id") long id) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		drivingSchoolService.deleteDrivingSchoolById(id);

		return drivingSchools(null);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/korisnici")
	public ModelAndView users(@RequestParam(value = "startingIndex", required = false) Integer startingPage) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		if (startingPage == null || startingPage < 0)
			startingPage = 0;

		int count = 50;

		PageResultDTO<UserDTO> dto = userService.findUsersPaged(startingPage, count);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationUsers");
		mav.addObject("users", dto.getDtos());
		mav.addObject("isFirstPage", dto.getFirstPage());
		mav.addObject("isLastPage", dto.getLastPage());
		mav.addObject("nextIndex", startingPage + 1);
		mav.addObject("previousIndex", startingPage - 1);
		mav.addObject("pageInfo", new PageInfo("korisnici", "Korisnici", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/slike")
	public ModelAndView images() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		List<DocumentDTO> dtos = galleryManager.getGlobalImageAlbums();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationImages");
		mav.addObject("albums", dtos);
		mav.addObject("pageInfo", new PageInfo("slike", "Slike", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/slike/dodaj")
	public ModelAndView addAlbum() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DocumentDTO album = new DocumentDTO();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationImageAlbumAdd");
		mav.addObject("album", album);
		mav.addObject("pageInfo", new PageInfo("slike", "Dodavanje albuma", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/slike/dodaj-slike")
	public ModelAndView administrationImageAlbumAddImages(@RequestParam(value = "id", required = true) String albumId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DocumentDTO album = galleryManager.getGlobalImageAlbum(albumId);

		String title = "Dodavanje slika - " + album.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationImageAlbumAddImages");
		mav.addObject("album", album);
		mav.addObject("pageInfo", new PageInfo("slike", title, PageCategories.ADMINISTRACIJA));

		galleryManager.addGalleryUploaderData(mav, -1);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/slike/izmeni")
	public ModelAndView administrationImageAlbumUpdate(@RequestParam(value = "id", required = true) String albumId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DocumentDTO album = galleryManager.getGlobalImageAlbum(albumId);

		String title = "Izmena albuma - " + album.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationImageAlbumAdd");
		mav.addObject("album", album);
		mav.addObject("pageInfo", new PageInfo("slike", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/slike/izmeni-sliku")
	public ModelAndView administrationAlbumUpdateImage(@RequestParam(value = "id", required = true) String imageId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DocumentDTO image = galleryManager.getGlobalImage(imageId);

		String title = "Izmena slike - " + image.getName();

		// moramo skinuti ektenziju sa naziva fajla
		String string = FilenameUtils.removeExtension(image.getName());

		image.setName(string);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationImageAlbumImageUpdate");
		mav.addObject("image", image);
		mav.addObject("pageInfo", new PageInfo("slike", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/slike/obrisi")
	public ModelAndView imageAlbumDelete(@RequestParam(value = "id", required = true) String albumId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		galleryManager.deleteGlobalImageAlbum(albumId);

		return images();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/slike/uredi-slike")
	public ModelAndView administrationAlbumManageImages(@RequestParam(value = "id", required = true) String albumId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DocumentDTO album = galleryManager.getGlobalImageAlbum(albumId);

		List<DocumentDTO> images = galleryManager.getAlbumImages(album.getId());

		String title = "Izmena albuma - " + album.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationImageAlbumManageImages");
		mav.addObject("album", album);
		mav.addObject("images", images);
		mav.addObject("pageInfo", new PageInfo("slike", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/slike/obrisi-sliku")
	public ModelAndView drivingSchoolAdministrationAlbumDeleteImage(
			@RequestParam(value = "id", required = true) String imageId) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		DocumentDTO globalImage = galleryManager.getGlobalImage(imageId);

		galleryManager.deleteGlobalImage(imageId);

		return administrationAlbumManageImages(globalImage.getParentId());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/fajlovi")
	public ModelAndView files() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		List<DocumentDTO> dtos = galleryManager.getGlobalFileAlbums();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationFiles");
		mav.addObject("albums", dtos);
		mav.addObject("pageInfo", new PageInfo("fajlovi", "Fajlovi", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/auto-skole/clanovi/dodaj")
	public ModelAndView drivingSchoolAdministrationMemberAdd(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = drivingSchoolService.findDrivingSchoolById(schoolId);
		DrivingSchoolMemberDTO member = new DrivingSchoolMemberDTO();
		member.setSchool(school);

		String title = "Dodavanje člana skole - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationDrivingSchoolMemberAdd");
		mav.addObject("school", school);
		mav.addObject("member", member);
		mav.addObject("pageInfo", new PageInfo("administracija-skole", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/auto-skole/clanovi/izmeni")
	public ModelAndView drivingSchoolAdministrationMemberUpdate(
			@RequestParam(value = "id", required = true) Long memberId) {

		DrivingSchoolMemberDTO memberDTO = drivingSchoolService.findDrivingSchoolMember(memberId);

		Long schoolId = memberDTO.getSchool().getId();

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = drivingSchoolService.findDrivingSchoolById(schoolId);

		memberDTO.setEmail(memberDTO.getUser().getEmail());
		memberDTO.setSchoolId(schoolId);

		String title = "Izmena člana škole - " + memberDTO.getUser().getFirstName() + " "
				+ memberDTO.getUser().getLastName() + " - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationDrivingSchoolMemberAdd");
		mav.addObject("school", school);
		mav.addObject("member", memberDTO);
		mav.addObject("pageInfo", new PageInfo("administracija-skole", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/auto-skole/clanovi/obrisi")
	public ModelAndView deleteDrivingSchoolMember(@RequestParam(value = "id", required = true) Long memberId) {

		DrivingSchoolMemberDTO memberDTO = drivingSchoolService.findDrivingSchoolMember(memberId);

		Long schoolId = memberDTO.getSchool().getId();

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		drivingSchoolService.removeDrivingSchoolMember(memberId);

		return showDrivingSchool(schoolId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/moj-profil")
	public ModelAndView showMyProfile() {

		if (!credentialsManager.isUserLoggedIn())
			return credentialsManager.getForbiddenMAV();

		Long userId = SecurityUtils.getUserId();

		UserDTO userDTO = userService.getUserDTO(userId);
		
		List<UserDrivingSchoolMembershipRequestDTO> requests = userDrivingSchoolMembershipRequestService.findAllRequestsForUser(userId);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("administrationMyProfile");
		mv.addObject("user", userDTO);
		mv.addObject("requests", requests);
		mv.addObject("pageInfo", new PageInfo("osnovni-podaci", "Osnovni podaci", PageCategories.ADMINISTRACIJA));
		
		//ako je korisnik clan nekih skola treba da se prikazu na ekranu
		List<DrivingSchoolDTO> schools = drivingSchoolService.findDrivingSchoolsByMember(userId);

		mv.addObject("drivingSchools", schools);
		
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/moj-profil/izmeni")
	public ModelAndView updateMyProfile() {

		if (!credentialsManager.isUserLoggedIn())
			return credentialsManager.getForbiddenMAV();

		Long userId = SecurityUtils.getUserId();

		UserDTO userDTO = userService.getUserDTO(userId);
		
		List<DrivingCategoryDTO> drivingCategories = drivingCategoryService.findAll();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("administrationMyProfileUpdate");
		mv.addObject("user", userDTO);
		mv.addObject("drivingCategories", drivingCategories);
		mv.addObject("pageInfo", new PageInfo("osnovni-podaci", "Osnovni podaci", PageCategories.ADMINISTRACIJA));

		galleryManager.addImageUploaderData(mv, userDTO.getNormalProfileImagePath());

		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/lista-clanstava")
	public ModelAndView listOfDrivingSchoolsMembership() {

		if (!credentialsManager.isUserLoggedIn())
			return credentialsManager.getForbiddenMAV();

		Long userId = SecurityUtils.getUserId();
		
		List<UserDrivingSchoolMembershipRequestDTO> requests = userDrivingSchoolMembershipRequestService.findAllRequestsForUser(userId);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("listOfDrivingSchoolsMembership");
		mv.addObject("requests", requests);
		mv.addObject("pageInfo", new PageInfo("lista-clanstava", "Lista članstava u auto školama", PageCategories.ADMINISTRACIJA));
		
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/uclani-se-u-auto-skolu")
	public ModelAndView requestMembershipInDrivingSchool() {

		if (!credentialsManager.isUserLoggedIn())
			return credentialsManager.getForbiddenMAV();

		Long userId = SecurityUtils.getUserId();

		UserDTO userDTO = userService.getUserDTO(userId);
		
		List<DrivingSchoolDTO> schools = drivingSchoolService.findAllActiveSchools(userId);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("requestForMembershipInDrivingSchool");
		mv.addObject("user", userDTO);
		mv.addObject("schools", schools);
		mv.addObject("pageInfo", new PageInfo("lista-clanstava", "Zahtev za članstvo", PageCategories.ADMINISTRACIJA));
		
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/zahtev-za-clanstvo-uspesno-poslat")
	public ModelAndView requestMembershipInDrivingSchoolSuccess() {

		if (!credentialsManager.isUserLoggedIn())
			return credentialsManager.getForbiddenMAV();
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("requestForMembershipInDrivingSchoolSuccess");
		mv.addObject("pageInfo", new PageInfo("lista-clanstava", "Zahtev za članstvo uspešno poslat", PageCategories.ADMINISTRACIJA));
		
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/confirmMembershipRequest")
	public ModelAndView drivingSchoolAdministrationNotificationConfirmation(@RequestParam(value = "token", required = true) String token) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("requestForMembershipInDrivingSchoolConfirmation");
		
		try {
			Assert.notNull(token);
			
			if (token != ""){
				UserDrivingSchoolMembershipRequestDTO requestDTO = userDrivingSchoolMembershipRequestService.findRequestForToken(token);
				
				MembershipRequestStatus status = MembershipRequestStatus.valueOf(requestDTO.getStatus());
				
				String title = "Potvrda zahteva za članstvo učenika " + requestDTO.getUser().getFirstName() + " " + requestDTO.getUser().getLastName();
				
				if (status.equals(MembershipRequestStatus.APPROVED) || status.equals(MembershipRequestStatus.REJECTED)){
					mav.addObject("confirmed", false);
					mav.addObject("error", true);
					mav.addObject("errorDescription", "Već ste potvrdili ovaj zahtev.");
					mav.addObject("pageInfo", new PageInfo("potvrda-zahteva-za-clanstvo", title, PageCategories.AUTOSKOLE));
					
					return mav;
				}
				
				userDrivingSchoolMembershipRequestService.membershipRequestDecision(token, null, MembershipRequestStatus.APPROVED.toString());
				
				mav.addObject("confirmed", true);
				mav.addObject("userName", requestDTO.getUser().getFirstName() + " " + requestDTO.getUser().getLastName());
				mav.addObject("pageInfo", new PageInfo("potvrda-zahteva-za-clanstvo", title, PageCategories.AUTOSKOLE));
			}
			
			if (token == "") {
				String title = "Potvrda zahteva za članstvo neuspešna!";
				
				mav.addObject("confirmed", false);
				mav.addObject("error", true);
				mav.addObject("errorDescription", "Token za potvrdu ne može biti prazan.");
				mav.addObject("pageInfo", new PageInfo("potvrda-zahteva-za-clanstvo", title, PageCategories.AUTOSKOLE));
			}
			
		} catch (Exception e) {
			String title = "Potvrda zahteva za članstvo neuspešna!";
			
			mav.addObject("confirmed", false);
			mav.addObject("error", false);
			mav.addObject("pageInfo", new PageInfo("potvrda-zahteva-za-clanstvo", title, PageCategories.AUTOSKOLE));
		}
		
		
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/adminServisi")
	public ModelAndView adminServices() {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		List<String> syncTypes = Arrays.asList(new String[] { "Kompletna sinhronizacija baza", "Sinhronizacija korisnika" });

		ModelAndView mav = new ModelAndView();
		mav.setViewName("adminServices");
		mav.addObject("syncTypes", syncTypes);
		mav.addObject("userName", null);
		mav.addObject("userEmail", null);
		mav.addObject("pageInfo", new PageInfo("adminServisi", "Admin Servisi", PageCategories.ADMINISTRACIJA));

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija/adminServisi/sinhronizacijaBaza")
	public ModelAndView synchronizeDB(@RequestParam(value = "sync-type", required = true) String syncType,
			// @RequestParam(value = "user-name", required = false) String
			// userName,
			@RequestParam(value = "user-email", required = false) String userEmail,
			@RequestParam(value = "send-notifications", required = false) boolean sendNotifications) {

		ModelAndView mav = new ModelAndView();

		try {
			if (!credentialsManager.canAdministerSystem())
				return credentialsManager.getForbiddenMAV();

			List<UserSyncDTO> syncResult = new ArrayList<>();

			if (syncType.equals("Kompletna sinhronizacija baza")) {
				syncResult = administrationService.synchronizeDBEDTtoForum(null, sendNotifications);
			} else {
				syncResult = administrationService.synchronizeDBEDTtoForum(userEmail, sendNotifications);
			}

			List<UserSyncDTO> successfullSync = new ArrayList<>();
			List<UserSyncDTO> unsuccessfullSync = new ArrayList<>();
			int successfullSyncCounter = 0;
			int unsuccessfullSyncCounter = 0;

			for (Iterator<UserSyncDTO> iterator = syncResult.iterator(); iterator.hasNext();) {
				UserSyncDTO userSyncDTO = (UserSyncDTO) iterator.next();

				if (userSyncDTO.getSyncSuccessful()) {
					successfullSync.add(userSyncDTO);
					successfullSyncCounter++;
				} else {
					unsuccessfullSync.add(userSyncDTO);
					unsuccessfullSyncCounter++;
				}
			}

			mav.setViewName("adminServicesSyncDB");
			mav.addObject("successfullSync", successfullSync);
			mav.addObject("unsuccessfullSync", unsuccessfullSync);
			mav.addObject("successfullSyncCounter", successfullSyncCounter);
			mav.addObject("unsuccessfullSyncCounter", unsuccessfullSyncCounter);

			mav.addObject("pageInfo", new PageInfo("adminServisi", "Admin Servisi - Sinhronizacija Baza [" + syncType + "]", PageCategories.ADMINISTRACIJA));

		} catch (Exception e) {
			mav.setViewName("adminServicesError");
			mav.addObject("service", "AdministrationController.synchronizeDB()");
			mav.addObject("error", e.getMessage());

			mav.addObject("pageInfo", new PageInfo("adminServisi", "Admin Servisi - Greška u izvršavanju",
					PageCategories.ADMINISTRACIJA));
		}

		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/administracija/pretraga-pitanja")
	public ModelAndView searchQuestions(@RequestParam(value = "q1", required=false) String q1, @RequestParam(value = "q2", required=false) String q2, @RequestParam(value = "q3", required=false) String q3, @RequestParam(value = "a1", required=false) String a1, @RequestParam(value = "a2", required=false) String a2, @RequestParam(value = "a3", required=false) String a3) {

		if (!credentialsManager.canAdministerSystem())
			return credentialsManager.getForbiddenMAV();

		List<QuestionDTO> questions = questionsService.findQuestions(q1, q2, q3, a1, a2, a3);

		String title = "Pretraga pitanja";

		ModelAndView mav = new ModelAndView();
		mav.setViewName("administrationFindQuestions");
		mav.addObject("q1", q1);
		mav.addObject("q2", q2);
		mav.addObject("q3", q3);
		mav.addObject("a1", a1);
		mav.addObject("a2", a2);
		mav.addObject("a3", a3);
		mav.addObject("questions", questions);
		mav.addObject("hasSearch", questions != null);
		mav.addObject("hasQuestions", questions != null && questions.size() > 0);
		mav.addObject("pageInfo", new PageInfo("pretraga-pitanja", title, PageCategories.ADMINISTRACIJA));

		return mav;
	}
}
