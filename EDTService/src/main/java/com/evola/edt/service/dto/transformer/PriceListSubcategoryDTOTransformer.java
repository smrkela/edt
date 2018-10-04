/**
 * 
 */
package com.evola.edt.service.dto.transformer;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.evola.edt.model.PriceListSubcategory;
import com.evola.edt.model.dto.PriceListSubCategoryDTO;

/**
 * @author Nikola 21.09.2013.
 * 
 */
@Component
public class PriceListSubcategoryDTOTransformer implements
		DTOTransformer<PriceListSubCategoryDTO, PriceListSubcategory> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.evola.edt.service.dto.transformer.DTOTransformer#transformToDTO(java
	 * .lang.Object, java.lang.String[])
	 */
	@Override
	public PriceListSubCategoryDTO transformToDTO(PriceListSubcategory entity,
			String... fetchFields) {
		if (entity != null) {
			PriceListSubCategoryDTO dto = new PriceListSubCategoryDTO(
					entity.getName());
			dto.setId(entity.getId());
			return dto;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.evola.edt.service.dto.transformer.DTOTransformer#transformToEntity
	 * (java.lang.Object)
	 */
	@Override
	public PriceListSubcategory transformToEntity(PriceListSubCategoryDTO dto) {
		if (dto != null) {
			PriceListSubcategory entity = new PriceListSubcategory(
					dto.getName());
			entity.setId(dto.getId());
			return entity;
		}
		return null;
	}

}
