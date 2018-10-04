package com.evola.edt.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.service.UserService;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;

/**
 * @author Nikola 18.05.2013.
 * 
 */
@Controller
public class ResetPasswordController {
	Logger log = Logger.getLogger(getClass());
	@Inject
	UserService userService;

	@RequestMapping(method = RequestMethod.GET, value = "/resetPassword")
	public ModelAndView resetPassword(
			@RequestParam(value = "token", required = true) String token) {
		
//		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("resetPasswordPage");
		
		try {
			
			Boolean tokenValid = userService.isResetPasswordTokenValid(token);
//			modelMap.put("tokenValid", tokenValid);
			mv.addObject("tokenValid", tokenValid);
			
		} catch (Exception e) {

			log.error(e, e);
			mv.addObject("tokenValid", "false");
//			modelMap.put("tokenValid", false);
		}

//		mv.addAllObjects(modelMap);
//		mv.addObject(modelMap);
		
		mv.addObject("pageInfo", new PageInfo("reset-sifre",
				"Resetovanje sifre", PageCategories.GLAVNA));
		
		
		
		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/resetuj-sifru")
	public ModelAndView askResetPasswordPage() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("askResetPasswordPage");
		mv.addObject("pageInfo", new PageInfo("resetPassword",
				"Resetovanje sifre", PageCategories.GLAVNA));
		
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/zaboravljeno-korisnicko-ime")
	public ModelAndView forgottenUsername() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("forgottenUsernamePage");
		mv.addObject("pageInfo", new PageInfo("forgottenUsername",
				"Zaboravljeno korisničko ime", PageCategories.GLAVNA));
		
		return mv;
	}
}
