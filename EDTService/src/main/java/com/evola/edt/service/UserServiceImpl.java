package com.evola.edt.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.WebRequest;

import com.evola.edt.component.RecaptchaComponent;
import com.evola.edt.forum.repository.ForumRepository;
import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.managers.ActivityManager;
import com.evola.edt.managers.EmailManager;
import com.evola.edt.model.AbstractTokenAction;
import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolStudent;
import com.evola.edt.model.PendingRegistration;
import com.evola.edt.model.ResetPassword;
import com.evola.edt.model.User;
import com.evola.edt.model.UserQuestionStatLearn;
import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.PageResultDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.repository.DailyTestUserResultRepository;
import com.evola.edt.repository.DrivingCategoryRepository;
import com.evola.edt.repository.DrivingSchoolRepository;
import com.evola.edt.repository.DrivingSchoolStudentRepository;
import com.evola.edt.repository.PendingRegistrationRepository;
import com.evola.edt.repository.ResetPasswordRepository;
import com.evola.edt.repository.UserQuestionStatLearnRepository;
import com.evola.edt.repository.UserQuestionStatTestRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.UserPreviewDTO;
import com.evola.edt.service.dto.UserProfileDTO;
import com.evola.edt.service.dto.transformer.DrivingCategoryDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolDTOTransformer;
import com.evola.edt.service.dto.transformer.UserDTOTransformer;
import com.evola.edt.service.rest.ExecutionStatus;
import com.evola.edt.service.util.PageResultUtil;
import com.evola.edt.utils.social.SocialMediaService;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.TokenUtil;

@Named
public class UserServiceImpl implements UserService {

//	@Inject
//	private ForumRepository forumRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserDTOTransformer userTransformer;

	@Inject
	private DrivingCategoryRepository drivingCategoryRepository;

	@Inject
	private DrivingCategoryDTOTransformer drivingCategoryTransformer;

	@Inject
	private PendingRegistrationRepository pendingRegistrationRepository;

	@Inject
	private UserQuestionStatLearnRepository userQuestionStatLearnRepository;

	@Inject
	private UserQuestionStatTestRepository userQuestionStatTestRepository;

	@Inject
	private ResetPasswordRepository resetPasswordRepository;

	@Inject
	private GalleryManager galleryManager;

	@Inject
	private DailyTestUserResultRepository dailyTestUserRepository;

	@Inject
	private DrivingSchoolStudentRepository drivingSchoolStudentRepository;

	@Inject
	private RecaptchaComponent recaptcha;

	@Value("${registrationTokenMaxAgeInHours}")
	private String registrationTokenMaxAgeInHours;

	@Inject
	private HttpServletRequest request;

	@Inject
	private WebRequest webRequest;

	@Inject
	private EmailManager emailManager;
	
	@Inject
	private ActivityManager mActivity; 
	
	@Inject
	private UserDTOTransformer userDTOTransformer;
	
	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDTOTransformer;

	@Inject
	private DrivingSchoolRepository drivingSchoolRepository;

	
	@Transactional(readOnly = true)
	public UserProfileDTO loadUser(Long userId) {

		UserProfileDTO dto = new UserProfileDTO();

		User user = userRepository.findOne(userId);
		UserDTO userDTO = userTransformer.transformToDTO(user, "drivingCategory");

		Iterable<DrivingCategory> categories = drivingCategoryRepository.findAll();

		List<DrivingCategoryDTO> dtos = new LinkedList<DrivingCategoryDTO>();

		for (DrivingCategory dc : categories) {

			dtos.add(drivingCategoryTransformer.transformToDTO(dc));
		}

		dto.setUser(userDTO);
		dto.setDrivingCategories(dtos);

		return dto;
	}

	/**
	 * @author Nikola 22.04.2013.
	 * @param userId
	 * @return
	 */
	public UserDTO getUserDTO(Long userId) {
		User user = userRepository.findOne(userId);
		UserDTO userDTO = userTransformer.transformToDTO(user, "drivingCategory");
		return userDTO;
	}

