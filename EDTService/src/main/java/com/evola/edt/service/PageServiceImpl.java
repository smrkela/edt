package com.evola.edt.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.jcr.ContentRepositoryManager;
import com.evola.edt.managers.ActivityManager;
import com.evola.edt.model.Page;
import com.evola.edt.model.PageCategory;
import com.evola.edt.model.PageComment;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.PageCommentDTO;
import com.evola.edt.model.dto.PageDTO;
import com.evola.edt.model.dto.PageResultDTO;
import com.evola.edt.repository.PageCategoryRepository;
import com.evola.edt.repository.PageCommentRepository;
import com.evola.edt.repository.PageRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.PagesDTO;
import com.evola.edt.service.dto.transformer.PageCommentDTOTransformer;
import com.evola.edt.service.dto.transformer.PageDTOTransformer;
import com.evola.edt.service.util.PageResultUtil;
import com.evola.edt.utils.TidyUtil;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * @author Nikola 14.05.2013.
 * 
 */
@Named
public class PageServiceImpl implements PageService {

	@Inject
	private PageRepository pageRepository;

	@Inject
	private PageCommentRepository pageCommentRepository;

	@Inject
	private PageDTOTransformer pageDTOTransformer;

	@Inject
	private PageCommentDTOTransformer pageCommentDTOTransformer;

	@Inject
	private UserRepository userRepository;

	@Inject
	private ContentRepositoryManager repositoryManager;

	@Inject
	private PageCategoryRepository pageCategoryRepository;
	
	@Inject
	private UserCredentialsManager credentialsManager;
	
	@Inject
	private ActivityManager mActivity;

	@Override
	@Transactional
	public void addNewPage(PageDTO pageDto) {
		
		credentialsManager.checkSystemAdministration();

		Long countByUniqueName = pageRepository.countByUniqueName(pageDto.getUniqueName());

		if (countByUniqueName > 0) {
			throw new IllegalStateException("Postoji strana sa datim jedinstvenim imenom");
		}

		Page page = pageDTOTransformer.transformToEntity(pageDto);

		// moramo procistiti html
		page.setContent(TidyUtil.tidify(page.getContent()));
		page.setNormalPreview(TidyUtil.tidify(page.getNormalPreview()));

		// moramo podesiti autora
		User author = userRepository.findById(SecurityUtils.getUserId());
		page.setAuthor(author);

		// ubacujemo trenutno vreme za vreme pravljenja
		page.setCreationDate(new Date());

		// setujemo tip
		page.setPageType("NEWS");

		page = pageRepository.save(page);
		
		mActivity.newsAdded(page);

		savePageImages(pageDto, page.getId());
	}

	@Override
	@Transactional
	public void addNewInformations(PageDTO pageDto) {
		
		credentialsManager.checkSystemAdministration();

		Long countByUniqueName = pageRepository.countByUniqueName(pageDto.getUniqueName());

		if (countByUniqueName > 0) {
			throw new IllegalStateException("Postoji strana sa datim jedinstvenim imenom");
		}

		Page page = pageDTOTransformer.transformToEntity(pageDto);

		// moramo procistiti html
		page.setContent(TidyUtil.tidify(page.getContent()));

		// moramo podesiti autora
		User author = userRepository.findById(SecurityUtils.getUserId());
		page.setAuthor(author);

		// ubacujemo trenutno vreme za vreme pravljenja
		page.setCreationDate(new Date());

		// setujemo tip
		page.setPageType("INFORMATIONS");

		page = pageRepository.save(page);
		
		mActivity.informationsAdded(page);
	}
	
	@Override
	@Transactional
	public void addNewHelp(PageDTO pageDto) {
		
		credentialsManager.checkSystemAdministration();

		Long countByUniqueName = pageRepository.countByUniqueName(pageDto.getUniqueName());

		if (countByUniqueName > 0) {
			throw new IllegalStateException("Postoji strana sa datim jedinstvenim imenom");
		}

		Page page = pageDTOTransformer.transformToEntity(pageDto);

		// moramo procistiti html
		page.setContent(TidyUtil.tidify(page.getContent()));

		// moramo podesiti autora
		User author = userRepository.findById(SecurityUtils.getUserId());
		page.setAuthor(author);

		// ubacujemo trenutno vreme za vreme pravljenja
		page.setCreationDate(new Date());

		// setujemo tip
		page.setPageType("HELP");

		pageRepository.save(page);
	}

