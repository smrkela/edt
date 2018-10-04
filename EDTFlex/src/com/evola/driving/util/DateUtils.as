package com.evola.driving.util
{
	import flash.utils.Endian;

	import mx.controls.DateField;
	import mx.formatters.DateFormatter;
	import mx.utils.ObjectUtil;

	public class DateUtils
	{

		// Days of week
		public static const MONDAY:String="monday";
		public static const TUESDAY:String="tuesday";
		public static const WEDNESDAY:String="wednesday";
		public static const THURSDAY:String="thursday";
		public static const FRIDAY:String="friday";
		public static const SATURDAY:String="saturday";
		public static const SUNDAY:String="sunday";

		// Months of year
		public static const JANUARY:String="january";
		public static const FEBRUARY:String="february";
		public static const MARCH:String="march";
		public static const APRIL:String="april";
		public static const MAY:String="may";
		public static const JUNE:String="june";
		public static const JULY:String="july";
		public static const AUGUST:String="august";
		public static const SEPTEMBER:String="september";
		public static const OCTOBER:String="october";
		public static const NOVEMBER:String="november";
		public static const DECEMBER:String="december";

		// Date parts
		public static const YEAR:String="fullYear";
		public static const MONTH:String="month";
		public static const WEEK:String="week";
		public static const DAY_OF_MONTH:String="date";
		public static const HOURS:String="hours";
		public static const MINUTES:String="minutes";
		public static const SECONDS:String="seconds";
		public static const MILLISECONDS:String="milliseconds";
		public static const DAY_OF_WEEK:String="day";

		// Numeric value of "last", to get last item for a specific time
		public static const LAST:Number=-1;

		// Date masks
		public static const SHORT_DATE_MASK:String="MM/DD/YY";
		public static const MEDIUM_DATE_MASK:String="MMM D, YYYY";
		public static const LONG_DATE_MASK:String="MMMM D, YYYY";
		public static const FULL_DATE_MASK:String="EEEE, MMMM D, YYYY";

		// Time masks
		public static const SHORT_TIME_MASK:String="L:NN A";
		public static const MEDIUM_TIME_MASK:String="L:NN:SS A";
		// TZD = TimeZoneDesignation = GMT + or - X hours, non-standard, requires a slight hack
		public static const LONG_TIME_MASK:String=MEDIUM_TIME_MASK + " TZD";

		// Internal values for using in date/time calculations
		private static const SECOND_VALUE:uint=1000;
		private static const MINUTE_VALUE:uint=DateUtils.SECOND_VALUE * 60;
		private static const HOUR_VALUE:uint=DateUtils.MINUTE_VALUE * 60;
		private static const DAY_VALUE:uint=DateUtils.HOUR_VALUE * 24;
		private static const WEEK_VALUE:uint=DateUtils.DAY_VALUE * 7;

		public static function get todayWithNoTime():Date
		{

			var now:Date=new Date();

			return new Date(now.fullYear, now.month, now.date);
		}

		/**
		 * Metoda vraca broj cetvrti godine od 1 do 4.
		 * @param date
		 * @return
		 *
		 */
		public static function getQuarter(date:Date):int
		{
			if (date.month >= 0 && date.month <= 2)
				return 1;
			if (date.month >= 3 && date.month <= 5)
				return 2;
			if (date.month >= 6 && date.month <= 8)
				return 3;
			if (date.month >= 9 && date.month <= 11)
				return 4;

			return -1;
		}

		/**
		 * Metoda vraca broj polovine godine 1 ili 2.
		 * @param date
		 * @return
		 *
		 */
		public static function getHalfYear(date:Date):int
		{
			if (date.month >= 0 && date.month <= 5)
				return 1;
			if (date.month >= 6 && date.month <= 11)
				return 2;

			return -1;
		}

		public static function getWeek(date:Date):int
		{

			var year=date.getFullYear();

			// get a Date for January 4th this year
			// (week one is the week with this date in it)
			var jan4:Date=new Date(year, 0, 4);

			// get the day of the week for January 4th, monday being zero
			var jan4DayOfWeek:Number=(jan4.getDay() + 6) % 7;

			// get the day of the year for the first day (monday) of week one that year
			// (this may be a negative number if week one started in December the previous year)
			var monday1DayOfYear:Number=3 - jan4DayOfWeek;

			// get the day of the year for the current Date
			var dayOfYear:Number=getDayOfYear(date);

			// the week number is the number of days passed since the first day of week one,
			// divided by seven (the number of days per week), rounded down.
			var week:Number=1 + Math.floor((dayOfYear - monday1DayOfYear) / 7);

			// if the week number is below one, it is still the last week of the previous year.
			if (week < 1)
			{
				// get the Date of the day before January 1st this year
				var previousLastDay:Date=new Date(year, 0, 0);

				// get the week number of that day
				week=getWeek(previousLastDay);
			}

			// if the week number is over 52, it may in fact already be week one of the next year
			else if (week > 52)
			{
				// get a Date of January 4th next year
				var nextJan4:Date=new Date(year + 1, 0, 4);

				// get the day of the week for next January 4th, monday being zero
				var nextJan4DayOfWeek:Number=(nextJan4.getDay() + 6) % 7;

				// get the first day of week one of the next year
				var nextMonday1:Date=new Date(year + 1, 0, 4 - nextJan4DayOfWeek);

				// if that Date is this Date or before it, it is week one
				if (ObjectUtil.dateCompare(nextMonday1, date) < 1)
					week=1;
			}

			return week;
		}

		/**
		 * Metoda vraca dan u godini pocevsi od nule.
		 * @param date
		 * @return
		 *
		 */
		public static function getDayOfYear(date:Date):Number
		{

			var year:Number=date.getFullYear();

			// start with the day of month of this date, minus one
			// (first day of year is day zero)
			var dayOfYear:Number=date.getDate() - 1;

			var month:Number=date.getMonth();

			while (month > 0)
			{
				// get the last day of the previous month
				var lastDayOfPreviousMonth:Date=new Date(year, month, 0);

				// add the day of month of that day
				// (which is the number of days in that month)
				dayOfYear+=lastDayOfPreviousMonth.getDate();

				month--;
			}

			return dayOfYear;
		}

		/**
		 * Determines the number of "dateParts" difference between 2 dates
		 *
		 * @param datePart      The part of the date that will be checked
		 * @param startDate     The starting date
		 * @param endDate       The ending date
		 *
		 * @return                      The number of "dateParts" difference
		 */
		public static function dateDiff(datePart:String, startDate:Date, endDate:Date):Number
		{
			var _returnValue:Number=0;

			switch (datePart)
			{
				case DateUtils.MILLISECONDS:
					var _adjustment:Number=(startDate.timezoneOffset - endDate.timezoneOffset) * 60 * 1000;
					_returnValue=endDate.time - startDate.time + _adjustment;
					break;
				case DateUtils.SECONDS:
					_returnValue=Math.floor(DateUtils.dateDiff(DateUtils.MILLISECONDS, startDate, endDate) / DateUtils.SECOND_VALUE);
					break;
				case DateUtils.MINUTES:
					_returnValue=Math.floor(DateUtils.dateDiff(DateUtils.MILLISECONDS, startDate, endDate) / DateUtils.MINUTE_VALUE);
					break;
				case DateUtils.HOURS:
					_returnValue=Math.floor(DateUtils.dateDiff(DateUtils.MILLISECONDS, startDate, endDate) / DateUtils.HOUR_VALUE);
					break;
				case DateUtils.DAY_OF_MONTH:
					_returnValue=Math.floor(DateUtils.dateDiff(DateUtils.MILLISECONDS, startDate, endDate) / DateUtils.DAY_VALUE);
					break;
				case DateUtils.WEEK:
					_returnValue=Math.floor(DateUtils.dateDiff(DateUtils.MILLISECONDS, startDate, endDate) / DateUtils.WEEK_VALUE);
					break;
				case DateUtils.MONTH:
					// if start date is after end date, switch values and take inverse of return value
					if (DateUtils.dateDiff(DateUtils.MILLISECONDS, startDate, endDate) < 0)
					{
						_returnValue-=DateUtils.dateDiff(DateUtils.MONTH, endDate, startDate);
					}
					else
					{
						// get number of months based upon years
						_returnValue=DateUtils.dateDiff(DateUtils.YEAR, startDate, endDate) * 12;
						// subtract months then perform test to verify whether to subtract one month or not
						// the check below gets the correct starting number of months (but may need to have one month removed after check)
						if (endDate.month != startDate.month)
						{
							_returnValue+=(endDate.month <= startDate.month) ? 12 - startDate.month + endDate.month : endDate.month - startDate.month;
								// else they are the same, but the years are potentially not the same
						}
						else if ((startDate.fullYear != endDate.fullYear) && (endDate.dateUTC < startDate.dateUTC))
						{
							_returnValue+=12;
						}
						// have to perform same checks as YEAR
						// i.e. if end date day is <= start date day, and end date milliseconds < start date milliseconds
						if ((endDate[DateUtils.DAY_OF_MONTH] < startDate[DateUtils.DAY_OF_MONTH]) || (endDate[DateUtils.DAY_OF_MONTH] == startDate[DateUtils.DAY_OF_MONTH] && endDate[DateUtils.MILLISECONDS] < startDate[DateUtils.MILLISECONDS]))
						{
							_returnValue-=1;
						}
					}
					break;
				case DateUtils.YEAR:
					_returnValue=endDate.fullYear - startDate.fullYear;
					// this fixes the previous problem with dates that ran into 2 calendar years
					// previously, if 2 dates were in separate calendar years, but the months were not > 1 year apart, then it was returning too many years
					// e.g. 11/2008 to 2/2009 was returning 1, but should have been returning 0 (zero)
					// if start date before end date and months are less than 1 year apart, add 1 to year to fix offset issue
					// if end date before start date and months are less than 1 year apart, subtract 1 year to fix offset issue
					// added month and milliseconds check to make sure that a date that was e.g. 9/11/07 9:15AM would not return 1 year if the end date was 9/11/08 9:14AM
					if (_returnValue != 0)
					{
						// if start date is after end date
						if (_returnValue < 0)
						{
							// if end date month is >= start date month, and end date day is >= start date day, and end date milliseconds > start date milliseconds
							if ((endDate[DateUtils.MONTH] > startDate[DateUtils.MONTH]) || (endDate[DateUtils.MONTH] == startDate[DateUtils.MONTH] && endDate[DateUtils.DAY_OF_MONTH] > startDate[DateUtils.DAY_OF_MONTH]) || (endDate[DateUtils.MONTH] == startDate[DateUtils.MONTH] && endDate[DateUtils.DAY_OF_MONTH] == startDate[DateUtils.DAY_OF_MONTH] && endDate[DateUtils.MILLISECONDS] > startDate[DateUtils.MILLISECONDS]))
							{
								_returnValue+=1;
							}
						}
						else
						{
							// if end date month is <= start date month, and end date day is <= start date day, and end date milliseconds < start date milliseconds
							if ((endDate[DateUtils.MONTH] < startDate[DateUtils.MONTH]) || (endDate[DateUtils.MONTH] == startDate[DateUtils.MONTH] && endDate[DateUtils.DAY_OF_MONTH] < startDate[DateUtils.DAY_OF_MONTH]) || (endDate[DateUtils.MONTH] == startDate[DateUtils.MONTH] && endDate[DateUtils.DAY_OF_MONTH] == startDate[DateUtils.DAY_OF_MONTH] && endDate[DateUtils.MILLISECONDS] < startDate[DateUtils.MILLISECONDS]))
							{
								_returnValue-=1;
							}
						}
					}
					break;

			}

			return _returnValue;
		}

		public static function getTimeAgoText(timeValue:Number, timeType:String):String
		{

			var timeTypeText:String;

			switch (timeType)
			{

				case SECONDS:
				{
					if (timeValue == 1)
						timeTypeText="sekund";
					else
						timeTypeText="sekundi";
					break;
				}

				case MINUTES:
				{
					if (timeValue == 1)
						timeTypeText="minut";
					else
						timeTypeText="minuta";
					break;
				}

				case HOURS:
				{
					if (timeValue == 1)
						timeTypeText="sat";
					else
						timeTypeText="sati";
					break;
				}

				case DAY_OF_MONTH:
				{
					if (timeValue == 1)
						timeTypeText="dan";
					else
						timeTypeText="dana";
					break;
				}

				default:
				{
					timeTypeText="DateUtils: NO TRANSLATION FOUND";
					break;
				}
			}

			var text:String="pre " + timeValue + " " + timeTypeText;

			return text;
		}

		public static function getPrettyTimeAgoText(timestamp:Date):String
		{

			var timeType:String;
			var timeAmount:Number;

			var now:Date=new Date();

			var minutesDiff:Number=DateUtils.dateDiff(DateUtils.MINUTES, timestamp, now);

			if (minutesDiff < 60)
			{

				timeType=DateUtils.MINUTES;
				timeAmount=minutesDiff;
			}
			else
			{

				var hoursDiff:Number=DateUtils.dateDiff(DateUtils.HOURS, timestamp, now);

				if (hoursDiff < 24)
				{

					timeType=DateUtils.HOURS;
					timeAmount=hoursDiff;
				}
				else
				{

					var daysDiff:Number=DateUtils.dateDiff(DateUtils.DAY_OF_MONTH, timestamp, now);

					timeType=DateUtils.DAY_OF_MONTH;
					timeAmount=daysDiff;
				}
			}

			if (timeType == DateUtils.MINUTES && timeAmount < 1)
			{

				return "maločas";
			}

			return DateUtils.getTimeAgoText(timeAmount, timeType);
		}

		public static function increaseDate(ddate:Date, datePart:String):Date
		{

			var date:Date=new Date(ddate.fullYear, ddate.month, ddate.date, ddate.hours, ddate.minutes, ddate.seconds);

			if (datePart == YEAR)
				date.fullYear++;
			else if (datePart == MONTH)
				date.month++;
			else if (datePart == WEEK)
				date.date+=7;
			else if (datePart == DAY_OF_MONTH)
				date.date++;
			else if (datePart == HOURS)
				date.hours++;
			else if (datePart == MINUTES)
				date.minutes++;
			else if (datePart == SECONDS)
				date.seconds++;

			return date;
		}

		public static function decreaseDate(ddate:Date, datePart:String):Date
		{

			var date:Date=new Date(ddate.fullYear, ddate.month, ddate.date, ddate.hours, ddate.minutes, ddate.seconds);

			if (datePart == YEAR)
				date.fullYear--;
			else if (datePart == MONTH)
				date.month--;
			else if (datePart == WEEK)
				date.date-=7;
			else if (datePart == DAY_OF_MONTH)
				date.date--;
			else if (datePart == HOURS)
				date.hours--;
			else if (datePart == MINUTES)
				date.minutes--;
			else if (datePart == SECONDS)
				date.seconds--;

			return date;
		}

		public static function getYearLength(year:int):Number
		{

			var maxDate:Date=new Date(year, 11, 31, 24, 0, 0, 0);
			var minDate:Date=new Date(year, 0, 1, 0, 0, 0);

			return maxDate.time - minDate.time;
		}

		public static function getHalfYearLength(year:int, firstHalf:Boolean):Number
		{

			var maxDate:Date=new Date(year, 11, 31, 24, 0, 0, 0);
			var minDate:Date=new Date(year, 0, 1, 0, 0, 0);
			var halfDate:Date=new Date(year, 5, 30, 24, 0, 0, 0);

			var time:Number;

			if (firstHalf)
				time=halfDate.time - minDate.time
			else
				time=maxDate.time - halfDate.time;

			return time;
		}

		public static function getQuarterYearLength(year:int, quarter:int):Number
		{

			var minDate:Date=new Date(year, 0, 1, 0, 0, 0, 0);
			var maxDate:Date=new Date(year, 11, 31, 24, 0, 0, 0);
			var quarter1:Date=new Date(year, 2, 31, 24, 0, 0, 0);
			var quarter2:Date=new Date(year, 5, 30, 24, 0, 0, 0);
			var quarter3:Date=new Date(year, 8, 30, 24, 0, 0, 0);

			var time:Number;

			if (quarter == 1)
			{

				time=(quarter1.time - minDate.time);
			}
			else if (quarter == 2)
			{

				time=(quarter2.time - quarter1.time);
			}
			else if (quarter == 3)
			{

				time=(quarter3.time - quarter2.time);
			}
			else
			{

				time=(maxDate.time - quarter3.time);
			}

			return time;
		}

		public static function cloneDate(date:Date):Date
		{

			return new Date(date.fullYear, date.month, date.date, date.hours, date.minutes, date.seconds, date.milliseconds);
		}

		public static function getEarlierDate(date1:Date, date2:Date):Date
		{

			if (date1.time < date2.time)
				return date1;

			return date2;
		}


		public static function formatDateExtended(date:Date):String
		{

			var df:DateFormatter=new DateFormatter();
			df.formatString="DD/MM/YYYY HH:NN";

			return df.format(date);
		}
		
		public static function formatDateSimple(date:Date):String
		{
			var df:DateFormatter=new DateFormatter();
			df.formatString="DD/MM/YY";
			
			return df.format(date);
		}
	}
}


