package com.evola.edt.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Nikola 18.05.2013.
 * 
 */
@Controller
public class SchoolInfoController {
	Logger log = Logger.getLogger(getClass());

	// @Inject
	// UserService userService;

	@ModelAttribute("postTest")
	public PostTest getNames() {

		PostTest pt = new PostTest();
		pt.setName("Auto skola GRLE");
		
		return pt;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/informacije_o_profilima_auto_skola")
	public String resetPassword() {

		return "schoolInfoPage";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/informacije_o_profilima_auto_skola")
	public String postTest(@ModelAttribute("postTest") PostTest postTest) {

		return "schoolInfoPage";
	}
	
	public class PostTest{
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
