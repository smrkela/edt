package com.evola.edt.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.w3c.tidy.Tidy;

public class TidyUtil {

	public static String tidify(String htmlContent) {

		try {

			ByteArrayInputStream bais = new ByteArrayInputStream(
					htmlContent.getBytes("UTF-8"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			Tidy tidy = new Tidy();
			tidy.setXHTML(true);
			tidy.setTidyMark(false);
			tidy.setDocType("omit");
			tidy.setMakeClean(true);
			tidy.setQuiet(false);
			//tidy.setIndentContent(true);
			//tidy.setSmartIndent(true);
			//tidy.setIndentAttributes(true);
			//tidy.setWord2000(true);
			tidy.setPrintBodyOnly(true);
			tidy.setInputEncoding("UTF8");
			tidy.setOutputEncoding("UTF8");
			
			tidy.parse(bais, baos);

			return baos.toString("UTF-8");

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {

		TidyUtil util = new TidyUtil();

		String tidify = util.tidify("<b>Some</b><br>");

		System.out.println(tidify);
	}

}
