package com.evola.edt.service;

import com.evola.edt.model.dto.*;
import com.evola.edt.service.dto.UploadImagesDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;

public interface DrivingSchoolAdministrationService {

    public abstract void addNewEmployee(DrivingSchoolEmployeeDTO dto);

    public abstract void updateEmployee(DrivingSchoolEmployeeDTO dto);

    public abstract void saveDrivingSchool(DrivingSchoolDTO pageDto);

    public abstract void addNewNotification(DrivingSchoolNotificationDTO dto);

    public abstract void updateNotification(DrivingSchoolNotificationDTO dto);

    public abstract void addNewCar(DrivingSchoolCarDTO dto);

    public abstract void updateCar(DrivingSchoolCarDTO dto);

    public abstract void addNewAlbum(DocumentDTO dto);

    public abstract void addDrivingSchoolMark(DrivingSchoolMarkDTO dto);

    public abstract void removeDrivingSchoolMark(Long id);

    public abstract void updateAlbum(DocumentDTO dto);

    public abstract void addImagesToAlbum(UploadImagesDTO dto);

    public abstract void updateAlbumImage(DocumentDTO dto);

    public void addPriceList(PriceListDTO priceListDTO);

    public PriceListDTO findPriceListForDrivingSchoolToEdit(Long drivingSchoolId);

	public abstract void addNewStudent(DrivingSchoolStudentDTO dto);

	public abstract void updateStudent(DrivingSchoolStudentDTO dto);
	
	public abstract void connectToExistingStudent(Long userId, Long studentId);
	
	public abstract void drivingSchoolNotificationConfirmed(DrivingSchoolNotificationConfirmationDTO dto);

}