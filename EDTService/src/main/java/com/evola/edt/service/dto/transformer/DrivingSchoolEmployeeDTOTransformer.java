package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolEmployee;
import com.evola.edt.model.dto.DrivingSchoolEmployeeDTO;

@Named
public class DrivingSchoolEmployeeDTOTransformer implements
		DTOTransformer<DrivingSchoolEmployeeDTO, DrivingSchoolEmployee> {

	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDTOTransformer;

	@Override
	public DrivingSchoolEmployeeDTO transformToDTO(DrivingSchoolEmployee entity,
			String... fetchFields) {
		if (entity == null) {
			return null;
		}

		DrivingSchoolEmployeeDTO dto = new DrivingSchoolEmployeeDTO();
		dto.setComment(entity.getComment());
		dto.setId(entity.getId());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setRole(entity.getRole());
		dto.setOrderIndex(entity.getOrderIndex());
		dto.setExperience(entity.getExperience());

		if (Arrays.asList(fetchFields).contains("school")) {
			dto.setSchool(drivingSchoolDTOTransformer.transformToDTO(entity.getSchool()));
		}

		return dto;
	}

	@Override
	public DrivingSchoolEmployee transformToEntity(DrivingSchoolEmployeeDTO dto) {

		if (dto == null) {
			return null;
		}

		DrivingSchool school = drivingSchoolDTOTransformer.transformToEntity(dto.getSchool());

		DrivingSchoolEmployee entity = new DrivingSchoolEmployee();
		entity.setId(dto.getId());
		entity.setComment(dto.getComment());
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setRole(dto.getRole());
		entity.setSchool(school);
		entity.setOrderIndex(dto.getOrderIndex());
		entity.setExperience(dto.getExperience());

		return entity;
	}
}
