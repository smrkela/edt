package com.evola.edt.utils.sorters;

import java.util.Comparator;

import com.evola.edt.model.dto.PageCategoryDTO;

/**
 * @author Daci, 08.03.2014.
 *
 */
public class PageCategoryOrderIndexSorter implements Comparator<PageCategoryDTO> {

	/**
	 * Sortira PageCategory objekte u listi prema orderIndex-u
	 */
	@Override
	public int compare(PageCategoryDTO pcDTO1, PageCategoryDTO pcDTO2) {
		return pcDTO1.getOrderIndex().compareTo(pcDTO2.getOrderIndex());
	}

}
