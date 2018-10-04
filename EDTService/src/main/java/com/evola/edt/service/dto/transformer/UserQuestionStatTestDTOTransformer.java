package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.evola.edt.model.UserQuestionStatTest;
import com.evola.edt.model.dto.UserQuestionStatTestDTO;

@Named
public class UserQuestionStatTestDTOTransformer implements
		DTOTransformer<UserQuestionStatTestDTO, UserQuestionStatTest> {

	@Autowired
	private QuestionDTOTransformer questionDTOTransformer;

	@Override
	public UserQuestionStatTestDTO transformToDTO(UserQuestionStatTest dc, String... fetchFields) {

		if (dc == null) {
			return null;
		}
		
		UserQuestionStatTestDTO dto = new UserQuestionStatTestDTO();
		dto.setId(dc.getId());
		dto.setTime(dc.getTime());
		dto.setCorrect(dc.getCorrect());

		if (Arrays.asList(fetchFields).contains("question"))
			dto.setQuestion(questionDTOTransformer.transformToDTO(dc.getQuestion()));

		return dto;
	}

	@Override
	public UserQuestionStatTest transformToEntity(UserQuestionStatTestDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		UserQuestionStatTest dc = new UserQuestionStatTest();
		dc.setId(dto.getId());
		dc.setTime(dto.getTime());
		return dc;
	}

}
