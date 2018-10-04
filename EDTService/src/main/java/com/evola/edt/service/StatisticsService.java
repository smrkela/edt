package com.evola.edt.service;

import com.evola.edt.service.dto.LeaderboardDTO;
import com.evola.edt.service.dto.LearningGroupsDTO;
import com.evola.edt.service.dto.LearningLessonsDTO;
import com.evola.edt.service.dto.StatisticsDTO;
import com.evola.edt.service.dto.UserExperienceDTO;

public interface StatisticsService {

	public abstract StatisticsDTO saveLearning(Long questionId, Long userId, String sessionUid);

	public abstract StatisticsDTO saveTesting(Long questionId, Long userId, Boolean isCorrect, Boolean isUpdate,
			String sessionUid);

	public abstract StatisticsDTO loadProgress(Long userId);

	public abstract StatisticsDTO loadLearnedPerCategory(Long userId);

	public abstract StatisticsDTO loadTestedPerCategory(Long userId);

	public abstract StatisticsDTO loadCurrentlyActiveUsersCount(Long userId);

	public abstract StatisticsDTO loadCurrentlyActiveUsers(Long userId);

	public abstract StatisticsDTO loadRecentLearningSessions(Long userId);

	public abstract StatisticsDTO loadAllLearningSessions(Long userId);

	public abstract StatisticsDTO loadRecentTestingSessions(Long userId);

	public abstract StatisticsDTO loadAllTestingSessions(Long userId);

	public abstract StatisticsDTO loadTestingSessionQuestions(String sessionUid, Long userId);

	public abstract StatisticsDTO loadLearningSessionQuestions(String sessionUid, Long userId);

	public abstract int getNumberOfActiveStudentsRecently(int numberOfRecentDays);
	
	public abstract StatisticsDTO getRecentActiveStudents(int numberOfRecentDays);

	public abstract LearningGroupsDTO loadGroups(Long userId);
	
	public abstract LearningLessonsDTO loadLessons(Long userId, Long categoryId);

	public abstract UserExperienceDTO loadUserExperience(Long userId);

	public abstract LeaderboardDTO loadLeaderboard(Long userId, Integer typeId);

	public abstract LearningGroupsDTO loadGuestGroups(Long drivingCategoryId);

	public abstract LearningLessonsDTO loadGuestLessons(Long drivingCategoryId, Long groupId);

	public abstract LearningGroupsDTO loadPlainGroups(Long userId);

}