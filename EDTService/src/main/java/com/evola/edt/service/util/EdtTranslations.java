package com.evola.edt.service.util;

public class EdtTranslations {

	public static String getDailyTestMessage(int totalUsersToday, int totalTestResultsToday) {

		if (totalUsersToday == 0 || totalTestResultsToday == 0)
			return null;

		String message = transliterate(totalUsersToday, "Danas je " + totalUsersToday + " takmičar uradio ", "Danas su " + totalUsersToday
				+ " takmičara uradila ", "Danas je " + totalUsersToday + " takmičara uradilo ");

		message += transliterate(totalTestResultsToday, totalTestResultsToday + " test.", totalTestResultsToday + " testa.",
				totalTestResultsToday + " testova.");

		return message;
	}

	private static String transliterate(int totalUsersToday, String message1, String message2, String message3) {

		String message = null;

		int modUsers = totalUsersToday % 10;
		int mod100Users = totalUsersToday % 100;

		if (mod100Users == 1 || (modUsers == 1 && mod100Users > 20))
			message = message1;
		else if (mod100Users >= 2 && mod100Users <= 4 || modUsers >= 2 && modUsers <= 4 && mod100Users > 20)
			message = message2;
		else
			message = message3;

		return message;
	}
	
	public static void main(String[] args){
		
		System.out.println(getDailyTestMessage(0, 0));
		System.out.println(getDailyTestMessage(1, 1));
		System.out.println(getDailyTestMessage(2, 3));
		System.out.println(getDailyTestMessage(4, 6));
		System.out.println(getDailyTestMessage(8, 8));
		System.out.println(getDailyTestMessage(11, 16));
		System.out.println(getDailyTestMessage(20, 21));
		System.out.println(getDailyTestMessage(22, 26));
		System.out.println(getDailyTestMessage(101, 103));
		System.out.println(getDailyTestMessage(104, 112));
		System.out.println(getDailyTestMessage(120, 153));
		
	}

}
