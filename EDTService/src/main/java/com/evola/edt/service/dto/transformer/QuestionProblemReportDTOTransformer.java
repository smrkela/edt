package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.QuestionProblemReport;
import com.evola.edt.model.dto.QuestionProblemReportDTO;

@Named
public class QuestionProblemReportDTOTransformer implements DTOTransformer<QuestionProblemReportDTO, QuestionProblemReport> {

	@Inject
	private ProblemCategoryDTOTransformer problemCategoryDTOTransformer;
	
	@Inject
	private QuestionDTOTransformer questionDTOTransformer;
	
	@Inject
	private UserDTOTransformer userDTOTransformer;
	
	@Override
	public QuestionProblemReportDTO transformToDTO(QuestionProblemReport q, String... fetchFields) {
		
		if (q == null) {
			return null;
		}
		
		QuestionProblemReportDTO dto = new QuestionProblemReportDTO();
		
		dto.setId(q.getId());
		dto.setFixDate(q.getFixDate());
		dto.setFixed(q.isFixed());
		dto.setReportingDate(q.getReportingDate());
		dto.setUserComment(q.getUserComment());
		
		if (Arrays.asList(fetchFields).contains("problemCategory")) {
			
			dto.setProblemCategoryDTO(problemCategoryDTOTransformer.transformToDTO(q.getProblemCategory()));
		}
		
		if (Arrays.asList(fetchFields).contains("question")) {
			
			dto.setQuestionDTO(questionDTOTransformer.transformToDTO(q.getQuestion()));
		}
		
		if (Arrays.asList(fetchFields).contains("user")) {
			
			dto.setUserDTO(userDTOTransformer.transformToDTO(q.getUser()));
		}
		
		return dto;
	}

	@Override
	public QuestionProblemReport transformToEntity(QuestionProblemReportDTO dto) {

		if (dto == null) {
			return null;
		}
		
		QuestionProblemReport e = new QuestionProblemReport();
		e.setFixDate(dto.getFixDate());
		e.setFixed(dto.isFixed());
		e.setId(dto.getId());
		e.setProblemCategory(problemCategoryDTOTransformer.transformToEntity(dto.getProblemCategoryDTO()));
		e.setQuestion(questionDTOTransformer.transformToEntity(dto.getQuestionDTO()));
		e.setReportingDate(dto.getReportingDate());
		e.setUser(userDTOTransformer.transformToEntity(dto.getUserDTO()));
		e.setUserComment(dto.getUserComment());
		
		return e;
	}

}
