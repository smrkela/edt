package com.evola.edt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.evola.edt.component.DailyTestManager;
import com.evola.edt.component.RealTestManager;
import com.evola.edt.component.UserPointsManager;
import com.evola.edt.forum.repository.ForumRepository;
import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.mail.EmailSender;
import com.evola.edt.model.DailyTest;
import com.evola.edt.model.User;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolMarkDTO;
import com.evola.edt.model.dto.PriceListDTO;
import com.evola.edt.model.dto.PriceListPriceDTO;
import com.evola.edt.model.dto.UserDTO;
import com.evola.edt.model.dto.UserSyncDTO;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.UploadImagesDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.service.dto.helpers.RealTestCreationDTO;
import com.evola.edt.service.dto.transformer.UserDTOTransformer;
import com.evola.edt.service.rest.ExecutionStatus;
import com.evola.edt.web.security.TokenUtil;
import com.evola.edt.web.security.UserCredentialsManager;

@Named
public class AdministrationServiceImpl implements AdministrationService {

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	GalleryManager galleryManager;

	@Inject
	private DrivingSchoolAdministrationService drivingSchoolAdministrationService;

	@Inject
	private DrivingSchoolService drivingSchoolService;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserDTOTransformer userDTOTransformer;

	@Inject
	private ForumRepository forumRepository;

	@Inject
	private EmailSender mailSender;

	@Inject
	private DailyTestManager dailyTestManager;

	@Inject
	private UserPointsManager pointManager;

	@Value("${mail.from}")
	private String emailFrom;

	@Value("${applicationUrl}")
	private String applicationUrl;

	@Value("${logoImg}")
	private String logoImg;

	@Inject
	private RealTestManager mRealTest;

	@Override
	public void addNewAlbum(DocumentDTO dto) {

		credentialsManager.checkSystemAdministration();

		String name = dto.getName();
		String description = dto.getDescription();

		name = StringUtils.trim(name);
		description = StringUtils.trim(description);

		boolean isValid = StringUtils.isNotBlank(name);

		Assert.isTrue(isValid);

		galleryManager.addGlobalImageAlbum(name, description);
	}

	@Override
	public void updateAlbum(DocumentDTO dto) {

		credentialsManager.checkSystemAdministration();

		String id = dto.getId();
		String name = dto.getName();
		String description = dto.getDescription();

		name = StringUtils.trim(name);
		description = StringUtils.trim(description);

		boolean isValid = StringUtils.isNotBlank(name);

		Assert.isTrue(isValid);
		Assert.notNull(id);

		galleryManager.updateGlobalImageAlbum(id, name, description);
	}

	@Override
	public void updateAlbumImage(DocumentDTO dto) {

		credentialsManager.checkSystemAdministration();

		String id = dto.getId();
		String name = dto.getName();
		String description = dto.getDescription();

		name = StringUtils.trim(name);
		description = StringUtils.trim(description);

		boolean isValid = StringUtils.isNotBlank(name);

		Assert.isTrue(isValid);
		Assert.notNull(id);

		galleryManager.updateGlobalImage(id, name, description);
	}

	@Override
	public void addImagesToAlbum(UploadImagesDTO dto) {

		credentialsManager.checkSystemAdministration();

		String id = dto.getId();

		Assert.notNull(id);

		galleryManager.addGlobalImagesToAlbum(id, dto.getAttachments());
	}

