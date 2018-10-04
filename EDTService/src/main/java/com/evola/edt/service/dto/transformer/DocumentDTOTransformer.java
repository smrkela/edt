package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.jcr.Document;
import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.helpers.DocumentDTO;

@Named
public class DocumentDTOTransformer {

	@Inject
	UserRepository userRepository;

	@Inject
	UserDTOTransformer userDTOTransformer;

	public DocumentDTO transformToDTO(Document dc, String... fetchFields) {

		if (dc == null) {
			return null;
		}
		
		DocumentDTO dto = new DocumentDTO();
		dto.setId(dc.getIdentifier());
		dto.setCreationDate(dc.getMetadata().getCreatedOn().getTime());
		dto.setDescription(dc.getMetadata().getDescription());
		dto.setName(dc.getName());
		dto.setPath(dc.getPath());
		dto.setSize(dc.getMetadata().getSize());
		dto.setParentId(dc.getParentId());

		if (Arrays.asList(fetchFields).contains("author")) {

			// autor mozda nije ni definisan a mozda i ne postoji u bazi
			try {
				Long userId = Long.parseLong(dc.getMetadata().getCreatedBy());

				dto.setAuthor(userDTOTransformer.transformToDTO(userRepository
						.findById(userId)));
			} catch (Exception e) {
			}
		}

		return dto;
	}

}
