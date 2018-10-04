package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.Page;
import com.evola.edt.model.PageCategory;

/**
 * @author Nikola 14.05.2013.
 * 
 */
public interface PageRepository extends JpaRepository<Page, Long> {

	public org.springframework.data.domain.Page<Page> findAll(Pageable pageable);
	
	@Query("SELECT e FROM Page e WHERE e.pageType='NEWS'")
	public org.springframework.data.domain.Page<Page> findAllNews(Pageable pageable);
	
	@Query("SELECT e FROM Page e WHERE e.pageType='INFORMATIONS' order by e.category ASC, e.orderIndex ASC")
	public org.springframework.data.domain.Page<Page> findAllInformations(Pageable pageable);
	
	@Query("SELECT e FROM Page e WHERE e.pageType='HELP' order by e.category ASC, e.orderIndex ASC")
	public org.springframework.data.domain.Page<Page> findAllHelps(Pageable pageable);

	public Page findByUniqueName(String uniqueName);

	@Query("select count(p) from Page p where p.uniqueName=:uniqueName")
	public Long countByUniqueName(@Param("uniqueName") String uniqueName);

	@Query("select count(c) from PageComment c where c.page.id = :pageId")
	public Long findNumOfComments(@Param("pageId") Long id);

	@Query("select count(p) from Page p where p.uniqueName=:uniqueName AND p.id <> :id")
	public Long countByUniqueNameExcept(@Param("uniqueName") String uniqueName, @Param("id") Long id);

	public List<Page> findByCategory(PageCategory category);

}
