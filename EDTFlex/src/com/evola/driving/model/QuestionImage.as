package com.evola.driving.model
{
	[Bindable]
	public class QuestionImage extends ModelBase
	{
		
		protected var _url : String;
		protected var _text : String;
		
		public function QuestionImage()
		{
			super();
		}

		public function get text():String
		{
			return _text;
		}

		public function set text(value:String):void
		{
			_text = value;
		}

		public function get url():String
		{
			return _url;
		}

		public function set url(value:String):void
		{
			_url = value;
		}

	}
}