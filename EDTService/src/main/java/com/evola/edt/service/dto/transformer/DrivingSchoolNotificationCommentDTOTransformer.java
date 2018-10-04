package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.DrivingSchoolNotificationComment;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DrivingSchoolNotificationCommentDTO;

@Named
public class DrivingSchoolNotificationCommentDTOTransformer implements
		DTOTransformer<DrivingSchoolNotificationCommentDTO, DrivingSchoolNotificationComment> {

	@Inject
	private DrivingSchoolNotificationDTOTransformer notificationDTOTransformer;

	@Inject
	private UserDTOTransformer userDTOTransformer;

	@Override
	public DrivingSchoolNotificationCommentDTO transformToDTO(DrivingSchoolNotificationComment entity,
			String... fetchFields) {
		
		if (entity == null) {
			return null;
		}

		DrivingSchoolNotificationCommentDTO dto = new DrivingSchoolNotificationCommentDTO();
		dto.setComment(entity.getComment());
		dto.setDate(entity.getDate());
		dto.setId(entity.getId());

		if (Arrays.asList(fetchFields).contains("author")) {
			dto.setAuthor(userDTOTransformer.transformToDTO(entity.getAuthor()));
		}

		if (Arrays.asList(fetchFields).contains("notification")) {
			dto.setNotification(notificationDTOTransformer.transformToDTO(entity.getNotification()));
		}

		return dto;
	}

	@Override
	public DrivingSchoolNotificationComment transformToEntity(DrivingSchoolNotificationCommentDTO dto) {
		
		if (dto == null) {
			return null;
		}

		DrivingSchoolNotification notification = notificationDTOTransformer.transformToEntity(dto.getNotification());

		User author = userDTOTransformer.transformToEntity(dto.getAuthor());

		DrivingSchoolNotificationComment comment = new DrivingSchoolNotificationComment();
		comment.setAuthor(author);
		comment.setId(dto.getId());
		comment.setDate(dto.getDate());
		comment.setComment(dto.getComment());
		comment.setNotification(notification);

		return comment;
	}
}
