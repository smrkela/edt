package com.evola.edt.service.dto.transformer;

import java.util.Arrays;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DailyTestUserResult;
import com.evola.edt.model.DailyTestUserResultQuestion;
import com.evola.edt.model.QuestionAnswer;
import com.evola.edt.model.dto.DailyTestUserResultQuestionDTO;
import com.evola.edt.model.dto.QuestionAnswerDTO;

/**
 * @author Daci, 13.05.2014.
 */

@Named
public class DailyTestUserResultQuestionDTOTransformer implements
		DTOTransformer<DailyTestUserResultQuestionDTO, DailyTestUserResultQuestion> {

	@Inject
	private QuestionDTOTransformer questionDTOTransformer;

	@Inject
	private QuestionAnswerDTOTransformer questionAnswerDTOTransformer;

	@Override
	public DailyTestUserResultQuestionDTO transformToDTO(DailyTestUserResultQuestion entity, String... fetchFields) {

		if (entity == null) {
			return null;
		}

		DailyTestUserResultQuestionDTO dto = new DailyTestUserResultQuestionDTO();

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
	public DailyTestUserResultQuestion transformToEntity(DailyTestUserResultQuestionDTO dto) {

		// Ova tranformacija nije potrebna jer se ne radi nikad update iz DTO u
		// entitet

		return null;
	}

	private void addUserAnswers(DailyTestUserResultQuestion question, DailyTestUserResultQuestionDTO questionDTO) {

		Set<QuestionAnswer> questionAnswers = question.getUserAnswers();

		for (QuestionAnswer questionAnswer : questionAnswers) {

			QuestionAnswerDTO questionAnswerDTO = questionAnswerDTOTransformer.transformToDTO(questionAnswer);

			questionDTO.getUserAnswers().add(questionAnswerDTO);
		}
	}

}
