package com.evola.edt.service.dto.transformer;

import java.util.Arrays;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.QuestionAnswer;
import com.evola.edt.model.RealTestUserResultQuestion;
import com.evola.edt.model.dto.QuestionAnswerDTO;
import com.evola.edt.model.dto.RealTestUserResultQuestionDTO;

@Named
public class RealTestUserResultQuestionDTOTransformer implements
		DTOTransformer<RealTestUserResultQuestionDTO, RealTestUserResultQuestion> {

	@Inject
	private QuestionDTOTransformer questionDTOTransformer;

	@Inject
	private QuestionAnswerDTOTransformer questionAnswerDTOTransformer;

	@Override
	public RealTestUserResultQuestionDTO transformToDTO(RealTestUserResultQuestion entity, String... fetchFields) {

		if (entity == null) {
			return null;
		}

		RealTestUserResultQuestionDTO dto = new RealTestUserResultQuestionDTO();

		if (Arrays.asList(fetchFields).contains("userAnswers")) {
			addUserAnswers(entity, dto);
		}

		if (Arrays.asList(fetchFields).contains("question")) {
			dto.setQuestion(questionDTOTransformer.transformToDTO(entity.getQuestion()));
		}

		dto.setIsCorrect(entity.getIsCorrect());
		dto.setHasAnswered(entity.getHasAnswered());
		dto.setId(entity.getId());

		return dto;
	}

	@Override
	public RealTestUserResultQuestion transformToEntity(RealTestUserResultQuestionDTO dto) {

		// Ova tranformacija nije potrebna jer se ne radi nikad update iz DTO u
		// entitet

		return null;
	}

	private void addUserAnswers(RealTestUserResultQuestion question, RealTestUserResultQuestionDTO questionDTO) {

		Set<QuestionAnswer> questionAnswers = question.getUserAnswers();

		for (QuestionAnswer questionAnswer : questionAnswers) {

			QuestionAnswerDTO questionAnswerDTO = questionAnswerDTOTransformer.transformToDTO(questionAnswer);

			questionDTO.getUserAnswers().add(questionAnswerDTO);
		}
	}

}
