package com.evola.edt.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.derby.iapi.util.StringUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.managers.EmailManager;
import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.MembershipRequestStatus;
import com.evola.edt.model.User;
import com.evola.edt.model.UserDrivingSchoolMembershipRequest;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.UserDrivingSchoolMembershipRequestDTO;
import com.evola.edt.repository.UserDrivingSchoolMembershipRequestRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.transformer.DrivingSchoolDTOTransformer;
import com.evola.edt.service.dto.transformer.UserDrivingSchoolMembershipRequestDTOTransformer;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.TokenUtil;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * @author Daci, Jan 15, 2015
 *
 */

@Named
public class UserDrivingSchoolMembershipRequestServiceImpl extends AbstractDrivingSchoolServiceImpl implements UserDrivingSchoolMembershipRequestService {
	
	@Inject
	private UserDrivingSchoolMembershipRequestRepository userDrivingSchoolMembershipRequestRepository;

	@Inject
	private UserDrivingSchoolMembershipRequestDTOTransformer userDrivingSchoolMembershipRequestDTOTransformer;
	
	@Inject
	private EmailManager emailManager;
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDTOTransformer;
	
	@Inject
	UserCredentialsManager credentialsManager;
	
	
	
	@Override
	@Transactional
	public void submitRequestForMembership(UserDrivingSchoolMembershipRequestDTO dto) {
		
		User user = userRepository.findById(SecurityUtils.getUserId());
		
		Assert.notNull(user);
		Assert.notNull(dto.getDrivingSchool());
		
		DrivingSchool drivingSchool = drivingSchoolRepository.findById(dto.getDrivingSchool().getId());
		
		/**
		 * ako skola nema e-mail i ako e-mail nije uneo ucenik prijaviti gresku i traziti od ucenika da unese skolu
		 * ako je ucenik uneo e-mail auto skole, onda treba poslati mail na tu adresu ali se NIKAKO NE SME raditi update auto skole i setovanje tog mail-a jer 
		 * korisnik moze da lupi bilo sta za mail (ab@cd.com) te ce se onda pokusati slanje na tu adresu; pride adresa se vidi i u profilu 
		 */
		if (drivingSchool.getEmail() == null) {
			if (dto.geteMail() == null || StringUtils.isEmpty(dto.geteMail().trim())){
				throw new RuntimeException("E-mail auto škole koju ste izabrali nam je nepoznat. Molimo Vas, unesite e-mail auto škole " +
						"u polje ispod kako bismo joj prosledili Vaš zahtev.");
			}
		}
		
		String confirmationToken = TokenUtil.generateToken();
		
		UserDrivingSchoolMembershipRequest membershipRequest = new UserDrivingSchoolMembershipRequest();
		
		membershipRequest.setUser(user);
		membershipRequest.setDrivingSchool(drivingSchool);
		membershipRequest.setComment(dto.getComment());
		membershipRequest.setReceiveNotifications(dto.getReceiveNotifications());
		membershipRequest.setCreationDate(new Date());
		membershipRequest.setStatus(MembershipRequestStatus.PENDING);
		membershipRequest.seteMail(dto.geteMail());
		membershipRequest.setConfirmationToken(confirmationToken);
		
		userDrivingSchoolMembershipRequestRepository.save(membershipRequest);
		
		String drivingSchoolEmail = drivingSchool.getEmail() != null ?  drivingSchool.getEmail() : dto.geteMail();  
		
		emailManager.sendMembershipRequest(user.getFirstName() + " " + user.getLastName(), user.getEmail(), dto.getComment(), drivingSchoolEmail, confirmationToken);
		
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<UserDrivingSchoolMembershipRequestDTO> findAllRequestsForUser(Long userId) {
		
		List<UserDrivingSchoolMembershipRequestDTO> requestDTOs = new LinkedList<UserDrivingSchoolMembershipRequestDTO>();
		List<UserDrivingSchoolMembershipRequest> findAll = userDrivingSchoolMembershipRequestRepository.findAllRequestsForUser(userId); 
		
		for (UserDrivingSchoolMembershipRequest request : findAll) {
			requestDTOs.add(userDrivingSchoolMembershipRequestDTOTransformer.transformToDTO(request, "drivingSchool"));
		}
		
		return requestDTOs;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<UserDrivingSchoolMembershipRequestDTO> findAllRequestsForSchool(Long schoolId) {
				
		List<UserDrivingSchoolMembershipRequestDTO> requestDTOs = new LinkedList<UserDrivingSchoolMembershipRequestDTO>();
		List<UserDrivingSchoolMembershipRequest> findAll = userDrivingSchoolMembershipRequestRepository.findAllRequestsForSchool(schoolId); 
		
		for (UserDrivingSchoolMembershipRequest request : findAll) {
			requestDTOs.add(userDrivingSchoolMembershipRequestDTOTransformer.transformToDTO(request, "user", "drivingSchoolStudent"));
		}
		
		return requestDTOs;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public UserDrivingSchoolMembershipRequestDTO findRequestForToken(String token) {
		
		UserDrivingSchoolMembershipRequest requestEntity = userDrivingSchoolMembershipRequestRepository.findRequestByToken(token);
		return userDrivingSchoolMembershipRequestDTOTransformer.transformToDTO(requestEntity, "user", "drivingSchool");

	}

	@Override
	@Transactional
	public void membershipRequestDecision(String token, String decisionComment, String decision) {
		
		User user = userRepository.findById(SecurityUtils.getUserId());
		
		Assert.notNull(token);
		Assert.notNull(decision);
		
		UserDrivingSchoolMembershipRequest requestEntity = userDrivingSchoolMembershipRequestRepository.findRequestByToken(token);
		UserDrivingSchoolMembershipRequestDTO requestDto = userDrivingSchoolMembershipRequestDTOTransformer.transformToDTO(requestEntity, "user", "drivingSchool");
		
		Assert.notNull(requestEntity.getDrivingSchool());		
		
		requestEntity.setStatus(MembershipRequestStatus.valueOf(decision));
		requestEntity.setDecisionDate(new Date());
		requestEntity.setDecisionComment(decisionComment);
		
		requestEntity = userDrivingSchoolMembershipRequestRepository.save(requestEntity);
		
		MembershipRequestStatus status = MembershipRequestStatus.valueOf(decision);
		
		if (status.equals(MembershipRequestStatus.APPROVED)) {
			emailManager.sendMembershipRequestApproved(requestDto.getUser().getFirstName(), requestDto.getUser().getEmail(), requestDto.getDrivingSchool().getName());
		} else if (status.equals(MembershipRequestStatus.REJECTED)) {
			emailManager.sendMembershipRequestRejected(requestDto.getUser().getFirstName(), requestDto.getUser().getEmail(), requestDto.getDrivingSchool().getName());
		}
		
	}


	@Override
	@Transactional
	public void removeStudentFromMembership(String membershipRequestToken) {
		
		UserDrivingSchoolMembershipRequest requestEntity = userDrivingSchoolMembershipRequestRepository.findRequestByToken(membershipRequestToken);
		
		requestEntity.setDrivingSchoolStudent(null);
		
		requestEntity = userDrivingSchoolMembershipRequestRepository.save(requestEntity);
	}


	@Override
	@Transactional
	public void deleteMembershipRequest(Long schoolId, String token) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			try {
				throw new Exception("Nemate prava da izvršite ovu akciju!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		userDrivingSchoolMembershipRequestRepository.deleteMembershipRequest(token);
	}


	@Override
	public void updateReceiveNotifications(Long decision, String token) {
		
		UserDrivingSchoolMembershipRequest request = userDrivingSchoolMembershipRequestRepository.findRequestByToken(token);
		
		if(decision == 1) {
			request.setReceiveNotifications(true);
		} else if (decision == 2) {
			request.setReceiveNotifications(false);
		} else {
			try {
				throw new Exception("Neispravan zahtev za setovanje prijema obavestenja.");
			} catch (Exception e) {
			}
		}
		
		request = userDrivingSchoolMembershipRequestRepository.save(request);
		
	}	
}
