package com.evola.edt.mail;

import java.util.Map;

import org.springframework.mail.SimpleMailMessage;

/**
 * @author Nikola 27.04.2013.
 *
 */
public interface EmailSender {

	/**
	 * Sends e-mail using Velocity template for the body and the properties
	 * passed in as Velocity variables.
	 * 
	 * @param msg
	 *            The e-mail message to be sent, except for the body.
	 * @param hTemplateVariables
	 *            Variables to use when processing the template.
	 */
	public abstract void send(SimpleMailMessage msg, String templatePath,
			Map<String, Object> hTemplateVariables);

}