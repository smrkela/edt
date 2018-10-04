package com.evola.edt.web.security;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * @author Nikola 21.04.2013.
 * 
 */
public class TokenUtil {
	static Logger log = Logger.getLogger(TokenUtil.class);

	/**
	 * @author Nikola 21.04.2013.
	 * @return
	 */
	public static String generateToken() {
		return RandomStringUtils.randomAlphanumeric(40).toUpperCase();
	}
}
