package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingSchoolStudent;
import com.evola.edt.model.User;

public interface DrivingSchoolStudentRepository extends JpaRepository<DrivingSchoolStudent, Long> {

	@Query("SELECT c FROM DrivingSchoolStudent c WHERE c.drivingSchool.id = :schoolId AND c.deleted = 0 ORDER BY c.lastName ASC")
	public List<DrivingSchoolStudent> findAll(@Param("schoolId") Long schoolId);
	
	@Query("SELECT c FROM DrivingSchoolStudent c WHERE c.drivingSchool.id = :schoolId AND c.deleted = 0 AND c.sendNotifications = 1 ORDER BY c.lastName ASC")
	public List<DrivingSchoolStudent> findAllForNotifications(@Param("schoolId") Long schoolId);
	
	@Query("SELECT c FROM DrivingSchoolStudent c WHERE c.drivingSchool.id = :schoolId AND c.id = :studentId AND c.deleted = 0")
	public DrivingSchoolStudent findOne(@Param("schoolId") Long schoolId, @Param("studentId") Long studentId);
	
	@Modifying
	@Query("UPDATE DrivingSchoolStudent SET deleted = 1 WHERE drivingSchool.id = :schoolId AND id = :studentId")
	public void delete(@Param("schoolId") Long schoolId, @Param("studentId") Long studentId);

	@Query("SELECT count(c) FROM DrivingSchoolStudent c WHERE c.drivingSchool.id = :schoolId")
	public Long countForSchool(@Param("schoolId") Long schoolId);
	
	@Query("SELECT c FROM DrivingSchoolStudent c WHERE c.drivingSchool.id = :schoolId AND c.isFirstAidPassed = 1 ORDER BY c.lastName ASC")
	public List<DrivingSchoolStudent> findAllWithFirstAid(@Param("schoolId") Long schoolId);
	
	@Query("SELECT c FROM DrivingSchoolStudent c WHERE c.drivingSchool.id = :schoolId AND c.isTheoryPassed = 1 ORDER BY c.lastName ASC")
	public List<DrivingSchoolStudent> findAllWithTheory(@Param("schoolId") Long schoolId);
	
	@Query("SELECT c FROM DrivingSchoolStudent c WHERE c.drivingSchool.id = :schoolId AND c.isPracticePassed = 1 ORDER BY c.lastName ASC")
	public List<DrivingSchoolStudent> findAllWithPractice(@Param("schoolId") Long schoolId);
	
	@Query("SELECT c FROM DrivingSchoolStudent c WHERE c.drivingSchool.id = :schoolId AND c.isAllPassed = 1 ORDER BY c.lastName ASC")
	public List<DrivingSchoolStudent> findAllWithAll(@Param("schoolId") Long schoolId);
	
	@Query("SELECT d FROM DrivingSchoolStudent d WHERE d.drivingSchool.id = :schoolId AND d.user.id = null AND d.deleted = 0 ORDER BY d.firstName ASC")
	public List<DrivingSchoolStudent> findDrivingSchoolStudentsWithoutMembershipRequest(@Param("schoolId") Long schoolId);
	
	@Query("SELECT e FROM DrivingSchoolStudent e WHERE e.user = :user")
	public List<DrivingSchoolStudent> findByUser(@Param("user") User user);

}
