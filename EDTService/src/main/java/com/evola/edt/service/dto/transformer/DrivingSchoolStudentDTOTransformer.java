package com.evola.edt.service.dto.transformer;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingLicenceCategory;
import com.evola.edt.model.DrivingSchoolStudent;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolStudentDTO;
import com.evola.edt.model.dto.UserDTO;

@Named
public class DrivingSchoolStudentDTOTransformer implements DTOTransformer<DrivingSchoolStudentDTO, DrivingSchoolStudent> {

	@Inject
	private DrivingLicenceCategoryDTOTransformer tDrivingLicenceCategory;

	@Inject
	private UserDTOTransformer tUser;

	@Inject
	private DrivingSchoolDTOTransformer tDrivingSchool;

	@Override
	public DrivingSchoolStudentDTO transformToDTO(DrivingSchoolStudent dc, String... fetchFields) {

		if (dc == null) {
			return null;
		}
		
		DrivingSchoolStudentDTO dto = new DrivingSchoolStudentDTO();
		dto.setId(dc.getId());
		dto.setCreationDate(dc.getCreationDate());
		dto.setFirstName(dc.getFirstName());
		dto.setLastName(dc.getLastName());
		dto.setEmail(dc.getEmail());
		dto.setPhone(dc.getPhone());
		dto.setIsMale(dc.getIsMale());
		dto.setAddress(dc.getAddress());
		dto.setCity(dc.getCity());
		dto.setRegisterDate(dc.getRegisterDate());
		dto.setIsTheoryCompleted(dc.getIsTheoryCompleted());
		dto.setIsTheoryPassed(dc.getIsTheoryPassed());
		dto.setTheoryPassedDate(dc.getTheoryPassedDate());
		dto.setIsPracticeCompleted(dc.getIsPracticeCompleted());
		dto.setIsPracticePassed(dc.getIsPracticePassed());
		dto.setPracticePassedDate(dc.getPracticePassedDate());
		dto.setIsFirstAidPassed(dc.getIsFirstAidPassed());
		dto.setFirstAidPassedDate(dc.getFirstAidPassedDate());
		dto.setIsAllPassed(dc.getIsAllPassed());
		dto.setAllPassedDate(dc.getAllPassedDate());
		dto.setComment(dc.getComment());
		dto.setSendNotifications(dc.getSendNotifications());
		dto.setInviteToJoin(dc.getInviteToJoin());
		dto.setInvitationSent(dc.getInvitationSent());
		dto.setDeleted(dc.getDeleted());

		
		if (Arrays.asList(fetchFields).contains("category"))
			dto.setCategory(tDrivingLicenceCategory.transformToDTO(dc.getCategory()));

		if (Arrays.asList(fetchFields).contains("user"))
			dto.setUser(tUser.transformToDTO(dc.getUser()));

		if (Arrays.asList(fetchFields).contains("drivingSchool"))
			dto.setDrivingSchool(tDrivingSchool.transformToDTO(dc.getDrivingSchool()));

		return dto;
	}

	@Override
	public DrivingSchoolStudent transformToEntity(DrivingSchoolStudentDTO dto) {

		if (dto == null) {
			return null;
		}
			
		DrivingSchoolStudent dc = new DrivingSchoolStudent();
		dc.setId(dto.getId());
		dc.setUser(tUser.transformToEntity(dto.getUser()));
		dc.setCreationDate(dto.getCreationDate());
		dc.setFirstName(dto.getFirstName());
		dc.setLastName(dto.getLastName());
		dc.setEmail(dto.getEmail());
		dc.setPhone(dto.getPhone());
		dc.setIsMale(dto.getIsMale());
		dc.setAddress(dto.getAddress());
		dc.setCity(dto.getCity());
		dc.setRegisterDate(dto.getRegisterDate());
		dc.setIsTheoryCompleted(dto.getIsTheoryCompleted());
		dc.setIsTheoryPassed(dto.getIsTheoryPassed());
		dc.setTheoryPassedDate(dto.getTheoryPassedDate());
		dc.setIsPracticeCompleted(dto.getIsPracticeCompleted());
		dc.setIsPracticePassed(dto.getIsPracticePassed());
		dc.setPracticePassedDate(dto.getPracticePassedDate());
		dc.setIsFirstAidPassed(dto.getIsFirstAidPassed());
		dc.setFirstAidPassedDate(dto.getFirstAidPassedDate());
		dc.setIsAllPassed(dto.getIsAllPassed());
		dc.setAllPassedDate(dto.getAllPassedDate());
		dc.setComment(dto.getComment());
		dc.setSendNotifications(dto.getSendNotifications());
		dc.setInviteToJoin(dto.getInviteToJoin());
		dc.setInvitationSent(dto.getInvitationSent());
		dc.setDeleted(dto.getDeleted());

		return dc;
	}

}
