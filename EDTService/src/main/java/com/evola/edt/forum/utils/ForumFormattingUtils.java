package com.evola.edt.forum.utils;

import java.util.Calendar;

/**
 * @author ZmajceK, 12.12.2013.
 *
 */
public class ForumFormattingUtils {

	/**
	 * Metoda formatira trenutni datum i vreme u Unix format
	 * @return unixTime - vreme u Unix formatu
	 */
	public static Long formatUnixDate(){
		
		Long unixTime;
		
		Calendar calendar = Calendar.getInstance();
		
		unixTime = calendar.getTimeInMillis()/1000;
		
		return unixTime;
	}
	
	
	/**
	 * Metoda formatira datum i vreme pre 24 sata u Unix format
	 * @return unixTime - vreme u Unix formatu
	 */
	public static Long getUnixDateBefore(){
		
		Long unixTime;
		
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day - 1);
		
		unixTime = calendar.getTimeInMillis()/1000;
		
		return unixTime;
	}
}
