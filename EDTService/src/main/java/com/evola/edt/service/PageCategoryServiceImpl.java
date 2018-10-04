package com.evola.edt.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.model.PageCategory;
import com.evola.edt.model.dto.PageCategoryDTO;
import com.evola.edt.repository.PageCategoryRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.transformer.PageCategoryDTOTransformer;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * @author Nikola 24.05.2013.
 * 
 */
@Named
public class PageCategoryServiceImpl implements PageCategoryService {
	
	@Inject
	private PageCategoryRepository pageCategoryRepository;

	@Inject
	private PageCategoryDTOTransformer pageCategoryDTOTransformer;

	@Inject
	private UserRepository userRepository;
	
	@Inject
	private UserCredentialsManager credentialsManager;

	@Override
	public List<PageCategoryDTO> findAll() {
		List<PageCategory> findAll = pageCategoryRepository.findAll();
		List<PageCategoryDTO> dtos = new ArrayList<PageCategoryDTO>();
		for (PageCategory pageCategory : findAll) {
			dtos.add(pageCategoryDTOTransformer.transformToDTO(pageCategory));
		}
		return dtos;
	}
	
	@Override
	public List<PageCategoryDTO> findAllForNews() {
		
		List<PageCategory> findAll = pageCategoryRepository.findByPageType("NEWS");
		
		List<PageCategoryDTO> dtos = new ArrayList<PageCategoryDTO>();
		for (PageCategory pageCategory : findAll) {
			dtos.add(pageCategoryDTOTransformer.transformToDTO(pageCategory));
		}
		return dtos;
	}
	
	@Override
	public List<PageCategoryDTO> findAllForInformations() {
		
		List<PageCategory> findAll = pageCategoryRepository.findByPageType("INFORMATION");
		
		List<PageCategoryDTO> dtos = new ArrayList<PageCategoryDTO>();
		for (PageCategory pageCategory : findAll) {
			dtos.add(pageCategoryDTOTransformer.transformToDTO(pageCategory));
		}
		return dtos;
	}
	
	@Override
	public List<PageCategoryDTO> findAllForHelp() {
		
		List<PageCategory> findAll = pageCategoryRepository.findByPageType("HELP");
		
		List<PageCategoryDTO> dtos = new ArrayList<PageCategoryDTO>();
		for (PageCategory pageCategory : findAll) {
			dtos.add(pageCategoryDTOTransformer.transformToDTO(pageCategory));
		}
		return dtos;
	}

	@Override
	public PageCategoryDTO findById(Long id) {

		Assert.notNull(id);

		PageCategory findOne = pageCategoryRepository.findOne(id);

		PageCategoryDTO dto = pageCategoryDTOTransformer
				.transformToDTO(findOne);

		return dto;
	}

	@Override
	public void deleteById(Long id) {

		credentialsManager.checkSystemAdministration();
		
		Assert.notNull(id);

		pageCategoryRepository.delete(id);
	}

	@Override
	@Transactional
	public void addNew(PageCategoryDTO dto) {

		credentialsManager.checkSystemAdministration();
		
		PageCategory pageCategory = pageCategoryDTOTransformer
				.transformToEntity(dto);

		pageCategoryRepository.save(pageCategory);
	}

	@Override
	@Transactional
	public void update(PageCategoryDTO dto) {
		
		credentialsManager.checkSystemAdministration();

		PageCategory oldPage = pageCategoryRepository.findOne(dto.getId());

		oldPage.setName(dto.getName());
		oldPage.setOrderIndex(dto.getOrderIndex());
		oldPage.setNote(dto.getNote());
		oldPage.setPageType(dto.getPageType());

		pageCategoryRepository.save(oldPage);
	}
}
