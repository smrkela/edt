package com.evola.driving.model
{
	import mx.collections.ArrayCollection;

	[Bindable]
	public class Question extends ModelBase
	{

		//broj moze da ima i neku naznaku pa mora da bude string
		protected var _number:String;
		protected var _text:String;
		protected var _remark:String;
		protected var _points:Number;
		protected var _helpUrl:String;
		protected var _questionId:String;

		protected var _questionCategories:ArrayCollection;
		protected var _drivingCategories:ArrayCollection;

		protected var _answers:ArrayCollection;
		protected var _images:ArrayCollection;

		private var _isMultiSelect:Boolean=false;
		private var _isMultiSelectSet:Boolean=false;
		
		//da li je dat odgovor na ovo pitanje
		private var _isAnswered : Boolean = false;
		
		//globalni redni broj pitanja
		public var globalNumber:int;
		
		//broj tacnih odgovora
		public var numOfCorrectAnswers:int;

		public function Question()
		{
			super();
		}
		
		public function get questionId():String
		{
			return _questionId;
		}

		public function set questionId(value:String):void
		{
			_questionId = value;
		}

		public function reset() : void{
			
			isAnswered = false;
			
			for each(var answer : QuestionAnswer in answers){
				
				answer.reset();
			}
		}
		
		public function get isAnswered() : Boolean{
			
			return _isAnswered;
		}
		
		public function set isAnswered(value : Boolean) : void{
			
			_isAnswered = value;
		}

		public function get helpUrl():String
		{
			return _helpUrl;
		}

		public function set helpUrl(value:String):void
		{
			_helpUrl=value;
		}

		public function get images():ArrayCollection
		{
			return _images;
		}

		public function set images(value:ArrayCollection):void
		{
			_images=value;
		}

		public function get answers():ArrayCollection
		{
			return _answers;
		}

		public function set answers(value:ArrayCollection):void
		{
			_answers=value;
		}

		public function get drivingCategories():ArrayCollection
		{
			return _drivingCategories;
		}

		public function set drivingCategories(value:ArrayCollection):void
		{
			_drivingCategories=value;
		}

		public function get questionCategories():ArrayCollection
		{
			return _questionCategories;
		}

		public function set questionCategories(value:ArrayCollection):void
		{
			_questionCategories=value;
		}

		public function get points():Number
		{
			return _points;
		}

		public function set points(value:Number):void
		{
			_points=value;
		}

		public function get remark():String
		{
			return _remark;
		}

		public function set remark(value:String):void
		{
			_remark=value;
		}

		public function get text():String
		{
			return _text;
		}

		public function set text(value:String):void
		{
			_text=value;
		}

		public function get number():String
		{
			return _number;
		}

		public function set number(value:String):void
		{
			_number=value;
		}

		public function isInDrivingCategory(dCategory:DrivingCategory):Boolean
		{

			if (drivingCategories == null || drivingCategories.length == 0 || dCategory.isAll())
				return true;

			for each (var dc:DrivingCategory in drivingCategories)
			{

				if (dc.equals(dCategory))

					return true;
			}

			return false;
		}

		public function isInQuestionCategory(qCategory:QuestionCategory):Boolean
		{
			if (questionCategories == null || questionCategories.length == 0)
				return false;

			if (qCategory.isAll())
				return true;

			for each (var dc:QuestionCategory in questionCategories)
			{

				if (dc.equals(qCategory))

					return true;
			}

			return false;
		}

		public function get isMultiSelect():Boolean
		{

			if (!_isMultiSelectSet)
			{

				_isMultiSelect=calculateMultiSelect();
				_isMultiSelectSet=true;
			}

			return _isMultiSelect;
		}

		public function set isMultiSelect(value:Boolean):void
		{

			//nista, vrednost se izracunava
		}

		/**
		 * Metoda racuna da li imamo vise tacnih odgovora.
		 */
		private function calculateMultiSelect():Boolean
		{

			var correctAnswersCount:int=0;

			for each (var answer:QuestionAnswer in answers)
			{

				if (answer.correct)
					correctAnswersCount++;

				if (correctAnswersCount > 1)
					break;
			}

			return correctAnswersCount > 1;
		}

		public function setUserAnswer(selectedValue:QuestionAnswer):void
		{

			if (isMultiSelect)
			{
				//ne radimo nista za multi
			}
			else
			{
				
				//prvo treba da setujemo sve odgovore na neselektovane 
				//pa onda da obradimo onog koji je izabran

				for each (var answer:QuestionAnswer in answers)
				{
					//answer.isUserSelected=answer == selectedValue;
					answer.reset();
				}
				
				selectedValue.isUserSelected = true;
			}
		}
		
		public function isCorrectlyAnswered():Boolean
		{

			var allAnswersOk : Boolean = true;
			
			for each(var answer : QuestionAnswer in answers){
				
				allAnswersOk = allAnswersOk && answer.isCorrect();
			}
			
			return allAnswersOk;
		}
		
		public function getQuestionCategoryIndex():Number
		{

			var index : int =int.MAX_VALUE;
			
			for each(var qc : QuestionCategory in questionCategories){
				
				if(qc.orderIndex < index)
					index = qc.orderIndex;
			}
			
			return index;
		}
		
		public function getQuestionIndex():Number
		{

			var index : int = parseInt(number);
			
			return index;
		}
	}
}
