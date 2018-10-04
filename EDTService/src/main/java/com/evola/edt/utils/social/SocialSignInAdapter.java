package com.evola.edt.utils.social;

import javax.inject.Inject;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.User;
import com.evola.edt.service.UserService;
import com.evola.edt.service.util.EdtSignInUtils;

@Service
public class SocialSignInAdapter implements SignInAdapter {

	@Inject
	UserService userService;

	@Inject
	private ActivityManager mActivity;

	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {

		User user = (User) userService.loadUserByUsername(userId);

		if (user != null) {

			userService.updateLoginTime(user.getId());

			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
		}

		String referer = null;
		// sada radimo redirect na stranicu na kojoj smo i bili
		if (request instanceof ServletWebRequest)
			referer = EdtSignInUtils.loadReferer(((ServletWebRequest) request).getRequest());

		EdtSignInUtils.addCookies(user, ((ServletWebRequest) request).getRequest(), ((ServletWebRequest) request).getResponse());

		mActivity.loggedIn(user);

		return referer;
	}

}
