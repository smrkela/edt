package com.evola.edt.service.dto.transformer;

import java.util.Arrays;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.User;
import com.evola.edt.model.UserQuestionStatLearn;
import com.evola.edt.model.UserQuestionStatTest;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.model.dto.UserQuestionStatLearnDTO;
import com.evola.edt.model.dto.UserQuestionStatTestDTO;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class UserDTOTransformer implements DTOTransformer<UserDTO, User> {

	@Inject
	private UserQuestionStatLearnDTOTransformer learnDtoTransformer;

	@Inject
	private UserQuestionStatTestDTOTransformer testDtoTransformer;

	@Inject
	private DrivingCategoryDTOTransformer drivingCategoryDtoTransformer;

	@Override
	public UserDTO transformToDTO(User dc, String... fetchFields) {
		
		if (dc == null) {
			return null;
		}
		
		UserDTO dto = new UserDTO();
		dto.setId(dc.getId());
		dto.setEmail(dc.getEmail());
		dto.setUsername(dc.getUsername());
		// dto.setPassword(dc.getPassword());
		dto.setFirstName(dc.getFirstName());
		dto.setLastName(dc.getLastName());
		dto.setLastLogin(dc.getLastLogin());
		dto.setIsMale(dc.getIsMale());
		dto.setQuestionsPerPage(dc.getQuestionsPerPage());
		dto.setRegistrationDate(dc.getRegistrationDate());

		if (Arrays.asList(fetchFields).contains("learnStatistics")) {
			addLearnStatistics(dc, dto);
		}
		if (Arrays.asList(fetchFields).contains("testingStatistics")) {
			addTestingStatistics(dc, dto);
		}
		if (Arrays.asList(fetchFields).contains("drivingCategory")) {
			addDrivingCategory(dc, dto);
		}
		
		dto.setActivationDate(dc.getActivationDate());
		dto.setSignInProvider(dc.getSignInProvider());

		dto.setLearnedQuestions(dc.getLearnedQuestions());
		dto.setTestedQuestions(dc.getTestedQuestions());
		dto.setPoints(dc.getPoints());
		
		return dto;
	}

	private void addDrivingCategory(User dc, UserDTO dto) {

		if (dc.getDrivingCategory() != null)
			dto.setDrivingCategory(drivingCategoryDtoTransformer
					.transformToDTO(dc.getDrivingCategory()));
	}

	@Override
	public User transformToEntity(UserDTO dto) {
		
		if(dto == null)
			return null;
		
		User dc = new User();
		dc.setId(dto.getId());
		dc.setEmail(dto.getEmail());
		dc.setUsername(dto.getUsername());
		dc.setPassword(dto.getPassword());
		dc.setFirstName(dto.getLastName());
		dc.setLastName(dto.getLastName());
		dc.setLastLogin(dto.getLastLogin());
		dc.setIsMale(dto.getIsMale());
		dc.setQuestionsPerPage(dto.getQuestionsPerPage());
		dc.setRegistrationDate(dto.getRegistrationDate());
		if (dto.getDrivingCategory() != null) {
			dc.setDrivingCategory(drivingCategoryDtoTransformer
					.transformToEntity(dto.getDrivingCategory()));
		}
		
		dc.setActivationDate(dto.getActivationDate());
		dc.setSignInProvider(dto.getSignInProvider());
		
		dc.setLearnedQuestions(dto.getLearnedQuestions());
		dc.setTestedQuestions(dto.getTestedQuestions());
		dc.setPoints(dto.getPoints());
		
		return dc;
	}

	private void addLearnStatistics(User q, UserDTO dto) {
		Set<UserQuestionStatLearn> learnStats = q.getLearnStatistics();
		for (UserQuestionStatLearn learnStat : learnStats) {
			UserQuestionStatLearnDTO questionAnswerDTO = learnDtoTransformer
					.transformToDTO(learnStat);
			dto.getLearningStats().add(questionAnswerDTO);
		}
	}

	private void addTestingStatistics(User q, UserDTO dto) {
		Set<UserQuestionStatTest> learnStats = q.getTestingStatistics();
		for (UserQuestionStatTest learnStat : learnStats) {
			UserQuestionStatTestDTO questionAnswerDTO = testDtoTransformer
					.transformToDTO(learnStat);
			dto.getTestingStats().add(questionAnswerDTO);
		}
	}

}
