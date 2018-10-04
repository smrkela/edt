package com.evola.edt.service.dto.transformer;

import javax.inject.Named;

import com.evola.edt.model.QuestionCategory;
import com.evola.edt.model.dto.QuestionCategoryDTO;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class QuestionCategoryDTOTransformer implements
		DTOTransformer<QuestionCategoryDTO, QuestionCategory> {

	@Override
	public QuestionCategoryDTO transformToDTO(QuestionCategory qc, String... fetchFields) {
		
		if (qc == null) {
			return null;
		}
		
		QuestionCategoryDTO dto = new QuestionCategoryDTO();
		dto.setId(qc.getId());
		dto.setName(qc.getName());
		dto.setCategoryId(qc.getCategoryId());
		dto.setOrderIndex(qc.getOrderIndex());
		
		return dto;
	}

	@Override
	public QuestionCategory transformToEntity(QuestionCategoryDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		QuestionCategory qc = new QuestionCategory();
		qc.setId(dto.getId());
		qc.setName(dto.getName());
		qc.setCategoryId(dto.getCategoryId());
		qc.setOrderIndex(dto.getOrderIndex());
		
		return qc;
	}

}
