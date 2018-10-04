package com.evola.edt.service.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.evola.edt.model.dto.PageCategoryDTO;
import com.evola.edt.model.dto.PageDTO;
import com.evola.edt.model.dto.PageResultDTO;
import com.evola.edt.service.PageCategoryService;
import com.evola.edt.service.PageService;

@Path("/page")
@Named
public class PageRestService extends AbstractRestService {

	@Inject
	private PageService pageService;
	
	@Inject 
	private PageCategoryService pageCategoryService;

	@GET
	@Path("/admin/getPage")
	@Produces("application/json")
	public PageDTO getPage(@QueryParam("pageId") Long pageId) {
		return pageService.findPageById(pageId);
	}

	@GET
	@Path("/getPage")
	@Produces("application/json")
	public PageDTO getPage(@QueryParam("uniqueName") String uniqueName) {
		return pageService.findPageByUniqueName(uniqueName);
	}

	@GET
	@Path("/getAllPages")
	@Produces("application/json")
	public PageResultDTO<PageDTO> getAllPages(
			@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("pageSize") Integer pageSize) {
		return pageService.findAll(pageNumber, pageSize);
	}

	@POST
	@Path("/admin/savePage")
	@Consumes("application/json")
	public Response savePage(PageDTO dto) {
		
		try {

			if (dto.isNew())
				pageService.addNewPage(dto);
			else
				pageService.updatePage(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}
	
	@POST
	@Path("/admin/saveInformations")
	@Consumes("application/json")
	public Response saveInformations(PageDTO dto) {
		
		try {

			if (dto.isNew())
				pageService.addNewInformations(dto);
			else
				pageService.updateInformations(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}
	
	@POST
	@Path("/admin/saveHelp")
	@Consumes("application/json")
	public Response saveHelp(PageDTO dto) {
		
		try {

			if (dto.isNew())
				pageService.addNewHelp(dto);
			else
				pageService.updateHelp(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}
	
	@POST
	@Path("/admin/savePageCategory")
	@Consumes("application/json")
	public Response savePageCategory(PageCategoryDTO dto) {
		
		try {

			if (dto.isNew())
				pageCategoryService.addNew(dto);
			else
				pageCategoryService.update(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

}
