package com.evola.edt.service.rest;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.evola.edt.model.dto.PageCategoryDTO;
import com.evola.edt.service.PageCategoryService;

@Path("/settings")
@Named
public class SettingsRestService extends AbstractRestService  {

	@Inject
	private PageCategoryService pageCategoryService;
	
	@GET
	@Path("/getPageCategories")
	@Produces("application/json")
	public List<PageCategoryDTO> getPage() {
		return pageCategoryService.findAll();
	}
	
	

}
