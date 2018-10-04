package com.evola.edt.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class FormattingUtils {

	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat simpleDf = new SimpleDateFormat("dd/MM/yyyy");
	private static DateFormat isoDf = new SimpleDateFormat("yyyy-MM-dd");

	public static String formatDateSimple(Date date){
		
		return simpleDf.format(date);
	}
	
	//NE MENJATI, KORISTI FLEX
	public static String formatDate(Date date) {

		return df.format(date);
	}

	//NE MENJATI, KORISTI FLEX
	public static Date parseDate(String dateString) {

		try {
			
			return df.parse(dateString);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static Object formatIsoDate(Date date) {

		return isoDf.format(date);
	}
	
	public static List<Integer> createPageIndices(int totalPages, int currentIndex) {

		List<Integer> indices = new LinkedList<Integer>();

		int totalIndices = Math.min(totalPages, 11);

		int startingIndex = Math.max(currentIndex - 5, 0);

		// sada nam je index dobro poravnat u levo ali mozda ne i u desno
		if (startingIndex + totalIndices > totalPages)
			startingIndex = totalPages - totalIndices;

		int i = startingIndex;

		while (totalIndices-- > 0) {

			indices.add(i++);
		}

		return indices;
	}

}
