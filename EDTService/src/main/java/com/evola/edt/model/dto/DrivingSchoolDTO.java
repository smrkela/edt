package com.evola.edt.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.txw2.annotation.XmlAttribute;

/**
 * @author Nikola 23.05.2013.
 * 
 */
@XmlRootElement
public class DrivingSchoolDTO extends BaseDTO {

	private String name;
	private String legalName;
	private String city;
	private String address;
	private String categories;
	private String website;
	private String aboutUs;
	private String country;
	private String phone;
	private String phone2;
	private String fax;
	private String email;
	private String workingHoursMonday;
	private String workingHoursTuesday;
	private String workingHoursWednesday;
	private String workingHoursThursday;
	private String workingHoursFriday;
	private String workingHoursSaturday;
	private String workingHoursSunday;
	private String googleMapsURL;
	private String logo;
	private String uniqueName;
	private List<DrivingSchoolMarkDTO> marks = new ArrayList<DrivingSchoolMarkDTO>();
	private String facebookURL;
	private String twitterURL;
	/**
	 * @author Daci 03.01.2014.
	 */
	private Double categoryBPrice;
	private Double averageMark;
	
	private Boolean isHidden;
	private Boolean hasPermit;
	private String permitNumber;
	private Date permitDate;
	private String registryNumber;

	private DrivingSchoolSiteLicenseDTO license;

	private Integer numberOfMembers;
	
	public DrivingSchoolDTO() {
		super();
	}

	public DrivingSchoolDTO(String name) {
		super();
		this.name = name;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	@XmlAttribute
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@XmlAttribute
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@XmlAttribute
	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	@XmlAttribute
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@XmlAttribute
	public String getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWorkingHoursMonday() {
		return workingHoursMonday;
	}

	public void setWorkingHoursMonday(String workingHoursMonday) {
		this.workingHoursMonday = workingHoursMonday;
	}

	public String getWorkingHoursTuesday() {
		return workingHoursTuesday;
	}

	public void setWorkingHoursTuesday(String workingHoursTuesday) {
		this.workingHoursTuesday = workingHoursTuesday;
	}

	public String getWorkingHoursWednesday() {
		return workingHoursWednesday;
	}

	public void setWorkingHoursWednesday(String workingHoursWednesday) {
		this.workingHoursWednesday = workingHoursWednesday;
	}

	public String getWorkingHoursThursday() {
		return workingHoursThursday;
	}

	public void setWorkingHoursThursday(String workingHoursThursday) {
		this.workingHoursThursday = workingHoursThursday;
	}

	public String getWorkingHoursFriday() {
		return workingHoursFriday;
	}

	public void setWorkingHoursFriday(String workingHoursFriday) {
		this.workingHoursFriday = workingHoursFriday;
	}

	public String getWorkingHoursSaturday() {
		return workingHoursSaturday;
	}

	public void setWorkingHoursSaturday(String workingHoursSaturday) {
		this.workingHoursSaturday = workingHoursSaturday;
	}

	public String getWorkingHoursSunday() {
		return workingHoursSunday;
	}

	public void setWorkingHoursSunday(String workingHoursSunday) {
		this.workingHoursSunday = workingHoursSunday;
	}

	public String getGoogleMapsURL() {
		return googleMapsURL;
	}

	public void setGoogleMapsURL(String googleMapsURL) {
		this.googleMapsURL = googleMapsURL;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLogoPath() {

		return "/drivingSchools/" + getId() + "/logo";
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
	
	@XmlElement(name="marks")
	public List<DrivingSchoolMarkDTO> getMarks() {
		return marks;
	}

	public void setMarks(List<DrivingSchoolMarkDTO> marks) {
		this.marks = marks;
	}

	public String getFacebookURL() {
		return facebookURL;
	}

	public void setFacebookURL(String facebookURL) {
		this.facebookURL = facebookURL;
	}

	public String getTwitterURL() {
		return twitterURL;
	}

	public void setTwitterURL(String twitterURL) {
		this.twitterURL = twitterURL;
	}
	
	public Double getCategoryBPrice() {
		return categoryBPrice;
	}

	public void setCategoryBPrice(Double categoryBPrice) {
		this.categoryBPrice = categoryBPrice;
	}

	public Double getAverageMark() {
		return averageMark;
	}

	public void setAverageMark(Double averageMark) {
		this.averageMark = averageMark;
	}

	public DrivingSchoolSiteLicenseDTO getLicense() {
		return license;
	}

	public void setLicense(DrivingSchoolSiteLicenseDTO license) {
		this.license = license;
	}

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public Boolean getHasPermit() {
		return hasPermit;
	}

	public void setHasPermit(Boolean hasPermit) {
		this.hasPermit = hasPermit;
	}

	public String getPermitNumber() {
		return permitNumber;
	}

	public void setPermitNumber(String permitNumber) {
		this.permitNumber = permitNumber;
	}

	public Date getPermitDate() {
		return permitDate;
	}

	public void setPermitDate(Date permitDate) {
		this.permitDate = permitDate;
	}

	public String getRegistryNumber() {
		return registryNumber;
	}

	public void setRegistryNumber(String registryNumber) {
		this.registryNumber = registryNumber;
	}

	public Integer getNumberOfMembers() {
		return numberOfMembers;
	}

	public void setNumberOfMembers(Integer numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}

}