	@Transactional
	public void registerUserDTO(UserDTO userDto) {

		// ne koristi se vise

		// User user = userTransformer.transformToEntity(userDto);
		//
		// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// user.setPassword(encoder.encode(user.getPassword()));
		//
		// SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		// simpleMailMessage.setFrom(emailFrom);
		// simpleMailMessage.setTo(userDto.getEmail());
		// simpleMailMessage.setSubject("Registracija uspešna");
		// Map<String, Object> variables = new HashMap<String, Object>();
		// variables.put("name", userDto.getFirstName());
		// variables.put("companyLogo", applicationUrl + logoImg);
		//
		// mailSender.send(simpleMailMessage, "registration.vm", variables);
		//
		// user.setLearnedQuestions(0);
		// user.setTestedQuestions(0);
		// user.setPoints(0);
		//
		// userRepository.save(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.evola.edt.service.UserService#registerUser(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Boolean)
	 */
	@Transactional
	public void registerUser(String firstName, String lastName, String username, String password, String email, Long drivingCategory, Boolean isMale,
			String recaptcha_challenge_field, String recaptcha_response_field, String signInProvider) {

		// ako imamo validan sign in provider onda nam neki podaci ne trebaju
		boolean isValidSignInProvider = SocialMediaService.FACEBOOK.name().equals(signInProvider)
				|| SocialMediaService.TWITTER.name().equals(signInProvider);
		
		boolean isMobile = "MOBILE".equals(signInProvider);

		// proveravamo validnost unetih vrednosti
		validateUserData(firstName, lastName, username, password, email);

		boolean existsByUsername = userRepository.countByUsername(username) > 0;

		if (existsByUsername) {
			throw new IllegalStateException("Postoji korisnik sa datim korisničkim imenom.");
		}

		boolean existsByEmail = userRepository.countByEmail(email) > 0;

		if (existsByEmail) {
			throw new IllegalStateException("Postoji korisnik sa datom email adresom.");
		}

		if (!isValidSignInProvider && !isMobile)
			recaptcha.check(recaptcha_challenge_field, recaptcha_response_field);

		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(username);
		user.setEmail(email);
		user.setIsMale(isMale);
		user.setPassword(password);
		user.setRegistrationDate(new Date());
		user.setLearnedQuestions(0);
		user.setTestedQuestions(0);
		user.setPoints(0);
		user.setSignInProvider(null);
		
		if(drivingCategory != null) {
			user.setDrivingCategory(drivingCategoryRepository.findOne(drivingCategory));
		}
		
		
		if (isValidSignInProvider)
			user.setSignInProvider(SocialMediaService.valueOf(signInProvider));

		// Kreira se korisnik na Forum
		// ovo znaci da ce korisnik biti banovan pri registraciji na forumu
//		ExecutionStatus status = forumRepository.createUser(user, true, null);
		
		ExecutionStatus status = new ExecutionStatus();
		status.setStatus(ExecutionStatus.Status.OK);
		

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(password));

		String generateToken = TokenUtil.generateToken();

		emailManager.sendRegistrationEmail(user, generateToken, password, status);

		user = userRepository.save(user);
		
		mActivity.registered(user);

		// ako se korisnik ulogovao preko signInProvider-a onda ga automatski
		// ulogujemo
		// u suprotnom saljemo validacioni email
		if (isValidSignInProvider) {

			// user preko providera nam je odmah enabled
			user.setEnabled(true);

			updateLoginTime(user.getId());

			// build an Authentication object with the user's info
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
					user.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			ProviderSignInUtils.handlePostSignUp(user.getUsername(), webRequest);

		} else {

			PendingRegistration pendingRegistration = new PendingRegistration(generateToken, user, new Date());
			pendingRegistrationRepository.save(pendingRegistration);
		}
	}

