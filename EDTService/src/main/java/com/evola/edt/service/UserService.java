package com.evola.edt.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.evola.edt.model.dto.PageResultDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.model.dto.UserDrivingSchoolMembershipRequestDTO;
import com.evola.edt.service.dto.UserPreviewDTO;
import com.evola.edt.service.dto.UserProfileDTO;

public interface UserService extends UserDetailsService {

	public abstract UserDetails findById(Long userId);

	public UserDTO getUserDTO(Long userId);

	public abstract UserProfileDTO loadUser(Long userId);

	public abstract UserProfileDTO updateUser(Long userId, String firstName, String lastName, String email, Boolean isMale, 
												Integer questionsPerPage, Long drivingCategoryId);

	public abstract void registerUserDTO(UserDTO userDto);

	public abstract void askForResetPassword(String email, String recaptcha_challenge_field, String recaptcha_response_field);

	public abstract void registerUser(String firstName, String lastName, String username, String password, String email, Long drivingCategory, Boolean isMale, 
										String recaptcha_challenge_field, String recaptcha_response_field, String signInProvider);

	public abstract void activateUser(String token);

	public abstract void resetPassword(String password, String token);

	public abstract Boolean isResetPasswordTokenValid(String token);

	public abstract void updateLoginTime(Long id);

	public abstract PageResultDTO<UserDTO> findUsersPaged(Integer startingPage, int count);

	public abstract void updateUserProfile(UserDTO dto);

	public Long countNumberOfUsers();

	public abstract void sendForgottenUsername(String email, String recaptchaChallange, String recaptchaResponse);

	public abstract int getNumOfLoggedInUsers(Date startDate, Date finishDate);

	public abstract int getNumOfRegisteredUsers(Date startDate, Date finishDate);

	public abstract int getNumOfAppUsers(Date startDate, Date finishDate);

	public abstract List<UserDTO> getLoggedInUsers(Date startDate, Date finishDate);
	
	public abstract List<UserDTO> getRegisteredUsers(Date startDate, Date finishDate);

	public abstract List<UserDTO> getApplicationUsers(Date startDate, Date finishDate);

	public abstract UserPreviewDTO getUserPreview(Long userId);
}