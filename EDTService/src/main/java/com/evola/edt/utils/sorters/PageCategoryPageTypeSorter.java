package com.evola.edt.utils.sorters;

import java.util.Comparator;

import com.evola.edt.model.dto.PageCategoryDTO;

/**
 * @author Daci, 08.03.2014.
 *
 */
public class PageCategoryPageTypeSorter implements Comparator<PageCategoryDTO> {

	/**
	 * Sortira PageCategory objekte u listi prema pageType-u
	 */
	@Override
	public int compare(PageCategoryDTO pcDTO1, PageCategoryDTO pcDTO2) {
		return pcDTO1.getPageType().compareTo(pcDTO2.getPageType());
	}

}
