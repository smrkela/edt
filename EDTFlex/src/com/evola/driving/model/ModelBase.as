package com.evola.driving.model
{
	[Bindable]
	public class ModelBase
	{
		
		protected var _id : String;
		
		public function ModelBase()
		{
		}

		public function get id():String
		{
			return _id;
		}

		public function set id(value:String):void
		{
			_id = value;
		}
		
		public function equals(base : ModelBase) : Boolean{
			
			if(!base || !base.id)
				return false;
			
			return _id == base.id;
		}

	}
}