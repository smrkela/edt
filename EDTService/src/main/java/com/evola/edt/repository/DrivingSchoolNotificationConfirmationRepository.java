package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.DrivingSchoolNotificationConfirmation;

public interface DrivingSchoolNotificationConfirmationRepository extends JpaRepository<DrivingSchoolNotificationConfirmation, Long> {

	@Query(value="SELECT c FROM DrivingSchoolNotificationConfirmation c WHERE c.notification.id = :notificationId")
	public List<DrivingSchoolNotificationConfirmation> findAllForNotification(@Param("notificationId") Long notificationId, Pageable pageable);
	
	@Query("SELECT c FROM DrivingSchoolNotificationConfirmation c WHERE c.token = :notificationToken")
	public DrivingSchoolNotificationConfirmation findByToken(@Param("notificationToken") String notificationToken);
	
	@Query("SELECT c FROM DrivingSchoolNotificationConfirmation c WHERE c.school.id = :schoolId AND c.notification.id = :notificationId ORDER BY c.creationDate DESC")
	public List<DrivingSchoolNotificationConfirmation> findAllForNotification(@Param("schoolId") Long schoolId, @Param("notificationId") Long notificationId);

}
