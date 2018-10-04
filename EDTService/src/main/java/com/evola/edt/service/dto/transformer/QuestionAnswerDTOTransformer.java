package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.QuestionAnswer;
import com.evola.edt.model.dto.QuestionAnswerDTO;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class QuestionAnswerDTOTransformer implements
		DTOTransformer<QuestionAnswerDTO, QuestionAnswer> {

	@Inject
	private QuestionDTOTransformer questionDTOTransformer;

	@Override
	public QuestionAnswerDTO transformToDTO(QuestionAnswer qa, String... fetchFields) {
		
		if (qa == null) {
			return null;
		}
		
		QuestionAnswerDTO dto = new QuestionAnswerDTO();
		dto.setId(qa.getId());
		dto.setText(qa.getText());
		dto.setCorrect(qa.getCorrect());
		dto.setOrderIndex(qa.getOrderIndex());
		if (Arrays.asList(fetchFields).contains("question")) {
			dto.setQuestionDTO(questionDTOTransformer.transformToDTO(qa
					.getQuestion()));
		}
		return dto;
	}

	@Override
	public QuestionAnswer transformToEntity(QuestionAnswerDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		QuestionAnswer qa = new QuestionAnswer();
		qa.setId(dto.getId());
		qa.setText(dto.getText());
		qa.setCorrect(dto.getCorrect());
		qa.setQuestion(questionDTOTransformer.transformToEntity(dto.getQuestionDTO()));
		qa.setOrderIndex(dto.getOrderIndex());
		return qa;
	}

}
