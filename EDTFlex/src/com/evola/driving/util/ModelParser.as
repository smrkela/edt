package com.evola.driving.util
{
	import com.evola.driving.model.DrivingCategory;
	import com.evola.driving.model.Question;
	import com.evola.driving.model.QuestionAnswer;
	import com.evola.driving.model.QuestionCategory;
	import com.evola.driving.model.QuestionImage;
	
	import mx.collections.ArrayCollection;

	public class ModelParser
	{

		private static const TAG_QUESTION_CATEGORY:String="question-category";
		private static const TAG_DRIVING_CATEGORY:String="driving-category";
		private static const TAG_QUESTION:String="question";
		private static const TAG_QUESTION_IMAGE:String="question-image";
		private static const TAG_QUESTION_ANSWER:String="question-answer";

		public static function createModel(allQuestionsXML : XML):EvolaDrivingModel
		{

			var model:EvolaDrivingModel=new EvolaDrivingModel();

			model.drivingCategories=parseDrivingCategories(allQuestionsXML);
			model.questionCategories=parseCategories(allQuestionsXML);
			//model.questions=parseQuestions(allQuestionsXML, model);

			return model;
		}

		private static function parseDrivingCategories(xml:XML):ArrayCollection
		{

			var v:ArrayCollection=new ArrayCollection();

			var drivingCategoriesXMLList:XMLList=xml.child(TAG_DRIVING_CATEGORY);

			for each (var dcXML:XML in drivingCategoriesXMLList)
			{

				v.addItem(DrivingCategory.createFromXML(dcXML));
			}

			return v;
		}

		private static function parseCategories(xml:XML):ArrayCollection
		{

			var v:ArrayCollection=new ArrayCollection();

			var questionCategoriesXMLList:XMLList=xml.child(TAG_QUESTION_CATEGORY);

			for each (var qcXML:XML in questionCategoriesXMLList)
			{

				v.addItem(QuestionCategory.createFromXML(qcXML));
			}

			return v;
		}

		public static function parseQuestions(questionsXml:XML, model:EvolaDrivingModel):ArrayCollection
		{

			var v:ArrayCollection=new ArrayCollection();

			var questionsXMLList:XMLList=questionsXml.child(TAG_QUESTION);

			for each (var qXML:XML in questionsXMLList)
			{

				var q:Question=new Question();
				q.id=qXML.attribute("id");
				q.questionId=qXML.attribute("question-id");
				q.text=qXML.attribute("text");
				q.number=qXML.attribute("number");
				q.remark=qXML.attribute("remark");
				q.points=qXML.attribute("points");
				q.helpUrl=qXML.attribute("help-url");

				q.questionCategories=parseQuestionCategories(qXML, model);
				q.drivingCategories=parseQuestionDrivingCategories(qXML, model);
				q.images=parseQuestionImages(qXML, model);
				q.answers=parseQuestionAnswers(qXML, model, q);

				v.addItem(q);
			}

			return v;
		}

		private static function parseQuestionAnswers(qXML:XML, model:EvolaDrivingModel, question:Question):ArrayCollection
		{
			var refs:XMLList=qXML.child(TAG_QUESTION_ANSWER);

			var v:ArrayCollection=new ArrayCollection();

			for each (var refXML:XML in refs)
			{

				var qi:QuestionAnswer=new QuestionAnswer();
				qi.correct=refXML.attribute("correct") == "true";
				qi.text=refXML.attribute("text");
				qi.question=question;

				v.addItem(qi);
			}

			return v;
		}

		private static function parseQuestionImages(qXML:XML, model:EvolaDrivingModel):ArrayCollection
		{
			var refs:XMLList=qXML.child(TAG_QUESTION_IMAGE);

			var v:ArrayCollection=new ArrayCollection();

			for each (var refXML:XML in refs)
			{

				var qi:QuestionImage=new QuestionImage();
				qi.url=refXML.attribute("url");
				qi.text=refXML.attribute("text");

				v.addItem(qi);
			}

			return v;
		}

		private static function parseQuestionDrivingCategories(qXML:XML, model:EvolaDrivingModel):ArrayCollection
		{

			var refs:XMLList=qXML.child(TAG_DRIVING_CATEGORY);

			var v:ArrayCollection=new ArrayCollection();

			for each (var refXML:XML in refs)
			{

				var value:String=refXML.attribute("category-id");

				v.addItem(model.getDrivingCategory(value));
			}

			return v;
		}

		private static function parseQuestionCategories(qXML:XML, model:EvolaDrivingModel):ArrayCollection
		{

			var refs:XMLList=qXML.child(TAG_QUESTION_CATEGORY);

			var v:ArrayCollection=new ArrayCollection();

			for each (var refXML:XML in refs)
			{

				var value:String=refXML.attribute("category-id");

				v.addItem(model.getQuestionCategory(value));
			}

			return v;
		}

	}
}
