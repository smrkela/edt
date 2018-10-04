package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.dto.DrivingSchoolDTO;

/**
 * @author Nikola 23.05.2013.
 * 
 */
@Named
public class DrivingSchoolDTOTransformer implements DTOTransformer<DrivingSchoolDTO, DrivingSchool> {

	@Autowired
	private DrivingSchoolSiteLicenseDTOTransformer licenseTransformer;

	@Override
	public DrivingSchoolDTO transformToDTO(DrivingSchool entity, String... fetchFields) {
		if (entity == null) {
			return null;
		}

		DrivingSchoolDTO dto = new DrivingSchoolDTO();
		dto.setId(entity.getId());
		dto.setAddress(entity.getAddress());
		dto.setCategories(entity.getCategories());
		dto.setCity(entity.getCity());
		dto.setLegalName(entity.getLegalName());
		dto.setName(entity.getName());
		dto.setWebsite(entity.getWebsite());
		dto.setAboutUs(entity.getAboutUs());
		dto.setCountry(entity.getCountry());
		dto.setPhone(entity.getPhone());
		dto.setPhone2(entity.getPhone2());
		dto.setFax(entity.getFax());
		dto.setEmail(entity.getEmail());
		dto.setWorkingHoursMonday(entity.getWorkingHoursMonday());
		dto.setWorkingHoursTuesday(entity.getWorkingHoursTuesday());
		dto.setWorkingHoursWednesday(entity.getWorkingHoursWednesday());
		dto.setWorkingHoursThursday(entity.getWorkingHoursThursday());
		dto.setWorkingHoursFriday(entity.getWorkingHoursFriday());
		dto.setWorkingHoursSaturday(entity.getWorkingHoursSaturday());
		dto.setWorkingHoursSunday(entity.getWorkingHoursSunday());
		dto.setGoogleMapsURL(entity.getGoogleMapsURL());
		dto.setUniqueName(entity.getUniqueName());
		dto.setFacebookURL(entity.getFacebookURL());
		dto.setTwitterURL(entity.getTwitterURL());
		dto.setCategoryBPrice(entity.getCategoryBPrice());
		dto.setAverageMark(entity.getAverageMark());
		dto.setIsHidden(entity.getIsHidden());
		dto.setHasPermit(entity.getHasPermit());
		dto.setPermitNumber(entity.getPermitNumber());
		dto.setPermitDate(entity.getPermitDate());
		dto.setRegistryNumber(entity.getRegistryNumber());

		if (Arrays.asList(fetchFields).contains("license"))
			dto.setLicense(licenseTransformer.transformToDTO(entity.getLicense()));

		return dto;
	}

	@Override
	public DrivingSchool transformToEntity(DrivingSchoolDTO dto) {
		if (dto == null) {
			return null;
		}

		DrivingSchool entity = new DrivingSchool();
		entity.setId(dto.getId());
		entity.setAddress(dto.getAddress());
		entity.setCategories(dto.getCategories());
		entity.setCity(dto.getCity());
		entity.setLegalName(dto.getLegalName());
		entity.setName(dto.getName());
		entity.setWebsite(dto.getWebsite());
		entity.setAboutUs(dto.getAboutUs());
		entity.setCountry(dto.getCountry());
		entity.setPhone(dto.getPhone());
		entity.setPhone2(dto.getPhone2());
		entity.setFax(dto.getFax());
		entity.setEmail(dto.getEmail());
		entity.setWorkingHoursMonday(dto.getWorkingHoursMonday());
		entity.setWorkingHoursTuesday(dto.getWorkingHoursTuesday());
		entity.setWorkingHoursWednesday(dto.getWorkingHoursWednesday());
		entity.setWorkingHoursThursday(dto.getWorkingHoursThursday());
		entity.setWorkingHoursFriday(dto.getWorkingHoursFriday());
		entity.setWorkingHoursSaturday(dto.getWorkingHoursSaturday());
		entity.setWorkingHoursSunday(dto.getWorkingHoursSunday());
		entity.setGoogleMapsURL(dto.getGoogleMapsURL());
		entity.setUniqueName(dto.getUniqueName());
		entity.setFacebookURL(dto.getFacebookURL());
		entity.setTwitterURL(dto.getTwitterURL());
		entity.setCategoryBPrice(dto.getCategoryBPrice());
		entity.setAverageMark(dto.getAverageMark());
		entity.setIsHidden(dto.getIsHidden());
		entity.setHasPermit(dto.getHasPermit());
		entity.setPermitNumber(dto.getPermitNumber());
		entity.setPermitDate(dto.getPermitDate());
		entity.setRegistryNumber(dto.getRegistryNumber());
		
		return entity;
	}

}
