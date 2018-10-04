package com.evola.edt.model.dto;

import java.util.Date;

public class DrivingSchoolSiteLicenseDTO extends BaseDTO {

	private String className = "DrivingSchoolSiteLicense";
	private DrivingSchoolDTO drivingSchool;
	private Date validFrom;
	private Date validTo;
	private boolean isActive;
	private String licenseType;
	private UserDTO author;
	private Date creationDate;
	
	//tranzijentno polje
	private Long schoolId;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public DrivingSchoolDTO getDrivingSchool() {
		return drivingSchool;
	}
	public void setDrivingSchool(DrivingSchoolDTO drivingSchool) {
		this.drivingSchool = drivingSchool;
	}
	public Date getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	public Date getValidTo() {
		return validTo;
	}
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public UserDTO getAuthor() {
		return author;
	}
	public void setAuthor(UserDTO author) {
		this.author = author;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

}
