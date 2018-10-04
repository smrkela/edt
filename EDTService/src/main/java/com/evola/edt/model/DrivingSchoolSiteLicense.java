package com.evola.edt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Entity
public class DrivingSchoolSiteLicense extends BaseEntity {
	
	
	@NotNull
	@OneToOne
	private DrivingSchool drivingSchool;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date validFrom;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date validTo;
	
	//da li je trenutno aktivna licenca, ne mora biti u okviru navedenih datuma
	private Boolean isActive;
	
	@Enumerated(EnumType.STRING)
	private DrivingSchoolLicenseType licenseType; 

	@ManyToOne
	private User author;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	public DrivingSchoolSiteLicense() {
	}

	public DrivingSchool getDrivingSchool() {
		return drivingSchool;
	}

	public void setDrivingSchool(DrivingSchool drivingSchool) {
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

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public DrivingSchoolLicenseType getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(DrivingSchoolLicenseType licenseType) {
		this.licenseType = licenseType;
	}


}
