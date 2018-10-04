package com.evola.edt.web.security;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.User;
import com.evola.edt.service.UserService;
import com.evola.edt.service.util.EdtSignInUtils;

public class EDTAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private UserService userService;
	
	@Inject
	private ActivityManager mActivity;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {

		// takodje update-ujemo login time user-a

		User user = (User) authentication.getPrincipal();

		getUserService().updateLoginTime(user.getId());
		
		EdtSignInUtils.addCookies(user, request, response);

		String referer = EdtSignInUtils.loadReferer(request);

		mActivity.loggedIn(user);
		
		if (StringUtils.isNotBlank(referer))

			getRedirectStrategy().sendRedirect(request, response, referer);
		else
			super.onAuthenticationSuccess(request, response, authentication);
		
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
