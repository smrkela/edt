package com.evola.edt.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.component.MarathonManager;
import com.evola.edt.component.PageMetadataManager;
import com.evola.edt.component.SessionStore;
import com.evola.edt.component.store.MarathonStoreData;
import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.repository.MarathonTestRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.helpers.MarathonTestOverviewDTO;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class MarathonController {

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	PageMetadataManager mPage;

	@Inject
	UserRepository rUser;

	@Inject
	MarathonManager mMarathon;

	@Inject
	SessionStore storeSession;

	@Inject
	MarathonTestRepository rMarathonTest;

	@RequestMapping(method = RequestMethod.GET, value = "/maraton")
	public ModelAndView overview() {

		// we need: all tests grouped by categories

		MarathonTestOverviewDTO dto = mMarathon.getOverviewData();

		MarathonStoreData marathon = storeSession.loadMarathon();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("marathonOverview");
		mv.addObject("data", dto);
		mv.addObject("isUserLoggedIn", SecurityUtils.isLoggedIn());
		mv.addObject("hasUserTakenTests", dto.getTotalTestsByCurrentUser() > 0);
		mv.addObject("hasActiveMarathon", marathon != null);
		
		mPage.addMarathonOverviewPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maraton/polaganje/nastavak")
	public ModelAndView runPrevious() {

		return run(true);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maraton/polaganje")
	public ModelAndView runNew() {

		return run(false);
	}

	private ModelAndView run(boolean previous) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("doMarathon");
		mv.addObject("isUserLoggedIn", SecurityUtils.isLoggedIn());
		mv.addObject("pageInfo", new PageInfo("maraton", "Polaganje maraton testa", PageCategories.UCENJE));

		if (!SecurityUtils.isLoggedIn()) {

			return mv;
		}

		// - za korisnikovu kategoriju moramo izvuci sva pitanja predvidjena
		// za ovaj maraton i smestiti ih u sesiju, kasnije cemo ih asinhrono
		// izvlaciti jedno po jedno posto samo po jedno pitanje prikazujemo u
		// sesiji
		// - takodje ovom maratonu moramo dodeliti neki id na osnovu kojeg ce
		// klijent
		// kasnije traziti pitanja

		User user = rUser.findOne(SecurityUtils.getUserId());

		// ako korisnik vec ima pokrenut maraton, nastavljamo ga
		MarathonStoreData loadedMarathon = null;

		if (previous)
			loadedMarathon = storeSession.loadMarathon();

		if (loadedMarathon == null) {

			List<Long> questionIds = mMarathon.getMarathonQuestionIds(user);
			UUID marathonId = UUID.randomUUID();

			// smestamo u sesiju
			storeSession.saveMarathon(marathonId.toString(), questionIds);

			loadedMarathon = storeSession.loadMarathon();
		}

		DrivingCategory drivingCategory = user.getDrivingCategory();

		String drivingCategoryString = drivingCategory != null ? drivingCategory.getName() : "Sve kategorije vožnje.";

		mv.addObject("drivingCategoryName", drivingCategoryString);
		mv.addObject("marathonId", loadedMarathon.getMarathonId());

		return mv;
	}

}
