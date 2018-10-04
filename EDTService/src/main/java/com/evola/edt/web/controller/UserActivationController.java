package com.evola.edt.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.service.UserService;

/**
 * @author Nikola 12.05.2013.
 * 
 */
@Controller
public class UserActivationController {
	
	Logger log = Logger.getLogger(getClass());
	
	@Inject
	UserService userService;

	@RequestMapping(method = RequestMethod.GET, value = "/activateUser")
	public ModelAndView activateUser(@RequestParam("token") String token) {
		String view = "activateUserPage";
		Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("pageInfo", new PageInfo("activateUser",
                "Aktiviranje korisnika", PageCategories.GLAVNA));
		try {
			Assert.notNull(token, "Token can't be null");
			userService.activateUser(token);
			modelMap.put("confirmed", true);
           return new ModelAndView(view, modelMap);
		} catch (Exception e) {
			log.error(e, e);
			modelMap.put("confirmed", false);
           return new ModelAndView(view, modelMap);
		}
	}
}
