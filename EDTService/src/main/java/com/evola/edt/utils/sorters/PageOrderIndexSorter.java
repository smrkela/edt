/**
 * 
 */
package com.evola.edt.utils.sorters;

import java.util.Comparator;

import com.evola.edt.model.dto.PageDTO;

/**
 * @author Daci, Jun 5, 2014
 *
 */
public class PageOrderIndexSorter implements Comparator<PageDTO> {

	/**
	 * Sortira PageCategory objekte u listi prema orderIndex-u
	 */
	@Override
	public int compare(PageDTO pageDTO1, PageDTO pageDTO2) {
		return pageDTO1.getOrderIndex().compareTo(pageDTO2.getOrderIndex());
	}

}
