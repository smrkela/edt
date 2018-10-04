package com.evola.edt.service.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.web.util.UrlUtils;

import com.evola.edt.model.User;
import com.evola.edt.utils.URLUtils;

public class EdtSignInUtils {

	public static void saveReferer(HttpServletRequest request) {

		if (request == null)
			return;

		String refererUrl = request.getHeader("Referer");

		if (StringUtils.isNotBlank(refererUrl)) {

			HttpSession session = request.getSession(true);
			session.setAttribute("login_referer", refererUrl);
		}
	}

	public static String loadReferer(HttpServletRequest request) {

		String referer = null;

		HttpSession session = request.getSession();

		if (session != null) {
			
			referer = (String) session.getAttribute("login_referer");

			if (StringUtils.isNotBlank(referer)) {

				session.removeAttribute("login_referer");
			}
			
			String serverName = request.getServerName();
			String scheme = request.getScheme();

			//sada moramo proveriti da li je referer sa naseg servera ili spoljasnjeg
			if(StringUtils.isNotBlank(serverName) && StringUtils.isNotBlank(scheme)){
				
				String path = scheme + "://"+serverName;
				
				//ako nam referer ne pocinje sa putanjom naseg servera onda otkazujemo referer-a, to je neki spoljasnji link
				if(!StringUtils.startsWith(referer, path))
					referer = null;
			}
		}

		return referer;
	}

	public static void addCookies(User user, HttpServletRequest request, HttpServletResponse response) {

		
		Cookie cookie = new Cookie("pbid", request.getSession().getId());
		cookie.setDomain(".vozacisrbije.com");
		cookie.setMaxAge(60*60*24);
		cookie.setPath("/");
		
		response.addCookie(cookie);
	}

}
