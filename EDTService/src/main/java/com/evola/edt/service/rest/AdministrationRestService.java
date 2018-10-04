package com.evola.edt.service.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.evola.edt.component.RealTestManager;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolMemberDTO;
import com.evola.edt.model.dto.DrivingSchoolSiteLicenseDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.model.dto.UserDrivingSchoolMembershipRequestDTO;
import com.evola.edt.service.AdministrationService;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.PageService;
import com.evola.edt.service.QuestionService;
import com.evola.edt.service.UserDrivingSchoolMembershipRequestService;
import com.evola.edt.service.UserService;
import com.evola.edt.service.dto.UploadImagesDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.service.dto.helpers.RealTestCreationDTO;

@Path("/admin")
@Named
public class AdministrationRestService extends AbstractRestService {

	@Inject
	private PageService pageService;

	@Inject
	private QuestionService questionService;

	@Inject
	DrivingSchoolService drivingSchoolService;

	@Inject
	AdministrationService adminService;

	@Inject
	UserService userService;

	@Inject
	UserDrivingSchoolMembershipRequestService userDrivingSchoolMembershipRequestService;

	@POST
	@Path("/saveQuestion")
	@Consumes("application/json")
	public Response saveQuestion(QuestionDTO dto) {

		try {

			if (dto.isNew())
				questionService.addNew(dto);
			else
				questionService.update(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/saveDrivingSchool")
	@Consumes("application/json")
	public Response saveDrivingSchool(DrivingSchoolDTO dto) {

		try {

			if (dto.isNew())
				drivingSchoolService.addNew(dto);
			else
				drivingSchoolService.update(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/addImageAlbum")
	@Consumes("application/json")
	public Response editDrivingSchoolAlbum(DocumentDTO dto) {

		try {

			if (dto.isNew())
				adminService.addNewAlbum(dto);
			else
				adminService.updateAlbum(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/updateImageAlbumImage")
	@Consumes("application/json")
	public Response editDrivingSchoolAlbumImage(DocumentDTO dto) {

		try {

			adminService.updateAlbumImage(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/addImageAlbumImages")
	@Consumes("application/json")
	public Response addDrivingSchoolAlbumImages(UploadImagesDTO dto) {

		try {

			adminService.addImagesToAlbum(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/saveDrivingSchoolMember")
	@Consumes("application/json")
	public Response saveDrivingSchoolMember(DrivingSchoolMemberDTO dto) {

		try {

			if (dto.isNew())
				drivingSchoolService.addNewMember(dto);
			else
				drivingSchoolService.updateMember(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/saveDrivingSchoolLicense")
	@Consumes("application/json")
	public Response saveDrivingSchoolLicense(DrivingSchoolSiteLicenseDTO dto) {

		try {

			drivingSchoolService.saveDrivingSchoolLicense(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/updateMyProfile")
	@Consumes("application/json")
	public Response updateMyProfile(UserDTO dto) {

		try {

			userService.updateUserProfile(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	/**
	 * @param dto
	 * @return
	 * @author Daci, 11.01.2015.
	 */
	@POST
	@Path("/submitRequestForMembership")
	@Consumes("application/json")
	public Response submitRequestForMembership(UserDrivingSchoolMembershipRequestDTO dto) {

		try {

			userDrivingSchoolMembershipRequestService.submitRequestForMembership(dto);

			return createOkResponse();

		} catch (Exception e) {
			System.out.println(e.getMessage());

			return createNotOkResponse(e);
		}
	}

	/**
	 * @author Daci, 11.01.2014.
	 */
	@POST
	@Path("/recalculateAverageMarks")
	@Produces("application/json")
	public Response recalculateAverageMarks() {
		try {
			boolean executionResult = adminService.recalculateAverageMarkForAllSchools();
			ExecutionStatus status = null;

			if (executionResult) {
				status = new ExecutionStatus(ExecutionStatus.Status.OK);
			} else {
				status = new ExecutionStatus("Nije uradjen update prosecnih ocena svih skola!", ExecutionStatus.Status.NOTOK);
			}

			return Response.ok().entity(status).build();
		} catch (Exception e) {
			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);
			return Response.serverError().entity(status).build();
		}
	}

	/**
	 * @author Daci, 11.01.2014.
	 */
	@POST
	@Path("/recalculateCategoryBPrice")
	@Produces("application/json")
	public Response recalculateCategoryBPrice() {
		try {
			boolean executionResult = adminService.recalculateCategoryBPriceForAllSchools();
			ExecutionStatus status = null;

			if (executionResult) {
				status = new ExecutionStatus(ExecutionStatus.Status.OK);
			} else {
				status = new ExecutionStatus("Nije uradjen update cena za polaganje za B kategoriju kod svih skola!", ExecutionStatus.Status.NOTOK);
			}

			return Response.ok().entity(status).build();
		} catch (Exception e) {
			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);
			return Response.serverError().entity(status).build();
		}
	}

	@POST
	@Path("/generateDailyTest")
	@Produces("application/json")
	public Response generateDailyTest() {
		try {

			String executionResult = adminService.generateDailyTest();

			ExecutionStatus status = new ExecutionStatus(executionResult, ExecutionStatus.Status.OK);

			return Response.ok().entity(status).build();

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);

			return Response.serverError().entity(status).build();
		}
	}

	@POST
	@Path("/updateUserExperience")
	@Produces("application/json")
	public Response updateUserExperience() {
		try {

			String executionResult = adminService.updateUserExperience();

			ExecutionStatus status = new ExecutionStatus(executionResult, ExecutionStatus.Status.OK);

			return Response.ok().entity(status).build();

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);

			return Response.serverError().entity(status).build();
		}
	}

	@POST
	@Path("/saveRealTest")
	@Consumes("application/json")
	public Response saveRealTest(RealTestCreationDTO dto) {

		try {
			
			String result = adminService.saveRealTest(dto);

			ExecutionStatus status = new ExecutionStatus(result, ExecutionStatus.Status.OK);

			return Response.ok().entity(status).build();

		} catch (Exception e) {
			System.out.println(e.getMessage());

			return createNotOkResponse(e);
		}
	}
}
