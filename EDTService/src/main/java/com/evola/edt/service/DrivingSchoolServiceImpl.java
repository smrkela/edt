package com.evola.edt.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.jsoup.Jsoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.model.DrivingLicenceCategory;
import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolCar;
import com.evola.edt.model.DrivingSchoolEmployee;
import com.evola.edt.model.DrivingSchoolLicenseType;
import com.evola.edt.model.DrivingSchoolMark;
import com.evola.edt.model.DrivingSchoolMember;
import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.DrivingSchoolNotificationComment;
import com.evola.edt.model.DrivingSchoolNotificationConfirmation;
import com.evola.edt.model.DrivingSchoolSiteLicense;
import com.evola.edt.model.DrivingSchoolStudent;
import com.evola.edt.model.MembershipRequestStatus;
import com.evola.edt.model.User;
import com.evola.edt.model.UserDrivingSchoolMembershipRequest;
import com.evola.edt.model.dto.DrivingLicenceCategoryDTO;
import com.evola.edt.model.dto.DrivingSchoolCarDTO;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolEmployeeDTO;
import com.evola.edt.model.dto.DrivingSchoolMarkDTO;
import com.evola.edt.model.dto.DrivingSchoolMemberDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationCommentDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationConfirmationDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.model.dto.DrivingSchoolSiteLicenseDTO;
import com.evola.edt.model.dto.DrivingSchoolStudentDTO;
import com.evola.edt.model.dto.PageResultDTO;
import com.evola.edt.repository.DrivingSchoolCarRepository;
import com.evola.edt.repository.DrivingSchoolEmployeeRepository;
import com.evola.edt.repository.DrivingSchoolMarkRepository;
import com.evola.edt.repository.DrivingSchoolMemberRepository;
import com.evola.edt.repository.DrivingSchoolNotificationCommentRepository;
import com.evola.edt.repository.DrivingSchoolNotificationConfirmationRepository;
import com.evola.edt.repository.DrivingSchoolNotificationRepository;
import com.evola.edt.repository.DrivingSchoolSiteLicenseRepository;
import com.evola.edt.repository.DrivingSchoolStudentRepository;
import com.evola.edt.repository.UserDrivingSchoolMembershipRequestRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.DrivingSchoolLoginDTO;
import com.evola.edt.service.dto.DrivingSchoolNotificationsDTO;
import com.evola.edt.service.dto.DrivingSchoolsDTO;
import com.evola.edt.service.dto.transformer.DrivingLicenceCategoryDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolCarDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolEmployeeDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolMarkDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolMemberDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolNotificationCommentDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolNotificationConfirmationDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolNotificationDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolSiteLicenseDTOTransformer;
import com.evola.edt.service.dto.transformer.DrivingSchoolStudentDTOTransformer;
import com.evola.edt.service.util.PageResultUtil;
import com.evola.edt.utils.DateUtils;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

@Named
public class DrivingSchoolServiceImpl extends AbstractDrivingSchoolServiceImpl implements DrivingSchoolService {

	@Inject
	private DrivingSchoolEmployeeRepository drivingSchoolEmployeeRepository;
	
	@Inject
	private DrivingSchoolStudentRepository rDrivingSchoolStudent;

	@Inject
	private DrivingSchoolCarRepository drivingSchoolCarRepository;

	@Inject
	private DrivingSchoolNotificationRepository drivingSchoolNotificationRepository;

	@Inject
	private DrivingSchoolNotificationCommentRepository drivingSchoolNotificationCommentRepository;

	@Inject
	private DrivingSchoolMarkRepository drivingSchoolMarkRepository;

	@Inject
	private DrivingSchoolDTOTransformer drivingSchoolDTOTransformer;

	@Inject
	private DrivingSchoolEmployeeDTOTransformer drivingSchoolEmployeeDTOTransformer;
	
	@Inject
	private DrivingSchoolStudentDTOTransformer tDrivingSchoolStudent;
	
	@Inject
	private DrivingLicenceCategoryDTOTransformer tDrivingLicenceCategory;

	@Inject
	private DrivingSchoolCarDTOTransformer drivingSchoolCarDTOTransformer;

	@Inject
	private DrivingSchoolNotificationDTOTransformer drivingSchoolNotificationDTOTransformer;

	@Inject
	private DrivingSchoolNotificationCommentDTOTransformer drivingSchoolNotificationCommentDTOTransformer;

	@Inject
	private DrivingSchoolMarkDTOTransformer drivingSchoolMarkDTOTransformer;

	@Inject
	private DrivingSchoolMemberRepository drivingSchoolMemberRepository;

	@Inject
	private DrivingSchoolMemberDTOTransformer drivingSchoolMemberDTOTransformer;

	@Inject
	UserRepository userRepository;

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	private DrivingSchoolSiteLicenseRepository drivingSchoolSiteLicenseRepository;

	@Inject
	private DrivingSchoolSiteLicenseDTOTransformer drivingSchoolSiteLicenseTransformer;
	
