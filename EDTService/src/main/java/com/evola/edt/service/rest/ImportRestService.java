package com.evola.edt.service.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import com.evola.edt.service.ImportService;

@Path("/import")
@Named
public class ImportRestService {

	Logger log = Logger.getLogger(getClass());

	@Inject
	private ImportService importService;

	@POST
	@Path("/importQuestions")
	@Produces("application/json")
	public void importQuestions(
			@FormParam(value = "questionXMLString") String questionsXMLString) {
		importService.importQuestions(questionsXMLString);
	}

	@POST
	@Path("/importQuestionCategories")
	@Produces("application/json")
	public void importQuestionCategories(
			@FormParam(value = "categoriesXMLString") String categoriesXMLString) {
		importService.importQuestionCategories(categoriesXMLString);
	}

	@POST
	@Path("/importDrivingCategories")
	@Produces("application/json")
	public void importDrivingCategories(
			@FormParam(value = "categoriesXMLString") String categoriesXMLString) {
		importService.importDrivingCategories(categoriesXMLString);
	}

}
