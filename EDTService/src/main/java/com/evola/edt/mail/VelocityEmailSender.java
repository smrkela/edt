package com.evola.edt.mail;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author Nikola 27.04.2013.
 * 
 */
@Named
public class VelocityEmailSender implements EmailSender {

	private Logger log = Logger.getLogger(getClass());

	@Inject
	private VelocityEngine velocityEngine;
	@Inject
	private JavaMailSender mailSender;

	public VelocityEmailSender() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.evola.edt.mail.EmailSender#send(org.springframework.mail.
	 * SimpleMailMessage, java.lang.String, java.util.Map)
	 */
	@Override
	@Async
	public void send(final SimpleMailMessage msg, final String templatePath,
			final Map<String, Object> hTemplateVariables) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
						true, "UTF-8");
				message.setTo(msg.getTo());
				message.setFrom(msg.getFrom());
				message.setSubject(msg.getSubject());

				String body = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, templatePath, "UTF-8",
						hTemplateVariables);
				message.setText(body, true);
			}
		};
		log.info("Sending email to " + getTo(msg));
		mailSender.send(preparator);

	}

	/**
	 * @author Nikola 27.06.2013.
	 * @param msg
	 * @return
	 */
	private String getTo(SimpleMailMessage msg) {
		String[] to = msg.getTo();
		StringBuilder toString = new StringBuilder();
		for (String mail : to) {
			if (toString.length() > 0) {
				toString.append(",");
			}
			toString.append(mail);
		}
		return toString.toString();
	}

}
