package com.evola.edt.service;

import java.util.List;

import com.evola.edt.model.dto.UserDrivingSchoolMembershipRequestDTO;

/**
 * @author Daci, Jan 15, 2015
 *
 */
public interface UserDrivingSchoolMembershipRequestService {

	public abstract void submitRequestForMembership(UserDrivingSchoolMembershipRequestDTO dto);
	
	public abstract List<UserDrivingSchoolMembershipRequestDTO> findAllRequestsForUser(Long userId);
	
	public abstract List<UserDrivingSchoolMembershipRequestDTO> findAllRequestsForSchool(Long schoolId);

	public abstract UserDrivingSchoolMembershipRequestDTO findRequestForToken(String token);
	
	public abstract void membershipRequestDecision(String token, String decisionComment, String decision);
	
	public abstract void removeStudentFromMembership(String membershipRequestToken);
	
	public abstract void deleteMembershipRequest(Long schoolId, String token);

	public abstract void updateReceiveNotifications(Long decision, String token);
}