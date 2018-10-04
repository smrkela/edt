package com.evola.edt.service.rest.learningapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.model.dto.UserDiscussionPreviewDTO;
import com.evola.edt.repository.DrivingCategoryRepository;
import com.evola.edt.service.QuestionService;
import com.evola.edt.service.StatisticsService;
import com.evola.edt.service.UserService;
import com.evola.edt.service.dto.AllQuestionMessagesDTO;
import com.evola.edt.service.dto.LeaderboardDTO;
import com.evola.edt.service.dto.LearningGroupsDTO;
import com.evola.edt.service.dto.LearningLessonsDTO;
import com.evola.edt.service.dto.LessonDTO;
import com.evola.edt.service.dto.MyQuestionMessagesDTO;
import com.evola.edt.service.dto.QuestionMessageDTO;
import com.evola.edt.service.dto.QuestionMessagesDTO;
import com.evola.edt.service.dto.StatisticsDTO;
import com.evola.edt.service.dto.UserExperienceDTO;
import com.evola.edt.service.dto.UserPreviewDTO;
import com.evola.edt.service.rest.AbstractRestService;
import com.evola.edt.utils.EUMockMapper;
import com.evola.edt.web.security.SecurityUtils;

@Path("/learningApp")
@Named
public class LAService extends AbstractRestService {

	Logger log = Logger.getLogger(getClass());

	@Inject
	private UserService userService;

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private DrivingCategoryRepository rDrivingCategory;

	@Inject
	private QuestionService questionService;

	@GET
	@Path("/getUser")
	@Produces("application/json")
	public UserDTO getUser() {

		if (SecurityUtils.isLoggedIn())
			return userService.getUserDTO(SecurityUtils.getUserId());
		else
			return null;
	}

	@GET
	@Path("getGuestPrepare")
	@Produces("application/json")
	public Map getGuestPrepare() {

		List<DrivingCategory> all = rDrivingCategory.findAllSorted();

		List<Map<String, Object>> list = EUMockMapper.BASIC.map(all);

		Map<String, Object> result = new HashMap<String, Object>();

		result.put("drivingCategories", list);

		return result;
	}

	@GET
	@Path("getGroups")
	@Produces("application/json")
	public Map<String, Object> loadGroups() {

		// ucitavamo grupe sa statistikama
		LearningGroupsDTO groups = statisticsService.loadGroups(SecurityUtils.getUserId());

		// ucitavamo podatke o korisniku
		UserPreviewDTO userDTO = userService.getUserPreview(SecurityUtils.getUserId());

		UserDiscussionPreviewDTO discussionDTO = questionService.getUserQuestionsPreview(SecurityUtils.getUserId());

		Map<String, Object> map = new HashMap<>();
		map.put("groups", groups);
		map.put("discussions", discussionDTO);
		map.put("user", userDTO);

		return map;
	}

	@GET
	@Path("getGuestGroups/{drivingCategoryId}")
	@Produces("application/json")
	public LearningGroupsDTO loadGuestGroups(@PathParam("drivingCategoryId") Long drivingCategoryId) {

		return statisticsService.loadGuestGroups(drivingCategoryId);
	}

	@GET
	@Path("getGuestLessons/{drivingCategoryId}/{questionCategoryId}")
	@Produces("application/json")
	public LearningLessonsDTO loadGuestLessons(@PathParam("drivingCategoryId") Long dId, @PathParam("questionCategoryId") Long qId) {

		return statisticsService.loadGuestLessons(dId, qId);
	}

	@GET
	@Path("getLessons/{groupId}")
	@Produces("application/json")
	public Map<String, Object> loadLessons(@PathParam("groupId") Long groupId) {

		LearningLessonsDTO lessons = statisticsService.loadLessons(SecurityUtils.getUserId(), groupId);
		LearningGroupsDTO groups = statisticsService.loadPlainGroups(SecurityUtils.getUserId());

		Map<String, Object> map = new HashMap<>();
		map.put("lessons", lessons);
		map.put("groups", groups);

		return map;
	}

	@GET
	@Path("/loadUserExperience")
	@Produces("application/json")
	public UserExperienceDTO loadUserExperience() {

		return statisticsService.loadUserExperience(SecurityUtils.getUserId());
	}

