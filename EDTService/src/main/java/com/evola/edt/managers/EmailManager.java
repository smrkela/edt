package com.evola.edt.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.evola.edt.mail.EmailSender;
import com.evola.edt.model.User;
import com.evola.edt.service.rest.ExecutionStatus;
import com.evola.edt.web.security.TokenUtil;

@Component
public class EmailManager {

	@Value("${mail.from}")
	private String emailFrom;

	@Value("${mail.to}")
	private String emailTo;

	@Value("${registrationTokenMaxAgeInHours}")
	private String registrationTokenMaxAgeInHours;

	@Value("${applicationUrl}")
	private String applicationUrl;

	@Value("${logoImg}")
	private String logoImg;

	@Value("${envImg}")
	private String envImg;
	
	@Value("${mailImagesCell}")
	private String mailImagesCell;
	
//	@Value("${mailImagesActivateAccount}")
//	private String mailImagesActivateAccount;
	
	@Value("${mailImagesInfo}")
	private String mailImagesInfo;
	
	@Value("${mailImagesLearning}")
	private String mailImagesLearning;
	
	@Value("${mailImagesApp}")
	private String mailImagesApp;
	
	@Value("${mailImagesContact}")
	private String mailImagesContact;
	
	@Value("${mailImagesHelp}")
	private String mailImagesHelp;
	
	@Value("${mailImagesForum}")
	private String mailImagesForum;
	
	@Value("${mailImagesFb}")
	private String mailImagesFb;
	
	@Value("${mailImagesTw}")
	private String mailImagesTw;
	
	@Value("${mailImagesGo}")
	private String mailImagesGo;
	
	@Value("${mailImagesLogo}")
	private String mailImagesLogo;
	
	@Value("${mailLineLeft}")
	private String mailLineLeft;
	
	@Value("${mailLineTop}")
	private String mailLineTop;
	
	@Value("${mailLineDown}")
	private String mailLineDown;
	
	@Value("${mailLineRight}")
	private String mailLineRight;
	
//	@Value("${mailChangePassword}")
//	private String mailChangePassword;
	
	@Value("${mailImagesRegister}")
	private String mailImagesRegister;
	
//	@Value("${confirmNotification}")
//	private String confirmNotification;
	
	@Value("${mailImagesFbLogo}")
	private String mailImagesFbLogo;
	
	@Value("${mailImagesGoLogo}")
	private String mailImagesGoLogo;
	
	@Value("${mailImagesTwLogo}")
	private String mailImagesTwLogo;

	@Inject
	private EmailSender mailSender;

	public void sendRegistrationEmail(User user, String activationToken, String plainPassword, ExecutionStatus forumStatus) {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Registracija uspešna");
		Map<String, Object> variables = new HashMap<String, Object>();
				
		variables.put("name", user.getFirstName());
		variables.put("activationLink", applicationUrl + "activateUser?token=" + activationToken);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
//		variables.put("mailImagesActivateAccount", applicationUrl + mailImagesActivateAccount);
		variables.put("mailImagesInfo", applicationUrl + mailImagesInfo);
		variables.put("mailImagesLearning", applicationUrl + mailImagesLearning);
		variables.put("mailImagesApp", applicationUrl + mailImagesApp);
		variables.put("mailImagesContact", applicationUrl + mailImagesContact);
		variables.put("mailImagesHelp", applicationUrl + mailImagesHelp);
		variables.put("mailImagesForum", applicationUrl + mailImagesForum);
		variables.put("mailImagesFb", applicationUrl + mailImagesFb);
		variables.put("mailImagesTw", applicationUrl + mailImagesTw);
		variables.put("mailImagesGo", applicationUrl + mailImagesGo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);
		

		// slanje maila u zavisnosti od uspesnosti registracije na Forumu
		if (forumStatus.getStatus() == ExecutionStatus.Status.OK) {

			mailSender.send(simpleMailMessage, "registration.vm", variables);

		} else {
			
			mailSender.send(simpleMailMessage, "registrationForumFailed.vm", variables);

			StringTokenizer st = new StringTokenizer(emailTo, ";");
			String[] recipients = new String[st.countTokens()];

			for (int i = 0; i < (st.countTokens() + 1); i++) {
				recipients[i] = st.nextToken();
			}

			// slanje maila administratorima za rucnu aktivaciju na forumu

			SimpleMailMessage errorMailSender = new SimpleMailMessage();

			errorMailSender.setFrom(emailFrom);
			errorMailSender.setTo(recipients);
			errorMailSender.setSubject("Registracija novog korisnika na Forumu nesupešna");
			Map<String, Object> variablesFailedForum = new HashMap<String, Object>();
			variablesFailedForum.put("firstName", user.getFirstName());
			variablesFailedForum.put("lastName", user.getLastName());
			variablesFailedForum.put("username", user.getUsername());
			variablesFailedForum.put("password", plainPassword);
			variablesFailedForum.put("userEmail", user.getEmail());
			variablesFailedForum.put("isMale", user.getIsMale());
			variablesFailedForum.put("error", forumStatus.getMessage().toString());
			variablesFailedForum.put("companyLogo", applicationUrl + logoImg);

			mailSender.send(errorMailSender, "registrationForumFailedToAdmin.vm", variablesFailedForum);
		}
	}
	
