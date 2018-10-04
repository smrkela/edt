package com.evola.edt.service.dto.transformer;

import java.util.Arrays;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionAnswer;
import com.evola.edt.model.QuestionCategory;
import com.evola.edt.model.QuestionImage;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.QuestionAnswerDTO;
import com.evola.edt.model.dto.QuestionCategoryDTO;
import com.evola.edt.model.dto.QuestionDTO;
import com.evola.edt.model.dto.QuestionImageDTO;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class QuestionDTOTransformer implements
		DTOTransformer<QuestionDTO, Question> {

	@Inject
	private DrivingCategoryDTOTransformer drivingCategoryDTOTransformer;
	@Inject
	private QuestionAnswerDTOTransformer questionAnswerDTOTransformer;
	@Inject
	private QuestionCategoryDTOTransformer questionCategoryDTOTransformer;
	@Inject
	private QuestionImageDTOTransformer questionImageDTOTransformer;

	@Override
	public QuestionDTO transformToDTO(Question q, String... fetchFields) {
		
		if (q == null) {
			return null;
		}
		
		QuestionDTO dto = new QuestionDTO();
		dto.setId(q.getId());
		dto.setQuestionId(q.getQuestionId());
		dto.setNumber(q.getNumber());
		dto.setText(q.getText());
		dto.setRemark(q.getRemark());
		dto.setPoints(q.getPoints());
		dto.setHelpUrl(q.getHelpUrl());
		
		if (Arrays.asList(fetchFields).contains("questionCategories")) {
			addQuestionCategories(q, dto);
		}
		if (Arrays.asList(fetchFields).contains("drivingCategories")) {
			addDrivingCategories(q, dto);
		}
		if (Arrays.asList(fetchFields).contains("questionAnswers")) {
			addQuestionAnswers(q, dto);
		}
		if (Arrays.asList(fetchFields).contains("questionImages")) {
			addQuestionImage(q, dto);
		}
		
		return dto;
	}

	private void addQuestionAnswers(Question q, QuestionDTO dto) {
		
		Set<QuestionAnswer> questionAnswers = q.getQuestionAnswers();
		
		String[] letters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i"};
		
		int index = 0;
		
		for (QuestionAnswer questionAnswer : questionAnswers) {
			
			QuestionAnswerDTO questionAnswerDTO = questionAnswerDTOTransformer
					.transformToDTO(questionAnswer);
			
			questionAnswerDTO.setLetter(letters[index++]+")");
			
			dto.getQuestionAnswers().add(questionAnswerDTO);
		}
	}

	private void addQuestionImage(Question q, QuestionDTO dto) {
		Set<QuestionImage> questionImages = q.getQuestionImages();
		for (QuestionImage questionImage : questionImages) {
			QuestionImageDTO questionImageDTO = questionImageDTOTransformer
					.transformToDTO(questionImage);
			dto.getQuestionImages().add(questionImageDTO);
		}
	}

	private void addDrivingCategories(Question q, QuestionDTO dto) {
		Set<DrivingCategory> drivingCategories = q.getDrivingCategories();
		for (DrivingCategory drivingCategory : drivingCategories) {
			DrivingCategoryDTO drivingCategoryDTO = drivingCategoryDTOTransformer
					.transformToDTO(drivingCategory);
			dto.getDrivingCategories().add(drivingCategoryDTO);
		}
	}

	private void addQuestionCategories(Question q, QuestionDTO dto) {
		Set<QuestionCategory> questionCategories = q.getQuestionCategories();
		for (QuestionCategory questionCategory : questionCategories) {
			QuestionCategoryDTO drivingCategoryDTO = questionCategoryDTOTransformer
					.transformToDTO(questionCategory);
			dto.getQuestionCategories().add(drivingCategoryDTO);
		}
	}

	@Override
	public Question transformToEntity(QuestionDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		Question question = new Question();
		question.setId(dto.getId());
		question.setQuestionId(dto.getQuestionId());
		question.setNumber(dto.getNumber());
		question.setText(dto.getText());
		question.setRemark(dto.getRemark());
		question.setPoints(dto.getPoints());
		question.setHelpUrl(dto.getHelpUrl());
		return question;
	}
	

}
