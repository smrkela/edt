/**
 * 
 */
package com.evola.edt.service.dto.transformer;

import org.springframework.stereotype.Component;

import com.evola.edt.model.PriceListCategory;
import com.evola.edt.model.dto.PriceListCategoryDTO;

/**
 * @author Nikola 21.09.2013.
 * 
 */
@Component
public class PriceListCategoryDTOTransformer implements
		DTOTransformer<PriceListCategoryDTO, PriceListCategory> {

	@Override
	public PriceListCategoryDTO transformToDTO(PriceListCategory entity,
			String... fetchFields) {
		if (entity != null) {
			PriceListCategoryDTO dto = new PriceListCategoryDTO();
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			return dto;
		}
		return null;
	}

	@Override
	public PriceListCategory transformToEntity(PriceListCategoryDTO dto) {
		if (dto != null) {
			PriceListCategory entity = new PriceListCategory(dto.getName());
			entity.setId(dto.getId());
			return entity;
		}
		return null;
	}

}
