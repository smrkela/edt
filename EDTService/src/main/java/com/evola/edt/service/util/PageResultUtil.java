package com.evola.edt.service.util;

import com.evola.edt.model.dto.PageResultDTO;

/**
 * @author Nikola 19.05.2013.
 * 
 */
public class PageResultUtil {
	public static PageResultDTO populateCommon(
			org.springframework.data.domain.Page page, PageResultDTO dto) {
		dto.setFirstPage(page.isFirstPage());
		dto.setHasNextPage(page.hasNextPage());
		dto.setHasPreviousPage(page.hasPreviousPage());
		dto.setLastPage(page.isLastPage());
		dto.setNumber(dto.getNumber());
		dto.setNumberOfElements(page.getNumberOfElements());
		dto.setTotalElements(page.getTotalElements());
		dto.setTotalPages(page.getTotalPages());
		return dto;
	}
}