	@GET
	@Path("/loadLeaderboard/{typeId}")
	@Produces("application/json")
	public LeaderboardDTO loadLeaderboard(@PathParam("typeId") Integer typeId) {
		return statisticsService.loadLeaderboard(SecurityUtils.getUserId(), typeId);
	}

	@GET
	@Path("/getLessonQuestions/{groupId}/{lessonId}")
	@Produces("application/json")
	public LessonDTO getLessonQuestions(@PathParam("groupId") Long groupId, @PathParam("lessonId") Integer lessonId) {

		return questionService.getLessonQuestions(SecurityUtils.getUserId(), groupId, lessonId);
	}

	@GET
	@Path("/getRemainingLessonQuestions/{groupId}/{lessonId}/{isTesting}/{repeatCount}")
	@Produces("application/json")
	public LessonDTO getRemainingLessonQuestions(@PathParam("groupId") Long groupId, @PathParam("lessonId") Integer lessonId, @PathParam("isTesting") Boolean isTesting, @PathParam("repeatCount") Integer repeatCount) {

		return questionService.getRemainingLessonQuestions(SecurityUtils.getUserId(), groupId, lessonId, isTesting, repeatCount);
	}

	@GET
	@Path("/getFavoriteLessonQuestions/{groupId}/{lessonId}")
	@Produces("application/json")
	public LessonDTO getFavoriteLessonQuestions(@PathParam("groupId") Long groupId, @PathParam("lessonId") Integer lessonId) {

		return questionService.getFavoriteLessonQuestions(SecurityUtils.getUserId(), groupId, lessonId);
	}

	@GET
	@Path("/saveQuestionMessage")
	@Produces("application/json")
	public QuestionMessageDTO saveQuestionMessage(

	@QueryParam(value = "questionId") Long questionId, @QueryParam(value = "messageText") String messageText, @QueryParam(value = "parentMessageId") Long parentMessageId) {
		return questionService.saveQuestionMessage(SecurityUtils.getUserId(), questionId, messageText, parentMessageId);
	}

	@GET
	@Path("/getQuestionMessages")
	@Produces("application/json")
	public QuestionMessagesDTO getQuestionMessages(@QueryParam("questionId") Long questionId, @QueryParam("pageIndex") Integer pageIndex) {
		return questionService.getQuestionsMessages(questionId, pageIndex);
	}

	@GET
	@Path("/deleteQuestionMessage")
	@Produces("application/json")
	public void deleteQuestionMessage(@QueryParam("questionMessageId") Long questionMessageId) {

		questionService.deleteQuestionMessage(SecurityUtils.getUserId(), questionMessageId);
	}

	@GET
	@Path("/getMyQuestionMessages")
	@Produces("application/json")
	public MyQuestionMessagesDTO getMyQuestionMessages(@QueryParam("startPage") Integer startPage, @QueryParam("searchText") String searchText) {
		return questionService.getMyQuestionsMessages(SecurityUtils.getUserId(), startPage, searchText);
	}

	@GET
	@Path("/getAllQuestionMessages")
	@Produces("application/json")
	public AllQuestionMessagesDTO getAllQuestionMessages(@QueryParam("startPage") Integer startPage, @QueryParam("searchText") String searchText) {
		return questionService.getAllQuestionsMessages(SecurityUtils.getUserId(), startPage, searchText);
	}

	@GET
	@Path("/getAllLearningSessions")
	@Produces("application/json")
	public StatisticsDTO getAllLearningSessions() {
		return statisticsService.loadAllLearningSessions(SecurityUtils.getUserId());
	}

	@GET
	@Path("/getAllTestingSessions")
	@Produces("application/json")
	public StatisticsDTO getAllTestingSessions() {
		return statisticsService.loadAllTestingSessions(SecurityUtils.getUserId());
	}

	/**
	 * Helper service.
	 * 
	 * @return
	 */
	@GET
	@Path("/ping")
	@Produces("application/json")
	public String getAllQuestionMessages() {
		return "{\"result\": \"OK\"}";
	}

}
