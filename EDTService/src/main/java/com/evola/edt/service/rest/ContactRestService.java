package com.evola.edt.service.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.evola.edt.component.RecaptchaComponent;
import com.evola.edt.mail.EmailSender;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.service.DrivingSchoolService;

/**
 * @author Daci 06.12.2013.
 * 
 */
@Path("/kontakt")
@Named
public class ContactRestService extends AbstractRestService {

	@Inject
	private EmailSender emailSender;

	@Value("${mail.from}")
	private String emailFrom;

	@Value("${mail.to}")
	private String emailTo;

	@Value("${applicationUrl}")
	private String applicationUrl;

	@Value("${logoImg}")
	private String logoImg;

	@Value("${envImg}")
	private String envImg;

	@Inject
	private DrivingSchoolService drivingSchoolService;

	@Inject
	private RecaptchaComponent recaptcha;

	@POST
	@Path("/posalji-mail")
	@Produces("application/json")
	public Response sendEmail(@FormParam("inputName") String userName, @FormParam("inputEmail") String userEmail,
			@FormParam("inputSubject") String subject, @FormParam("comment") String comment,
			@FormParam("recaptcha_challenge_field") String recaptcha_challenge_field,
			@FormParam("recaptcha_response_field") String recaptcha_response_field) {

		try {

			recaptcha.check(recaptcha_challenge_field, recaptcha_response_field);

			StringTokenizer st = new StringTokenizer(emailTo, ";");
			String[] recipients = new String[st.countTokens()];

			for (int i = 0; i < (st.countTokens() + 1); i++) {
				recipients[i] = st.nextToken();
			}

			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(emailFrom);
			simpleMailMessage.setTo(recipients);
			simpleMailMessage.setSubject("Poruka sa sajta VozaciSrbije.com");
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("userName", userName);
			variables.put("userEmail", userEmail);
			variables.put("subject", subject);
			variables.put("comment", comment);
			variables.put("companyLogo", applicationUrl + logoImg);

			emailSender.send(simpleMailMessage, "contact.vm", variables);

			ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.OK);

			return Response.ok().entity(status).build();

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);

			return Response.serverError().entity(status).build();
		}

	}

	/**
	 * @author Daci 03.03.2014.
	 */
	@POST
	@Path("/posalji-mail-auto-skola")
	@Produces("application/json")
	public Response sendEmailDrivingSchool(@FormParam("id") String id, @FormParam("inputName") String userName,
			@FormParam("inputEmail") String userEmail, @FormParam("comment") String comment,
			@FormParam("recaptcha_challenge_field") String recaptcha_challenge_field,
			@FormParam("recaptcha_response_field") String recaptcha_response_field) {

		try {

			recaptcha.check(recaptcha_challenge_field, recaptcha_response_field);

			// nadji e-mail
			DrivingSchoolDTO drivingSchool = drivingSchoolService.findDrivingSchoolById(Long.parseLong(id));
			String drivingSchoolEmail = drivingSchool.getEmail();

			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(emailFrom);
			simpleMailMessage.setTo(drivingSchoolEmail);
			simpleMailMessage.setSubject("Poruka sa sajta VozaciSrbije.com");
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("userName", userName);
			variables.put("userEmail", userEmail);
			variables.put("comment", comment);
			variables.put("companyLogo", applicationUrl + logoImg);

			emailSender.send(simpleMailMessage, "contactDrivingSchool.vm", variables);

			ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.OK);

			return Response.ok().entity(status).build();

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);

			return Response.serverError().entity(status).build();
		}

	}
}
