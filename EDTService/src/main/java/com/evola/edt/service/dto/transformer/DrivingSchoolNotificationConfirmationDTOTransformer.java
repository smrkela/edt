package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.DrivingSchoolNotificationConfirmation;
import com.evola.edt.model.DrivingSchoolStudent;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationConfirmationDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.DrivingSchoolStudentDTO;

/**
 * @author Daci, Jan 6, 2015
 *
 */
@Named
public class DrivingSchoolNotificationConfirmationDTOTransformer implements DTOTransformer<DrivingSchoolNotificationConfirmationDTO, DrivingSchoolNotificationConfirmation> {

	@Inject
	private DrivingSchoolNotificationDTOTransformer notificationDTOTransformer;
	
	@Inject
	private DrivingSchoolStudentDTOTransformer studentDTOTransformer;
	
	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDTOTransformer;
	
	@Override
	public DrivingSchoolNotificationConfirmationDTO transformToDTO(DrivingSchoolNotificationConfirmation entity, String... fetchFields) {
		
		if (entity == null){
			return null;
		}
		
		DrivingSchoolNotificationConfirmationDTO dto = new DrivingSchoolNotificationConfirmationDTO();
		
		if (Arrays.asList(fetchFields).contains("notification")){
			DrivingSchoolNotificationDTO notificationDTO = notificationDTOTransformer.transformToDTO(entity.getNotification());
			dto.setNotification(notificationDTO);
		}
		
		if (Arrays.asList(fetchFields).contains("student")){
			DrivingSchoolStudentDTO studentDTO = studentDTOTransformer.transformToDTO(entity.getStudent());
			dto.setStudent(studentDTO);
		}
		
		if (Arrays.asList(fetchFields).contains("school")){
			DrivingSchoolDTO schoolDTO = drivingSchoolDTOTransformer.transformToDTO(entity.getSchool());
			dto.setSchool(schoolDTO);
		}
		
		dto.setId(entity.getId());
		dto.setCreationDate(entity.getCreationDate());
		dto.setToken(entity.getToken());
		dto.setConfirmed(entity.getConfirmed());
		dto.setConfirmationDate(entity.getConfirmationDate());
		
		return dto;
	}

	@Override
	public DrivingSchoolNotificationConfirmation transformToEntity(DrivingSchoolNotificationConfirmationDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		DrivingSchoolNotificationConfirmation entity = new DrivingSchoolNotificationConfirmation();
		
		DrivingSchoolNotification notification = notificationDTOTransformer.transformToEntity(dto.getNotification());
		DrivingSchoolStudent student = studentDTOTransformer.transformToEntity(dto.getStudent());
		DrivingSchool school = drivingSchoolDTOTransformer.transformToEntity(dto.getSchool());
		
		entity.setNotification(notification);
		entity.setStudent(student);
		entity.setSchool(school);
		entity.setCreationDate(dto.getCreationDate());
		entity.setToken(dto.getToken());
		entity.setConfirmed(dto.getConfirmed());
		entity.setConfirmationDate(dto.getConfirmationDate());
		
		return entity;
	}

}
