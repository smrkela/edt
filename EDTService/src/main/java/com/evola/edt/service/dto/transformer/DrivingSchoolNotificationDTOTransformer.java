package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.UserDTO;

/**
 * @author Nikola 14.05.2013.
 * 
 */
@Named
public class DrivingSchoolNotificationDTOTransformer implements DTOTransformer<DrivingSchoolNotificationDTO, DrivingSchoolNotification> {

	@Inject
	private UserDTOTransformer userDTOTransformer;
	
	@Inject
	private DrivingSchoolDTOTransformer schoolDTOTransformer;

	@Override
	public DrivingSchoolNotificationDTO transformToDTO(DrivingSchoolNotification entity, String... fetchFields) {
		
		if (entity == null) {
			return null;
		}
		
		DrivingSchoolNotificationDTO dto = new DrivingSchoolNotificationDTO();
		
		if (Arrays.asList(fetchFields).contains("author")) {
			UserDTO userDTO = userDTOTransformer
					.transformToDTO(entity.getAuthor());
			dto.setAuthorDTO(userDTO);
		}
		
		if (Arrays.asList(fetchFields).contains("school")) {
			DrivingSchoolDTO schoolDTO = schoolDTOTransformer.transformToDTO(entity.getSchool());
			dto.setSchool(schoolDTO);
		}
		
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setContent(entity.getContent());
		dto.setCreationDate(entity.getCreationDate());
		dto.setSendNotification(entity.getSendNotification());
		dto.setRequestNotificationReadConfirmation(entity.getRequestNotificationReadConfirmation());
		
		return dto;
	}

	@Override
	public DrivingSchoolNotification transformToEntity(DrivingSchoolNotificationDTO dto) {
		
		if (dto == null) {
			return null;
		}
		
		User author= userDTOTransformer.transformToEntity(dto.getAuthorDTO());
		DrivingSchool school= schoolDTOTransformer.transformToEntity(dto.getSchool());
		
		DrivingSchoolNotification notification = new DrivingSchoolNotification();
		notification.setAuthor(author);
		notification.setSchool(school);
		notification.setId(dto.getId());
		notification.setCreationDate(dto.getCreationDate());
		notification.setTitle(dto.getTitle());
		notification.setContent(dto.getContent());
		notification.setSendNotification(dto.getSendNotification());
		notification.setRequestNotificationReadConfirmation(dto.getRequestNotificationReadConfirmation());
		
		return notification;
	}
}
