package com.evola.edt.service.dto.transformer;

import org.springframework.stereotype.Component;

import com.evola.edt.model.Currency;
import com.evola.edt.model.Price;
import com.evola.edt.model.dto.PriceDTO;

/**
 * @author Nikola 22.09.2013.
 * 
 */
@Component
public class PriceDTOTransformer implements DTOTransformer<PriceDTO, Price> {

	@Override
	public PriceDTO transformToDTO(Price entity, String... fetchFields) {
		if (entity != null) {
			PriceDTO dto = new PriceDTO(entity.getCurrency().getLabel(),
					entity.getValue());
			return dto;
		}
		return null;
	}

	@Override
	public Price transformToEntity(PriceDTO dto) {
		if (dto != null) {
			Price price = new Price(Currency.valueOf(dto.getCurrency()),
					dto.getValue());
			return price;
		}
		return null;
	}

}
