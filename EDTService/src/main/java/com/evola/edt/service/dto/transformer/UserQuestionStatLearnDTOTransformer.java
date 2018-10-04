package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.evola.edt.model.UserQuestionStatLearn;
import com.evola.edt.model.dto.UserQuestionStatLearnDTO;

@Named
public class UserQuestionStatLearnDTOTransformer implements
		DTOTransformer<UserQuestionStatLearnDTO, UserQuestionStatLearn> {
	
	@Autowired
	private QuestionDTOTransformer questionDTOTransformer;

	@Override
	public UserQuestionStatLearnDTO transformToDTO(UserQuestionStatLearn dc, String... fetchFields) {
		
		if (dc == null) {
			return null;
		}
		
		UserQuestionStatLearnDTO dto = new UserQuestionStatLearnDTO();
		dto.setId(dc.getId());
		dto.setTime(dc.getTime());
		
		if (Arrays.asList(fetchFields).contains("question"))
			dto.setQuestion(questionDTOTransformer.transformToDTO(dc.getQuestion()));
		
		return dto;
	}

	@Override
	public UserQuestionStatLearn transformToEntity(UserQuestionStatLearnDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		UserQuestionStatLearn dc = new UserQuestionStatLearn();
		dc.setId(dto.getId());
		dc.setTime(dto.getTime());
		return dc;
	}

}
