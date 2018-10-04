package com.evola.edt.web.security;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.evola.edt.model.User;

public class SecurityUtils {

	public static Long getUserId() {

		if (isLoggedIn()) {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			User principal = (User) authentication.getPrincipal();

			return principal.getId();
		}
		return null;
	}

	public static User getUser() {

		if (isLoggedIn()) {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			User principal = (User) authentication.getPrincipal();

			return principal;
		}

		return null;
	}

	public static Boolean isLoggedIn() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return authentication != null
				&& (authentication instanceof UsernamePasswordAuthenticationToken || authentication instanceof RememberMeAuthenticationToken);
	}

	public static Boolean isFullyLoggedIn() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return authentication != null && authentication instanceof UsernamePasswordAuthenticationToken;
	}

}
