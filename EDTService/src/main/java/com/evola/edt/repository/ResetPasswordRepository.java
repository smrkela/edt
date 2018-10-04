package com.evola.edt.repository;

import org.springframework.data.repository.CrudRepository;

import com.evola.edt.model.ResetPassword;

/**
 * @author Nikola 18.05.2013.
 * 
 */
public interface ResetPasswordRepository extends
		CrudRepository<ResetPassword, Long> {
	public ResetPassword findByToken(String token);
}
