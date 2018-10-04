/**
 * 
 */
package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolStudent;
import com.evola.edt.model.MembershipRequestStatus;
import com.evola.edt.model.User;
import com.evola.edt.model.UserDrivingSchoolMembershipRequest;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.DrivingSchoolStudentDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.model.dto.UserDrivingSchoolMembershipRequestDTO;

/**
 * @author Daci, Jan 11, 2015
 *
 */
@Named
public class UserDrivingSchoolMembershipRequestDTOTransformer implements DTOTransformer<UserDrivingSchoolMembershipRequestDTO, UserDrivingSchoolMembershipRequest> {

	@Inject
	private UserDTOTransformer userDTOTransformer;
	
	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDTOTransformer;
	
	@Inject
	private DrivingSchoolStudentDTOTransformer drivingSchoolStudentDTOTransformer; 
	
	@Override
	public UserDrivingSchoolMembershipRequestDTO transformToDTO(UserDrivingSchoolMembershipRequest entity, String... fetchFields) {
		
		if (entity == null){
			return null;
		}
		
		UserDrivingSchoolMembershipRequestDTO dto = new UserDrivingSchoolMembershipRequestDTO();
		
		if (Arrays.asList(fetchFields).contains("user")) {
			UserDTO userDTO = userDTOTransformer.transformToDTO(entity.getUser(), "drivingCategory"); 
			dto.setUser(userDTO);
		}
		
		if (Arrays.asList(fetchFields).contains("drivingSchool")) {
			DrivingSchoolDTO drivingSchoolDTO = drivingSchoolDTOTransformer.transformToDTO(entity.getDrivingSchool());
			dto.setDrivingSchool(drivingSchoolDTO);
		}
		
		if (Arrays.asList(fetchFields).contains("drivingSchoolStudent")) {
			DrivingSchoolStudentDTO drivingSchoolStudentDTO = drivingSchoolStudentDTOTransformer.transformToDTO(entity.getDrivingSchoolStudent());
			dto.setDrivingSchoolStudent(drivingSchoolStudentDTO);
		}
		
		dto.setComment(entity.getComment());
		dto.setReceiveNotifications(entity.getReceiveNotifications());
		dto.setCreationDate(entity.getCreationDate());
		dto.setStatus(entity.getStatus().name());
		dto.setDecisionDate(entity.getDecisionDate());
		dto.setDecisionComment(entity.getDecisionComment());
		dto.seteMail(entity.geteMail());
		dto.setConfirmationToken(entity.getConfirmationToken());
		dto.setId(entity.getId());
		
		return dto;
	}

	@Override
	public UserDrivingSchoolMembershipRequest transformToEntity(UserDrivingSchoolMembershipRequestDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		UserDrivingSchoolMembershipRequest entity = new UserDrivingSchoolMembershipRequest();
		
		User user = userDTOTransformer.transformToEntity(dto.getUser());
		DrivingSchool drivingSchool = drivingSchoolDTOTransformer.transformToEntity(dto.getDrivingSchool());
		DrivingSchoolStudent drivingSchoolStudent = drivingSchoolStudentDTOTransformer.transformToEntity(dto.getDrivingSchoolStudent());
		
		entity.setUser(user);
		entity.setDrivingSchool(drivingSchool);
		entity.setComment(dto.getComment());
		entity.setReceiveNotifications(dto.getReceiveNotifications());
		entity.setCreationDate(dto.getCreationDate());
		entity.setStatus(MembershipRequestStatus.valueOf(dto.getStatus()));
		entity.setDecisionDate(dto.getDecisionDate());
		entity.setDecisionComment(dto.getDecisionComment());
		entity.seteMail(dto.geteMail());
		entity.setConfirmationToken(dto.getConfirmationToken());
		entity.setDrivingSchoolStudent(drivingSchoolStudent);
		
		return entity;
	}

}