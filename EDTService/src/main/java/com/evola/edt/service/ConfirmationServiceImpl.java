package com.evola.edt.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.model.PendingRegistration;
import com.evola.edt.model.User;
import com.evola.edt.repository.PendingRegistrationRepository;
import com.evola.edt.repository.UserRepository;

@Named
public class ConfirmationServiceImpl implements ConfirmationService {

	@Inject
	private UserRepository userRepository;

	@Inject
	private PendingRegistrationRepository pendingRegistrationRepository;

	@Value("${registrationTokenMaxAgeInHours}")
	private String registrationTokenMaxAgeInHours;

	/* (non-Javadoc)
	 * @see com.evola.edt.service.ConfirmationService#confirmRegistration(java.lang.String)
	 */
	@Override
	@Transactional
	public void confirmRegistration(String token) {
		Assert.notNull(registrationTokenMaxAgeInHours,
				"registrationTokenMaxAgeInHours can't be null");
		Assert.notNull(token, "Token can't be null");
		PendingRegistration pendingRegistration = pendingRegistrationRepository
				.findByToken(token);
		validateToken(pendingRegistration);
		pendingRegistration.setConfirmed(true);
		pendingRegistrationRepository.save(pendingRegistration);
		User user = pendingRegistration.getUser();
		user.setEnabled(true);
		userRepository.save(user);
	}

	private void validateToken(PendingRegistration pendingRegistration) {
		if (pendingRegistration == null) {
			throw new IllegalStateException(
					"Pending registration can't be found.");
		}
		Integer registrationTokenMaxAgeInHoursInteger = Integer
				.valueOf(registrationTokenMaxAgeInHours);
		DateTime dateCreated = new DateTime(
				pendingRegistration.getCreationDate());
		DateTime now = DateTime.now();
		if (dateCreated.plusHours(registrationTokenMaxAgeInHoursInteger)
				.isBefore(now)) {
			throw new IllegalStateException("Token expired.");
		}
	}
}
