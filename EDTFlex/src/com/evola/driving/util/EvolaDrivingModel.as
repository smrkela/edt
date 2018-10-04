package com.evola.driving.util
{
	import com.evola.driving.model.DrivingCategory;
	import com.evola.driving.model.Question;
	import com.evola.driving.model.QuestionAnswer;
	import com.evola.driving.model.QuestionCategory;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.managers.IBrowserManager;

	[Bindable]
	public class EvolaDrivingModel
	{

		//kategorije pitanja
		public var questionCategories:ArrayCollection;

		//kategorije vozila
		public var drivingCategories:ArrayCollection;

		//da li je trenutno testiranje
		public var isTestingMode:Boolean=true;

		//da li je ucenje ili testiranje u toku
		public var isInProgress:Boolean=false;

		//da li da se odgovori prikazuju odmah po odgovoranju
		public var useQuickResponse:Boolean;

		//trenutno izabrano pitanje
		public var selectedQuestion:Question;

		//da li da se prikazu informacije o tacnosti odgovora posle izvrsenog testiranja
		public var testFinished:Boolean=false;

		//id-evi pitanja koja su sacuvana u toku jedne sesije ucenja
		public var learningSavedQuestions:Object={};

		//uid sesije ucenja
		public var learningSessionUID:String;

		//id-evi pitanja koja su sacuvana u toku jedne sesije testiranja
		public var testingSavedQuestions:Object={};

		//uid sesije testiranja
		public var testingSessionUID:String;

		//da li je ucitavanje pitanja u toku
		public var isLoadingQuestions:Boolean;

		private var _cacheQuestionCategories:Object={};

		private var _cacheDrivingCategories:Object={};
		
		//da li se ceka na potvrdu odgovora pitanja
		public var isUnconfirmed : Boolean;

		public function EvolaDrivingModel()
		{
		}

		public function getQuestionCategory(id:String):QuestionCategory
		{

			var cat:QuestionCategory=_cacheQuestionCategories[id];

			if (cat)
				return cat;

			for each (var c:QuestionCategory in questionCategories)
			{

				if (c.categoryId == id)
				{
					cat=c;
					break;
				}
			}

			_cacheQuestionCategories[id]=cat;

			return cat;
		}

		public function getDrivingCategory(id:String):DrivingCategory
		{

			var cat:DrivingCategory=_cacheDrivingCategories[id];

			if (cat)
				return cat;

			for each (var c:DrivingCategory in drivingCategories)
			{

				if (c.categoryId == id)
				{
					cat=c;
					break;
				}
			}

			_cacheDrivingCategories[id]=cat;

			return cat;
		}

		private function normalCompareFunction(a:Question, b:Question, fields:Array=null):int
		{

			var ci1:Number=a.getQuestionCategoryIndex();
			var ci2:Number=b.getQuestionCategoryIndex();

			//prvo poredimo kategorije
			if (ci1 < ci2)
				return -1;

			if (ci2 < ci1)
				return 1;

			//sada poredimo indexe pitanja
			var qi1:Number=a.getQuestionIndex();
			var qi2:Number=b.getQuestionIndex();

			return qi1 < qi2 ? -1 : 1;
		}

	}
}