	public void sendSocialRegistrationEmail(User user, ExecutionStatus forumStatus, String plainPassword) {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Registracija uspešna preko Facebook-a");
		Map<String, Object> variables = new HashMap<String, Object>();	
		
		variables.put("name", user.getFirstName());
		variables.put("password", plainPassword);
		variables.put("username", user.getUsername());
		variables.put("mailLineLeft", applicationUrl + mailLineLeft);
		variables.put("mailLineTop", applicationUrl + mailLineTop);
		variables.put("mailLineDown", applicationUrl + mailLineDown);
		variables.put("mailLineRight", applicationUrl + mailLineRight);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
		variables.put("mailImagesInfo", applicationUrl + mailImagesInfo);
		variables.put("mailImagesLearning", applicationUrl + mailImagesLearning);
		variables.put("mailImagesApp", applicationUrl + mailImagesApp);
		variables.put("mailImagesContact", applicationUrl + mailImagesContact);
		variables.put("mailImagesHelp", applicationUrl + mailImagesHelp);
		variables.put("mailImagesForum", applicationUrl + mailImagesForum);
		variables.put("mailImagesFb", applicationUrl + mailImagesFb);
		variables.put("mailImagesTw", applicationUrl + mailImagesTw);
		variables.put("mailImagesGo", applicationUrl + mailImagesGo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);

		// slanje maila u zavisnosti od uspesnosti registracije na Forumu
		if (forumStatus.getStatus() == ExecutionStatus.Status.OK) {

			mailSender.send(simpleMailMessage, "registrationSocial.vm", variables);

		} else {
			
			mailSender.send(simpleMailMessage, "registrationSocial.vm", variables);

			StringTokenizer st = new StringTokenizer(emailTo, ";");
			String[] recipients = new String[st.countTokens()];

			for (int i = 0; i < (st.countTokens() + 1); i++) {
				recipients[i] = st.nextToken();
			}

			// slanje maila administratorima za rucnu aktivaciju na forumu

			SimpleMailMessage errorMailSender = new SimpleMailMessage();

			errorMailSender.setFrom(emailFrom);
			errorMailSender.setTo(recipients);
			errorMailSender.setSubject("Registracija novog korisnika na Forumu nesupešna");
			Map<String, Object> variablesFailedForum = new HashMap<String, Object>();
			variablesFailedForum.put("firstName", user.getFirstName());
			variablesFailedForum.put("lastName", user.getLastName());
			variablesFailedForum.put("username", user.getUsername());
			variablesFailedForum.put("password", plainPassword);
			variablesFailedForum.put("userEmail", user.getEmail());
			variablesFailedForum.put("isMale", user.getIsMale());
			variablesFailedForum.put("error", forumStatus.getMessage().toString());
			variablesFailedForum.put("companyLogo", applicationUrl + logoImg);

			mailSender.send(errorMailSender, "registrationForumFailedToAdmin.vm", variablesFailedForum);
		}
	}
	
	public String sendResetPasswordEmail(User user) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Zahtev za promenom šifre");
		
		Map<String, Object> variables = new HashMap<String, Object>();
		
		variables.put("name", user.getFirstName());
		
		String generateToken = TokenUtil.generateToken();
		
		variables.put("resetLink", applicationUrl + "resetPassword?token=" + generateToken);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
//		variables.put("mailChangePassword", applicationUrl + mailChangePassword);
		variables.put("mailImagesInfo", applicationUrl + mailImagesInfo);
		variables.put("mailImagesLearning", applicationUrl + mailImagesLearning);
		variables.put("mailImagesApp", applicationUrl + mailImagesApp);
		variables.put("mailImagesContact", applicationUrl + mailImagesContact);
		variables.put("mailImagesHelp", applicationUrl + mailImagesHelp);
		variables.put("mailImagesForum", applicationUrl + mailImagesForum);
		variables.put("mailImagesFb", applicationUrl + mailImagesFb);
		variables.put("mailImagesTw", applicationUrl + mailImagesTw);
		variables.put("mailImagesGo", applicationUrl + mailImagesGo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);
		
		mailSender.send(simpleMailMessage, "resetPassword.vm", variables);
		
