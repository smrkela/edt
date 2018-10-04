package com.evola.driving.model
{

	[Bindable]
	public class DrivingCategory extends ModelBase
	{
		private static const ALL:String="$DC_ALL";
		
		public static const CATEGORY_ALL : DrivingCategory = createCategoryAll();

		protected var _name:String;
		protected var _categoryId:String;

		public function DrivingCategory()
		{
			super();
		}

		public function get categoryId():String
		{
			return _categoryId;
		}

		public function set categoryId(value:String):void
		{
			_categoryId=value;
		}

		public function get name():String
		{
			return _name;
		}

		public function set name(value:String):void
		{
			_name=value;
		}

		public function isAll():Boolean
		{
			return _categoryId == ALL;
		}

		public static function createFromXML(dcXML:XML):DrivingCategory
		{

			var dc:DrivingCategory=new DrivingCategory();
			dc.id=dcXML.attribute("id");
			dc.categoryId=dcXML.attribute("category-id");
			dc.name=dcXML.attribute("name");

			return dc;
		}
		
		private static function createCategoryAll() : DrivingCategory{
			
			var dcAll : DrivingCategory = new DrivingCategory();
			dcAll.categoryId = ALL;
			dcAll.name = "Sve";
			
			return dcAll;
		}
	}
}
