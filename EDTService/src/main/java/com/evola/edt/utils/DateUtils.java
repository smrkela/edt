package com.evola.edt.utils;

import java.util.Date;

import org.joda.time.DateTime;

public class DateUtils {

	private static String[] monthNames = new String[] { "Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar" };

	public static boolean isToday(Date date) {

		DateTime dt = new DateTime(date);
		DateTime now = DateTime.now();

		if (dt.getYear() == now.getYear() && dt.getMonthOfYear() == now.getMonthOfYear() && dt.getDayOfMonth() == now.getDayOfMonth())
			return true;

		return false;
	}

	/**
	 * Metoda vraca pun naziv meseca za brojeve od 1 do 12.
	 * 
	 * @param month
	 * @return
	 */
	public static String getMonthName(int month) {

		return monthNames[month - 1];
	}

	/**
	 * Metoda testira da li je prosledjeni datum posle referentnog datuma.
	 * 
	 * @param testedDate
	 * @param referenceDate
	 * @return
	 */
	public static boolean isAfter(Date testedDate, Date referenceDate) {

		if (testedDate == null)
			return false;

		if (referenceDate == null)
			return true;

		return testedDate.after(referenceDate);
	}

	public static String getTimeTakenString(Integer timeTaken, boolean isShort) {

		String string = "";

		int time = timeTaken;

		if (time > 3600) {

			int hours = time / 3600;

			time = time - hours * 3600;

			if (hours == 1)
				string = hours + " sat ";
			else
				string = hours + " sati ";
		}

		if (time > 60) {

			int minutes = time / 60;

			time = time - minutes * 60;

			if (isShort) {

				string += minutes + " min. ";

			} else {

				if (minutes == 1)
					string += minutes + " minut ";
				else
					string += minutes + " minuta ";
			}
		}

		if (time > 0) {

			if (isShort) {

				string += time + " sek.";

			} else {

				if (time == 1)
					string += time + " sekund";
				else
					string += time + " sekundi";
			}
		}

		return string;
	}

}
