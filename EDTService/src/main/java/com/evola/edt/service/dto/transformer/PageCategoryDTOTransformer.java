package com.evola.edt.service.dto.transformer;

import javax.inject.Named;

import com.evola.edt.model.PageCategory;
import com.evola.edt.model.dto.PageCategoryDTO;

/**
 * @author Nikola 23.05.2013.
 *
 */
@Named
public class PageCategoryDTOTransformer implements
		DTOTransformer<PageCategoryDTO, PageCategory> {

	@Override
	public PageCategoryDTO transformToDTO(PageCategory entity,
			String... fetchFields) {
		
		if (entity == null) {
			return null;
		}
		
		PageCategoryDTO dto = new PageCategoryDTO(entity.getName());
		dto.setId(entity.getId());
		dto.setOrderIndex(entity.getOrderIndex());
		dto.setNote(entity.getNote());
		dto.setPageType(entity.getPageType());
		
		return dto;
	}

	@Override
	public PageCategory transformToEntity(PageCategoryDTO dto) {
		if (dto == null) {
			return null;
		}
		
		PageCategory category = new PageCategory(dto.getName());
		category.setId(dto.getId());
		category.setOrderIndex(dto.getOrderIndex());
		category.setNote(dto.getNote());
		category.setPageType(dto.getPageType());
		
		return category;
	}

}
