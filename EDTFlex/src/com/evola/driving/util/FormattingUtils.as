package com.evola.driving.util
{
	import mx.controls.DateField;
	import mx.formatters.DateFormatter;

	public class FormattingUtils
	{

		public static function parseJavaDateString(javaDateString:String):Date
		{

			if (!javaDateString)
				return null;

			var date:Date=DateFormatter.parseDateString(javaDateString);

			return date;
		}

	}
}
