package com.evola.driving.model
{
	public class User extends ModelBase
	{
		
		private var _email : String;
		private var _username : String;
		private var _firstName : String;
		private var _lastName : String;
		private var _isMale : Boolean;
		private var _questionsPerPage : int;
		private var _drivingCategory : DrivingCategory;
		
		public function User()
		{
		}

		public function get username():String
		{
			return _username;
		}

		public function set username(value:String):void
		{
			_username = value;
		}

		public function get drivingCategory():DrivingCategory
		{
			return _drivingCategory;
		}

		public function set drivingCategory(value:DrivingCategory):void
		{
			_drivingCategory = value;
		}

		public function get questionsPerPage():int
		{
			return _questionsPerPage;
		}

		public function set questionsPerPage(value:int):void
		{
			_questionsPerPage = value;
		}

		public function get isMale():Boolean
		{
			return _isMale;
		}

		public function set isMale(value:Boolean):void
		{
			_isMale = value;
		}

		public function get lastName():String
		{
			return _lastName;
		}

		public function set lastName(value:String):void
		{
			_lastName = value;
		}

		public function get firstName():String
		{
			return _firstName;
		}

		public function set firstName(value:String):void
		{
			_firstName = value;
		}

		public function get email():String
		{
			return _email;
		}

		public function set email(value:String):void
		{
			_email = value;
		}
		
		public static function createFromXML(userXML : XML) : User{
			
			var user:User=new User();
			user.id=userXML.@id;
			user.firstName=userXML.attribute("firstName");
			user.lastName=userXML.attribute("lastName");
			user.email=userXML.@email;
			user.isMale=userXML.attribute("isMale") == "true";
			user.questionsPerPage = int(userXML.attribute("questions-per-page"));
			user.username = userXML.attribute("username");
			
			if(userXML.child("driving-category").length() > 0)
				user.drivingCategory = DrivingCategory.createFromXML(userXML.child("driving-category")[0]);
			
			if(!user.questionsPerPage)
				user.questionsPerPage = 20;
			
			return user;
		}

	}
}