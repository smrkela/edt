package com.evola.edt.service.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.service.UserService;
import com.evola.edt.service.dto.UserProfileDTO;
import com.evola.edt.web.security.SecurityUtils;

@Path("/user")
@Named
public class UserRestService extends AbstractRestService {

	Logger log = Logger.getLogger(getClass());

	@Inject
	private UserService userService;

	@GET
	@Path("/loadUser")
	@Produces("application/xml")
	public UserProfileDTO loadUser() {
		return userService.loadUser(SecurityUtils.getUserId());
	}

	@GET
	@Path("/getUser")
	@Produces("application/xml")
	public UserDTO getUser() {
		return userService.getUserDTO(SecurityUtils.getUserId());
	}

	@POST
	@Path("/registerUserDTO")
	@Consumes("application/json")
	public void registerUserDTO(UserDTO userDto) {
		userService.registerUserDTO(userDto);
	}

	@POST
	@Path("/askForResetPassword")
	@Produces("application/json")
	public Response askForResetPassword(@FormParam("email") String email,
			@FormParam("recaptcha_challenge_field") String recaptcha_challenge_field,
			@FormParam("recaptcha_response_field") String recaptcha_response_field) {
		try {

			userService.askForResetPassword(email, recaptcha_challenge_field, recaptcha_response_field);

			ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.OK);
			return Response.ok().entity(status).build();

		} catch (Exception e) {
			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);
			return Response.serverError().entity(status).build();
		}
	}

	@POST
	@Path("/sendForgottenUsername")
	@Produces("application/json")
	public Response sendForgottenUsername(@FormParam("email") String email,
			@FormParam("recaptcha_challenge_field") String recaptcha_challenge_field,
			@FormParam("recaptcha_response_field") String recaptcha_response_field) {

		try {

			userService.sendForgottenUsername(email, recaptcha_challenge_field, recaptcha_response_field);

			ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.OK);

			return Response.ok().entity(status).build();

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);
			return Response.serverError().entity(status).build();
		}
	}

	@POST
	@Path("/askForResetPasswordXML")
	@Produces("application/xml")
	public Response askForResetPasswordXML(@FormParam("email") String email) {
		try {

			userService.askForResetPassword(email, null, null);
			ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.OK);
			return Response.ok().entity(status).build();

		} catch (Exception e) {
			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);
			return Response.serverError().entity(status).build();
		}
	}

	@POST
	@Path("/resetPassword")
	@Produces("application/json")
	public Response resetPassword(@FormParam("password") String password, @FormParam("token") String token) {

		try {

			userService.resetPassword(password, token);

			ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.OK);
			return Response.ok().entity(status).build();

		} catch (Exception e) {
			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);
			return Response.serverError().entity(status).build();
		}
	}

	@POST
	@Path("/registerUser")
	@Produces("application/json")
	public Response registerUser(@FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("username") String username, 
			@FormParam("password") String password, @FormParam("email") String email, @FormParam("drivingCategory") Long drivingCategory, 
			@FormParam("isMale") Boolean isMale, @FormParam("recaptcha_challenge_field") String recaptcha_challenge_field,
			@FormParam("recaptcha_response_field") String recaptcha_response_field, @FormParam("signInProvider") String signInProvider) {
		try {

			userService.registerUser(firstName, lastName, username, password, email, drivingCategory, isMale, recaptcha_challenge_field,
					recaptcha_response_field, signInProvider);

			ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.OK);
			return Response.ok().entity(status).build();

		} catch (Exception e) {
			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);
			return Response.serverError().entity(status).build();
		}
	}

	@GET
	@Path("/updateUser")
	@Produces("application/xml")
	public UserProfileDTO updateUser(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
			@QueryParam("email") String email, @QueryParam("isMale") Boolean isMale,
			@QueryParam("questionsPerPage") Integer questionsPerPage, @QueryParam("drivingCategoryId") Long drivingCategoryId) {

		return userService.updateUser(SecurityUtils.getUserId(), firstName, lastName, email, isMale, questionsPerPage, drivingCategoryId);
	}

}
