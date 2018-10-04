package com.evola.edt.web.controller;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.FormParam;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.component.PageMetadataManager;
import com.evola.edt.model.dto.PageCategoryDTO;
import com.evola.edt.model.dto.PageCommentDTO;
import com.evola.edt.model.dto.PageDTO;
import com.evola.edt.service.PageCategoryService;
import com.evola.edt.service.PageService;
import com.evola.edt.service.UserService;
import com.evola.edt.service.dto.PagesDTO;
import com.evola.edt.utils.sorters.PageCategoryOrderIndexSorter;
import com.evola.edt.utils.sorters.PageOrderIndexSorter;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * @author Nikola 10.06.2013.
 * 
 */
@Controller
public class PageController {

	@Inject
	private PageService pageService;

	@Inject
	private PageCategoryService pageCategoryService;

	@Inject
	private UserService userService;

	@Inject
	private UserCredentialsManager credentialsManager;
	
	@Inject
	private PageMetadataManager pageManager;

	@RequestMapping(method = RequestMethod.GET, value = "/vesti/{uniqueName}")
	public ModelAndView page(@PathVariable("uniqueName") String uniqueName) {

		PageDTO page = pageService.findPageByUniqueName(uniqueName);

		// i uzimamo poslednji par vesti za widget
		PagesDTO recentdto = pageService.findNews(0, 5);

		// i uzimamo poslednjih par komentara za widget
		List<PageCommentDTO> latestComments = pageService.findLatestComments(5);

		// uzimamo komentare stranice
		List<PageCommentDTO> comments = pageService.findComments(page.getId());

		// osvezavamo broj komentara, ovaj podatak nemamo u stranici automatski
		page.setNumOfComments(comments.size());

		ModelAndView mv = new ModelAndView("pagePage");
		mv.addObject("page", page);
		mv.addObject("comments", comments);
		mv.addObject("recentPages", recentdto.getPages());
		mv.addObject("recentComments", latestComments);
		
		pageManager.addPage(mv, page.getUniqueName(), page.getTitle(), page.getSmallPreview(), page.getId());

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/vesti")
	public ModelAndView news(@RequestParam(value = "startingIndex", required = false) Integer startingPage) {

		if (startingPage == null || startingPage < 0)
			startingPage = 0;

		int count = 10;

		// uzimamo stranicene vesti
		PagesDTO dto = pageService.findNews(startingPage, count);

		// i uzimamo poslednji par vesti za widget
		PagesDTO recentdto = pageService.findNews(0, 5);

		// i uzimamo poslednjih par komentara za widget
		List<PageCommentDTO> latestComments = pageService.findLatestComments(5);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("pages");
		mav.addObject("pages", dto.getPages());
		mav.addObject("recentPages", recentdto.getPages());
		mav.addObject("recentComments", latestComments);
		mav.addObject("isFirstPage", dto.getIsFirstPage());
		mav.addObject("isLastPage", dto.getIsLastPage());
		mav.addObject("nextIndex", startingPage + 1);
		mav.addObject("previousIndex", startingPage - 1);
		
		pageManager.addPagesPage(mav);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/informacije")
	public ModelAndView informations() {

		// uzimaju se sve kategorije koje su tipa 'INFORMATION' 
		List<PageCategoryDTO> categories = pageCategoryService.findAllForInformations();
		
		// sortiranje liste prema orderIndex-u
		Collections.sort(categories, new PageCategoryOrderIndexSorter());

		// sada treba da uzmemo sve stranice koje za tip kategorije imaju 'INFORMATION' i grupisemo ih po ovim kategorijama
		PagesDTO informations = pageService.findInformations(0, 1000);

		// grupisanje po kategorijama
		for (PageDTO page : informations.getPages()) {

			for (PageCategoryDTO category : categories) {

				if (category.equals(page.getCategoryDTO())) {

					category.getPages().add(page);
				}
			}
		}

		ModelAndView mv = new ModelAndView("informationsPage");
		mv.addObject("pageCategories", categories);
		
		pageManager.addInformationsPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/informacije/{uniqueName}")
	public ModelAndView informationsPage(@PathVariable("uniqueName") String uniqueName) {

		PageDTO page = pageService.findPageByUniqueName(uniqueName);

		PageCategoryDTO categoryDTO = pageCategoryService.findById(page.getCategoryDTO().getId());

		// ucitavamo i listu ostalih stranica u tekucoj kategoriji
		List<PageDTO> pages = pageService.findPageByCategory(page.getCategoryDTO().getId());
		
		// sortiranje liste stranica prema orderIndex-u
		Collections.sort(pages, new PageOrderIndexSorter());
		
		boolean showFBButtons = true;

		ModelAndView mv = new ModelAndView("informationPage");
		mv.addObject("page", page);
		mv.addObject("pages", pages);
		mv.addObject("category", categoryDTO);
		mv.addObject("showFBButtons", showFBButtons); // da li da se prikazu ili ne Like i Share dugmici na stranici
		
		// izvlace se 3 prve recenice za fbDescription
		String content = Jsoup.parse(page.getContent()).text();
		int position = content.indexOf('.', 1 + content.indexOf('.', 1 + content.indexOf('.')));
		content = content.substring(0, position + 1);
		
		pageManager.addInformationPage(mv, page.getUniqueName(), page.getTitle(), content);

		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/pomoc")
	public ModelAndView help() {

		// uzimaju se sve kategorije koje su tipa 'HELP' 
		List<PageCategoryDTO> categories = pageCategoryService.findAllForHelp();
				
		// sortiranje liste prema orderIndex-u
		Collections.sort(categories, new PageCategoryOrderIndexSorter());
				
		// sada treba da uzmemo sve stranice i grupisemo ih po ovim kategorijama
		PagesDTO informations = pageService.findHelps(0, 1000);

		for (PageDTO page : informations.getPages()) {

			for (PageCategoryDTO category : categories) {

				if (category.equals(page.getCategoryDTO())) {

					category.getPages().add(page);
				}
			}
		}

		ModelAndView mv = new ModelAndView("helpsPage");
		mv.addObject("pageCategories", categories);
		
		pageManager.addHelpsPage(mv);

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/pomoc/{uniqueName}")
	public ModelAndView helpPage(@PathVariable("uniqueName") String uniqueName) {

		PageDTO page = pageService.findPageByUniqueName(uniqueName);

		PageCategoryDTO categoryDTO = pageCategoryService.findById(page.getCategoryDTO().getId());

		// ucitavamo i listu ostalih stranica u tekucoj kategoriji
		List<PageDTO> pages = pageService.findPageByCategory(page.getCategoryDTO().getId());

		ModelAndView mv = new ModelAndView("helpPage");
		mv.addObject("page", page);
		mv.addObject("pages", pages);
		mv.addObject("category", categoryDTO);
		
		pageManager.addHelpPage(mv, page.getUniqueName(), page.getTitle());

		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/savePageComment")
	public ModelAndView saveNotificationComment(@FormParam("pageUniqueName") String pageUniqueName,
			@FormParam("comment") String comment) {

		if (!credentialsManager.isUserLoggedIn())
			return credentialsManager.getForbiddenMAV();

		pageService.savePageComment(pageUniqueName, comment);

		return page(pageUniqueName);
	}

}
