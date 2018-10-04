package com.evola.edt.service.dto.transformer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.LearningSession;
import com.evola.edt.model.UserQuestionStatLearn;
import com.evola.edt.model.dto.LearningSessionDTO;
import com.evola.edt.model.dto.UserQuestionStatLearnDTO;
import com.evola.edt.utils.FormattingUtils;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class LearningSessionDTOTransformer implements DTOTransformer<LearningSessionDTO, LearningSession> {

	@Inject
	private UserQuestionStatLearnDTOTransformer learnDtoTransformer;

	@Inject
	private UserDTOTransformer userDtoTransformer;

	@Override
	public LearningSessionDTO transformToDTO(LearningSession dc, String... fetchFields) {

		if (dc == null) {
			return null;
		}
		
		LearningSessionDTO dto = new LearningSessionDTO();
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

	private void addUser(LearningSession dc, LearningSessionDTO dto) {

		if (dc.getUser() != null)
			dto.setUser((userDtoTransformer.transformToDTO(dc.getUser())));
	}

	@Override
	public LearningSession transformToEntity(LearningSessionDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		LearningSession dc = new LearningSession();
		dc.setId(dto.getId());
		dc.setStart(FormattingUtils.parseDate(dto.getStart()));
		dc.setUid(dto.getUid());
		dc.setUser(userDtoTransformer.transformToEntity(dto.getUser()));

		return dc;
	}

	private void addLearns(LearningSession q, LearningSessionDTO dto) {

		Set<UserQuestionStatLearn> learnStats = q.getLearns();

		dto.setLearns(new HashSet<UserQuestionStatLearnDTO>());

		for (UserQuestionStatLearn learnStat : learnStats) {

			UserQuestionStatLearnDTO questionAnswerDTO = learnDtoTransformer.transformToDTO(learnStat, "question");
			
			dto.getLearns().add(questionAnswerDTO);
		}
	}

}
