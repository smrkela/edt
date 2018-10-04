package com.evola.edt.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;

import org.apache.commons.lang.StringUtils;

public class URLUtils {

	public static String encode(String s) {

		String encodedURL = s;

		try {

			encodedURL = URLEncoder.encode(s, "UTF-8");

		} catch (UnsupportedEncodingException e) {
		}

		return encodedURL;
	}
	
	/**
	 * Metoda pravi URL title za pitanje. Pitanje predstavlja skup reci naslova
	 * pitanja odvojena crticom.
	 * 
	 * @param question
	 * @return
	 */
	public static String createQuestionTitle(String text) {

		if(text == null)
			return "";
		
		String title = text.toLowerCase();

		title = title.trim();

		title = title.replace(",", "");
		title = title.replace(".", "");
		title = title.replace("/", "");
		
		String[] targets = new String[]{"š","đ","č","ć","ž"};
		String[] replace = new String[]{"s","d","c","c","z"};
		
		//menjamo uobicajena slova
		title = StringUtils.replaceEach(title, targets, replace);

		// delimo ga na reci

		String[] split = title.split("\\s+");

		title = StringUtils.join(split, "-");

		// moramo uraditi standardizovati tekst
		title = Normalizer.normalize(title, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

		return title;
	}

}
