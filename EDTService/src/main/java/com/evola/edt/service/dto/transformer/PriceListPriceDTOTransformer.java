/**
 * 
 */
package com.evola.edt.service.dto.transformer;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.evola.edt.model.PriceListPrice;
import com.evola.edt.model.dto.PriceListPriceDTO;

/**
 * @author Nikola 21.09.2013.
 * 
 */
@Component
public class PriceListPriceDTOTransformer implements
		DTOTransformer<PriceListPriceDTO, PriceListPrice> {
	@Inject
	private PriceListSubcategoryDTOTransformer priceListSubcategoryDTOTransformer;
	@Inject
	private PriceListCategoryDTOTransformer priceListCategoryDTOTransformer;
	@Inject
	private PriceDTOTransformer priceDTOTransformer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.evola.edt.service.dto.transformer.DTOTransformer#transformToDTO(java
	 * .lang.Object, java.lang.String[])
	 */
	@Override
	public PriceListPriceDTO transformToDTO(PriceListPrice entity,
			String... fetchFields) {
		if (entity != null) {
			PriceListPriceDTO dto = new PriceListPriceDTO(
					priceListSubcategoryDTOTransformer.transformToDTO(entity
							.getPriceListSubcategory()),
					priceListCategoryDTOTransformer.transformToDTO(entity
							.getPriceListCategory()),
					priceDTOTransformer.transformToDTO(entity.getPrice()));
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
	public PriceListPrice transformToEntity(PriceListPriceDTO dto) {
		if (dto != null) {
			PriceListPrice entity = new PriceListPrice(
					priceListSubcategoryDTOTransformer.transformToEntity(dto
							.getPriceListSubCategoryDTO()),
					priceListCategoryDTOTransformer.transformToEntity(dto
							.getPriceListCategoryDTO()),
					priceDTOTransformer.transformToEntity(dto.getPriceDTO()));
			entity.setId(dto.getId());
			return entity;
		}
		return null;
	}
}
