package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingLicenceCategory;
import com.evola.edt.model.dto.DrivingLicenceCategoryDTO;

@Named
public class DrivingLicenceCategoryDTOTransformer implements DTOTransformer<DrivingLicenceCategoryDTO, DrivingLicenceCategory> {

	@Inject
	private DrivingCategoryDTOTransformer tDrivingCategory;

	@Override
	public DrivingLicenceCategoryDTO transformToDTO(DrivingLicenceCategory dc, String... fetchFields) {

		if(dc == null)
			return null;
		
		DrivingLicenceCategoryDTO dto = new DrivingLicenceCategoryDTO();
		dto.setId(dc.getId());
		dto.setName(dc.getName());
		dto.setCategoryId(dc.getCategoryId());
		dto.setOrderIndex(dc.getOrderIndex());

		if (Arrays.asList(fetchFields).contains("category"))
			dto.setCategory(tDrivingCategory.transformToDTO(dc.getDrivingCategory()));

		return dto;
	}

	@Override
	public DrivingLicenceCategory transformToEntity(DrivingLicenceCategoryDTO dto) {

		if (dto == null) {
			return null;
		}
		
		DrivingLicenceCategory dc = new DrivingLicenceCategory();
		dc.setId(dto.getId());
		dc.setName(dto.getName());
		dc.setCategoryId(dto.getCategoryId());
		dc.setOrderIndex(dto.getOrderIndex());
		dc.setDrivingCategory(tDrivingCategory.transformToEntity(dto.getCategory()));

		return dc;
	}

}