	@Override
	@Transactional
	public void updatePage(PageDTO pageDto) {
		
		credentialsManager.checkSystemAdministration();

		Long countByUniqueName = pageRepository.countByUniqueNameExcept(pageDto.getUniqueName(), pageDto.getId());

		if (countByUniqueName > 0) {
			throw new IllegalStateException("Postoji strana sa datim jedinstvenim imenom");
		}

		Page page = pageDTOTransformer.transformToEntity(pageDto);

		Page oldPage = pageRepository.findOne(page.getId());
		oldPage.setTitle(page.getTitle());
		oldPage.setContent(TidyUtil.tidify(page.getContent()));
		oldPage.setSmallPreview(page.getSmallPreview());
		oldPage.setNormalPreview(TidyUtil.tidify(page.getNormalPreview()));
		oldPage.setCategory(page.getCategory());
		oldPage.setUniqueName(page.getUniqueName());
		oldPage.setOrderIndex(page.getOrderIndex());
		

		pageRepository.save(oldPage);

		savePageImages(pageDto, page.getId());
	}

	@Override
	@Transactional
	public void updateInformations(PageDTO pageDto) {
		
		credentialsManager.checkSystemAdministration();

		Long countByUniqueName = pageRepository.countByUniqueNameExcept(pageDto.getUniqueName(), pageDto.getId());

		if (countByUniqueName > 0) {
			throw new IllegalStateException("Postoji strana sa datim jedinstvenim imenom");
		}

		Page page = pageDTOTransformer.transformToEntity(pageDto);

		Page oldPage = pageRepository.findOne(page.getId());
		oldPage.setTitle(page.getTitle());
		oldPage.setContent(TidyUtil.tidify(page.getContent()));
		oldPage.setCategory(page.getCategory());
		oldPage.setUniqueName(page.getUniqueName());
		oldPage.setOrderIndex(page.getOrderIndex());

		pageRepository.save(oldPage);
	}
	
	@Override
	@Transactional
	public void updateHelp(PageDTO pageDto) {
		
		credentialsManager.checkSystemAdministration();

		Long countByUniqueName = pageRepository.countByUniqueNameExcept(pageDto.getUniqueName(), pageDto.getId());

		if (countByUniqueName > 0) {
			throw new IllegalStateException("Postoji strana sa datim jedinstvenim imenom");
		}

		Page page = pageDTOTransformer.transformToEntity(pageDto);

		Page oldPage = pageRepository.findOne(page.getId());
		oldPage.setTitle(page.getTitle());
		oldPage.setContent(TidyUtil.tidify(page.getContent()));
		oldPage.setCategory(page.getCategory());
		oldPage.setUniqueName(page.getUniqueName());
		oldPage.setOrderIndex(page.getOrderIndex());

		pageRepository.save(oldPage);
	}

	@Override
	@Transactional(readOnly = true)
	public PageDTO findPageById(Long pageId) {
		Assert.notNull(pageId);
		Page page = pageRepository.findOne(pageId);
		return pageDTOTransformer.transformToDTO(page, "pageCategory");
	}

