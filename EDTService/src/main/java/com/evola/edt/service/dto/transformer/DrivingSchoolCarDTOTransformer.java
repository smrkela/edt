package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolCar;
import com.evola.edt.model.dto.DrivingSchoolCarDTO;

@Named
public class DrivingSchoolCarDTOTransformer implements
		DTOTransformer<DrivingSchoolCarDTO, DrivingSchoolCar> {

	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDTOTransformer;

	@Override
	public DrivingSchoolCarDTO transformToDTO(DrivingSchoolCar entity, String... fetchFields) {
		
		if (entity == null) {
			return null;
		}

		DrivingSchoolCarDTO dto = new DrivingSchoolCarDTO();
		dto.setId(entity.getId());
		dto.setOrderIndex(entity.getOrderIndex());
		dto.setDescription(entity.getDescription());
		dto.setMake(entity.getMake());
		dto.setModel(entity.getModel());
		dto.setType(entity.getType());
		dto.setYear(entity.getYear());

		if (Arrays.asList(fetchFields).contains("school")) {
			dto.setSchool(drivingSchoolDTOTransformer.transformToDTO(entity
					.getSchool()));
		}

		return dto;
	}

	@Override
	public DrivingSchoolCar transformToEntity(DrivingSchoolCarDTO dto) {

		if (dto == null) {
			return null;
		}

		DrivingSchool school = drivingSchoolDTOTransformer
				.transformToEntity(dto.getSchool());

		DrivingSchoolCar entity = new DrivingSchoolCar();
		entity.setId(dto.getId());
		entity.setSchool(school);
		entity.setOrderIndex(dto.getOrderIndex());
		entity.setDescription(dto.getDescription());
		entity.setMake(dto.getMake());
		entity.setModel(dto.getModel());
		entity.setType(dto.getType());
		entity.setYear(dto.getYear());

		return entity;
	}
}
