package com.evola.driving.model
{

	[Bindable]
	public class QuestionCategory extends ModelBase
	{
		public static const ALL:String="$QC_ALL";

		protected var _name:String;
		protected var _categoryId:String;
		protected var _orderIndex:int;

		public function QuestionCategory()
		{
			super();
		}

		public function get orderIndex():int
		{
			return _orderIndex;
		}

		public function set orderIndex(value:int):void
		{
			_orderIndex=value;
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

		public static function createFromXML(qcXML:XML):QuestionCategory
		{

			var qc:QuestionCategory=new QuestionCategory();
			qc.id=qcXML.attribute("id");
			qc.categoryId=qcXML.attribute("category-id");
			qc.name=qcXML.attribute("name");
			qc.orderIndex=qcXML.attribute("order-index");

			return qc;
		}

	}
}
