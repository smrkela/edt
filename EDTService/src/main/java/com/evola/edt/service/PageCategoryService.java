package com.evola.edt.service;

import java.util.List;

import com.evola.edt.model.dto.PageCategoryDTO;

public interface PageCategoryService {

	/**
	 * @author Nikola 24.05.2013.
	 * @return
	 */
	public abstract List<PageCategoryDTO> findAll();

	public abstract PageCategoryDTO findById(Long id);
	
	public abstract void deleteById(Long id);

	public abstract void addNew(PageCategoryDTO dto);

	public abstract void update(PageCategoryDTO dto);

	public abstract List<PageCategoryDTO> findAllForNews();

	public abstract List<PageCategoryDTO> findAllForInformations();
	
	public abstract List<PageCategoryDTO> findAllForHelp();

}