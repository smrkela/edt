package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingSchoolMember;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolMemberDTO;
import com.evola.edt.model.dto.UserDTO;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class DrivingSchoolMemberDTOTransformer implements
		DTOTransformer<DrivingSchoolMemberDTO, DrivingSchoolMember> {

	@Inject
	private UserDTOTransformer userDtoTransformer;

	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDtoTransformer;

	@Override
	public DrivingSchoolMemberDTO transformToDTO(DrivingSchoolMember dc, String... fetchFields) {

		if (dc == null) {
			return null;
		}
		
		DrivingSchoolMemberDTO dto = new DrivingSchoolMemberDTO();
		dto.setId(dc.getId());
		dto.setCreationDate(dc.getCreationDate());
		dto.setRole(dc.getRole());

		if (Arrays.asList(fetchFields).contains("school")) {

			DrivingSchoolDTO schoolDTO = drivingSchoolDtoTransformer
					.transformToDTO(dc.getDrivingSchool());
			dto.setSchool(schoolDTO);
		}

		if (Arrays.asList(fetchFields).contains("user")) {

			UserDTO userDTO = userDtoTransformer.transformToDTO(dc.getUser());
			dto.setUser(userDTO);
		}

		return dto;
	}

	@Override
	public DrivingSchoolMember transformToEntity(DrivingSchoolMemberDTO dto) {

		if (dto == null)
			return null;

		DrivingSchoolMember dc = new DrivingSchoolMember();
		dc.setId(dto.getId());
		dc.setCreationDate(dto.getCreationDate());
		dc.setRole(dto.getRole());

		if (dto.getSchool() != null) {
			dc.setDrivingSchool(drivingSchoolDtoTransformer
					.transformToEntity(dto.getSchool()));
		}

		if (dto.getUser() != null) {
			dc.setUser(userDtoTransformer.transformToEntity(dto.getUser()));
		}

		return dc;
	}

}
