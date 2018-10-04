package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolMark;
import com.evola.edt.model.User;

/**
 * @author Nikola 14.05.2013.
 * 
 */
public interface DrivingSchoolMarkRepository extends
		JpaRepository<DrivingSchoolMark, Long> {

	@Query("SELECT c FROM DrivingSchoolMark c WHERE c.school.id = :schoolId ORDER BY c.date DESC")
	public List<DrivingSchoolMark> findAll(@Param("schoolId") Long schoolId);

	@Query("SELECT AVG(c.mark) FROM DrivingSchoolMark c WHERE c.school.id = :schoolId")
	public Double findAverageMark(@Param("schoolId") Long schoolId);
	
	public DrivingSchoolMark findByAuthor(User author);
	
	public DrivingSchoolMark findByAuthorAndSchool(User author, DrivingSchool school);

}
