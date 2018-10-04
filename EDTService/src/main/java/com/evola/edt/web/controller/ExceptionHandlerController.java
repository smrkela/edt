package com.evola.edt.web.controller;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;

@Controller
public class ExceptionHandlerController {

	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception ex) {

		ModelAndView mav = new ModelAndView("error");

		mav.addObject("message", ex.getMessage());
		mav.addObject("pageInfo", new PageInfo("greska", "Greška", PageCategories.GLAVNA));

		return mav;
	}

	@RequestMapping("nepoznata-stranica")
	public ModelAndView error404(HttpServletRequest request, HttpServletResponse response) {

		// retrieve some useful information from the request
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		// String servletName = (String)
		// request.getAttribute("javax.servlet.error.servlet_name");
		String exceptionMessage = getExceptionMessage(throwable, statusCode);

		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}

		String message = "Stranica <i>" + requestUri + "</i> ne postoji.";

		ModelAndView mav = new ModelAndView("notAvailable");

		mav.addObject("message", message);
		mav.addObject("pageInfo", new PageInfo("greska", "Greška", PageCategories.GLAVNA));

		return mav;
	}

	@RequestMapping("greska")
	public ModelAndView customError(HttpServletRequest request, HttpServletResponse response) {

		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");

		String eMessage = getExceptionMessage(throwable, statusCode);

		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}

		String message = MessageFormat.format("Greška <b>{0}</b> za web adresu <i>{1}</i> sa porukom <i>{2}</i>",
				statusCode, requestUri, eMessage);

		ModelAndView mav = new ModelAndView("error");

		mav.addObject("message", message);
		mav.addObject("pageInfo", new PageInfo("greska", "Greška", PageCategories.GLAVNA));

		return mav;
	}

	private String getExceptionMessage(Throwable throwable, Integer statusCode) {

		if (throwable != null) {
			return throwable.getMessage();
		}

		if (statusCode != null) {

			HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

			if (HttpStatus.BAD_REQUEST.equals(httpStatus))
				return "Pogrešna URL adresa";

			return httpStatus.getReasonPhrase();
		}

		return "";
	}
}
