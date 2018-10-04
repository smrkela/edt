package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingSchoolEmployee;

/**
 * @author Nikola 14.05.2013.
 * 
 */
public interface DrivingSchoolEmployeeRepository extends JpaRepository<DrivingSchoolEmployee, Long> {

	@Query("SELECT c FROM DrivingSchoolEmployee c WHERE c.school.id = :schoolId ORDER BY c.orderIndex DESC")
	public List<DrivingSchoolEmployee> findAll(@Param("schoolId") Long schoolId);
	
	@Query("SELECT c FROM DrivingSchoolEmployee c WHERE c.school.id = :schoolId AND c.id = :employeeId")
	public DrivingSchoolEmployee findOne(@Param("schoolId") Long schoolId, @Param("employeeId") Long employeeId);
	
	@Modifying
	@Query("DELETE FROM DrivingSchoolEmployee WHERE school.id = :schoolId AND id = :employeeId")
	public void delete(@Param("schoolId") Long schoolId, @Param("employeeId") Long employeeId);

	@Query("SELECT count(c) FROM DrivingSchoolEmployee c WHERE c.school.id = :schoolId")
	public Long countForSchool(@Param("schoolId") Long schoolId);

}
