package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolMark;
import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolMarkDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.UserDTO;

/**
 * @author Nikola 14.05.2013.
 * 
 */
@Named
public class DrivingSchoolMarkDTOTransformer implements
		DTOTransformer<DrivingSchoolMarkDTO, DrivingSchoolMark> {

	@Inject
	private UserDTOTransformer userDTOTransformer;

	@Inject
	private DrivingSchoolDTOTransformer schoolDTOTransformer;

	@Override
	public DrivingSchoolMarkDTO transformToDTO(DrivingSchoolMark entity,
			String... fetchFields) {

		if (entity == null) {
			return null;
		}

		DrivingSchoolMarkDTO dto = new DrivingSchoolMarkDTO();

		if (Arrays.asList(fetchFields).contains("author")) {
			UserDTO userDTO = userDTOTransformer.transformToDTO(entity
					.getAuthor());
			dto.setAuthor(userDTO);
		}

		if (Arrays.asList(fetchFields).contains("school")) {
			DrivingSchoolDTO schoolDTO = schoolDTOTransformer
					.transformToDTO(entity.getSchool());
			dto.setSchool(schoolDTO);
		}

		dto.setId(entity.getId());
		dto.setComment(entity.getComment());
		dto.setDate(entity.getDate());
		dto.setMark(entity.getMark());
		return dto;
	}

	@Override
	public DrivingSchoolMark transformToEntity(DrivingSchoolMarkDTO dto) {

		if (dto == null) {
			return null;
		}

		User author = userDTOTransformer.transformToEntity(dto.getAuthor());

		DrivingSchool school = schoolDTOTransformer.transformToEntity(dto
				.getSchool());
		
		DrivingSchoolMark drivingSchoolMark = new DrivingSchoolMark();
		drivingSchoolMark.setAuthor(author);
		drivingSchoolMark.setSchool(school);
		drivingSchoolMark.setId(dto.getId());
		drivingSchoolMark.setDate(dto.getDate());
		drivingSchoolMark.setComment(dto.getComment());
		drivingSchoolMark.setMark(dto.getMark());
		drivingSchoolMark.setId(dto.getId());
		return drivingSchoolMark;
	}
}
