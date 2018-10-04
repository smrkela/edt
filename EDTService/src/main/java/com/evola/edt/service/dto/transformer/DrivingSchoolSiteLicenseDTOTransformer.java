package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.evola.edt.model.DrivingSchoolLicenseType;
import com.evola.edt.model.DrivingSchoolSiteLicense;
import com.evola.edt.model.dto.DrivingSchoolSiteLicenseDTO;

@Named
public class DrivingSchoolSiteLicenseDTOTransformer implements
		DTOTransformer<DrivingSchoolSiteLicenseDTO, DrivingSchoolSiteLicense> {

	@Autowired
	private DrivingSchoolDTOTransformer drivingSchoolTransformer;

	@Autowired
	private UserDTOTransformer userTransformer;

	@Override
	public DrivingSchoolSiteLicenseDTO transformToDTO(DrivingSchoolSiteLicense entity, String... fetchFields) {
		if (entity == null) {
			return null;
		}

		DrivingSchoolSiteLicenseDTO dto = new DrivingSchoolSiteLicenseDTO();
		dto.setId(entity.getId());
		dto.setActive(entity.getIsActive());
		dto.setCreationDate(entity.getCreationDate());
		dto.setValidFrom(entity.getValidFrom());
		dto.setValidTo(entity.getValidTo());
		dto.setLicenseType(entity.getLicenseType().name());

		if (Arrays.asList(fetchFields).contains("drivingSchool"))
			dto.setDrivingSchool(drivingSchoolTransformer.transformToDTO(entity.getDrivingSchool()));

		if (Arrays.asList(fetchFields).contains("author"))
			dto.setAuthor(userTransformer.transformToDTO(entity.getAuthor()));

		return dto;
	}

	@Override
	public DrivingSchoolSiteLicense transformToEntity(DrivingSchoolSiteLicenseDTO dto) {
		if (dto == null) {
			return null;
		}

		DrivingSchoolSiteLicense entity = new DrivingSchoolSiteLicense();
		entity.setId(dto.getId());
		entity.setIsActive(dto.isActive());
		entity.setCreationDate(dto.getCreationDate());
		entity.setValidFrom(dto.getValidFrom());
		entity.setValidTo(dto.getValidTo());
		entity.setLicenseType(DrivingSchoolLicenseType.valueOf(dto.getLicenseType()));

		entity.setDrivingSchool(drivingSchoolTransformer.transformToEntity(dto.getDrivingSchool()));
		entity.setAuthor(userTransformer.transformToEntity(dto.getAuthor()));

		return entity;
	}

}
