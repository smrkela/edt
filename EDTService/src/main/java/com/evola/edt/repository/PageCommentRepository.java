package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.PageComment;

/**
 * @author Nikola 14.05.2013.
 * 
 */
public interface PageCommentRepository extends JpaRepository<PageComment, Long> {

	public Page<PageComment> findAll(Pageable pageable);

	@Query("SELECT c FROM PageComment c WHERE c.page.id = :pageId ORDER BY c.date DESC")
	public List<PageComment> findAll(@Param("pageId") Long pageId);

}
