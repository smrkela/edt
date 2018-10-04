package com.evola.edt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.managers.ActivityManager;
import com.evola.edt.managers.EmailManager;
import com.evola.edt.model.DrivingLicenceCategory;
import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolCar;
import com.evola.edt.model.DrivingSchoolEmployee;
import com.evola.edt.model.DrivingSchoolMark;
import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.DrivingSchoolNotificationConfirmation;
import com.evola.edt.model.DrivingSchoolStudent;
import com.evola.edt.model.PriceList;
import com.evola.edt.model.User;
import com.evola.edt.model.UserDrivingSchoolMembershipRequest;
import com.evola.edt.model.dto.DrivingSchoolCarDTO;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolEmployeeDTO;
import com.evola.edt.model.dto.DrivingSchoolMarkDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationConfirmationDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.DrivingSchoolStudentDTO;
import com.evola.edt.model.dto.PriceListCategoryDTO;
import com.evola.edt.model.dto.PriceListCategoryViewDTO;
import com.evola.edt.model.dto.PriceListDTO;
import com.evola.edt.model.dto.PriceListSubCategoryDTO;
import com.evola.edt.model.dto.PriceListSubcategoryViewDTO;
import com.evola.edt.repository.DrivingSchoolCarRepository;
import com.evola.edt.repository.DrivingSchoolEmployeeRepository;
import com.evola.edt.repository.DrivingSchoolMarkRepository;
import com.evola.edt.repository.DrivingSchoolNotificationConfirmationRepository;
import com.evola.edt.repository.DrivingSchoolNotificationRepository;
import com.evola.edt.repository.DrivingSchoolRepository;
import com.evola.edt.repository.DrivingSchoolStudentRepository;
import com.evola.edt.repository.PriceListRepository;
import com.evola.edt.repository.UserDrivingSchoolMembershipRequestRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.UploadImagesDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.service.dto.transformer.DrivingSchoolDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolStudentDTOTransformer;
import com.evola.edt.service.dto.transformer.PriceListDTOTransformer;
import com.evola.edt.service.exception.EDTServiceException;
import com.evola.edt.utils.TidyUtil;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.TokenUtil;
import com.evola.edt.web.security.UserCredentialsManager;

