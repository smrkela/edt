package com.evola.edt.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.ActivityMessage;

public interface ActivityMessageRepository extends CrudRepository<ActivityMessage, Long> {

	@Query("SELECT e FROM ActivityMessage e WHERE e.type IN (:types)")
	public Page<ActivityMessage> findByType(@Param("types") List<String> types, Pageable pageRequest);

	@Query("SELECT e FROM ActivityMessage e WHERE e.type IN (:types) AND e.creationDate > :from AND e.creationDate < :to")
	public Page<ActivityMessage> findByTypeAndDate(@Param("types") List<String> includedTypes, @Param("from") Date fromDate,
			@Param("to") Date toDate, Pageable pageRequest);

}