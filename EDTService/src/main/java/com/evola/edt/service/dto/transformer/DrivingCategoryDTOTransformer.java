package com.evola.edt.service.dto.transformer;

import javax.inject.Named;

import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.dto.DrivingCategoryDTO;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class DrivingCategoryDTOTransformer implements DTOTransformer<DrivingCategoryDTO, DrivingCategory> {

	@Override
	public DrivingCategoryDTO transformToDTO(DrivingCategory dc, String... fetchFields) {
		
		if (dc == null) {
			return null;
		}
		
		DrivingCategoryDTO dto = new DrivingCategoryDTO();
		dto.setId(dc.getId());
		dto.setName(dc.getName());
		dto.setCategoryId(dc.getCategoryId());
		dto.setOrderIndex(dc.getOrderIndex());
		return dto;
	}

	@Override
	public DrivingCategory transformToEntity(DrivingCategoryDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		DrivingCategory dc = new DrivingCategory();
		dc.setId(dto.getId());
		dc.setName(dto.getName());
		dc.setCategoryId(dto.getCategoryId());
		dc.setOrderIndex(dto.getOrderIndex());
		return dc;
	}

}
