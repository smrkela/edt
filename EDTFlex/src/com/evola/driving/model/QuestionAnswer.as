package com.evola.driving.model
{
	import com.evola.driving.util.PresenterProvider;

	[Bindable]
	public class QuestionAnswer extends ModelBase
	{

		public var question:Question;

		protected var _text:String;
		protected var _correct:Boolean;

		//popunjava se ako korisnik izabere ovaj odgovor
		private var _isUserSelected:Boolean;

		public function QuestionAnswer()
		{
			super();
		}

		public function get correct():Boolean
		{
			return _correct;
		}

		public function set correct(value:Boolean):void
		{
			_correct=value;
		}

		public function get text():String
		{
			return _text;
		}

		public function set text(value:String):void
		{
			_text=value;
		}

		public function get isUserSelected():Boolean
		{

			return _isUserSelected;
		}

		public function set isUserSelected(value:Boolean):void
		{

			_isUserSelected=value;

			if (value){
				
				//obelezavamo pitanje odgovorenim ako ima izabranih odgovora koliko i tacnih 
				
				var totalAnswered : int = 0;
				
				for each(var qa : QuestionAnswer in question.answers){
					
					if(qa.isUserSelected)
						totalAnswered++;
				}
				
				question.isAnswered=totalAnswered == question.numOfCorrectAnswers;
				
				if(question.isAnswered){
					
					PresenterProvider.mainPresenter.saveQuestionTestingStat(question);
				}
			}
		}

		public function reset():void
		{

			isUserSelected=false;
		}

		public function isCorrect():Boolean
		{
			return correct && isUserSelected || !correct && !isUserSelected;
		}
	}
}
