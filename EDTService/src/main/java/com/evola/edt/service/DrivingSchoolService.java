package com.evola.edt.service;

import java.util.Collection;
import java.util.List;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.DrivingSchoolNotificationComment;
import com.evola.edt.model.User;
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
import com.evola.edt.model.dto.PriceListCategoryDTO;
import com.evola.edt.model.dto.PriceListDTO;
import com.evola.edt.model.dto.PriceListSubCategoryDTO;
import com.evola.edt.service.dto.DrivingSchoolLoginDTO;
import com.evola.edt.service.dto.DrivingSchoolNotificationsDTO;
import com.evola.edt.service.dto.DrivingSchoolsDTO;

public interface DrivingSchoolService {

	public abstract DrivingSchoolDTO findDrivingSchoolById(Long pageId);

	public abstract List<DrivingSchoolDTO> findAll();
	
	public abstract List<DrivingSchoolDTO> findAllActiveSchools(Long userId);

	public abstract List<DrivingSchoolDTO> findDrivingSchoolsByCity(String city);

	public abstract List<DrivingSchoolEmployeeDTO> findDrivingSchoolEmployees(Long schoolId);

	public abstract List<DrivingSchoolCarDTO> findDrivingSchoolCars(Long schoolId);

	public abstract DrivingSchoolNotificationsDTO findDrivingSchoolNotifications(Long schoolId, Integer startingIndex, int count);

	public abstract DrivingSchoolNotificationDTO findNotification(Long notificationId);

	public abstract List<DrivingSchoolNotificationCommentDTO> findNotificationComments(Long notificationId);

	public abstract List<DrivingSchoolMarkDTO> findDrivingSchoolMarks(Long schoolId);

	public abstract Double findDrivingSchoolAverageMark(Long schoolId);

	public abstract DrivingSchoolEmployeeDTO findDrivingSchoolEmployee(Long schoolId, Long employeeId);

	public abstract void removeDrivingSchoolEmployee(Long schoolId, Long employeeId);

	public abstract List<DrivingSchoolNotificationDTO> findDrivingSchoolNotifications(Long schoolId);

	public abstract DrivingSchoolNotificationDTO findDrivingSchoolNotification(Long schoolId, Long notificationId);

	public abstract DrivingSchoolCarDTO findDrivingSchoolCar(Long schoolId, Long vehicleId);

	public abstract void removeDrivingSchoolCar(Long schoolId, Long vehicleId);

	public abstract void removeDrivingSchoolNotification(Long schoolId, Long notificationId);
	
	public List<DrivingSchoolNotificationConfirmationDTO> findAllDrivingSchoolNotificationConfirmations(Long schoolId, Long notificationId);

	public DrivingSchoolMarkDTO findDrivingMarkEditedByUser(User user, Long schoolID);

	/**
	 * @param drivingSchoolId
	 * @return {@link PriceListDTO} Nikola 21.09.2013.
	 */
	public PriceListDTO findPriceListForDrivingSchool(Long drivingSchoolId);

	/**
	 * @return
	 * @author Nikola 22.09.2013.
	 */
	public List<PriceListCategoryDTO> findPriceListCategories();

	/**
	 * @return
	 * @author Nikola 22.09.2013.
	 */
	public List<PriceListSubCategoryDTO> findPriceListSubcategories();

	public abstract PageResultDTO<DrivingSchoolDTO> findDrivingSchoolsPaged(Integer startingPage, int count);

	public abstract void deleteDrivingSchoolById(long id);

	public abstract void addNew(DrivingSchoolDTO dto);

	public abstract void update(DrivingSchoolDTO dto);
	
	/**
	 * @param pageDto
	 * @author Daci 03.01.2014.
	 */
	public void updateCategoryBPrice(DrivingSchoolDTO pageDto);
	
	/**
	 * @param pageDto
	 * @author Daci 03.01.2014.
	 */
	public void updateAverageMark(DrivingSchoolDTO pageDto);

	public abstract List<DrivingSchoolMemberDTO> findDrivingSchoolMembersById(Long id);

	public abstract void addNewMember(DrivingSchoolMemberDTO dto);

	public abstract void updateMember(DrivingSchoolMemberDTO dto);

	public abstract DrivingSchoolMemberDTO findDrivingSchoolMember(Long memberId);

	public abstract void removeDrivingSchoolMember(Long memberId);

	public abstract List<String> findCityNames();

//	public abstract List<DrivingSchoolDTO> findBySearchCriteria(String searchText, String city, String category, Double searchMarkFrom, Double searchMarkTo,
//																Double searchPriceFrom, Double searchPriceTo);
	
	public abstract DrivingSchoolsDTO findBySearchAndSortCriteria(String searchText, String city, String category, Double searchMarkFrom, Double searchMarkTo,
																  Double searchPriceFrom, Double searchPriceTo, String sortType, int startingIndex, int count);
	
	public abstract DrivingSchoolSiteLicenseDTO findLincenseForSchool(Long schoolId);

	public abstract void saveDrivingSchoolLicense(DrivingSchoolSiteLicenseDTO dto);

	public abstract Long findDrivingSchoolIdByUniqueName(String uniqueName);

	public abstract DrivingSchoolDTO findDrivingSchoolByUniqueName(String uniqueName);

	public abstract void saveDrivingSchoolNotificationComment(Long notificationId, String comment);

	public abstract List<String> findCityNamesForActiveSchools();

	public abstract List<DrivingSchoolNotificationDTO> findRecentNotifications(int count);

	public abstract DrivingSchoolNotificationsDTO findDrivingSchoolsNotifications(Integer startingPage, int count);

	public abstract DrivingSchoolNotificationComment findNotificationComment(Long commentId);

	public abstract void removeDrivingSchoolNotificationComment(Long commentId);

	public abstract DrivingSchoolNotification findNotificationByCommentId(Long commentId);

	public abstract DrivingSchool findDrivingSchoolByNotification(Long notificationid);

	public abstract List<DrivingSchoolDTO> findDrivingSchoolsByMember(Long userId);

	public abstract List<DrivingSchoolDTO> getLicencedDrivingSchools();

	public abstract Collection<DrivingSchoolLoginDTO> getDrivingSchoolsLoginData();

	public abstract List<DrivingSchoolStudentDTO> findDrivingSchoolStudents(Long schoolId, String selectType);

	public abstract DrivingSchoolStudentDTO findDrivingSchoolStudent(Long schoolId, Long studentId);
	
	/**
	 * @param schoolId
	 * @param studentId
	 * @author Daci 25.12.2014.
	 */
	public abstract void removeDrivingSchoolStudent(Long schoolId, Long studentId);
	
	/**
	 * @param schoolId
	 * @return
	 * @author Daci 31.01.2015.
	 */
	public abstract List<DrivingSchoolStudentDTO> findDrivingSchoolStudentsWithoutMembershipRequest(Long schoolId);

	public abstract List<DrivingLicenceCategoryDTO> getDrivingLincenceCategories();
	
	/**
	 * @author Daci 08.01.2015.
	 * @param token
	 * @return
	 */
	public abstract DrivingSchoolNotificationConfirmationDTO findDrivingSchoolNotificationConfirmation (String token);
}