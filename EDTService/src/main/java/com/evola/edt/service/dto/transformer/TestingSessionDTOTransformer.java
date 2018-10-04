package com.evola.edt.service.dto.transformer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.TestingSession;
import com.evola.edt.model.UserQuestionStatTest;
import com.evola.edt.model.dto.TestingSessionDTO;
import com.evola.edt.model.dto.UserQuestionStatTestDTO;
import com.evola.edt.utils.FormattingUtils;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class TestingSessionDTOTransformer implements
		DTOTransformer<TestingSessionDTO, TestingSession> {

	@Inject
	private UserQuestionStatTestDTOTransformer testDtoTransformer;

	@Inject
	private UserDTOTransformer userDtoTransformer;

	@Override
	public TestingSessionDTO transformToDTO(TestingSession dc, String... fetchFields) {

		if (dc == null) {
			return null;
		}
		
		TestingSessionDTO dto = new TestingSessionDTO();
		dto.setId(dc.getId());
		dto.setStart(FormattingUtils.formatDate(dc.getStart()));
		dto.setUid(dc.getUid());

		if (Arrays.asList(fetchFields).contains("user")) {
			addUser(dc, dto);
		}
		if (Arrays.asList(fetchFields).contains("learns")) {
			addLearns(dc, dto);
		}

		return dto;
	}

	private void addUser(TestingSession dc, TestingSessionDTO dto) {

		if (dc.getUser() != null)
			dto.setUser((userDtoTransformer.transformToDTO(dc.getUser())));
	}

	@Override
	public TestingSession transformToEntity(TestingSessionDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		TestingSession dc = new TestingSession();
		dc.setId(dto.getId());
		dc.setStart(FormattingUtils.parseDate(dto.getStart()));
		dc.setUid(dto.getUid());
		dc.setUser(userDtoTransformer.transformToEntity(dto.getUser()));

		return dc;
	}

	private void addLearns(TestingSession q, TestingSessionDTO dto) {
		
		Set<UserQuestionStatTest> learnStats = q.getTests();
		
		dto.setTests(new HashSet<UserQuestionStatTestDTO>());
		
		for (UserQuestionStatTest learnStat : learnStats) {
			
			UserQuestionStatTestDTO questionAnswerDTO = testDtoTransformer
					.transformToDTO(learnStat, "question");
			
			dto.getTests().add(questionAnswerDTO);
		}
	}

}
