package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import com.evola.edt.model.UserDrivingSchoolMembershipRequest;

/**
 * @author Daci, Jan 11, 2015
 *
 */
public interface UserDrivingSchoolMembershipRequestRepository extends JpaRepository<UserDrivingSchoolMembershipRequest, Long> {
	
	@Query("SELECT u FROM UserDrivingSchoolMembershipRequest u WHERE u.user.id=:userId")
	public List<UserDrivingSchoolMembershipRequest> findAllRequestsForUser(@Param("userId") Long userId);
	
	@Query("SELECT u FROM UserDrivingSchoolMembershipRequest u WHERE u.drivingSchool.id=:schoolId")
	public List<UserDrivingSchoolMembershipRequest> findAllRequestsForSchool(@Param("schoolId") Long schoolId);
		
	@Query("SELECT u FROM UserDrivingSchoolMembershipRequest u WHERE u.confirmationToken = :confirmationToken")
	public UserDrivingSchoolMembershipRequest findRequestByToken(@Param("confirmationToken") String confirmationToken);

	@Modifying
	@Query("DELETE FROM UserDrivingSchoolMembershipRequest d WHERE d.confirmationToken = :token")
	public void deleteMembershipRequest(@Param("token") String token);
	
	@Query("SELECT u FROM UserDrivingSchoolMembershipRequest u WHERE u.drivingSchool.id = :schoolId AND u.drivingSchoolStudent.id = :studentId")
	public UserDrivingSchoolMembershipRequest findRequestBySchoolAndStudent(@Param("schoolId") Long schoolId, @Param("studentId") Long studentId);
	
}
