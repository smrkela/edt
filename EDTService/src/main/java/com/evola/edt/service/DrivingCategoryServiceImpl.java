package com.evola.edt.service;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.repository.DrivingCategoryRepository;
import com.evola.edt.service.dto.transformer.DrivingCategoryDTOTransformer;

/**
 * @author Daci, 28.12.2013.
 * 
 */
@Named
public class DrivingCategoryServiceImpl implements DrivingCategoryService {

	@Inject
	private DrivingCategoryRepository drivingCategoryRepository;

	@Inject
	private DrivingCategoryDTOTransformer drivingCategoryDTOTransformer;

	@Override
	@Transactional(readOnly = true)
	public List<DrivingCategoryDTO> findAll() {

		List<DrivingCategoryDTO> dtos = new LinkedList<DrivingCategoryDTO>();
		List<DrivingCategory> findAll = drivingCategoryRepository.findAllSorted();

		for (DrivingCategory page : findAll) {
			dtos.add(drivingCategoryDTOTransformer.transformToDTO(page));
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingCategoryDTO findOne(Long categoryId) {

		DrivingCategoryDTO dto = null;

		DrivingCategory findOne = drivingCategoryRepository.findOne(categoryId);

		dto = drivingCategoryDTOTransformer.transformToDTO(findOne);

		return dto;
	}
}