@Named
@Transactional
public class DrivingSchoolAdministrationServiceImpl extends AbstractDrivingSchoolServiceImpl implements
		DrivingSchoolAdministrationService {

	@Inject
	private DrivingSchoolEmployeeRepository drivingSchoolEmployeeRepository;

	@Inject
	private DrivingSchoolCarRepository drivingSchoolCarRepository;

	@Inject
	private DrivingSchoolNotificationRepository drivingSchoolNotificationRepository;

	@Inject
	private DrivingSchoolMarkRepository drivingSchoolMarkRepository;

	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDTOTransformer;

	@Inject
	private UserRepository userRepository;

	@Inject
	private PriceListRepository priceListRepository;

	@Inject
	private PriceListDTOTransformer priceListDTOTransformer;
	
	@Inject
	private DrivingSchoolRepository drivingSchoolRepository;

	@Inject
	private GalleryManager galleryManager;

	@Inject
	private UserCredentialsManager credentialsManager;

	@Inject
	private ActivityManager mActivity;
	
	@Inject
	private EmailManager emailManager;
	
	@Inject
	private DrivingSchoolStudentDTOTransformer drivingSchoolStudentDTOTransformer;
	
	@Inject
	private DrivingSchoolNotificationConfirmationRepository drivingSchoolNotificationConfirmationRepository;
	
	@Inject
	private DrivingSchoolStudentRepository drivingSchoolStudentRepository;
	
	@Inject
	private UserDrivingSchoolMembershipRequestRepository userDrivingSchoolMembershipRequestRepository;

	
	
	@Override
	@Transactional
	public void saveDrivingSchool(DrivingSchoolDTO dto) {

		Long schoolId = dto.getId();

		checkAdministrativePermissions(schoolId);

		// radimo prepisivanje
		DrivingSchool drivingSchool = drivingSchoolRepository.findOne(schoolId);

		drivingSchool.setName(dto.getName());
		drivingSchool.setLegalName(dto.getLegalName());
		drivingSchool.setCity(dto.getCity());
		drivingSchool.setAddress(dto.getAddress());
		drivingSchool.setCategories(dto.getCategories());
		drivingSchool.setCountry(dto.getCountry());
		drivingSchool.setPhone(dto.getPhone());
		drivingSchool.setPhone2(dto.getPhone2());
		drivingSchool.setFax(dto.getFax());
		drivingSchool.setWebsite(dto.getWebsite());
		drivingSchool.setEmail(dto.getEmail());
		drivingSchool.setAboutUs(dto.getAboutUs());
		drivingSchool.setWorkingHoursMonday(dto.getWorkingHoursMonday());
		drivingSchool.setWorkingHoursTuesday(dto.getWorkingHoursTuesday());
		drivingSchool.setWorkingHoursWednesday(dto.getWorkingHoursWednesday());
		drivingSchool.setWorkingHoursThursday(dto.getWorkingHoursThursday());
		drivingSchool.setWorkingHoursFriday(dto.getWorkingHoursFriday());
		drivingSchool.setWorkingHoursSaturday(dto.getWorkingHoursSaturday());
		drivingSchool.setWorkingHoursSunday(dto.getWorkingHoursSunday());
		drivingSchool.setGoogleMapsURL(dto.getGoogleMapsURL());
		drivingSchool.setFacebookURL(dto.getFacebookURL());
		drivingSchool.setTwitterURL(dto.getTwitterURL());
		drivingSchool.setPermitDate(dto.getPermitDate());
		drivingSchool.setPermitNumber(dto.getPermitNumber());
		drivingSchool.setRegistryNumber(dto.getRegistryNumber());

		galleryManager.saveDrivingSchoolLogo(schoolId, dto.getLogo());
	}

	@Override
	@Transactional
	public void addNewEmployee(DrivingSchoolEmployeeDTO dto) {

		checkAdministrativePermissions(dto.getSchoolId());

		String firstName = dto.getFirstName();
		String lastName = dto.getLastName();
		String role = dto.getRole();
		String experience = dto.getExperience();
		String comment = dto.getComment();
		String imageId = dto.getImage();
		Long schoolId = dto.getSchoolId();
		Integer orderIndex = dto.getOrderIndex();

		firstName = StringUtils.trim(firstName);
		lastName = StringUtils.trim(lastName);
		role = StringUtils.trim(role);
		experience = StringUtils.trim(experience);
		comment = StringUtils.trim(comment);

		boolean isValid = StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)
				&& StringUtils.isNotBlank(role);

		Assert.isTrue(isValid);

		DrivingSchool school = drivingSchoolRepository.findOne(schoolId);
		Long numOfEmployees = drivingSchoolEmployeeRepository.countForSchool(schoolId);

		// nije obavezno za unos ali jeste u bazi
		if (orderIndex == null) {

			// mnozimo sa brojem 100 da bi se lakse unosile medjuvrednosti
			orderIndex = numOfEmployees != null ? numOfEmployees.intValue() + 1 : 1;
			orderIndex *= 100;
		}

		DrivingSchoolEmployee employee = new DrivingSchoolEmployee();
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setRole(role);
		employee.setExperience(experience);
		employee.setComment(comment);
		employee.setOrderIndex(orderIndex);
		employee.setSchool(school);

		employee = drivingSchoolEmployeeRepository.save(employee);

		galleryManager.saveDrivingSchoolEmployeeImage(schoolId, employee.getId(), imageId);
	}

	@Override
	@Transactional
	public void updateEmployee(DrivingSchoolEmployeeDTO dto) {

		String firstName = dto.getFirstName();
		String lastName = dto.getLastName();
		String role = dto.getRole();
		String experience = dto.getExperience();
		String comment = dto.getComment();
		String imageId = dto.getImage();
		Long schoolId = dto.getSchoolId();
		Long employeeId = dto.getId();
		Integer orderIndex = dto.getOrderIndex();

		checkAdministrativePermissions(schoolId);

		boolean isValid = StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)
				&& StringUtils.isNotBlank(role);

		Assert.isTrue(isValid);
		Assert.notNull(employeeId);

		DrivingSchool school = drivingSchoolRepository.findOne(schoolId);
		DrivingSchoolEmployee employee = drivingSchoolEmployeeRepository.findOne(schoolId, employeeId);

		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setRole(role);
		employee.setExperience(experience);
		employee.setComment(comment);
		employee.setSchool(school);
		
		if (orderIndex != null)
			employee.setOrderIndex(orderIndex);

		employee = drivingSchoolEmployeeRepository.save(employee);

		galleryManager.saveDrivingSchoolEmployeeImage(schoolId, employee.getId(), imageId);
	}
	
	@Override
	@Transactional
	public void addNewStudent(DrivingSchoolStudentDTO dto) {

		checkAdministrativePermissions(dto.getSchoolId());

		String firstName = StringUtils.trim(dto.getFirstName());
		String lastName = StringUtils.trim(dto.getLastName());
		Long schoolId = dto.getSchoolId();
		String email = StringUtils.trim(dto.getEmail());
		String city = StringUtils.trim(dto.getCity());
		String address = StringUtils.trim(dto.getAddress());
		Long categoryId = dto.getCategoryId();
		String comment = StringUtils.trim(dto.getComment());

		boolean isValid = StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName);
		
		Assert.isTrue(isValid);

		DrivingSchool school = drivingSchoolRepository.findOne(schoolId);
		DrivingLicenceCategory drivingCategory = categoryId != null ? rDrivingLicenceCategory.findOne(categoryId) : null;
		User user = userRepository.findById(dto.getUserId());

		DrivingSchoolStudent student = new DrivingSchoolStudent();
		student.setUser(user);
		student.setCreationDate(new Date());
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setEmail(email);
		student.setCity(city);
		student.setAddress(address);
		student.setComment(comment);
		student.setDrivingSchool(school);
		student.setIsTheoryCompleted(dto.getIsTheoryCompleted());
		student.setIsTheoryPassed(dto.getIsTheoryPassed());
		student.setIsPracticeCompleted(dto.getIsPracticeCompleted());
		student.setSendNotifications(dto.getSendNotifications());
		student.setIsMale(dto.getIsMale());
		student.setIsAllPassed(dto.getIsAllPassed());
		student.setIsFirstAidPassed(dto.getIsFirstAidPassed());
		student.setIsPracticePassed(dto.getIsPracticePassed());
		student.setPhone(dto.getPhone());
		student.setRegisterDate(dto.getRegisterDate());
		student.setTheoryPassedDate(dto.getTheoryPassedDate()); 
		student.setPracticePassedDate(dto.getPracticePassedDate());
		student.setFirstAidPassedDate(dto.getFirstAidPassedDate());
		student.setAllPassedDate(dto.getAllPassedDate());
		student.setCategory(drivingCategory);
		student.setDeleted(false);
		student.setInviteToJoin(dto.getInviteToJoin());
		
		if(dto.getInviteToJoin() == true && dto.getMembershipRequestToken() == null){
			student.setInvitationSent(new Date());
			emailManager.sendInvitation(firstName, email, drivingSchoolRepository.findOne(dto.getSchoolId()).getName());
		}

		student = rDrivingSchoolStudent.save(student);
		
		// onda treba update-ovati membership request ID-jem studenta, ako token postoji
		if (StringUtils.isNotBlank(dto.getMembershipRequestToken())) {
			UserDrivingSchoolMembershipRequest membershipRequest = userDrivingSchoolMembershipRequestRepository.findRequestByToken(dto.getMembershipRequestToken());
			membershipRequest.setDrivingSchoolStudent(student);
			membershipRequest = userDrivingSchoolMembershipRequestRepository.save(membershipRequest);
		}
	}

	@Override
	@Transactional
	public void updateStudent(DrivingSchoolStudentDTO dto) {

		String firstName = dto.getFirstName();
		String lastName = dto.getLastName();
		String comment = dto.getComment();
		Long schoolId = dto.getSchoolId();
		String email = dto.getEmail();
		String city = dto.getCity();
		String address = dto.getAddress();
		Long categoryId = dto.getCategoryId();
		 
		firstName = StringUtils.trim(firstName);
		lastName = StringUtils.trim(lastName);
		comment = StringUtils.trim(comment);
		email = StringUtils.trim(email);
		city = StringUtils.trim(city);
		address = StringUtils.trim(address);
		Long studentId = dto.getId();

		checkAdministrativePermissions(schoolId);

		boolean isValid = StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName);

		Assert.isTrue(isValid);
		Assert.notNull(studentId);

		DrivingSchool school = drivingSchoolRepository.findOne(schoolId);
		DrivingSchoolStudent student = rDrivingSchoolStudent.findOne(schoolId, studentId);
		DrivingLicenceCategory drivingCategory = categoryId != null ? rDrivingLicenceCategory.findOne(categoryId) : null;

		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setEmail(email);
		student.setCity(city);
		student.setAddress(address);
		student.setComment(comment);
		student.setDrivingSchool(school);
		student.setIsTheoryCompleted(dto.getIsTheoryCompleted());
		student.setIsTheoryPassed(dto.getIsTheoryPassed());
		student.setIsPracticeCompleted(dto.getIsPracticeCompleted());
		student.setSendNotifications(dto.getSendNotifications());
		student.setIsMale(dto.getIsMale());
		student.setIsAllPassed(dto.getIsAllPassed());
		student.setIsFirstAidPassed(dto.getIsFirstAidPassed());
		student.setIsPracticePassed(dto.getIsPracticePassed());
		student.setPhone(dto.getPhone());
		student.setRegisterDate(dto.getRegisterDate());
		student.setTheoryPassedDate(dto.getTheoryPassedDate()); 
		student.setPracticePassedDate(dto.getPracticePassedDate());
		student.setFirstAidPassedDate(dto.getFirstAidPassedDate());
		student.setAllPassedDate(dto.getAllPassedDate());
		student.setCategory(drivingCategory);
		student.setDeleted(false);
		student.setInviteToJoin(dto.getInviteToJoin());
		
		if(student.getInvitationSent() == null && dto.getInviteToJoin() == true){
			student.setInvitationSent(new Date());
			emailManager.sendInvitation(firstName, email, drivingSchoolRepository.findOne(dto.getSchoolId()).getName());
		}
		
		student = rDrivingSchoolStudent.save(student);
	}
	
	
	
	@Override
	@Transactional
	public void connectToExistingStudent(Long membershipRequestId, Long studentId) {
		
		Assert.notNull(membershipRequestId);
		Assert.notNull(studentId);
		
		UserDrivingSchoolMembershipRequest membershipRequest = userDrivingSchoolMembershipRequestRepository.findOne(membershipRequestId);

		DrivingSchoolStudent student = drivingSchoolStudentRepository.findOne(studentId);
		
		membershipRequest.setDrivingSchoolStudent(student);
		membershipRequest = userDrivingSchoolMembershipRequestRepository.save(membershipRequest);
		
	}

	
	@Override
	@Transactional
	public void addNewNotification(DrivingSchoolNotificationDTO dto) {

		checkAdministrativePermissions(dto.getSchoolId());

		String title = StringUtils.trim(dto.getTitle());
		String content = StringUtils.trim(dto.getContent());

		boolean isValid = StringUtils.isNotBlank(title) && StringUtils.isNotBlank(content);

		content = TidyUtil.tidify(content);

		Assert.isTrue(isValid);

		DrivingSchool school = drivingSchoolRepository.findOne(dto.getSchoolId());

		DrivingSchoolNotification notification = new DrivingSchoolNotification();
		notification.setTitle(title);
		notification.setAuthor(userRepository.findById(SecurityUtils.getUserId()));
		notification.setContent(content);
		notification.setCreationDate(Calendar.getInstance().getTime());
		notification.setSchool(school);
		notification.setSendNotification(dto.getSendNotification());
		notification.setRequestNotificationReadConfirmation(dto.getRequestNotificationReadConfirmation());

		notification = drivingSchoolNotificationRepository.save(notification);
		
		mActivity.drivingSchoolNotification(notification);
		
		//slanje obavestenja aktivnim ucenicima
		sendNotifications(true, dto, notification);
	}

	@Override
	@Transactional
	public void updateNotification(DrivingSchoolNotificationDTO dto) {

		checkAdministrativePermissions(dto.getSchoolId());

		String title = StringUtils.trim(dto.getTitle());
		String content = StringUtils.trim(dto.getContent());

		boolean isValid = StringUtils.isNotBlank(title) && StringUtils.isNotBlank(content);

		Assert.isTrue(isValid);
		Assert.notNull(dto.getId());

		content = TidyUtil.tidify(content);

		DrivingSchoolNotification notification = drivingSchoolNotificationRepository.findOne(dto.getSchoolId(), dto.getId());

		/** slanje obavestenja aktivnim ucenicima pre promene na notification objektu; potrebno je poslati notifikaciju pre promene objekta u bazi jer se na osnovu
		 *  trenutnog stanja objekta iz baze (notification), stanja DTO objekta "sa front end-a" (dto) i toga da li je u pitanju novo obavestenje ili ne odredjuje
		 *  da li ce se poslati mail ili ne u metodi sendNotification(); staro stanje obavestenja je potrebno zbog provere stanja flag-ova
		 */
		sendNotifications(false, dto, notification);
				
		notification.setTitle(title);
		notification.setContent(content);
		
		/**
		 * ukoliko je obavestenje jednom poslato ne moze se odstiklirati i opet poslati - tako da nema promene u bazi
		 * moze samo jednom da se posalje obavestenje
		 */
		if (notification.getSendNotification() == false && dto.getSendNotification() == true){
			notification.setSendNotification(dto.getSendNotification());
		}
		
		/**
		 * ukoliko je trazena potvrda za obavestenje jednom ne moze se odstiklirati i opet poslati zahtev za potvrdu - tako da nema promene u bazi
		 * moze samo jednom da se posalje takav zahtev
		 */
		if (notification.getRequestNotificationReadConfirmation() == false && dto.getRequestNotificationReadConfirmation() == true){
			notification.setRequestNotificationReadConfirmation(dto.getRequestNotificationReadConfirmation());
		}

		notification = drivingSchoolNotificationRepository.save(notification);
	}

	@Override
	@Transactional
	public void addNewCar(DrivingSchoolCarDTO dto) {

		String make = dto.getMake();
		String model = dto.getModel();
		String type = dto.getType();
		Integer year = dto.getYear();
		String description = dto.getDescription();
		Long schoolId = dto.getSchoolId();
		String imageId = dto.getImage();
		Integer orderIndex = dto.getOrderIndex();

		checkAdministrativePermissions(schoolId);

		make = StringUtils.trim(make);
		model = StringUtils.trim(model);
		type = StringUtils.trim(type);
		description = StringUtils.trim(description);

		boolean isValid = StringUtils.isNotBlank(make) && StringUtils.isNotBlank(model);

		Assert.isTrue(isValid);

		DrivingSchool school = drivingSchoolRepository.findOne(schoolId);
		Long numOfCars = drivingSchoolCarRepository.countForSchool(schoolId);

		// nije obavezno za unos ali jeste u bazi
		if (orderIndex == null) {

			// mnozimo sa brojem 100 da bi se lakse unosile medjuvrednosti
			orderIndex = numOfCars != null ? numOfCars.intValue() + 1 : 1;
			orderIndex *= 100;
		}

		DrivingSchoolCar car = new DrivingSchoolCar();
		car.setMake(make);
		car.setModel(model);
		car.setType(type);
		car.setYear(year);
		car.setDescription(description);
		car.setOrderIndex(orderIndex);
		car.setSchool(school);

		car = drivingSchoolCarRepository.save(car);

		galleryManager.saveDrivingSchoolCarImage(schoolId, car.getId(), imageId);
	}

	@Override
	@Transactional
	public void updateCar(DrivingSchoolCarDTO dto) {

		String make = dto.getMake();
		String model = dto.getModel();
		String type = dto.getType();
		Integer year = dto.getYear();
		String description = dto.getDescription();
		Long schoolId = dto.getSchoolId();
		String imageId = dto.getImage();
		Long carId = dto.getId();
		Integer orderIndex = dto.getOrderIndex();

		checkAdministrativePermissions(schoolId);

		make = StringUtils.trim(make);
		model = StringUtils.trim(model);
		type = StringUtils.trim(type);
		description = StringUtils.trim(description);

		boolean isValid = StringUtils.isNotBlank(make) && StringUtils.isNotBlank(model);

		Assert.isTrue(isValid);
		Assert.notNull(carId);

		DrivingSchool school = drivingSchoolRepository.findOne(schoolId);
		DrivingSchoolCar car = drivingSchoolCarRepository.findOne(schoolId, carId);

		car.setMake(make);
		car.setModel(model);
		car.setType(type);
		car.setYear(year);
		car.setDescription(description);
		car.setSchool(school);

		if (orderIndex != null)
			car.setOrderIndex(orderIndex);

		car = drivingSchoolCarRepository.save(car);

		galleryManager.saveDrivingSchoolCarImage(schoolId, car.getId(), imageId);
	}

	@Override
	public void addNewAlbum(DocumentDTO dto) {

		String relatedId = dto.getRelatedId();
		String name = dto.getName();
		String description = dto.getDescription();

		name = StringUtils.trim(name);
		description = StringUtils.trim(description);

		boolean isValid = StringUtils.isNotBlank(name);

		Assert.isTrue(isValid);
		Assert.notNull(relatedId);

		Long schoolId = Long.parseLong(relatedId);

		checkAdministrativePermissions(schoolId);

		credentialsManager.checkDrivingSchoolGalleryLimitMessage(schoolId);

		galleryManager.addDrivingSchoolAlbum(schoolId, name, description);
	}

	@Override
	public void updateAlbum(DocumentDTO dto) {

		String id = dto.getId();
		String relatedId = dto.getRelatedId();
		String name = dto.getName();
		String description = dto.getDescription();

		name = StringUtils.trim(name);
		description = StringUtils.trim(description);

		boolean isValid = StringUtils.isNotBlank(name);

		Assert.isTrue(isValid);
		Assert.notNull(relatedId);
		Assert.notNull(id);

		Long schoolId = Long.parseLong(relatedId);

		checkAdministrativePermissions(schoolId);

		galleryManager.updateDrivingSchoolAlbum(schoolId, id, name, description);
	}

	@Override
	public void updateAlbumImage(DocumentDTO dto) {

		String id = dto.getId();
		String relatedId = dto.getRelatedId();
		String name = dto.getName();
		String description = dto.getDescription();

		name = StringUtils.trim(name);
		description = StringUtils.trim(description);

		boolean isValid = StringUtils.isNotBlank(name);

		Assert.isTrue(isValid);
		Assert.notNull(relatedId);
		Assert.notNull(id);

		Long schoolId = Long.parseLong(relatedId);

		checkAdministrativePermissions(schoolId);

		galleryManager.updateDrivingSchoolImage(schoolId, id, name, description);
	}

	/**
	 * Metoda proverava da li trenutni user ima pravo administracije nad odredjenom skolom.
	 * 
	 * @param schoolId
	 */
	private void checkAdministrativePermissions(Long schoolId) {

		credentialsManager.checkDrivingSchoolAdministration(schoolId);
	}

	@Override
	public void addDrivingSchoolMark(DrivingSchoolMarkDTO dto) {

		Assert.notNull(dto);

		User user = userRepository.findById(SecurityUtils.getUserId());

		DrivingSchool drivingSchool = drivingSchoolRepository.findOne(dto.getSchool().getId());

		DrivingSchoolMark schoolMark = drivingSchoolMarkRepository.findByAuthorAndSchool(user, drivingSchool);

		if (schoolMark != null)
			throw new EDTServiceException("Već ste dali ocenu za autoškolu " + drivingSchool.getName() + ".");

		DrivingSchoolMark drivingSchoolMark = new DrivingSchoolMark();
		drivingSchoolMark.setAuthor(user);
		drivingSchoolMark.setComment(dto.getComment());
		drivingSchoolMark.setDate(new Date());
		drivingSchoolMark.setSchool(drivingSchool);
		drivingSchoolMark.setMark(dto.getMark());

		drivingSchoolMarkRepository.save(drivingSchoolMark);
	}

	@Override
	public void removeDrivingSchoolMark(Long id) {

		Assert.notNull(id);

		User user = userRepository.findById(SecurityUtils.getUserId());
		DrivingSchoolMark mark = drivingSchoolMarkRepository.findOne(id);

		if (!user.getId().equals(mark.getAuthor().getId())) {
			throw new EDTServiceException("Korisnik " + user.getUsername() + " nema pravo da obriše ovu ocenu.");
		}

		drivingSchoolMarkRepository.delete(id);
	}

	@Override
	public void addImagesToAlbum(UploadImagesDTO dto) {

		String relatedId = dto.getRelatedId();
		String id = dto.getId();

		Assert.notNull(relatedId);
		Assert.notNull(id);

		Long schoolId = Long.parseLong(relatedId);

		checkAdministrativePermissions(schoolId);

		galleryManager.addDrivingSchoolImagesToAlbum(schoolId, id, dto.getAttachments());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.evola.edt.service.DrivingSchoolAdministrationService#addPriceList
	 * (com.evola.edt.service.dto.PriceListDTO)
	 */
	@Override
	public void addPriceList(PriceListDTO priceListDTO) {

		Assert.notNull(priceListDTO);

		checkAdministrativePermissions(priceListDTO.getDrivingSchoolDTO().getId());

		PriceList transformToEntity = priceListDTOTransformer.transformToEntity(priceListDTO);
		priceListRepository.save(transformToEntity);
	}

	@Transactional(readOnly = true)
	public PriceListDTO findPriceListForDrivingSchoolToEdit(Long drivingSchoolId) {
		List<PriceListCategoryDTO> priceListCategories = findPriceListCategories();
		List<PriceListSubCategoryDTO> priceListSubCategories = findPriceListSubcategories();
		PriceListDTO priceListDTO = findPriceListForDrivingSchool(drivingSchoolId);

		if (priceListDTO == null) {
			priceListDTO = new PriceListDTO();
			for (PriceListCategoryDTO priceListCategory : priceListCategories) {
				addCategory(priceListSubCategories, priceListDTO, priceListCategory);
			}
		} else {
			for (PriceListCategoryDTO priceListCategory : priceListCategories) {
				boolean containsCategory = false;
				for (PriceListCategoryViewDTO listCategoryViewDTO : priceListDTO.getListCategoryViewDTOs()) {
					if (priceListCategory.getId().equals(listCategoryViewDTO.getId())) {
						containsCategory = true;
					}
					for (PriceListSubCategoryDTO priceListSubCategory : priceListSubCategories) {
						boolean containsSubcategory = false;
						for (PriceListSubcategoryViewDTO priceListSubcategoryViewDTO : listCategoryViewDTO
								.getPriceListSubcategoryViewDTOs()) {
							if (priceListSubCategory.getId().equals(priceListSubcategoryViewDTO.getId())) {
								containsSubcategory = true;
							}
						}
						if (!containsSubcategory) {
							addSubcategory(listCategoryViewDTO, priceListSubCategory);
						}
					}
				}
				if (!containsCategory) {
					addCategory(priceListSubCategories, priceListDTO, priceListCategory);
				}
			}
		}
		return priceListDTO;
	}

	private void addCategory(List<PriceListSubCategoryDTO> priceListSubCategories, PriceListDTO priceListDTO,
			PriceListCategoryDTO priceListCategory) {
		PriceListCategoryViewDTO priceListCategoryViewDTO = new PriceListCategoryViewDTO();
		priceListCategoryViewDTO.setName(priceListCategory.getName());
		priceListCategoryViewDTO.setId(priceListCategory.getId());
		for (PriceListSubCategoryDTO priceListSubCategory : priceListSubCategories) {
			addSubcategory(priceListCategoryViewDTO, priceListSubCategory);
		}
		priceListDTO.getListCategoryViewDTOs().add(priceListCategoryViewDTO);
	}

	private void addSubcategory(PriceListCategoryViewDTO priceListCategoryViewDTO,
			PriceListSubCategoryDTO priceListSubCategory) {
		PriceListSubcategoryViewDTO priceListSubcategoryViewDTO = new PriceListSubcategoryViewDTO();
		priceListSubcategoryViewDTO.setName(priceListSubCategory.getName());
		priceListSubcategoryViewDTO.setId(priceListSubCategory.getId());
		priceListCategoryViewDTO.getPriceListSubcategoryViewDTOs().add(priceListSubcategoryViewDTO);
	}
	
	
	/* (non-Javadoc)
	 * @author Daci, 08.01.2015.
	 * potvrda notifikacije
	 */
	@Override
	public void drivingSchoolNotificationConfirmed(DrivingSchoolNotificationConfirmationDTO dto) {
	
		Assert.notNull(dto);
		
		DrivingSchoolNotificationConfirmation notification = drivingSchoolNotificationConfirmationRepository.findOne(dto.getId());
		
		notification.setConfirmed(true);
		notification.setConfirmationDate(new Date());
		
		notification = drivingSchoolNotificationConfirmationRepository.save(notification);
		
	}	

	/**
	 * @author Daci, 08.01.2015.
	 */
	private void sendNotifications(boolean newNotification, DrivingSchoolNotificationDTO notificationDTO, DrivingSchoolNotification notification) {
		
		/** 
		 * Novo obavestenje
		 * 
		 * ako se zahteva slanje obavestenja ucenicima, ali ne i potvrda ucenika da su procitali mail 
		 * onda treba da se nadju svi aktivni ucenici i da se posalju svi mail-ovi 
		 */
		if (newNotification == true && notificationDTO.getSendNotification() == true && notificationDTO.getRequestNotificationReadConfirmation() == false) {
			sendNewNotificationWithoutConfirmationRequest(notificationDTO);			
		}
		
		/**
		 * Novo obavestenje
		 * 
		 * ako se zahteva slanje obavestenja ucenicima i zahteva potvrda ucenika da su procitali mail 
		 * onda treba da se nadju svi ucenici i da se posalju svi mail-ovi sa generisanim tokenima za potvrdu 
		 */
		if (newNotification == true && notificationDTO.getSendNotification() == true && notificationDTO.getRequestNotificationReadConfirmation() == true) {
			sendNewNotificationWithConfirmationRequest(notificationDTO, notification);			
		}
		
		
		/**
		 * Postojece obavestenje
		 * 
		 * ako se zahteva slanje obavestenja ucenicima, ali ne i potvrda ucenika da su procitali mail onda treba da se nadju svi
		 * ucenici i da se posalju svi mail-ovi dodatni uslov je da obavestenje nije bilo poslato ranije		
		 */
		if (newNotification == false && notificationDTO.getSendNotification() == true && notificationDTO.getRequestNotificationReadConfirmation() == false 
				&& notification.getSendNotification() == false) {
			sendExistingNotificationWithoutConfirmationRequest(notificationDTO);
		}
		
		/**
		 * Postojece obavestenje
		 * 
		 * ako se zahteva slanje obavestenja ucenicima i zahteva potvrda ucenika da su procitali mail onda treba da se nadju svi
		 * ucenici i da se posalju svi mail-ovi sa generisanim tokenima za potvrdu 		
		 */
		if (newNotification == false && notificationDTO.getSendNotification() == true && notificationDTO.getRequestNotificationReadConfirmation() == true 
				&& notification.getRequestNotificationReadConfirmation() == false) {
			sendExistingNotificationWithConfirmationRequest(notificationDTO, notification);
		}
		
	}

	
	private void sendNewNotificationWithoutConfirmationRequest(DrivingSchoolNotificationDTO notificationDTO){
	
		/**
		 * pri kreiranju novog obavestenja:
		 * 1. proverava se da li je stiklirano "Pošalji obaveštenje svim aktivnim učenicima" (ili druga opcija) 
		 * 2. proverava iz liste studenata koji su samo studenti a ne i clanovi
		 * - ako su stiklirani - poslati im mail
		 * - ako nisu ne slati im mail
		 * 3. proverava iz liste studenata koji su studenti kreirani na osnovu zahteva od strane korisnika
		 * - ako je korisnik stiklirao da dobija obavestenja - poslati mu mail (ovo se vidi u UserDrivingSchoolMembershipRequest)
		 * - ako nije - ne slati mu mail
		 */
		
		DrivingSchool school = drivingSchoolRepository.findOne(notificationDTO.getSchoolId());
		
		List<DrivingSchoolStudent> students = drivingSchoolStudentRepository.findAllForNotifications(notificationDTO.getSchoolId());
		
		for (DrivingSchoolStudent student : students) {
			if (student.getEmail() != null && student.getEmail() != "" && student.getUser() == null) {
				
				emailManager.sendNotification(student.getFirstName(), student.getEmail(), school.getName(), notificationDTO.getContent());
			
			} else if (student.getUser() != null) {
			
				UserDrivingSchoolMembershipRequest request = userDrivingSchoolMembershipRequestRepository.findRequestBySchoolAndStudent(school.getId(), student.getId());
				
				if (request.getReceiveNotifications()){
					emailManager.sendNotification(student.getFirstName(), student.getEmail(), school.getName(), notificationDTO.getContent());
				}
			}
		}
		
	}
	
	
	private void sendNewNotificationWithConfirmationRequest(DrivingSchoolNotificationDTO notificationDTO, DrivingSchoolNotification notification){
		
		/**
		 * pri kreiranju novog obavestenja:
		 * 1. proverava se da li je stiklirano "Pošalji obaveštenje svim aktivnim učenicima" (ili druga opcija) 
		 * 2. proverava iz liste studenata koji su samo studenti a ne i clanovi
		 * - ako su stiklirani - poslati im mail
		 * - ako nisu ne slati im mail
		 * 3. proverava iz liste studenata koji su studenti kreirani na osnovu zahteva od strane korisnika
		 * - ako je korisnik stiklirao da dobija obavestenja - poslati mu mail (ovo se vidi u UserDrivingSchoolMembershipRequest)
		 * - ako nije - ne slati mu mail
		 */
		
		DrivingSchool school = drivingSchoolRepository.findOne(notificationDTO.getSchoolId());
		
		List<DrivingSchoolStudent> students = drivingSchoolStudentRepository.findAllForNotifications(notificationDTO.getSchoolId());
		
		for (DrivingSchoolStudent student : students) {
			if (student.getEmail() != null && student.getEmail() != "" && student.getUser() == null) {
			
				DrivingSchoolNotificationConfirmation notificationConfirmation = new DrivingSchoolNotificationConfirmation();
				notificationConfirmation.setNotification(notification);
				notificationConfirmation.setStudent(student);
				notificationConfirmation.setSchool(school);
				notificationConfirmation.setCreationDate(new Date());
				
				String generatedToken = TokenUtil.generateToken();
				notificationConfirmation.setToken(generatedToken);
				
				notificationConfirmation = drivingSchoolNotificationConfirmationRepository.save(notificationConfirmation);				
				
				emailManager.sendNotificationWithConfirmation(student.getFirstName(), student.getEmail(), school.getName(), notificationDTO.getContent(), generatedToken);
			
			} else if (student.getUser() != null) {
				
				UserDrivingSchoolMembershipRequest request = userDrivingSchoolMembershipRequestRepository.findRequestBySchoolAndStudent(school.getId(), student.getId());
				
				if (request != null && request.getReceiveNotifications()){
					DrivingSchoolNotificationConfirmation notificationConfirmation = new DrivingSchoolNotificationConfirmation();
					notificationConfirmation.setNotification(notification);
					notificationConfirmation.setStudent(student);
					notificationConfirmation.setSchool(school);
					notificationConfirmation.setCreationDate(new Date());
					
					String generatedToken = TokenUtil.generateToken();
					notificationConfirmation.setToken(generatedToken);
					
					notificationConfirmation = drivingSchoolNotificationConfirmationRepository.save(notificationConfirmation);				
					
					emailManager.sendNotificationWithConfirmation(student.getFirstName(), student.getEmail(), school.getName(), notificationDTO.getContent(), generatedToken);
				}
			}
		}
	}
	
	
	private void sendExistingNotificationWithoutConfirmationRequest(DrivingSchoolNotificationDTO notificationDTO){
		
		DrivingSchool school = drivingSchoolRepository.findOne(notificationDTO.getSchoolId());
		
		List<DrivingSchoolStudent> students = drivingSchoolStudentRepository.findAllForNotifications(notificationDTO.getSchoolId());
		
		for (DrivingSchoolStudent student : students) {
			if (student.getEmail() != null && student.getEmail() != "" && student.getUser() == null) {
				
				emailManager.sendNotification(student.getFirstName(), student.getEmail(), school.getName(), notificationDTO.getContent());
			
			} else if (student.getUser() != null) {
				
				UserDrivingSchoolMembershipRequest request = userDrivingSchoolMembershipRequestRepository.findRequestBySchoolAndStudent(school.getId(), student.getId());
				
				if (request.getReceiveNotifications()){
					emailManager.sendNotification(student.getFirstName(), student.getEmail(), school.getName(), notificationDTO.getContent());
				}
			}
		}
	}

	
	private void sendExistingNotificationWithConfirmationRequest(DrivingSchoolNotificationDTO notificationDTO, DrivingSchoolNotification notification){
		
		DrivingSchool school = drivingSchoolRepository.findOne(notificationDTO.getSchoolId());
		
		List<DrivingSchoolStudent> students = drivingSchoolStudentRepository.findAllForNotifications(notificationDTO.getSchoolId());
		
		for (DrivingSchoolStudent student : students) {
			if (student.getEmail() != null && student.getEmail() != "" && student.getUser() == null) {	
			
				DrivingSchoolNotificationConfirmation notificationConfirmation = new DrivingSchoolNotificationConfirmation();
				notificationConfirmation.setNotification(notification);
				notificationConfirmation.setStudent(student);
				notificationConfirmation.setSchool(school);
				notificationConfirmation.setCreationDate(new Date());
				
				String generatedToken = TokenUtil.generateToken();
				notificationConfirmation.setToken(generatedToken);
				
				notificationConfirmation = drivingSchoolNotificationConfirmationRepository.save(notificationConfirmation);				
				
				emailManager.sendNotificationWithConfirmation(student.getFirstName(), student.getEmail(), school.getName(), notificationDTO.getContent(), generatedToken);
			
			} else if (student.getUser() != null) {
				
				UserDrivingSchoolMembershipRequest request = userDrivingSchoolMembershipRequestRepository.findRequestBySchoolAndStudent(school.getId(), student.getId());
				
				if (request.getReceiveNotifications()){
					DrivingSchoolNotificationConfirmation notificationConfirmation = new DrivingSchoolNotificationConfirmation();
					notificationConfirmation.setNotification(notification);
					notificationConfirmation.setStudent(student);
					notificationConfirmation.setSchool(school);
					notificationConfirmation.setCreationDate(new Date());
					
					String generatedToken = TokenUtil.generateToken();
					notificationConfirmation.setToken(generatedToken);
					
					notificationConfirmation = drivingSchoolNotificationConfirmationRepository.save(notificationConfirmation);				
					
					emailManager.sendNotificationWithConfirmation(student.getFirstName(), student.getEmail(), school.getName(), notificationDTO.getContent(), generatedToken);
				}
			}
		}
	}

} 