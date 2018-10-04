package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingSchoolNotificationComment;

public interface DrivingSchoolNotificationCommentRepository extends JpaRepository<DrivingSchoolNotificationComment, Long> {

	@Query("SELECT c FROM DrivingSchoolNotificationComment c WHERE c.notification.id = :notificationId ORDER BY c.date DESC")
	public List<DrivingSchoolNotificationComment> findAll(@Param("notificationId") Long notificationId);

}
