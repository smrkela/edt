package com.evola.edt.service;

import java.util.List;

import com.evola.edt.model.dto.DrivingCategoryDTO;

/**
 * @author Daci, 28.12.2013.
 *
 */
public interface DrivingCategoryService {

	public List<DrivingCategoryDTO> findAll();

	public DrivingCategoryDTO findOne(Long categoryId);
	
}
