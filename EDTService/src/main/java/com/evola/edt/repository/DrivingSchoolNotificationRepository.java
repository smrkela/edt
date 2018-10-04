package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingSchoolNotification;

public interface DrivingSchoolNotificationRepository extends
		JpaRepository<DrivingSchoolNotification, Long> {

	@Query("SELECT e FROM DrivingSchoolNotification e WHERE e.school.isHidden = FALSE")
	public Page<DrivingSchoolNotification> findAllForVisibleSchools(Pageable pageable);

	@Query("select count(c) from DrivingSchoolNotificationComment c where c.notification.id = :notificationId")
	public Long findNumOfComments(@Param("notificationId") Long id);
	
	@Query("SELECT c FROM DrivingSchoolNotification c WHERE c.school.id = :schoolId ORDER BY c.creationDate DESC")
	public List<DrivingSchoolNotification> findAll(@Param("schoolId") Long schoolId);
	
	@Query("SELECT c FROM DrivingSchoolNotification c WHERE c.school.id = :schoolId AND c.id = :notificationId")
	public DrivingSchoolNotification findOne(@Param("schoolId") Long schoolId, @Param("notificationId") Long notificationId);

	@Modifying
	@Query("DELETE FROM DrivingSchoolNotification WHERE school.id = :schoolId AND id = :id")
	public void delete(@Param("schoolId") Long schoolId, @Param("id") Long id);
	
	@Query(value="SELECT c FROM DrivingSchoolNotification c WHERE school.id = :schoolId")
	public Page<DrivingSchoolNotification> findAll(@Param("schoolId") Long schoolId, Pageable pageable);
	
	@Query(value="SELECT e FROM DrivingSchoolNotification e WHERE e.school.isHidden = FALSE")
	public Page<DrivingSchoolNotification> findRecent(Pageable pageable);

}
