package com.evola.edt.repository;

import org.springframework.data.repository.CrudRepository;

import com.evola.edt.model.PendingRegistration;

/**
 * @author Nikola 27.04.2013.
 *
 */
public interface PendingRegistrationRepository extends
		CrudRepository<PendingRegistration, Long> {
	public PendingRegistration findByToken(String token);
}