	/**
	 * @author Daci, 03.01.2014.
	 */
	@Override
	public void calculateCategoryBPrice(Long schoolID) {

		try {
			Double categoryBPrice = new Double(0);

			DrivingSchoolDTO drivingSchool = drivingSchoolService.findDrivingSchoolById(schoolID);
			PriceListDTO priceListForDrivingSchool = drivingSchoolAdministrationService.findPriceListForDrivingSchoolToEdit(schoolID);

			for (PriceListPriceDTO priceListPrice : priceListForDrivingSchool.getPriceListPriceDTOs()) {
				if (priceListPrice.getPriceListCategoryDTO().getName().equals("B")) {
					categoryBPrice += priceListPrice.getPriceDTO().getValue();
				}
			}

			drivingSchool.setCategoryBPrice(categoryBPrice);
			drivingSchoolService.updateCategoryBPrice(drivingSchool);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * @author Daci, 03.01.2014.
	 */
	@Override
	public void calculateAverageMark(Long schoolID) {

		try {
			Double averageMark = new Double("0");
			int counter = 0;

			DrivingSchoolDTO drivingSchool = drivingSchoolService.findDrivingSchoolById(schoolID);
			List<DrivingSchoolMarkDTO> drivingSchoolMarks = drivingSchoolService.findDrivingSchoolMarks(schoolID);

			for (DrivingSchoolMarkDTO drivingSchoolMark : drivingSchoolMarks) {
				averageMark += drivingSchoolMark.getMark();
				counter += 1;
			}

			averageMark = averageMark / counter;

			drivingSchool.setAverageMark(averageMark);
			drivingSchoolService.updateAverageMark(drivingSchool);

			System.out.println(averageMark);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * @author Daci, 11.01.2014.
	 */
	@Override
	@Transactional
	// znaci da se otvara transakcija po pozivu i da moze da radi
	// update/brisanje u bazi
	// @Transactional (readOnly = true) znaci da se otvara transakcija po pozivu
	// ali da ne moze da radi update/brisanje u bazi
	public boolean recalculateAverageMarkForAllSchools() {

		credentialsManager.checkSystemAdministration();

		boolean executionResult = true;

		try {

			List<DrivingSchoolDTO> drivingSchools = drivingSchoolService.findAll();

			for (Iterator<DrivingSchoolDTO> dsIterator = drivingSchools.iterator(); dsIterator.hasNext();) {
				DrivingSchoolDTO drivingSchool = (DrivingSchoolDTO) dsIterator.next();

				List<DrivingSchoolMarkDTO> drivingSchoolMarks = drivingSchoolService.findDrivingSchoolMarks(drivingSchool.getId());

				if (!drivingSchoolMarks.isEmpty()) { // ako skola ima ocene,
														// makar jednu
					Double averageMark = new Double("0");
					int counter = 0;

					for (DrivingSchoolMarkDTO drivingSchoolMark : drivingSchoolMarks) {
						averageMark += drivingSchoolMark.getMark();
						counter += 1;
					}

					averageMark = averageMark / counter;
					if (averageMark != drivingSchool.getAverageMark()) {
						drivingSchool.setAverageMark(averageMark);
						drivingSchoolService.updateAverageMark(drivingSchool);
					}

				} else if (drivingSchoolMarks.isEmpty() && drivingSchool.getAverageMark() != null) { // ako
																										// skola
																										// nema
																										// ni
																										// jednu
																										// oce
																										// ali
																										// IMA
																										// prosecnu
																										// ocenu
																										// onda
																										// je
																										// pregazi
					Double averageMark = new Double("0");
					averageMark = null;
					drivingSchool.setAverageMark(averageMark);
					drivingSchoolService.updateAverageMark(drivingSchool);
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			executionResult = false;
			return executionResult;
		}

		return executionResult;

	}

	/**
	 * @author Daci, 11.01.2014.
	 */
	@Override
	@Transactional
	public boolean recalculateCategoryBPriceForAllSchools() {

		credentialsManager.checkSystemAdministration();

		boolean executionResult = true;

		try {

			List<DrivingSchoolDTO> drivingSchools = drivingSchoolService.findAll();

			for (Iterator<DrivingSchoolDTO> dsIterator = drivingSchools.iterator(); dsIterator.hasNext();) {
				DrivingSchoolDTO drivingSchool = (DrivingSchoolDTO) dsIterator.next();

				PriceListDTO priceListForDrivingSchool = drivingSchoolAdministrationService.findPriceListForDrivingSchoolToEdit(drivingSchool.getId());

				if (!priceListForDrivingSchool.getPriceListPriceDTOs().isEmpty()) { // ako
																					// ima
																					// definisan
																					// cenovnik
					Double categoryBPrice = new Double(0);

					for (PriceListPriceDTO priceListPrice : priceListForDrivingSchool.getPriceListPriceDTOs()) {
						if (priceListPrice.getPriceListCategoryDTO().getName().equals("B")) {
							categoryBPrice += priceListPrice.getPriceDTO().getValue();
						}
					}

					if (categoryBPrice != drivingSchool.getCategoryBPrice()) {
						drivingSchool.setCategoryBPrice(categoryBPrice);
						drivingSchoolService.updateCategoryBPrice(drivingSchool);
					}

					// ako skola nema ni jednu cenu za B kategoriju ali IMA
					// prosecnu cenu onda je pregaziti i postaviti na null
				} else if (priceListForDrivingSchool.getPriceListPriceDTOs().isEmpty() && drivingSchool.getCategoryBPrice() != null) {
					Double categoryBPrice = new Double(0);
					categoryBPrice = null;
					drivingSchool.setCategoryBPrice(categoryBPrice);
					drivingSchoolService.updateCategoryBPrice(drivingSchool);
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			executionResult = false;
			return executionResult;
		}

		return executionResult;
	}

	/**
	 * @author Daci, 24.01.2014. vraca listu sa UserSyncDTO objektima, koji u
	 *         sebi sadrzi UserDTO bez obzira da li je korisnik uspesno
	 *         sinhronizovan ili nije
	 */
	@Override
	@Transactional
	public List<UserSyncDTO> synchronizeDBEDTtoForum(String userEmail, boolean sendNotifications) {
		List<UserSyncDTO> syncResult = new ArrayList<UserSyncDTO>();
		ExecutionStatus status;
		Random random = new Random();
		List<User> users = new ArrayList<>();

		if (userEmail != null) {
			users.add(userRepository.findByEmail(userEmail)); // uzimamo samo
																// korisnika
																// koji je
																// prosledjen
																// ("Sinhronizacija korisnika")
		} else {
			users = userRepository.findAllEnabledUsers(); // uzimamo sve
															// korisnike sa
															// sajta
															// ("Kompletna sinhronizacija baza")
		}

		if (users.get(0) != null) { // ako postoje korisnici, tj. ako je
									// pronadjen korisnik sa ukucanim e-mail-om;
									// users.size() > 0
			for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
				User user = (User) iterator.next();

				int randomNumber = random.nextInt(900000) + 100000;
				String newPassword = "vozaciSrbijeForum" + String.valueOf(randomNumber);

				// korisnik NECE biti banovan na Forumu, obizirom da se radi o
				// korisnicima koji su vec registrovani na sajtu
				status = forumRepository.createUser(user, false, newPassword);

				// slanje maila u zavisnosti od uspesnosti registracije na
				// Forumu i sendNotification inputa sa forme
				if (status.getStatus() == ExecutionStatus.Status.OK) {

					UserSyncDTO userSync = new UserSyncDTO();

					userSync.setId(user.getId());
					userSync.setUser(userDTOTransformer.transformToDTO(user));
					userSync.setSyncSuccessful(true);

					if (sendNotifications) {
						try {
							// slanje mail-a korisniku da je migriran na Forum
							SimpleMailMessage mailMessage = new SimpleMailMessage();

							mailMessage.setFrom(emailFrom);
							mailMessage.setTo(user.getEmail());
							mailMessage.setSubject("Kreiran nalog na Forumu VozaciSrbije.com");

							Map<String, Object> variables = new HashMap<String, Object>();

							variables.put("name", user.getFirstName());
							variables.put("username", user.getUsername());
							variables.put("password", newPassword);
							variables.put("companyLogo", applicationUrl + logoImg);

							mailSender.send(mailMessage, "syncToForum.vm", variables);

							userSync.seteMailNotificationSent(true);
						} catch (Exception e) {
							userSync.seteMailNotificationSent(false);
						}
					} else {
						userSync.seteMailNotificationSent(false);
					}

					syncResult.add(userSync);
				} else {
					UserSyncDTO userSync = new UserSyncDTO();

					userSync.setId(user.getId());
					userSync.setUser(userDTOTransformer.transformToDTO(user));
					userSync.setErrorMessage(status.getMessage().toString());
					userSync.setSyncSuccessful(false);

					syncResult.add(userSync);
				}
			}
		} else {
			UserSyncDTO userSync = new UserSyncDTO();

			userSync.setId(new Long(0));
			userSync.setUser(new UserDTO());
			userSync.setErrorMessage("U bazi ne postoji korisnik sa unetim kredencijalima [" + userEmail + "]!");
			userSync.setSyncSuccessful(false);

			syncResult.add(userSync);
		}

		return syncResult;
	}

	@Override
	@Transactional
	public String generateDailyTest() {

		boolean isNewTest = dailyTestManager.createTest();

		if (isNewTest)
			return "Napravljen je novi test.";
		else
			return "Osvezen je vec postojeci test.";
	}

	@Override
	@Transactional
	public String updateUserExperience() {

		pointManager.updateAllUsers();

		return "Uspešno osveženi poeni korisnika.";
	}

	@Override
	@Transactional
	public String saveRealTest(RealTestCreationDTO dto) {

		credentialsManager.checkSystemAdministration();

		mRealTest.save(dto);

		return "Uspešno sačuvan test.";
	}

}
