package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingSchoolCar;

/**
 * @author Nikola 14.05.2013.
 * 
 */
public interface DrivingSchoolCarRepository extends JpaRepository<DrivingSchoolCar, Long> {

	@Query("SELECT c FROM DrivingSchoolCar c WHERE c.school.id = :schoolId ORDER BY c.orderIndex DESC")
	public List<DrivingSchoolCar> findAll(@Param("schoolId") Long schoolId);

	@Query("SELECT c FROM DrivingSchoolCar c WHERE c.school.id = :schoolId AND c.id = :carId")
	public DrivingSchoolCar findOne(@Param("schoolId") Long schoolId, @Param("carId") Long carId);

	@Modifying
	@Query("DELETE FROM DrivingSchoolCar WHERE school.id = :schoolId AND id = :id")
	public void delete(@Param("schoolId") Long schoolId, @Param("id") Long id);

	@Query("SELECT count(c) FROM DrivingSchoolCar c WHERE c.school.id = :schoolId")
	public Long countForSchool(@Param("schoolId") Long schoolId);

}
