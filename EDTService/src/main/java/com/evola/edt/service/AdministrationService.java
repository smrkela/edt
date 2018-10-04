package com.evola.edt.service;

import java.util.List;

import com.evola.edt.model.dto.UserSyncDTO;
import com.evola.edt.service.dto.UploadImagesDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.service.dto.helpers.RealTestCreationDTO;

public interface AdministrationService {

	void addNewAlbum(DocumentDTO dto);

	void updateAlbum(DocumentDTO dto);

	void updateAlbumImage(DocumentDTO dto);

	void addImagesToAlbum(UploadImagesDTO dto);

	void calculateCategoryBPrice(Long schoolID);
	
	void calculateAverageMark(Long schoolID);
	
	boolean recalculateAverageMarkForAllSchools();
	
	boolean recalculateCategoryBPriceForAllSchools();
	
	List<UserSyncDTO> synchronizeDBEDTtoForum(String userEmail, boolean sendNotifications);

	String generateDailyTest();

	String updateUserExperience();

	String saveRealTest(RealTestCreationDTO dto);
}
