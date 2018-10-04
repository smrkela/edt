package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingSchoolMember;

public interface DrivingSchoolMemberRepository extends JpaRepository<DrivingSchoolMember, Long> {

	@Query("SELECT c FROM DrivingSchoolMember c WHERE c.drivingSchool.id = :schoolId")
	public List<DrivingSchoolMember> findAll(@Param("schoolId") Long schoolId);

	@Query("SELECT c FROM DrivingSchoolMember c WHERE c.drivingSchool.id = :schoolId AND c.id = :memberId")
	public DrivingSchoolMember findOne(@Param("schoolId") Long schoolId, @Param("memberId") Long memberId);

	@Query("DELETE FROM DrivingSchoolMember WHERE drivingSchool.id = :schoolId AND id = :id")
	public void delete(@Param("schoolId") Long schoolId, @Param("id") Long id);

	@Query("SELECT count(c) FROM DrivingSchoolMember c WHERE c.drivingSchool.id = :schoolId")
	public Long countForSchool(@Param("schoolId") Long schoolId);

	@Query("SELECT count(c) FROM DrivingSchoolMember c WHERE c.drivingSchool.id = :schoolId AND c.user.email = :email")
	public Long countByUserEmail(@Param("schoolId") Long schoolId, @Param("email") String email);

	@Query("SELECT count(c) FROM DrivingSchoolMember c WHERE c.drivingSchool.id = :schoolId AND c.user.email = :email AND c.id != :id")
	public Long countByUserEmailExcluding(@Param("schoolId") Long schoolId, @Param("email") String email, @Param("id") Long id);

	@Query("SELECT count(c) FROM DrivingSchoolMember c WHERE c.drivingSchool.id = :schoolId AND c.user.id = :userId AND c.role = :role")
	public Long countMembers(@Param("schoolId") Long schoolId, @Param("userId") Long userId, @Param("role") String role);

}
