package com.evola.edt.service.dto.transformer;

import javax.inject.Named;

import com.evola.edt.model.QuestionImage;
import com.evola.edt.model.dto.QuestionImageDTO;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class QuestionImageDTOTransformer implements
		DTOTransformer<QuestionImageDTO, QuestionImage> {

	@Override
	public QuestionImageDTO transformToDTO(QuestionImage qi, String... fetchFields) {
		
		if (qi == null) {
			return null;
		}
		
		QuestionImageDTO dto = new QuestionImageDTO();
		dto.setId(qi.getId());
		dto.setUrl(qi.getUrl());
		dto.setText(qi.getText());
		dto.setOrderIndex(qi.getOrderIndex());
		return dto;
	}

	@Override
	public QuestionImage transformToEntity(QuestionImageDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		QuestionImage qi = new QuestionImage();
		qi.setId(dto.getId());
		qi.setUrl(dto.getUrl());
		qi.setText(dto.getText());
		qi.setOrderIndex(dto.getOrderIndex());
		return qi;
	}

}
