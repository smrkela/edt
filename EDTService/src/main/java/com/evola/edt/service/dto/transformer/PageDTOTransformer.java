package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.Page;
import com.evola.edt.model.PageCategory;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.PageCategoryDTO;
import com.evola.edt.model.dto.PageDTO;
import com.evola.edt.model.dto.UserDTO;

/**
 * @author Nikola 14.05.2013.
 * 
 */
@Named
public class PageDTOTransformer implements DTOTransformer<PageDTO, Page> {

	@Inject
	private PageCategoryDTOTransformer categoryDTOTransformer;
	
	@Inject
	private UserDTOTransformer userDTOTransformer;

	@Override
	public PageDTO transformToDTO(Page entity, String... fetchFields) {
		if (entity == null) {
			return null;
		}
		PageDTO dto = new PageDTO(entity.getId(), entity.getTitle(),
				entity.getContent());
		
		if (Arrays.asList(fetchFields).contains("pageCategory")) {
			PageCategoryDTO categoryDTO = categoryDTOTransformer
					.transformToDTO(entity.getCategory());
			dto.setCategoryDTO(categoryDTO);
		}
		
		if (Arrays.asList(fetchFields).contains("author")) {
			UserDTO userDTO = userDTOTransformer
					.transformToDTO(entity.getAuthor());
			dto.setAuthorDTO(userDTO);
		}
		
		dto.setId(entity.getId());
		dto.setUniqueName(entity.getUniqueName());
		dto.setCreationDate(entity.getCreationDate());
		dto.setSmallPreview(entity.getSmallPreview());
		dto.setSmallPreviewImage(entity.getSmallPreviewImage());
		dto.setNormalPreview(entity.getNormalPreview());
		dto.setNormalPreviewImage(entity.getNormalPreviewImage());
		dto.setOrderIndex(entity.getOrderIndex());
		
		return dto;
	}

	@Override
	public Page transformToEntity(PageDTO dto) {
		if (dto == null) {
			return null;
		}
		PageCategory pageCategory = categoryDTOTransformer
				.transformToEntity(dto.getCategoryDTO());
		User author= userDTOTransformer.transformToEntity(dto.getAuthorDTO());
		Page page = new Page(dto.getTitle(), dto.getContent(), pageCategory);
		page.setAuthor(author);
		page.setId(dto.getId());
		page.setUniqueName(dto.getUniqueName());
		page.setCreationDate(dto.getCreationDate());
		page.setSmallPreview(dto.getSmallPreview());
		page.setSmallPreviewImage(dto.getSmallPreviewImage());
		page.setNormalPreview(dto.getNormalPreview());
		page.setNormalPreviewImage(dto.getNormalPreviewImage());
		page.setOrderIndex(dto.getOrderIndex());
		
		return page;
	}
}