	@Inject
	private DrivingSchoolNotificationConfirmationRepository drivingSchoolNotificationConfirmationRepository;
	
	@Inject
	private DrivingSchoolNotificationConfirmationDTOTransformer drivingSchoolNotificationConfirmationDTOTransformer; 
	
	@Inject
	private UserDrivingSchoolMembershipRequestRepository userDrivingSchoolMembershipRequestRepository;

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolDTO findDrivingSchoolById(Long pageId) {

		Assert.notNull(pageId);

		DrivingSchool page = drivingSchoolRepository.findOne(pageId);
		return drivingSchoolDTOTransformer.transformToDTO(page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolDTO> findAll() {

		List<DrivingSchoolDTO> dtos = new LinkedList<DrivingSchoolDTO>();
		List<DrivingSchool> findAll = drivingSchoolRepository.findAll();

		for (DrivingSchool page : findAll) {
			dtos.add(drivingSchoolDTOTransformer.transformToDTO(page));
		}

		return dtos;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolDTO> findAllActiveSchools(Long userId) {
		
		List<UserDrivingSchoolMembershipRequest> findAllMembershipRequests = userDrivingSchoolMembershipRequestRepository.findAllRequestsForUser(userId);
		List<Long> schoolIDs = new LinkedList<Long>();
		
		for (UserDrivingSchoolMembershipRequest request : findAllMembershipRequests) {
			if (request.getStatus() == MembershipRequestStatus.APPROVED){
				schoolIDs.add(request.getDrivingSchool().getId());
			}
		}
		
		//ako je lista prazna, upit puca (unexpected end of subtree), zato ubacujemo dummy id 
		//da ne bismo pravili dva upita
		if(schoolIDs.isEmpty())
			schoolIDs.add(Long.MAX_VALUE);
		
		List<DrivingSchool> findAll = drivingSchoolRepository.findAllActiveSchools(schoolIDs);
		List<DrivingSchoolDTO> dtos = new LinkedList<DrivingSchoolDTO>();
		
		for (DrivingSchool drivingSchool : findAll) {
			dtos.add(drivingSchoolDTOTransformer.transformToDTO(drivingSchool));
		}
		
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolDTO> findDrivingSchoolsByCity(String city) {

		List<DrivingSchoolDTO> dtos = new LinkedList<DrivingSchoolDTO>();
		List<DrivingSchool> findAll = drivingSchoolRepository.findDrivingSchoolsByCity(city);

		for (DrivingSchool page : findAll) {
			dtos.add(drivingSchoolDTOTransformer.transformToDTO(page));
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolEmployeeDTO> findDrivingSchoolEmployees(Long schoolId) {

		List<DrivingSchoolEmployee> latestComments = drivingSchoolEmployeeRepository.findAll(schoolId);

		List<DrivingSchoolEmployeeDTO> dtos = new LinkedList<DrivingSchoolEmployeeDTO>();

		for (DrivingSchoolEmployee q : latestComments) {

			DrivingSchoolEmployeeDTO dto = drivingSchoolEmployeeDTOTransformer.transformToDTO(q, "school");

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolStudentDTO> findDrivingSchoolStudents(Long schoolId, String selectType) {
		
		List<DrivingSchoolStudent> students = null;
		
		if (StringUtils.isBlank(selectType))
			selectType = null;
		
		if (selectType != null){
	
			if (selectType != null && selectType.equals("1")) {
				students = rDrivingSchoolStudent.findAllWithFirstAid(schoolId);
			} else if (selectType != null && selectType.equals("2")) {
				students = rDrivingSchoolStudent.findAllWithTheory(schoolId);
			} else if (selectType != null && selectType.equals("3")) {
				students = rDrivingSchoolStudent.findAllWithPractice(schoolId);
			} else if (selectType != null && selectType.equals("4")) {
				students = rDrivingSchoolStudent.findAllWithAll(schoolId);
			}
		
		} else {
			students = rDrivingSchoolStudent.findAll(schoolId);
		}
		
		List<DrivingSchoolStudentDTO> dtos = new LinkedList<DrivingSchoolStudentDTO>();

		for (DrivingSchoolStudent q : students) {

			DrivingSchoolStudentDTO dto = tDrivingSchoolStudent.transformToDTO(q, "drivingSchool");

			dtos.add(dto);
		}

		return dtos;
	}
		
	
	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolCarDTO> findDrivingSchoolCars(Long schoolId) {

		List<DrivingSchoolCar> cars = drivingSchoolCarRepository.findAll(schoolId);

		List<DrivingSchoolCarDTO> dtos = new LinkedList<DrivingSchoolCarDTO>();

		for (DrivingSchoolCar q : cars) {

			DrivingSchoolCarDTO dto = drivingSchoolCarDTOTransformer.transformToDTO(q, "school");

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolNotificationsDTO findDrivingSchoolNotifications(Long schoolId, Integer startingIndex, int count) {

		org.springframework.data.domain.Page<DrivingSchoolNotification> findAll = drivingSchoolNotificationRepository.findAll(schoolId,
				new PageRequest(startingIndex, count, new Sort(Direction.DESC, "creationDate")));

		List<DrivingSchoolNotificationDTO> dtos = new LinkedList<DrivingSchoolNotificationDTO>();

		for (DrivingSchoolNotification q : findAll) {

			DrivingSchoolNotificationDTO dto = drivingSchoolNotificationDTOTransformer.transformToDTO(q, "author");

			// ucitavamo i broj komentara za svaku vest
			dto.setNumOfComments(drivingSchoolNotificationRepository.findNumOfComments(q.getId()).intValue());

			dtos.add(dto);
		}

		DrivingSchoolNotificationsDTO dto = new DrivingSchoolNotificationsDTO();
		dto.setNotifications(dtos);
		dto.setIsFirstPage(findAll.isFirstPage());
		dto.setIsLastPage(findAll.isLastPage());

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolNotificationDTO> findRecentNotifications(int count) {

		// limiti za prikaz
		int MAX_TITLE_SIZE = 50;
		int MAX_CONTENT_SIZE = 200;

		org.springframework.data.domain.Page<DrivingSchoolNotification> findAll = drivingSchoolNotificationRepository
				.findRecent(new PageRequest(0, count, new Sort(Direction.DESC, "creationDate")));

		List<DrivingSchoolNotificationDTO> dtos = new LinkedList<DrivingSchoolNotificationDTO>();

		for (DrivingSchoolNotification q : findAll) {

			DrivingSchoolNotificationDTO dto = drivingSchoolNotificationDTOTransformer.transformToDTO(q, "school");

			// cistimo sadrzaj u obican tekst
			dto.setContent(Jsoup.parse(q.getContent()).text());

			// moramo ograniciti velicinu naslova i velicinu sadrzaja
			dto.setTitle(StringUtils.abbreviate(dto.getTitle(), MAX_TITLE_SIZE));
			dto.setContent(StringUtils.abbreviate(dto.getContent(), MAX_CONTENT_SIZE));

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolNotificationDTO findNotification(Long notificationId) {

		Assert.notNull(notificationId);

		DrivingSchoolNotification schoolNotification = drivingSchoolNotificationRepository.findOne(notificationId);

		return drivingSchoolNotificationDTOTransformer.transformToDTO(schoolNotification, "author", "school");
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolNotificationCommentDTO> findNotificationComments(Long notificationId) {

		List<DrivingSchoolNotificationComment> latestComments = drivingSchoolNotificationCommentRepository.findAll(notificationId);

		List<DrivingSchoolNotificationCommentDTO> dtos = new LinkedList<DrivingSchoolNotificationCommentDTO>();

		for (DrivingSchoolNotificationComment q : latestComments) {

			DrivingSchoolNotificationCommentDTO dto = drivingSchoolNotificationCommentDTOTransformer.transformToDTO(q, "author",
					"notification");

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolMarkDTO> findDrivingSchoolMarks(Long schoolId) {

		List<DrivingSchoolMark> latestComments = drivingSchoolMarkRepository.findAll(schoolId);

		List<DrivingSchoolMarkDTO> dtos = new LinkedList<DrivingSchoolMarkDTO>();

		for (DrivingSchoolMark q : latestComments) {

			DrivingSchoolMarkDTO dto = drivingSchoolMarkDTOTransformer.transformToDTO(q, "author");

			dtos.add(dto);
		}

		return dtos;
	}

	public DrivingSchoolMarkDTO findDrivingMarkEditedByUser(User user, Long schoolID) {
		Assert.notNull(user);
		DrivingSchool school = drivingSchoolRepository.findOne(schoolID);
		DrivingSchoolMark findByUser = drivingSchoolMarkRepository.findByAuthorAndSchool(user, school);
		if (findByUser != null) {
			DrivingSchoolMarkDTO dto = drivingSchoolMarkDTOTransformer.transformToDTO(findByUser);
			return dto;
		} else {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Double findDrivingSchoolAverageMark(Long schoolId) {

		Double average = drivingSchoolMarkRepository.findAverageMark(schoolId);

		return average;
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolEmployeeDTO findDrivingSchoolEmployee(Long schoolId, Long employeeId) {

		Assert.notNull(employeeId);

		DrivingSchoolEmployee employee = drivingSchoolEmployeeRepository.findOne(schoolId, employeeId);

		return drivingSchoolEmployeeDTOTransformer.transformToDTO(employee, "school");
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolStudentDTO findDrivingSchoolStudent(Long schoolId, Long studentId) {

		Assert.notNull(studentId);

		DrivingSchoolStudent student = rDrivingSchoolStudent.findOne(schoolId, studentId);

		return tDrivingSchoolStudent.transformToDTO(student, "drivingSchool", "category");
	}

	@Override
	@Transactional
	public void removeDrivingSchoolEmployee(Long schoolId, Long employeeId) {

		drivingSchoolEmployeeRepository.delete(schoolId, employeeId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolNotificationDTO> findDrivingSchoolNotifications(Long schoolId) {

		List<DrivingSchoolNotification> latestComments = drivingSchoolNotificationRepository.findAll(schoolId);

		List<DrivingSchoolNotificationDTO> dtos = new LinkedList<DrivingSchoolNotificationDTO>();

		for (DrivingSchoolNotification q : latestComments) {

			DrivingSchoolNotificationDTO dto = drivingSchoolNotificationDTOTransformer.transformToDTO(q, "school");

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolNotificationDTO findDrivingSchoolNotification(Long schoolId, Long notificationId) {

		Assert.notNull(notificationId);

		DrivingSchoolNotification notification = drivingSchoolNotificationRepository.findOne(schoolId, notificationId);

		return drivingSchoolNotificationDTOTransformer.transformToDTO(notification, "school");
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolCarDTO findDrivingSchoolCar(Long schoolId, Long carId) {

		Assert.notNull(carId);

		DrivingSchoolCar car = drivingSchoolCarRepository.findOne(schoolId, carId);

		return drivingSchoolCarDTOTransformer.transformToDTO(car, "school");
	}

	@Override
	@Transactional
	public void removeDrivingSchoolCar(Long schoolId, Long carId) {

		drivingSchoolCarRepository.delete(schoolId, carId);
	}

	@Override
	@Transactional
	public void removeDrivingSchoolNotification(Long schoolId, Long notificationId) {

		drivingSchoolNotificationRepository.delete(notificationId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolNotificationConfirmationDTO> findAllDrivingSchoolNotificationConfirmations(Long schoolId, Long notificationId) {
		
		List<DrivingSchoolNotificationConfirmation> allConfirmations = drivingSchoolNotificationConfirmationRepository.findAllForNotification(schoolId, notificationId);
		
		List<DrivingSchoolNotificationConfirmationDTO> dtos = new LinkedList<DrivingSchoolNotificationConfirmationDTO>();
		
		for (DrivingSchoolNotificationConfirmation dsnc : allConfirmations) {
			DrivingSchoolNotificationConfirmationDTO dto = drivingSchoolNotificationConfirmationDTOTransformer.transformToDTO(dsnc, "notification", "student", "school");
			
			dtos.add(dto);
		}
		
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public PageResultDTO<DrivingSchoolDTO> findDrivingSchoolsPaged(Integer startingPage, int count) {

		Assert.notNull(startingPage);
		Assert.notNull(count);

		Page<DrivingSchool> all = drivingSchoolRepository.findAll(new PageRequest(startingPage, count, new Sort("name")));

		List<DrivingSchool> content = all.getContent();
		List<DrivingSchoolDTO> dtos = transformList(content);

		PageResultDTO<DrivingSchoolDTO> dto = new PageResultDTO<DrivingSchoolDTO>();
		PageResultUtil.populateCommon(all, dto);
		dto.setDtos(dtos);

		return dto;
	}

	@Override
	@Transactional
	public void deleteDrivingSchoolById(long id) {

		drivingSchoolRepository.delete(id);
	}

	@Override
	@Transactional
	public void addNew(DrivingSchoolDTO pageDto) {

		credentialsManager.checkSystemAdministration();

		Long countByUniqueName = drivingSchoolRepository.countByUniqueName(pageDto.getUniqueName());

		if (countByUniqueName > 0) {
			throw new IllegalStateException("Postoji autoskola sa datim jedinstvenim imenom");
		}

		DrivingSchool page = drivingSchoolDTOTransformer.transformToEntity(pageDto);

		// moramo podesiti autora
		User author = userRepository.findById(SecurityUtils.getUserId());
		page.setAuthor(author);

		// ubacujemo trenutno vreme za vreme pravljenja
		page.setCreationDate(new Date());

		drivingSchoolRepository.save(page);
	}

	@Override
	@Transactional
	public void update(DrivingSchoolDTO pageDto) {

		credentialsManager.checkSystemAdministration();

		Long countByUniqueName = drivingSchoolRepository.countByUniqueNameExcept(pageDto.getUniqueName(), pageDto.getId());

		if (countByUniqueName > 0) {
			throw new IllegalStateException("Postoji autoskola sa datim jedinstvenim imenom");
		}

		DrivingSchool page = drivingSchoolDTOTransformer.transformToEntity(pageDto);

		DrivingSchool oldPage = drivingSchoolRepository.findOne(page.getId());
		oldPage.setName(page.getName());
		oldPage.setUniqueName(page.getUniqueName());
		oldPage.setIsHidden(page.getIsHidden());
		oldPage.setHasPermit(page.getHasPermit());

		drivingSchoolRepository.save(oldPage);
	}

	@Override
	@Transactional
	public void updateCategoryBPrice(DrivingSchoolDTO pageDto) {

		DrivingSchool page = drivingSchoolDTOTransformer.transformToEntity(pageDto);

		DrivingSchool oldPage = drivingSchoolRepository.findOne(page.getId());
		oldPage.setCategoryBPrice(page.getCategoryBPrice());

		drivingSchoolRepository.save(oldPage);
	}

	@Override
	@Transactional
	public void updateAverageMark(DrivingSchoolDTO pageDto) {

		DrivingSchool page = drivingSchoolDTOTransformer.transformToEntity(pageDto);

		DrivingSchool oldPage = drivingSchoolRepository.findOne(page.getId());
		oldPage.setAverageMark(page.getAverageMark());

		drivingSchoolRepository.save(oldPage);
	}

	@Override
	@Transactional
	public List<DrivingSchoolMemberDTO> findDrivingSchoolMembersById(Long id) {

		Assert.notNull(id);

		List<DrivingSchoolMember> members = drivingSchoolMemberRepository.findAll(id);

		List<DrivingSchoolMemberDTO> dtos = new LinkedList<DrivingSchoolMemberDTO>();

		for (DrivingSchoolMember m : members) {

			dtos.add(drivingSchoolMemberDTOTransformer.transformToDTO(m, "user"));
		}

		return dtos;
	}

	@Override
	@Transactional
	public void addNewMember(DrivingSchoolMemberDTO memberDto) {

		credentialsManager.checkSystemAdministration();

		Long schoolId = memberDto.getSchoolId();
		String role = memberDto.getRole();
		String email = memberDto.getEmail();

		role = StringUtils.trim(role);
		email = StringUtils.trim(email);

		Long countByEmail = drivingSchoolMemberRepository.countByUserEmail(schoolId, email);

		if (countByEmail > 0) {
			throw new IllegalStateException("Vec postoji clan sa zadatim emailom");
		}

		Long numOfUsers = userRepository.countByEmail(email);

		if (numOfUsers != 1) {
			throw new IllegalStateException("Ne postoji korisnik sa zadatim emailom.");
		}

		validateRole(role);

		DrivingSchool drivingSchool = drivingSchoolRepository.findOne(schoolId);
		User user = userRepository.findByEmail(email);
		User author = userRepository.findById(SecurityUtils.getUserId());

		DrivingSchoolMember member = new DrivingSchoolMember();
		member.setCreationDate(new Date());
		member.setDrivingSchool(drivingSchool);
		member.setUser(user);
		member.setRole(role);
		member.setAuthor(author);

		drivingSchoolMemberRepository.save(member);
	}

	@Override
	@Transactional
	public void updateMember(DrivingSchoolMemberDTO memberDto) {

		credentialsManager.checkSystemAdministration();

		Long schoolId = memberDto.getSchoolId();
		String role = memberDto.getRole();
		String email = memberDto.getEmail();
		Long memberId = memberDto.getId();

		role = StringUtils.trim(role);
		email = StringUtils.trim(email);

		Long countByEmail = drivingSchoolMemberRepository.countByUserEmailExcluding(schoolId, email, memberId);

		if (countByEmail > 0) {
			throw new IllegalStateException("Vec postoji clan sa zadatim emailom");
		}

		Long numOfUsers = userRepository.countByEmail(email);

		if (numOfUsers != 1) {
			throw new IllegalStateException("Ne postoji korisnik sa zadatim emailom.");
		}

		validateRole(role);

		User user = userRepository.findByEmail(email);

		DrivingSchoolMember member = drivingSchoolMemberRepository.findOne(memberId);
		member.setUser(user);
		member.setRole(role);

		drivingSchoolMemberRepository.save(member);
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolMemberDTO findDrivingSchoolMember(Long memberId) {

		Assert.notNull(memberId);

		DrivingSchoolMember member = drivingSchoolMemberRepository.findOne(memberId);

		return drivingSchoolMemberDTOTransformer.transformToDTO(member, "school", "user");
	}

	@Override
	@Transactional
	public void removeDrivingSchoolMember(Long employeeId) {

		drivingSchoolMemberRepository.delete(employeeId);
	}

	private void validateRole(String role) {

		String[] memberRoles = new String[] { "admin" };

		if (!Arrays.asList(memberRoles).contains(role))
			throw new IllegalArgumentException("Nije definisana rola '" + role + "' za clanove autoskola.");
	}

	@Override
	@Transactional
	public List<String> findCityNames() {

		List<String> names = drivingSchoolRepository.findUniqueCityNames();

		return names;
	}

	@Override
	@Transactional
	public List<String> findCityNamesForActiveSchools() {

		List<String> names = drivingSchoolRepository.findUniqueCityNamesForActiveSchools();

		return names;
	}

	// @Override
	// public List<DrivingSchoolDTO> findBySearchCriteria(String searchText,
	// String city, String category,
	// Double searchMarkFrom, Double searchMarkTo, Double searchPriceFrom,
	// Double searchPriceTo) {
	//
	// List<DrivingSchoolDTO> dtos = new LinkedList<DrivingSchoolDTO>();
	//
	// if (StringUtils.isBlank(searchText))
	// searchText = null;
	// else
	// searchText = "%" + searchText + "%";
	//
	// if (StringUtils.isBlank(category))
	// category = null;
	// else
	// category = "%" + category + "%";
	//
	// if (StringUtils.isBlank(city))
	// city = null;
	//
	// List<Object[]> schools =
	// drivingSchoolRepository.findBySearchCriteria(searchText, city, category,
	// searchMarkFrom, searchMarkTo, searchPriceFrom, searchPriceTo);
	//
	// for (Object page : schools) {
	//
	// Object[] obj = (Object[]) page;
	//
	// DrivingSchoolDTO dto = new DrivingSchoolDTO();
	//
	// dto.setName((String) obj[0]);
	// dto.setCity((String) obj[1]);
	// dto.setCategories((String) obj[2]);
	// dto.setUniqueName((String) obj[3]);
	// dto.setCategoryBPrice((Double) obj[4]);
	// dto.setAverageMark((Double) obj[5]);
	//
	// dtos.add(dto);
	// }
	//
	// return dtos;
	// }

	@Override
	public DrivingSchoolsDTO findBySearchAndSortCriteria(String searchText, String city, String category, Double searchMarkFrom,
			Double searchMarkTo, Double searchPriceFrom, Double searchPriceTo, String sortType, int startingIndex, int count) {

		List<DrivingSchoolDTO> dtos = new LinkedList<DrivingSchoolDTO>();

		if (StringUtils.isBlank(searchText))
			searchText = null;
		else
			searchText = "%" + searchText + "%";

		if (StringUtils.isBlank(category))
			category = null;
		else
			category = "%" + category + "%";

		if (StringUtils.isBlank(city))
			city = null;

		Sort sorting = null;

		if (sortType != null && sortType.equals("Ime uzlazno")) {
			sorting = new Sort(Direction.ASC, "name");
		} else if (sortType != null && sortType.equals("Ime silazno")) {
			sorting = new Sort(Direction.DESC, "name");
		} else if (sortType != null && sortType.equals("Grad uzlazno")) {
			sorting = new Sort(Direction.ASC, "city");
		} else if (sortType != null && sortType.equals("Grad silazno")) {
			sorting = new Sort(Direction.DESC, "city");
		} else if (sortType != null && sortType.equals("Cena uzlazno")) {
			sorting = new Sort(Direction.ASC, "categoryBPrice");
		} else if (sortType != null && sortType.equals("Cena silazno")) {
			sorting = new Sort(Direction.DESC, "categoryBPrice");
		} else if (sortType != null && sortType.equals("Ocena uzlazno")) {
			sorting = new Sort(Direction.ASC, "averageMark");
		} else if (sortType != null && sortType.equals("Ocena silazno")) {
			sorting = new Sort(Direction.DESC, "averageMark");
		}

		Page<DrivingSchool> schools = drivingSchoolRepository.findBySearchAndSortCriteria(searchText, city, category, searchMarkFrom,
				searchMarkTo, searchPriceFrom, searchPriceTo, new PageRequest(startingIndex, count, sorting));

		for (DrivingSchool drivingSchool : schools) {
			dtos.add(drivingSchoolDTOTransformer.transformToDTO(drivingSchool));
		}

		DrivingSchoolsDTO dsdto = new DrivingSchoolsDTO();
		dsdto.setSchools(dtos);
		dsdto.setIsFirstPage(schools.isFirstPage());
		dsdto.setIsLastPage(schools.isLastPage());
		dsdto.setTotalSchools(schools.getTotalElements());
		dsdto.setTotalPages(schools.getTotalPages());

		return dsdto;
	}

	@Override
	@Transactional
	public DrivingSchoolSiteLicenseDTO findLincenseForSchool(Long schoolId) {

		Assert.notNull(schoolId);

		DrivingSchool drivingSchool = drivingSchoolRepository.findOne(schoolId);

		DrivingSchoolSiteLicense license = drivingSchoolSiteLicenseRepository.findByDrivingSchool(drivingSchool);

		DrivingSchoolSiteLicenseDTO licenseDTO = drivingSchoolSiteLicenseTransformer.transformToDTO(license, "author");

		return licenseDTO;
	}

	@Override
	@Transactional
	public void saveDrivingSchoolLicense(DrivingSchoolSiteLicenseDTO dto) {

		credentialsManager.checkSystemAdministration();

		Assert.notNull(dto);

		Long schoolId = dto.getSchoolId();

		Assert.notNull(schoolId);

		// radimo prepisivanje parametara

		DrivingSchool school = drivingSchoolRepository.findOne(schoolId);
		DrivingSchoolSiteLicense license = drivingSchoolSiteLicenseRepository.findByDrivingSchool(school);

		// ako licenca jos uvek ne postoji pravimo je
		if (license == null) {

			license = new DrivingSchoolSiteLicense();
			license.setDrivingSchool(school);

		} else {

			Assert.isTrue(license.getId() == dto.getId());
		}

		if (license.getAuthor() == null)
			license.setAuthor(userRepository.findOne(SecurityUtils.getUserId()));

		if (license.getCreationDate() == null)
			license.setCreationDate(new Date());

		license.setIsActive(dto.isActive());
		license.setLicenseType(DrivingSchoolLicenseType.valueOf(dto.getLicenseType()));
		license.setValidFrom(dto.getValidFrom());
		license.setValidTo(dto.getValidTo());

		drivingSchoolSiteLicenseRepository.save(license);
	}

	@Transactional
	@Override
	public Long findDrivingSchoolIdByUniqueName(String uniqueName) {

		Assert.notNull(uniqueName);

		Long id = null;

		DrivingSchool findByName = drivingSchoolRepository.findByUniqueName(uniqueName);

		if (findByName != null)
			id = findByName.getId();

		return id;
	}

	@Override
	public DrivingSchoolDTO findDrivingSchoolByUniqueName(String uniqueName) {

		Assert.notNull(uniqueName);

		DrivingSchool school = drivingSchoolRepository.findByUniqueName(uniqueName);

		DrivingSchoolDTO schoolDTO = null;

		if (school != null)
			schoolDTO = findDrivingSchoolById(school.getId());

		return schoolDTO;
	}

	@Transactional
	@Override
	public void saveDrivingSchoolNotificationComment(Long notificationId, String comment) {

		DrivingSchoolNotification notification = drivingSchoolNotificationRepository.findOne(notificationId);

		DrivingSchoolNotificationComment c = new DrivingSchoolNotificationComment();
		c.setAuthor(userRepository.findOne(SecurityUtils.getUserId()));
		c.setComment(comment);
		c.setDate(new Date());
		c.setNotification(notification);

		drivingSchoolNotificationCommentRepository.save(c);
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolNotificationsDTO findDrivingSchoolsNotifications(Integer startingIndex, int count) {

		int MAX_TITLE_SIZE = 200;
		int MAX_CONTENT_SIZE = 400;

		org.springframework.data.domain.Page<DrivingSchoolNotification> findAll = drivingSchoolNotificationRepository
				.findAllForVisibleSchools(new PageRequest(startingIndex, count, new Sort(Direction.DESC, "creationDate")));

		List<DrivingSchoolNotificationDTO> dtos = new LinkedList<DrivingSchoolNotificationDTO>();

		for (DrivingSchoolNotification q : findAll) {

			DrivingSchoolNotificationDTO dto = drivingSchoolNotificationDTOTransformer.transformToDTO(q, "author", "school");

			// radimo limitiranje i ciscenje duzine sadrzaja ovde
			dto.setContent(Jsoup.parse(q.getContent()).text());
			dto.setContent(StringUtils.abbreviate(dto.getContent(), MAX_CONTENT_SIZE));

			dto.setTitle(q.getSchool().getName() + " " + q.getSchool().getCity() + " - " + q.getTitle());
			dto.setTitle(StringUtils.abbreviate(dto.getTitle(), MAX_TITLE_SIZE));

			// ucitavamo i broj komentara za svaku vest
			dto.setNumOfComments(drivingSchoolNotificationRepository.findNumOfComments(q.getId()).intValue());

			dtos.add(dto);
		}

		DrivingSchoolNotificationsDTO dto = new DrivingSchoolNotificationsDTO();
		dto.setNotifications(dtos);
		dto.setIsFirstPage(findAll.isFirstPage());
		dto.setIsLastPage(findAll.isLastPage());

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolNotificationComment findNotificationComment(Long commentId) {

		Assert.notNull(commentId);

		return drivingSchoolNotificationCommentRepository.findOne(commentId);
	}

	@Override
	@Transactional
	public void removeDrivingSchoolNotificationComment(Long commentId) {

		Assert.notNull(commentId);

		drivingSchoolNotificationCommentRepository.delete(commentId);
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolNotification findNotificationByCommentId(Long commentId) {

		Assert.notNull(commentId);

		// NAPOMENA: Ova metoda inicijalizuje i skolu

		DrivingSchoolNotification notification = drivingSchoolNotificationCommentRepository.findOne(commentId).getNotification();

		Hibernate.initialize(notification.getSchool());

		return notification;
	}

	@Override
	@Transactional(readOnly = true)
	public DrivingSchool findDrivingSchoolByNotification(Long notificationid) {

		Assert.notNull(notificationid);

		return drivingSchoolNotificationRepository.findOne(notificationid).getSchool();
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolDTO> findDrivingSchoolsByMember(Long userId) {

		Assert.notNull(userId);

		List<DrivingSchool> schools = drivingSchoolRepository.findByMember(userId);

		List<DrivingSchoolDTO> dtos = transformList(schools);

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolDTO> getLicencedDrivingSchools() {

		List<DrivingSchool> schools = drivingSchoolRepository.findLicencedSchools();

		List<DrivingSchoolDTO> dtos = new LinkedList<DrivingSchoolDTO>();

		for (DrivingSchool drivingSchool : schools) {

			DrivingSchoolDTO dto = drivingSchoolDTOTransformer.transformToDTO(drivingSchool);

			Long numberOfMembers = drivingSchoolMemberRepository.countForSchool(drivingSchool.getId());

			dto.setLicense(findLincenseForSchool(drivingSchool.getId()));
			dto.setNumberOfMembers(numberOfMembers.intValue());

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<DrivingSchoolLoginDTO> getDrivingSchoolsLoginData() {

		List<DrivingSchoolMember> allMembers = drivingSchoolMemberRepository.findAll();

		// mapa skola i clanova
		Map<Long, DrivingSchoolLoginDTO> membersMap = new HashMap<Long, DrivingSchoolLoginDTO>();

		for (DrivingSchoolMember member : allMembers) {

			DrivingSchool drivingSchool = member.getDrivingSchool();

			if (drivingSchool != null){
				
				if(membersMap.containsKey(drivingSchool.getId())){
					
					DrivingSchoolLoginDTO dto = membersMap.get(drivingSchool.getId());
					
					//radimo update korisnika ako se ulogovao kasnije
					if(DateUtils.isAfter(member.getUser().getLastLogin(), dto.getLoginDate())){
						
						dto.setLoginDate(member.getUser().getLastLogin());
						dto.setRegistrationDate(member.getUser().getRegistrationDate());
						dto.setUserId(member.getUser().getId());
						dto.setUserName(member.getUser().getFirstName() + " " +member.getUser().getLastName());
					}
					
					dto.setTotalMembers(dto.getTotalMembers() + 1);
					
				}
				else{
					
					DrivingSchoolLoginDTO dto = new DrivingSchoolLoginDTO();
					dto.setDrivingSchoolId(drivingSchool.getId());
					dto.setDrivingSchoolName(drivingSchool.getName());
					dto.setLoginDate(member.getUser().getLastLogin());
					dto.setRegistrationDate(member.getUser().getRegistrationDate());
					dto.setUserId(member.getUser().getId());
					dto.setUserName(member.getUser().getFirstName() + " " +member.getUser().getLastName());
					dto.setTotalMembers(1);
					
					membersMap.put(drivingSchool.getId(), dto);
				}
			}
		}

		return membersMap.values();
	}

	private List<DrivingSchoolDTO> transformList(List<DrivingSchool> schools, String... fetchFields) {

		List<DrivingSchoolDTO> dtos = new LinkedList<DrivingSchoolDTO>();

		for (DrivingSchool drivingSchool : schools) {
			dtos.add(drivingSchoolDTOTransformer.transformToDTO(drivingSchool, fetchFields));
		}
		return dtos;
	}

	@Override
	@Transactional
	public List<DrivingLicenceCategoryDTO> getDrivingLincenceCategories() {

		List<DrivingLicenceCategory> findAllSorted = rDrivingLicenceCategory.findAllSorted();
		
		List<DrivingLicenceCategoryDTO> dtos = new LinkedList<DrivingLicenceCategoryDTO>();
		
		if(findAllSorted != null){
			
			for(DrivingLicenceCategory category : findAllSorted){
				
				DrivingLicenceCategoryDTO dto = tDrivingLicenceCategory.transformToDTO(category);
				
				dtos.add(dto);
			}
		}
		
		return dtos;
	}
	
	@Override
	@Transactional
	public void removeDrivingSchoolStudent(Long schoolId, Long studentId) {

		Assert.notNull(schoolId);
		Assert.notNull(studentId);
		
		rDrivingSchoolStudent.delete(schoolId, studentId);
		
		UserDrivingSchoolMembershipRequest request = userDrivingSchoolMembershipRequestRepository.findRequestBySchoolAndStudent(schoolId, studentId);
		
		if (request != null) {
			userDrivingSchoolMembershipRequestRepository.delete(request.getId());
		}
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<DrivingSchoolStudentDTO> findDrivingSchoolStudentsWithoutMembershipRequest(Long schoolId) {

		Assert.notNull(schoolId);
		
		List<DrivingSchoolStudent> students = rDrivingSchoolStudent.findDrivingSchoolStudentsWithoutMembershipRequest(schoolId);
		
		List<DrivingSchoolStudentDTO> dtos = new LinkedList<DrivingSchoolStudentDTO>();

		for (DrivingSchoolStudent q : students) {

			DrivingSchoolStudentDTO dto = tDrivingSchoolStudent.transformToDTO(q, "category");

			dtos.add(dto);
		}

		return dtos;
	}	
	

	@Override
	@Transactional(readOnly = true)
	public DrivingSchoolNotificationConfirmationDTO findDrivingSchoolNotificationConfirmation(String token) {
		
		Assert.notNull(token);
		
		DrivingSchoolNotificationConfirmation notification = drivingSchoolNotificationConfirmationRepository.findByToken(token);
		
		return drivingSchoolNotificationConfirmationDTOTransformer.transformToDTO(notification, "notification", "student", "school");
	}

}