package com.evola.edt.service.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.evola.edt.service.StatisticsService;
import com.evola.edt.service.dto.LeaderboardDTO;
import com.evola.edt.service.dto.LearningGroupsDTO;
import com.evola.edt.service.dto.LearningLessonsDTO;
import com.evola.edt.service.dto.StatisticsDTO;
import com.evola.edt.service.dto.UserExperienceDTO;
import com.evola.edt.web.security.SecurityUtils;

@Path("/statistics")
@Named
public class StatisticsRestService extends AbstractRestService {

	Logger log = Logger.getLogger(getClass());

	@Inject
	private StatisticsService statisticsService;

	@GET
	@Path("/saveLearning")
	@Produces("application/xml")
	public StatisticsDTO saveLearning(
			@QueryParam("questionId") Long questionId,
			@QueryParam("sessionUid") String sessionUid) {
		return statisticsService.saveLearning(questionId,
				SecurityUtils.getUserId(), sessionUid);
	}

	@GET
	@Path("/saveTesting")
	@Produces("application/xml")
	public StatisticsDTO saveTesting(@QueryParam("questionId") Long questionId,
			@QueryParam("isCorrect") Boolean isCorrect,
			@QueryParam("isUpdate") Boolean isUpdate,
			@QueryParam("sessionUid") String sessionUid) {
		return statisticsService.saveTesting(questionId,
				SecurityUtils.getUserId(), isCorrect, isUpdate, sessionUid);
	}

	@GET
	@Path("/loadProgress")
	@Produces("application/xml")
	public StatisticsDTO loadProgress() {
		return statisticsService.loadProgress(SecurityUtils.getUserId());
	}
	
	@GET
	@Path("/loadGroups")
	@Produces("application/xml")
	public LearningGroupsDTO loadGroups() {
		return statisticsService.loadGroups(SecurityUtils.getUserId());
	}
	
	@GET
	@Path("/loadLessons")
	@Produces("application/xml")
	public LearningLessonsDTO loadLessons(@QueryParam("categoryId") Long categoryId) {
		return statisticsService.loadLessons(SecurityUtils.getUserId(), categoryId);
	}
	
	@GET
	@Path("/loadUserExperience")
	@Produces("application/xml")
	public UserExperienceDTO loadUserExperience() {
		return statisticsService.loadUserExperience(SecurityUtils.getUserId());
	}

	@GET
	@Path("/loadLeaderboard")
	@Produces("application/xml")
	public LeaderboardDTO loadLeaderboard(@QueryParam("typeId") Integer typeId) {
		return statisticsService.loadLeaderboard(SecurityUtils.getUserId(), typeId);
	}
	
	@GET
	@Path("/loadLearnedPerCategory")
	@Produces("application/xml")
	public StatisticsDTO loadLearnedPerCategory() {
		return statisticsService.loadLearnedPerCategory(SecurityUtils
				.getUserId());
	}

	@GET
	@Path("/loadTestedPerCategory")
	@Produces("application/xml")
	public StatisticsDTO loadTestedPerCategory() {
		return statisticsService.loadTestedPerCategory(SecurityUtils
				.getUserId());
	}

	@GET
	@Path("/loadCurrentlyActiveUsersCount")
	@Produces("application/xml")
	public StatisticsDTO loadCurrentlyActiveUsersCount() {
		return statisticsService.loadCurrentlyActiveUsersCount(SecurityUtils
				.getUserId());
	}

	@GET
	@Path("/loadCurrentlyActiveUsers")
	@Produces("application/xml")
	public StatisticsDTO loadCurrentlyActiveUsers() {
		return statisticsService.loadCurrentlyActiveUsers(SecurityUtils
				.getUserId());
	}

	@GET
	@Path("/loadRecentLearningSessions")
	@Produces("application/xml")
	public StatisticsDTO loadRecentLearningSessions() {
		return statisticsService.loadRecentLearningSessions(SecurityUtils
				.getUserId());
	}

	@GET
	@Path("/loadAllLearningSessions")
	@Produces("application/xml")
	public StatisticsDTO loadAllLearningSessions() {
		return statisticsService.loadAllLearningSessions(SecurityUtils
				.getUserId());
	}

	@GET
	@Path("/loadRecentTestingSessions")
	@Produces("application/xml")
	public StatisticsDTO loadRecentTestingSessions() {
		return statisticsService.loadRecentTestingSessions(SecurityUtils
				.getUserId());
	}

	@GET
	@Path("/loadAllTestingSessions")
	@Produces("application/xml")
	public StatisticsDTO loadAllTestingSessions() {
		return statisticsService.loadAllTestingSessions(SecurityUtils
				.getUserId());
	}
	
	@GET
	@Path("/loadLearningSessionQuestions")
	@Produces("application/xml")
	public StatisticsDTO loadLearningSessionQuestions(@QueryParam("sessionUid") String sessionUid) {
		return statisticsService.loadLearningSessionQuestions(sessionUid, SecurityUtils
				.getUserId());
	}
	
	@GET
	@Path("/loadTestingSessionQuestions")
	@Produces("application/xml")
	public StatisticsDTO loadTestingSessionQuestions(@QueryParam("sessionUid") String sessionUid) {
		return statisticsService.loadTestingSessionQuestions(sessionUid, SecurityUtils
				.getUserId());
	}

}
