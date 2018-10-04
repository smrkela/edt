package com.evola.edt.service.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.evola.edt.service.QuestionService;
import com.evola.edt.service.TranslationService;
import com.evola.edt.service.dto.AllQuestionMessagesDTO;
import com.evola.edt.service.dto.AllQuestionsDTO;
import com.evola.edt.service.dto.DoneQuestionsDTO;
import com.evola.edt.service.dto.LessonDTO;
import com.evola.edt.service.dto.MyQuestionMessagesDTO;
import com.evola.edt.service.dto.QuestionMessageDTO;
import com.evola.edt.service.dto.QuestionMessagesDTO;
import com.evola.edt.service.dto.QuestionsStatDTO;
import com.evola.edt.web.security.SecurityUtils;

@Path("/question")
@Named
public class QuestionRestService extends AbstractRestService {

	Logger log = Logger.getLogger(getClass());

	@Inject
	private QuestionService questionService;

	@Inject
	private TranslationService translationService;

	@GET
	@Path("/getAllQuestions")
	@Produces("application/xml")
	public AllQuestionsDTO getAllQuestions() {
		return questionService.getAllQuestions();
	}
	
	@GET
	@Path("/getDrivingCategories")
	@Produces("application/xml")
	public AllQuestionsDTO getDrivingCategories() {
		return questionService.getDrivingCategories();
	}

	@GET
	@Path("/reportQuestionProblem")
	@Produces("application/xml")
	public void reportQuestionProblem(

	@QueryParam(value = "questionId") Long questionId,
			@QueryParam(value = "problemCategoryId") String problemCategoryId,
			@QueryParam(value = "userComment") String userComment) {
		questionService.reportQuestionProblem(SecurityUtils.getUserId(),
				questionId, problemCategoryId, userComment);
	}

	@GET
	@Path("/getLearntQuestionsIds")
	@Produces("application/xml")
	public DoneQuestionsDTO getLearntQuestionsIds(@QueryParam("numOfLearnedQuestionsLimit") Long numOfLearnedQuestionsLimit) {
		return questionService.getLearntQuestionsIds(SecurityUtils.getUserId(), numOfLearnedQuestionsLimit);
	}

	@GET
	@Path("/getTestedAndLearntQuestionsIds")
	@Produces("application/xml")
	public DoneQuestionsDTO getTestedAndLearntQuestionsIds() {
		return questionService.getTestedAndLearntQuestionsIds(SecurityUtils
				.getUserId());
	}

	@GET
	@Path("/getAllQuestionsStats")
	@Produces("application/xml")
	public QuestionsStatDTO getAllQuestionsStats() {
		return questionService.getAllQuestionsStats(SecurityUtils
				.getUserId());
	}
	
	@GET
	@Path("/translateAll")
	@Produces("application/xml")
	public void translateAll(@QueryParam("password") String password) {
		translationService.translateAll(password);
	}
	
	@GET
	@Path("/getLessonQuestions")
	@Produces("application/xml")
	public LessonDTO getLessonQuestions(@QueryParam("groupId") Long groupId, @QueryParam("lessonId") Integer lessonId) {
		
		return questionService.getLessonQuestions(SecurityUtils.getUserId(), groupId, lessonId);
	}
	
	@GET
	@Path("/getRemainingLessonQuestions")
	@Produces("application/xml")
	public LessonDTO getRemainingLessonQuestions(@QueryParam("groupId") Long groupId, @QueryParam("lessonId") Integer lessonId, @QueryParam("isTesting") Boolean isTesting, @QueryParam("repeatCount") Integer repeatCount) {
		
		return questionService.getRemainingLessonQuestions(SecurityUtils.getUserId(), groupId, lessonId, isTesting, repeatCount);
	}
	
	@GET
	@Path("/getFavoriteLessonQuestions")
	@Produces("application/xml")
	public LessonDTO getFavoriteLessonQuestions(@QueryParam("groupId") Long groupId, @QueryParam("lessonId") Integer lessonId) {
		
		return questionService.getFavoriteLessonQuestions(SecurityUtils.getUserId(), groupId, lessonId);
	}

	@GET
	@Path("/toggleFavorite")
	@Produces("application/xml")
	public void toggleFavorite(@QueryParam("questionId") Long questionId) {
		
		questionService.toggleFavorite(SecurityUtils.getUserId(), questionId);
	}
	
	@GET
	@Path("/saveQuestionMessage")
	@Produces("application/xml")
	public QuestionMessageDTO saveQuestionMessage(

	@QueryParam(value = "questionId") Long questionId,
			@QueryParam(value = "messageText") String messageText, @QueryParam(value="parentMessageId") Long parentMessageId) {
		return questionService.saveQuestionMessage(SecurityUtils.getUserId(),
				questionId, messageText, parentMessageId);
	}
	
	@GET
	@Path("/getQuestionMessages")
	@Produces("application/xml")
	public QuestionMessagesDTO getQuestionMessages(@QueryParam("questionId") Long questionId, @QueryParam("pageIndex") Integer pageIndex) {
		return questionService.getQuestionsMessages(questionId, pageIndex);
	}
	
	@GET
	@Path("/deleteQuestionMessage")
	@Produces("application/xml")
	public void deleteQuestionMessage(@QueryParam("questionMessageId") Long questionMessageId) {
		
		questionService.deleteQuestionMessage(SecurityUtils.getUserId(), questionMessageId);
	}
	
	@GET
	@Path("/getMyQuestionMessages")
	@Produces("application/xml")
	public MyQuestionMessagesDTO getMyQuestionMessages(@QueryParam("startPage") Integer startPage, @QueryParam("searchText") String searchText) {
		return questionService.getMyQuestionsMessages(SecurityUtils.getUserId(), startPage, searchText);
	}
	
	@GET
	@Path("/getAllQuestionMessages")
	@Produces("application/xml")
	public AllQuestionMessagesDTO getAllQuestionMessages(@QueryParam("startPage") Integer startPage, @QueryParam("searchText") String searchText) {
		return questionService.getAllQuestionsMessages(SecurityUtils.getUserId(), startPage, searchText);
	}
	
}