		return generateToken;
	}

	public void sendForgottenUsername(User user) {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Zaboravljeno korisničko ime");
				
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("name", user.getFirstName());
		variables.put("username", user.getUsername());
		variables.put("mailLineLeft", applicationUrl + mailLineLeft);
		variables.put("mailLineTop", applicationUrl + mailLineTop);
		variables.put("mailLineDown", applicationUrl + mailLineDown);
		variables.put("mailLineRight", applicationUrl + mailLineRight);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
		variables.put("mailImagesInfo", applicationUrl + mailImagesInfo);
		variables.put("mailImagesLearning", applicationUrl + mailImagesLearning);
		variables.put("mailImagesApp", applicationUrl + mailImagesApp);
		variables.put("mailImagesContact", applicationUrl + mailImagesContact);
		variables.put("mailImagesHelp", applicationUrl + mailImagesHelp);
		variables.put("mailImagesForum", applicationUrl + mailImagesForum);
		variables.put("mailImagesFb", applicationUrl + mailImagesFb);
		variables.put("mailImagesTw", applicationUrl + mailImagesTw);
		variables.put("mailImagesGo", applicationUrl + mailImagesGo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);

		mailSender.send(simpleMailMessage, "forgottenUsername.vm", variables);
	}
	
	public void sendForumResetPasswordFail(String password, User user, ExecutionStatus status) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Promena šifre neuspešna na Forumu VozaciSrbije.com");
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("name", user.getFirstName());
		variables.put("companyLogo", applicationUrl + logoImg);

		mailSender.send(simpleMailMessage, "resetPasswordFailed.vm", variables);

		// mail za administratore
		StringTokenizer st = new StringTokenizer(emailTo, ";");
		String[] recipients = new String[st.countTokens()];

		for (int i = 0; i < (st.countTokens() + 1); i++) {
			recipients[i] = st.nextToken();
		}

		// slanje maila administratorima za rucnu aktivaciju na forumu
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(recipients);
		simpleMailMessage.setSubject("Promena šifre korisnika na Forumu nesupešna");
		Map<String, Object> variablesFailedForum = new HashMap<String, Object>();
		variablesFailedForum.put("firstName", user.getFirstName());
		variablesFailedForum.put("lastName", user.getLastName());
		variablesFailedForum.put("username", user.getUsername());
		variablesFailedForum.put("password", password);
		variablesFailedForum.put("userEmail", user.getEmail());
		variablesFailedForum.put("isMale", user.getIsMale());
		variablesFailedForum.put("error", status.getMessage().toString());
		variablesFailedForum.put("companyLogo", applicationUrl + logoImg);

		mailSender.send(simpleMailMessage, "resetPasswordFailedToAdmin.vm", variablesFailedForum);
	}
	
	public void sendForumActivationFail(User user, ExecutionStatus status) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

		StringTokenizer st = new StringTokenizer(emailTo, ";");
		String[] recipients = new String[st.countTokens()];

		for (int i = 0; i < (st.countTokens() + 1); i++) {
			recipients[i] = st.nextToken();
		}

		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(recipients);
		simpleMailMessage.setSubject("Aktivacija novog korisnika na Forumu nesupešna");
		Map<String, Object> variablesFailedForum = new HashMap<String, Object>();
		variablesFailedForum.put("firstName", user.getFirstName());
		variablesFailedForum.put("lastName", user.getLastName());
		variablesFailedForum.put("username", user.getUsername());
		variablesFailedForum.put("password", user.getPassword());
		variablesFailedForum.put("userEmail", user.getEmail());
		variablesFailedForum.put("isMale", user.getIsMale());
		variablesFailedForum.put("error", status.getMessage().toString());
		variablesFailedForum.put("companyLogo", applicationUrl + logoImg);

		mailSender.send(simpleMailMessage, "activationForumFailedToAdmin.vm", variablesFailedForum);
	}

	public void sendInvitation(String studentName, String studentEmail, String schoolName){
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(studentEmail);
		simpleMailMessage.setSubject("Poziv autoškole '" + schoolName + "' za priključenje sajtu Vozači Srbije");
		Map<String, Object> variables = new HashMap<String, Object>();
				
		variables.put("name", studentName);
		variables.put("drivingSchoolName", schoolName);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
		variables.put("mailImagesRegister", applicationUrl + mailImagesRegister);
		variables.put("mailImagesInfo", applicationUrl + mailImagesInfo);
		variables.put("mailImagesLearning", applicationUrl + mailImagesLearning);
		variables.put("mailImagesApp", applicationUrl + mailImagesApp);
		variables.put("mailImagesContact", applicationUrl + mailImagesContact);
		variables.put("mailImagesHelp", applicationUrl + mailImagesHelp);
		variables.put("mailImagesForum", applicationUrl + mailImagesForum);
		variables.put("mailImagesFb", applicationUrl + mailImagesFb);
		variables.put("mailImagesTw", applicationUrl + mailImagesTw);
		variables.put("mailImagesGo", applicationUrl + mailImagesGo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);
		
		
		mailSender.send(simpleMailMessage, "inviteToJoinStudent.vm", variables);

	}
	
	public void sendNotification(String studentName, String studentEmail, String schoolName, String notificationText){
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(studentEmail);
		simpleMailMessage.setSubject("Novo obaveštenje autoškole '" + schoolName + "'");
		Map<String, Object> variables = new HashMap<String, Object>();
				
		variables.put("name", studentName);
		variables.put("drivingSchoolName", schoolName);
		variables.put("notificationText", notificationText);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
		variables.put("mailImagesFbLogo", applicationUrl + mailImagesFbLogo);
		variables.put("mailImagesGoLogo", applicationUrl + mailImagesGoLogo);
		variables.put("mailImagesTwLogo", applicationUrl + mailImagesTwLogo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);
		
		
		mailSender.send(simpleMailMessage, "notificationToStudents.vm", variables);

	}
	
	public void sendNotificationWithConfirmation(String studentName, String studentEmail, String schoolName, String notificationText, String token){
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(studentEmail);
		simpleMailMessage.setSubject("Novo obaveštenje autoškole '" + schoolName + "'");
		Map<String, Object> variables = new HashMap<String, Object>();
				
		variables.put("name", studentName);
		variables.put("drivingSchoolName", schoolName);
		variables.put("notificationText", notificationText);
		variables.put("confirmationLink", applicationUrl + "confirmNotification?token=" + token);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
		variables.put("mailImagesFbLogo", applicationUrl + mailImagesFbLogo);
		variables.put("mailImagesGoLogo", applicationUrl + mailImagesGoLogo);
		variables.put("mailImagesTwLogo", applicationUrl + mailImagesTwLogo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);
//		variables.put("confirmNotification", applicationUrl + confirmNotification);
		
		
		
		mailSender.send(simpleMailMessage, "notificationToStudentsWithConfirmation.vm", variables);

	}
	
	public void sendMembershipRequest(String studentName, String studentEmail, String comment, String drivingSchoolEmail, String confirmationToken){
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(drivingSchoolEmail);
		simpleMailMessage.setSubject("Zahtev za članstvo");
		Map<String, Object> variables = new HashMap<String, Object>();
				
		variables.put("name", studentName);
		variables.put("email", studentEmail);
		variables.put("comment", comment);
		variables.put("confirmationLink", applicationUrl + "confirmMembershipRequest?token=" + confirmationToken);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
		variables.put("mailImagesFbLogo", applicationUrl + mailImagesFbLogo);
		variables.put("mailImagesGoLogo", applicationUrl + mailImagesGoLogo);
		variables.put("mailImagesTwLogo", applicationUrl + mailImagesTwLogo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);
//		variables.put("confirmNotification", applicationUrl + confirmNotification);
		
		mailSender.send(simpleMailMessage, "membershipRequest.vm", variables);

	}
	
	public void sendMembershipRequestApproved(String studentName, String studentEmail, String drivingSchoolName){
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(studentEmail);
		simpleMailMessage.setSubject("Zahtev za članstvo odobren");
		Map<String, Object> variables = new HashMap<String, Object>();
				
		variables.put("name", studentName);
		variables.put("drivingSchoolName", drivingSchoolName);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
		variables.put("mailImagesFbLogo", applicationUrl + mailImagesFbLogo);
		variables.put("mailImagesGoLogo", applicationUrl + mailImagesGoLogo);
		variables.put("mailImagesTwLogo", applicationUrl + mailImagesTwLogo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);
		
		mailSender.send(simpleMailMessage, "membershipRequestApproved.vm", variables);

	}
	
	public void sendMembershipRequestRejected(String studentName, String studentEmail, String drivingSchoolName){
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(emailFrom);
		simpleMailMessage.setTo(studentEmail);
		simpleMailMessage.setSubject("Zahtev za članstvo odbijen");
		Map<String, Object> variables = new HashMap<String, Object>();
				
		variables.put("name", studentName);
		variables.put("drivingSchoolName", drivingSchoolName);
		variables.put("mailImagesCell", applicationUrl + mailImagesCell);
		variables.put("mailImagesFbLogo", applicationUrl + mailImagesFbLogo);
		variables.put("mailImagesGoLogo", applicationUrl + mailImagesGoLogo);
		variables.put("mailImagesTwLogo", applicationUrl + mailImagesTwLogo);
		variables.put("mailImagesLogo", applicationUrl + mailImagesLogo);
		
		mailSender.send(simpleMailMessage, "membershipRequestRejected.vm", variables);

	}
}