	@Override
	@Transactional
	public void askForResetPassword(String email, String recaptcha_challenge_field, String recaptcha_response_field) {

		recaptcha.check(recaptcha_challenge_field, recaptcha_response_field);

		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new IllegalStateException("Ne postoji korisnik sa datom email adresom.");
		}

		String generateToken = emailManager.sendResetPasswordEmail(user);

		ResetPassword resetPassword = new ResetPassword(generateToken, user, new Date());

		resetPasswordRepository.save(resetPassword);
	}

	@Override
	@Transactional
	public void sendForgottenUsername(String email, String recaptchaChallange, String recaptchaResponse) {

		recaptcha.check(recaptchaChallange, recaptchaResponse);

		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new IllegalStateException("Ne postoji korisnik sa datom email adresom.");
		}

		emailManager.sendForgottenUsername(user);
	}

	@Override
	public Boolean isResetPasswordTokenValid(String token) {
		Assert.notNull(token, "Token must not be null");
		ResetPassword findByToken = resetPasswordRepository.findByToken(token);
		try {
			validateToken(findByToken);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private void validateToken(AbstractTokenAction findByToken) {

		if (findByToken == null) {
			throw new IllegalStateException("There is no token for given token string");
		}

		DateTime creationDate = new DateTime(findByToken.getCreationDate());

		if (creationDate.plusHours(Integer.valueOf(registrationTokenMaxAgeInHours)).isBeforeNow()) {
			throw new IllegalStateException("Token expired");
		}

		if (findByToken.getConfirmed()) {
			throw new IllegalStateException("Token already confirmed");
		}
	}

	@Override
	@Transactional
	public void resetPassword(String password, String token) {

		validateUserPassword(password);

		ResetPassword findByToken = resetPasswordRepository.findByToken(token);

		if (findByToken == null) {
			throw new IllegalStateException("There is no token for given token string");
		}

		findByToken.setConfirmed(true);

		resetPasswordRepository.save(findByToken);

		User user = findByToken.getUser();

		user.setPassword(password);

		// resetuje se sifra korisniku na forum-u; mora da se pozove prvo
		// promena na forumu jer se posle sifra kriptuje i nije citljiva
//		ExecutionStatus status = forumRepository.resetPassword(user);
		
		ExecutionStatus status = new ExecutionStatus();
		status.setStatus(ExecutionStatus.Status.OK);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(password));
		userRepository.save(user);

		// slanje maila ako nije prosla promena sifre na Forumu
		if (status.getStatus() == ExecutionStatus.Status.NOTOK)
			emailManager.sendForumResetPasswordFail(password, user, status);
	}

	@Override
	@Transactional
	public void activateUser(String token) {

		Assert.notNull(token, "Token must not be null");

		PendingRegistration findByToken = pendingRegistrationRepository.findByToken(token);

		validateToken(findByToken);

		findByToken.setConfirmed(true);

		pendingRegistrationRepository.save(findByToken);

		User user = findByToken.getUser();
		user.setEnabled(true);

		// user.setRegistrationDate(new Date());

		user.setActivationDate(new Date());

		userRepository.save(user);

		// Skidanje zabrane korisniku za pristup sajtu
//		ExecutionStatus status = forumRepository.unbanUser(user);
		
		ExecutionStatus status = new ExecutionStatus();
		status.setStatus(ExecutionStatus.Status.OK);

		// slanje maila administratorima za rucnu akciju na Forumu
		if (status.getStatus() == ExecutionStatus.Status.NOTOK)
			emailManager.sendForumActivationFail(user, status);

	}

	@Transactional()
	public UserProfileDTO updateUser(Long userId, String firstName, String lastName, String email, Boolean isMale,
			Integer questionsPerPage, Long drivingCategoryId) {

		User user = userRepository.findOne(userId);

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setIsMale(isMale);
		user.setQuestionsPerPage(questionsPerPage);

		if (drivingCategoryId != null)
			user.setDrivingCategory(drivingCategoryRepository.findOne(drivingCategoryId));
		else
			user.setDrivingCategory(null);

		UserDTO userDTO = userTransformer.transformToDTO(user, "drivingCategory");

		UserProfileDTO dto = new UserProfileDTO();
		dto.setUser(userDTO);

		return dto;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameAndEnabled(username, true);
		return user;
	}

	@Override
	public UserDetails findById(Long userId) {
		return userRepository.findById(userId);
	}

	@Transactional
	@Override
	public void updateLoginTime(Long id) {

		User findById = userRepository.findById(id);

		findById.setLastLogin(new Date());
	}

	@Override
	@Transactional(readOnly = true)
	public PageResultDTO<UserDTO> findUsersPaged(Integer startingPage, int count) {

		Assert.notNull(startingPage);
		Assert.notNull(count);

		Page<User> all = userRepository.findAll(new PageRequest(startingPage, count, new Sort("lastName")));

		List<User> content = all.getContent();
		List<UserDTO> dtos = transformUserList(content);

		PageResultDTO<UserDTO> dto = new PageResultDTO<UserDTO>();
		PageResultUtil.populateCommon(all, dto);
		dto.setDtos(dtos);

		return dto;
	}

	@Override
	@Transactional
	public void updateUserProfile(UserDTO dto) {

		String firstName = dto.getFirstName();
		String lastName = dto.getLastName();

		firstName = StringUtils.trim(firstName);
		lastName = StringUtils.trim(lastName);

		boolean isValid = StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName);

		Assert.isTrue(isValid);

		User user = userRepository.findOne(SecurityUtils.getUserId());

		user.setFirstName(firstName);
		user.setLastName(lastName);
		
		if (dto.getDrivingCategory() != null && dto.getDrivingCategory().getId() != null) {
			user.setDrivingCategory(drivingCategoryRepository.findOne(dto.getDrivingCategory().getId()));
		} else {
			user.setDrivingCategory(null);
		}

		userRepository.save(user);

		galleryManager.saveUserProfileImage(SecurityUtils.getUserId(), dto.getProfileImage());
	}
	

	/*
	 * broji sve korisnike bez obzira na status (enabled/disabled, tj.
	 * active/inactive)
	 */
	@Override
	@Transactional
	public Long countNumberOfUsers() {
		return userRepository.countNumberOfUsers();
	}

	/**
	 * Metoda proverava validnost podataka korisnika pri registraciji.
	 * 
	 * @param firstName
	 * @param lastName
	 * @param username
	 * @param password
	 * @param email
	 */
	private void validateUserData(String firstName, String lastName, String username, String password, String email) {

		// ime i prezime ne smeju biti prazna polja
		if (StringUtils.isBlank(firstName))
			throw new IllegalStateException("Ime ne sme biti prazno.");

		if (StringUtils.isBlank(lastName))
			throw new IllegalStateException("Prezime ne sme biti prazno.");

		if (StringUtils.isBlank(email))
			throw new IllegalStateException("Email adresa ne sme biti prazna.");

		validateUserPassword(password);

		validateUsername(username);
	}

	private void validateUsername(String username) {

		if (StringUtils.isBlank(username))
			throw new IllegalStateException("Korisničko ime ne sme biti prazno.");

		if (StringUtils.contains(username, " "))
			throw new IllegalStateException("Korisničko ime ne sme sadržati prazne karaktere.");

		int usernameMinLength = 4;
		int usernameMaxLength = 16;

		if (username.length() < usernameMinLength || username.length() > usernameMaxLength)
			throw new IllegalStateException("Korisničko ime mora sadržati između " + usernameMinLength + " i " + usernameMaxLength
					+ " karaktera.");
	}

	private void validateUserPassword(String password) {

		if (StringUtils.isBlank(password))
			throw new IllegalStateException("Korisnička šifra ne sme biti prazna.");

		if (StringUtils.contains(password, " "))
			throw new IllegalStateException("Korisnička šifra ne sme sadržati prazne karaktere.");

		int passwordMinLength = 6;
		int passwordMaxLength = 16;

		if (password.length() < passwordMinLength || password.length() > passwordMaxLength)
			throw new IllegalStateException("Šifra mora sadržati između " + passwordMinLength + " i " + passwordMaxLength + " karaktera.");
	}

	@Override
	@Transactional(readOnly = true)
	public int getNumOfLoggedInUsers(Date startDate, Date finishDate) {

		return userRepository.countNumberOfLoggedInUsers(startDate, finishDate).intValue();
	}

	@Override
	@Transactional(readOnly = true)
	public int getNumOfRegisteredUsers(Date startDate, Date finishDate) {

		return userRepository.countNumberOfRegisteredUsers(startDate, finishDate).intValue();
	}

	@Override
	@Transactional(readOnly = true)
	public int getNumOfAppUsers(Date startDate, Date finishDate) {

		return userRepository.contNumberOfApplicationUsers(startDate, finishDate).intValue();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> getLoggedInUsers(Date startDate, Date finishDate) {

		List<User> users = userRepository.getLoggedInUsers(startDate, finishDate);

		List<UserDTO> dtos = transformUserList(users);

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> getRegisteredUsers(Date startDate, Date finishDate) {

		List<User> users = userRepository.getRegisteredUsers(startDate, finishDate);

		List<UserDTO> dtos = transformUserList(users);

		return dtos;
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserDTO> getApplicationUsers(Date startDate, Date finishDate) {

		List<User> users = userRepository.getApplicationUsers(startDate, finishDate);

		List<UserDTO> dtos = transformUserList(users);

		return dtos;
	}

	private List<UserDTO> transformUserList(List<User> users) {
		List<UserDTO> dtos = new LinkedList<UserDTO>();

		for (User dc : users) {
			dtos.add(userTransformer.transformToDTO(dc));
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public UserPreviewDTO getUserPreview(Long userId) {

		User user = userRepository.findOne(userId);

		DrivingCategory drivingCategory = user.getDrivingCategory();

		UserPreviewDTO dto = new UserPreviewDTO();
		dto.setUserId(userId);
		dto.setUsername(user.getUsername());
		dto.setRegisterDate(user.getRegistrationDate());

		if (drivingCategory != null)
			dto.setDrivingCategoryName(drivingCategory.getName());

		Date learnDate = userQuestionStatLearnRepository.getFirstLearnDate(userId);
		Date testDate = userQuestionStatTestRepository.getFirstTestDate(userId);

		Date minDate = learnDate;

		if (learnDate == null && testDate != null && learnDate.after(testDate))
			minDate = testDate;

		dto.setLearningStartDate(minDate);

		Long numOfTests = dailyTestUserRepository.countByUser(user);

		dto.setNumOfDailyTestsDone(numOfTests != null ? numOfTests.intValue() : 0);

		List<DrivingSchoolStudent> students = drivingSchoolStudentRepository.findByUser(user);

		List<DrivingSchoolDTO> schools = new LinkedList<>();

		if (students != null) {

			for (DrivingSchoolStudent student : students) {

				DrivingSchool drivingSchool = student.getDrivingSchool();

				if (drivingSchool != null) {

					// prepisujemo samo neke parametre
					DrivingSchoolDTO schoolDto = new DrivingSchoolDTO();
					schoolDto.setId(drivingSchool.getId());
					schoolDto.setName(drivingSchool.getName());
					schoolDto.setCity(drivingSchool.getCity());
					schoolDto.setUniqueName(drivingSchool.getUniqueName());
					
					schools.add(schoolDto);
				}
			}
		}
		
		dto.setDrivingSchools(schools);

		return dto;
	}

}
