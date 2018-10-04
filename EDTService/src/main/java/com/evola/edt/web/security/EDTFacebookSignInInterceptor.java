package com.evola.edt.web.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ProviderSignInInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.evola.edt.service.util.EdtSignInUtils;

public class EDTFacebookSignInInterceptor implements ProviderSignInInterceptor<Facebook> {

	@Override
	public void preSignIn(ConnectionFactory<Facebook> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {

		// ako nemamo gresku odnosno prvi put dolazimo na login stranicu onda
		// cuvamo referer url u sesiji
		if (request instanceof ServletWebRequest) {

			ServletWebRequest servletRequest = (ServletWebRequest) request;

			EdtSignInUtils.saveReferer(servletRequest.getRequest());
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void postSignIn(Connection<Facebook> connection, WebRequest request) {

		int a = 2;
		int b = 3;
	}

}
