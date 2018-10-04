package com.evola.edt.service.dto.transformer;

import org.springframework.stereotype.Component;

import com.evola.edt.model.ProblemCategory;
import com.evola.edt.model.dto.ProblemCategoryDTO;

@Component
public class ProblemCategoryDTOTransformer implements DTOTransformer<ProblemCategoryDTO, ProblemCategory> {

	@Override
	public ProblemCategoryDTO transformToDTO(ProblemCategory pc, String... fetchFields) {
		
		if (pc == null) {
			return null;
		}
		
		ProblemCategoryDTO dto = new ProblemCategoryDTO();
		dto.setId(pc.getId());
		dto.setProblemCategoryId(pc.getProblemCategoryId());
		dto.setName(pc.getName());
		dto.setDescription(pc.getDescription());
		return dto;
	}

	@Override
	public ProblemCategory transformToEntity(ProblemCategoryDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		ProblemCategory pc = new ProblemCategory();
		pc.setId(dto.getId());
		pc.setProblemCategoryId(dto.getProblemCategoryId());
		pc.setName(dto.getName());
		pc.setDescription(dto.getDescription());
		return pc;
	}

}
