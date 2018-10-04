package com.evola.edt.service;

import java.util.List;

import com.evola.edt.model.dto.PageCommentDTO;
import com.evola.edt.model.dto.PageDTO;
import com.evola.edt.model.dto.PageResultDTO;
import com.evola.edt.service.dto.PagesDTO;

public interface PageService {

	public abstract PageDTO findPageById(Long pageId);

	public abstract PageDTO findPageByUniqueName(String uniqueName);

	public abstract PageResultDTO<PageDTO> findAll(Integer pageNumber,
			Integer pageSize);

	public abstract List<PageDTO> findLatestPages(int maxPages);

	public abstract PagesDTO findNews(Integer startingPage, int count);
	
	public abstract PagesDTO findInformations(Integer startingPage, int ount);

	public abstract PagesDTO findHelps(Integer startingPage, int cunt);

	public abstract List<PageCommentDTO> findLatestComments(int i);
	
	public abstract List<PageCommentDTO> findComments(Long pageId);

	public abstract void addNewPage(PageDTO dto);

	public abstract void updatePage(PageDTO dto);

	public abstract void removePage(Long pageId);

	public abstract void addNewInformations(PageDTO dto);

	public abstract void updateInformations(PageDTO dto);

	public abstract List<PageDTO> findPageByCategory(Long id);

	public abstract void savePageComment(String uniqueName, String comment);

	public abstract void addNewHelp(PageDTO pageDto);
	
	public abstract void updateHelp(PageDTO pageDto);

}