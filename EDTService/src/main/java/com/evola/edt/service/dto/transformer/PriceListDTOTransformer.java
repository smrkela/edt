package com.evola.edt.service.dto.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.evola.edt.model.PriceList;
import com.evola.edt.model.PriceListPrice;
import com.evola.edt.model.dto.PriceListCategoryDTO;
import com.evola.edt.model.dto.PriceListCategoryViewDTO;
import com.evola.edt.model.dto.PriceListDTO;
import com.evola.edt.model.dto.PriceListSubCategoryDTO;
import com.evola.edt.model.dto.PriceListPriceDTO;
import com.evola.edt.model.dto.PriceListSubcategoryViewDTO;

/**
 * @author Nikola 21.09.2013.
 * 
 */
@Component
public class PriceListDTOTransformer implements DTOTransformer<PriceListDTO, PriceList> {
	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDTOTransformer;
	@Inject
	private PriceListPriceDTOTransformer priceListSubcategoryPriceDTOTransformer;

	@Override
	public PriceListDTO transformToDTO(PriceList entity, String... fetchFields) {

		if (entity != null) {

			PriceListDTO dto = new PriceListDTO();

			dto.setId(entity.getId());
			dto.setDescription(entity.getDescription());

			if (Arrays.asList(fetchFields).contains("drivingSchool")) {
				dto.setDrivingSchoolDTO(drivingSchoolDTOTransformer.transformToDTO(entity.getDrivingSchool()));
			}

			List<PriceListPrice> priceListPrices = entity.getPriceListPrices();

			List<PriceListPriceDTO> priceListSubcategoryPriceDTOs = new ArrayList<PriceListPriceDTO>();

			for (PriceListPrice subcategoryPrice : priceListPrices) {

				PriceListPriceDTO transformToDTO = priceListSubcategoryPriceDTOTransformer
						.transformToDTO(subcategoryPrice);

				priceListSubcategoryPriceDTOs.add(transformToDTO);
			}

			dto.setPriceListPriceDTOs(priceListSubcategoryPriceDTOs);

			Map<PriceListCategoryDTO, List<PriceListSubCategoryDTO>> map = new HashMap<PriceListCategoryDTO, List<PriceListSubCategoryDTO>>();

			for (PriceListPriceDTO priceListPriceDTO : priceListSubcategoryPriceDTOs) {
				PriceListCategoryDTO priceListCategoryDTO = priceListPriceDTO.getPriceListCategoryDTO();
				List<PriceListSubCategoryDTO> list = null;
				if (!map.containsKey(priceListCategoryDTO)) {
					list = new ArrayList<PriceListSubCategoryDTO>();
					map.put(priceListCategoryDTO, list);
				} else {
					list = map.get(priceListCategoryDTO);
				}
				list.add(priceListPriceDTO.getPriceListSubCategoryDTO());
			}

			Set<Entry<PriceListCategoryDTO, List<PriceListSubCategoryDTO>>> entrySet = map.entrySet();
			List<PriceListCategoryViewDTO> priceListCategoryViewDTOs = new ArrayList<PriceListCategoryViewDTO>();

			for (Entry<PriceListCategoryDTO, List<PriceListSubCategoryDTO>> entry : entrySet) {

				PriceListCategoryDTO key = entry.getKey();
				List<PriceListSubCategoryDTO> value = entry.getValue();
				List<PriceListSubcategoryViewDTO> priceListSubcategoryViewDTOs = new ArrayList<PriceListSubcategoryViewDTO>();

				double sum = 0;
				
				for (PriceListSubCategoryDTO priceListSubCategoryDTO : value) {

					PriceListSubcategoryViewDTO priceListSubcategoryViewDTO = new PriceListSubcategoryViewDTO();
					
					priceListSubcategoryViewDTO.setName(priceListSubCategoryDTO.getName());
					priceListSubcategoryViewDTO.setId(priceListSubCategoryDTO.getId());

					for (PriceListPrice subcategoryPrice : priceListPrices) {

						if (subcategoryPrice.getPriceListSubcategory().getId().equals(priceListSubCategoryDTO.getId())
								&& key.getId().equals(subcategoryPrice.getPriceListCategory().getId())) {
							priceListSubcategoryViewDTO.setPrice(subcategoryPrice.getPrice().getValue());
							break;
						}
					}
					
					sum += priceListSubcategoryViewDTO.getPrice();
					
					priceListSubcategoryViewDTOs.add(priceListSubcategoryViewDTO);
				}

				PriceListCategoryViewDTO priceListCategoryViewDTO = new PriceListCategoryViewDTO();
				priceListCategoryViewDTO.setName(key.getName());
				priceListCategoryViewDTO.setId(key.getId());
				priceListCategoryViewDTO.setPriceListSubcategoryViewDTOs(priceListSubcategoryViewDTOs);
				priceListCategoryViewDTO.setTotalPrice(sum);

				priceListCategoryViewDTOs.add(priceListCategoryViewDTO);
			}

			dto.setListCategoryViewDTOs(priceListCategoryViewDTOs);

			return dto;
		}
		return null;
	}

	@Override
	public PriceList transformToEntity(PriceListDTO dto) {
		if (dto != null) {
			PriceList entity = new PriceList();
			entity.setId(dto.getId());
			entity.setDescription(dto.getDescription());
			entity.setDrivingSchool(drivingSchoolDTOTransformer.transformToEntity(dto.getDrivingSchoolDTO()));
			List<PriceListPriceDTO> priceListPriceDTOs = dto.getPriceListPriceDTOs();
			List<PriceListPrice> priceListPrices = new ArrayList<PriceListPrice>();
			for (PriceListPriceDTO priceListPriceDTO : priceListPriceDTOs) {
				PriceListPrice transformToEntity = priceListSubcategoryPriceDTOTransformer
						.transformToEntity(priceListPriceDTO);
				priceListPrices.add(transformToEntity);
			}
			entity.setPriceListPrices(priceListPrices);
			return entity;
		}
		return null;
	}

}
