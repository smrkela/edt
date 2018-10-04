package com.evola.edt.service.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;

import com.evola.edt.model.dto.DrivingSchoolCarDTO;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolEmployeeDTO;
import com.evola.edt.model.dto.DrivingSchoolMarkDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.DrivingSchoolStudentDTO;
import com.evola.edt.model.dto.PriceListDTO;
import com.evola.edt.model.dto.UserDrivingSchoolMembershipRequestDTO;
import com.evola.edt.service.AdministrationService;
import com.evola.edt.service.DrivingSchoolAdministrationService;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.UserDrivingSchoolMembershipRequestService;
import com.evola.edt.service.dto.UploadImagesDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;

@Path("/drivingSchoolAdministration")
@Named
public class DrivingSchoolAdministrationRestService extends AbstractRestService {

	@Inject
	private DrivingSchoolService service;

	@Inject
	private DrivingSchoolAdministrationService adminService;
	
	@Inject
	private AdministrationService administrationService;
	
	@Inject
	private UserDrivingSchoolMembershipRequestService userDrivingSchoolMembershipRequestService;

	@POST
	@Path("/updateBasicInfo")
	@Consumes("application/json")
	public Response updateBasicInfo(DrivingSchoolDTO pageDto) {

		try {

			adminService.saveDrivingSchool(pageDto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/addDrivingSchoolEmployee")
	public Response editDrivingSchool(DrivingSchoolEmployeeDTO dto) {

		try {

			if (dto.isNew())
				adminService.addNewEmployee(dto);
			else
				adminService.updateEmployee(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}
	
	@POST
	@Path("/addDrivingSchoolStudent")
	public Response editDrivingStudent(DrivingSchoolStudentDTO dto) {

		try {

			if (dto.isNew())
				adminService.addNewStudent(dto);
			else
				adminService.updateStudent(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/addDrivingSchoolNotification")
	@Consumes("application/json")
	public Response editDrivingSchool(DrivingSchoolNotificationDTO dto) {

		try {

			if (dto.isNew())
				adminService.addNewNotification(dto);
			else
				adminService.updateNotification(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/addDrivingSchoolCar")
	@Consumes("application/json")
	public Response editDrivingSchool(DrivingSchoolCarDTO dto) {

		try {

			if (dto.isNew())
				adminService.addNewCar(dto);
			else
				adminService.updateCar(dto);

			return createOkResponse();

		} catch (Exception e) {

			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/addDrivingSchoolAlbum")
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
	@Path("/updateDrivingSchoolAlbumImage")
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
	@Path("/addDrivingSchoolAlbumImages")
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
	@Path("/addDrivingSchoolMark")
	@Consumes("application/json")
	public Response addDrivingSchoolMark(DrivingSchoolMarkDTO dto) {
		try {
			adminService.addDrivingSchoolMark(dto);
			/**
			 * Daci, 03.01.2014.
			 */
			administrationService.calculateAverageMark(dto.getSchool().getId());
			return createOkResponse();
		} catch (Exception e) {
			return createNotOkResponse(e);
		}
	}

	@DELETE
	@Path("/removeDrivingSchoolMark")
	@Produces("application/json")
	@PreAuthorize(value = "isAuthenticated()")
	public Response removeDrivingSchoolMark(@FormParam("id") Long id) {
		try {
			adminService.removeDrivingSchoolMark(id);
			return createOkResponse();

		} catch (Exception e) {
			return createNotOkResponse(e);
		}
	}

	@POST
	@Path("/addDrivingSchoolPriceList")
	@Consumes("application/json")
	public Response addDrivingSchoolPriceList(PriceListDTO dto) {
		try {
			adminService.addPriceList(dto);
			/**
			 * Daci, 03.01.2014.
			 */
			administrationService.calculateCategoryBPrice(dto.getDrivingSchoolDTO().getId());
			return createOkResponse();
		} catch (Exception e) {
			return createNotOkResponse(e);
		}
	}
	
	/**
	 * Daci, 27.01.2015.
	 */
	@POST
	@Path("/membershipRequestDecision")
	public Response membershipRequestDecision(	@FormParam("token") String token, 
												@FormParam("decisionComment") String decisionComment,
												@FormParam("decision") String decision) {
		try {
		
			userDrivingSchoolMembershipRequestService.membershipRequestDecision(token, decisionComment, decision.toUpperCase());
			
			return createOkResponse();
		} catch (Exception e) {
			return createNotOkResponse(e);
		}
		
	}
	
	/**
	 * Daci, 02.02.2015.
	 */
	@POST
	@Path("/connectToExistingStudent")
	public Response connectToExistingStudent(@FormParam("membershipRequestId") Long membershipRequestId, 
											 @FormParam("studentId") Long studentId) {
		try {
			adminService.connectToExistingStudent(membershipRequestId, studentId);
			
			return createOkResponse();
		} catch (Exception e) {
			return createNotOkResponse(e);
		}
		
	}
	
	/**
	 * Daci, 08.02.2015.
	 */
	@POST
	@Path("/removeStudentFromMembership")
	public Response removeStudentFromMembership(@FormParam("membershipRequestToken") String membershipRequestToken) {
		try {
			userDrivingSchoolMembershipRequestService.removeStudentFromMembership(membershipRequestToken);
			return createOkResponse();
		} catch (Exception e) {
			return createNotOkResponse(e);
		}
		
	}

	/**
	 * Daci, 18.02.2015.
	 */
	@POST
	@Path("/deleteMembershipRequest")
	public Response deleteMembershipRequest(@FormParam("id") Long id,
											@FormParam("token") String token) {
		try {
			userDrivingSchoolMembershipRequestService.deleteMembershipRequest(id, token);
			return createOkResponse();
		} catch (Exception e) {
			return createNotOkResponse(e);
		}
		
	}
	
	
	/**
	 * Daci, 19.02.2015.
	 */
	@POST
	@Path("/updateReceiveNotifications")
	public Response updateReceiveNotifications( @FormParam("decision") Long decision,
												@FormParam("token") String token) {
		try {
			
			userDrivingSchoolMembershipRequestService.updateReceiveNotifications(decision, token);
			return createOkResponse();
			
		} catch (Exception e) {
			return createNotOkResponse(e);
		}
		
	}
}