	/**
	 * @author Nikola 15.05.2013.
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public PageResultDTO<PageDTO> findAll(Integer pageNumber, Integer pageSize) {
		Assert.notNull(pageNumber);
		Assert.notNull(pageSize);
		org.springframework.data.domain.Page<Page> all = pageRepository.findAll(new PageRequest(pageNumber, pageSize));
		List<Page> content = all.getContent();
		List<PageDTO> dtos = new LinkedList<PageDTO>();
		for (Page page : content) {
			dtos.add(pageDTOTransformer.transformToDTO(page));
		}
		PageResultDTO<PageDTO> dto = new PageResultDTO<PageDTO>();
		PageResultUtil.populateCommon(all, dto);
		dto.setDtos(dtos);
		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public PageDTO findPageByUniqueName(String uniqueName) {

		Assert.notNull(uniqueName);

		Page page = pageRepository.findByUniqueName(uniqueName);

		return pageDTOTransformer.transformToDTO(page, "author", "pageCategory");
	}

	@Override
	@Transactional(readOnly = true)
	public List<PageDTO> findLatestPages(int maxPages) {

		org.springframework.data.domain.Page<Page> all = pageRepository.findAllNews(new PageRequest(0, maxPages, new Sort(
				Direction.DESC, "creationDate")));

		List<Page> content = all.getContent();

		List<PageDTO> dtos = new LinkedList<PageDTO>();

		for (Page page : content) {
			dtos.add(pageDTOTransformer.transformToDTO(page));
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public PagesDTO findNews(Integer startingIndex, int count) {

		org.springframework.data.domain.Page<Page> findAll = pageRepository.findAllNews(new PageRequest(startingIndex,
				count, new Sort(Direction.DESC, "creationDate")));

		List<PageDTO> dtos = new LinkedList<PageDTO>();

		for (Page q : findAll) {

			PageDTO dto = pageDTOTransformer.transformToDTO(q, "author", "pageCategory");

			// ucitavamo i broj komentara za svaku vest
			dto.setNumOfComments(pageRepository.findNumOfComments(q.getId()).intValue());

			dtos.add(dto);
		}

		PagesDTO dto = new PagesDTO();
		dto.setPages(dtos);
		dto.setIsFirstPage(findAll.isFirstPage());
		dto.setIsLastPage(findAll.isLastPage());

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public PagesDTO findInformations(Integer startingIndex, int count) {

		org.springframework.data.domain.Page<Page> findAll = pageRepository.findAllInformations(new PageRequest(startingIndex, count));
		
		List<PageDTO> dtos = new LinkedList<PageDTO>();

		for (Page q : findAll) {

			PageDTO dto = pageDTOTransformer.transformToDTO(q, "author", "pageCategory");

			dtos.add(dto);
		}

		PagesDTO dto = new PagesDTO();
		dto.setPages(dtos);
		dto.setIsFirstPage(findAll.isFirstPage());
		dto.setIsLastPage(findAll.isLastPage());

		return dto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public PagesDTO findHelps(Integer startingIndex, int count) {

		org.springframework.data.domain.Page<Page> findAll = pageRepository.findAllHelps(new PageRequest(startingIndex, count));

		List<PageDTO> dtos = new LinkedList<PageDTO>();

		for (Page q : findAll) {

			PageDTO dto = pageDTOTransformer.transformToDTO(q, "author", "pageCategory");

			dtos.add(dto);
		}

		PagesDTO dto = new PagesDTO();
		dto.setPages(dtos);
		dto.setIsFirstPage(findAll.isFirstPage());
		dto.setIsLastPage(findAll.isLastPage());

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PageCommentDTO> findLatestComments(int count) {

		org.springframework.data.domain.Page<PageComment> latestComments = pageCommentRepository
				.findAll(new PageRequest(0, count, new Sort(Direction.DESC, "date")));

		List<PageCommentDTO> dtos = new LinkedList<PageCommentDTO>();

		for (PageComment q : latestComments) {

			PageCommentDTO dto = pageCommentDTOTransformer.transformToDTO(q, "author", "page");

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PageCommentDTO> findComments(Long pageId) {

		List<PageComment> latestComments = pageCommentRepository.findAll(pageId);

		List<PageCommentDTO> dtos = new LinkedList<PageCommentDTO>();

		for (PageComment q : latestComments) {

			PageCommentDTO dto = pageCommentDTOTransformer.transformToDTO(q, "author", "page");

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public void removePage(Long pageId) {
		
		credentialsManager.checkSystemAdministration();

		pageRepository.delete(pageId);
	}

	private void savePageImages(PageDTO pageDto, long pageId) {

		// sada cuvamo i fajl
		// putanja do slike nam je:
		// root/pages/pageId/smallPreview.png
		String logo = pageDto.getSmallPreviewImage();

		if (logo != null) {

			String folderPath = "/pages/" + pageId;

			repositoryManager.saveFileToRepository(folderPath, "smallPreview", logo, 100, 100);
		}

		logo = pageDto.getNormalPreviewImage();

		if (logo != null) {

			String folderPath = "/pages/" + pageId;

			repositoryManager.saveFileToRepository(folderPath, "normalPreview", logo, 500, 300);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<PageDTO> findPageByCategory(Long categoryId) {

		PageCategory category = pageCategoryRepository.findOne(categoryId);

		List<Page> pages = pageRepository.findByCategory(category);

		List<PageDTO> dtos = new LinkedList<PageDTO>();

		for (Page page : pages) {

			dtos.add(pageDTOTransformer.transformToDTO(page));
		}

		return dtos;
	}

	@Transactional
	@Override
	public void savePageComment(String pageUniqueName, String comment) {

		Page page = pageRepository.findByUniqueName(pageUniqueName);

		PageComment c = new PageComment();
		c.setAuthor(userRepository.findOne(SecurityUtils.getUserId()));
		c.setComment(comment);
		c.setDate(new Date());
		c.setPage(page);

		c = pageCommentRepository.save(c);
		
		mActivity.commentedNews(c);
	}

}
