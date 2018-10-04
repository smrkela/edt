package
{
	import com.evola.driving.controls.QuestionLoadingDialog;
	import com.evola.driving.controls.TestResultsControl;
	import com.evola.driving.controls.TimerControl;
	import com.evola.driving.controls.spinner.SpinnerUtil;
	import com.evola.driving.model.DrivingCategory;
	import com.evola.driving.model.Question;
	import com.evola.driving.model.QuestionAnswer;
	import com.evola.driving.model.QuestionCategory;
	import com.evola.driving.model.User;
	import com.evola.driving.util.EvolaDrivingModel;
	import com.evola.driving.util.PageManager;
	import com.evola.driving.util.QuestionsParser;
	import com.evola.driving.views.pages.HomePage;
	
	import flash.events.Event;
	import flash.events.ProgressEvent;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	import mx.collections.Sort;
	import mx.controls.Alert;
	import mx.managers.IBrowserManager;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	import mx.rpc.http.mxml.HTTPService;
	import mx.utils.UIDUtil;

	[Bindable]
	public class MainPresenter
	{

		public var model:EvolaDrivingModel=new EvolaDrivingModel();
		public var view:EvolaDrivingTesting;
		public var timerControl:TimerControl;
		public var homePage:HomePage;
		public var browserManager:IBrowserManager;

		//timer za cuvanje zadataka pri ucenju
		private var _learningTimer:Timer;

		public var questionsLoadingDialog:QuestionLoadingDialog;

		public function MainPresenter(view:EvolaDrivingTesting)
		{

			this.view=view;
		}

		public function startLearningSimple():void
		{
			
			//resetujemo mapu sacuvanih pitanja
			model.learningSavedQuestions={};
			model.learningSessionUID=UIDUtil.createUID();
			
			var excludedQuestionIds:ArrayCollection=null;
			
			model.isInProgress=true;
			model.isTestingMode=false;
		}

		public function startTestingSimple():void
		{
			
			//resetujemo mapu sacuvanih pitanja
			model.testingSavedQuestions={};
			model.testingSessionUID=UIDUtil.createUID();
			
			model.isInProgress=true;
			model.isTestingMode=true;
			model.useQuickResponse=true;
			model.testFinished=false;
		}

		public function back():void
		{

		}

		public function loginUser(user:User):void
		{

			Settings.user=user;

			PageManager.selectPage("HomePage");

			loadQuestions();
		}

		private function loadQuestions():void
		{

			model.isLoadingQuestions=true;

			if (!questionsLoadingDialog){
				
				questionsLoadingDialog=new QuestionLoadingDialog();
			}
			
			questionsLoadingDialog.start();

			PopUpManager.addPopUp(questionsLoadingDialog, view, true);
			PopUpManager.centerPopUp(questionsLoadingDialog);

			view.serviceGetAllQuestions.send();
		}

		public function selectQuestion(question:Question):void
		{

			model.selectedQuestion=question;

			//sada treba da cuvamo informaciju o citanju pitanja
			if (!model.isTestingMode)
			{

				saveQuestionLearning();
			}

		}

		private function saveQuestionLearning():void
		{

			if (!model.selectedQuestion)
				return;

			//ako smo vec sacuvali ovo pitanje u toku ove sesije ne cuvamo ga opet
			if (model.selectedQuestion.id in model.learningSavedQuestions)
				return;

			//cuvamo ucenje pitanja samo ako ono traje duze od odredjenog vremena
			if (_learningTimer && _learningTimer.running)
				_learningTimer.reset();

			if (!_learningTimer)
			{

				_learningTimer=new Timer(1000, 1);
				_learningTimer.addEventListener(TimerEvent.TIMER_COMPLETE, onLearningTimerComplete);
			}

			_learningTimer.start();
		}

		protected function onLearningTimerComplete(event:TimerEvent):void
		{

			//resetujemo timer da moze opet da se koristi
			_learningTimer.reset();

			if (!model.selectedQuestion)
				return;

			view.serviceSaveLearning.send({questionId: model.selectedQuestion.id, sessionUid: model.learningSessionUID});
			//trace("SAVING LEARNING...");

			model.learningSavedQuestions[model.selectedQuestion.id]=model.selectedQuestion;
		}

		public function saveQuestionTestingStat(question:Question):void
		{

			if (!question)
				return;

			var isUpdate:Boolean=false;

			//ako smo vec sacuvali ovo pitanje u toku ove sesije onda radimo update
			if (question.id in model.testingSavedQuestions)
				isUpdate=true;

			view.serviceSaveTesting.send({questionId: model.selectedQuestion.id, isCorrect: question.isCorrectlyAnswered(), isUpdate: isUpdate, sessionUid: model.testingSessionUID});
			//trace("SAVING TESTING...");

			model.testingSavedQuestions[question.id]=question;
		}

		public function assignNumberOfCorrectAnswers(questions:ListCollectionView):void
		{
			
			for each (var q:Question in questions)
			{
				
				//usput popunjavamo koliko tacnih odgovora ima pitanje
				var num:int=0;
				
				if (q.answers)
				{
					
					for each (var qa:QuestionAnswer in q.answers)
					{
						
						if (qa.correct)
							num++;
					}
				}
				
				q.numOfCorrectAnswers=num;
			}
		}
	}
}
