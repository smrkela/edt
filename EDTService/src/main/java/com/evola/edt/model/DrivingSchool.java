package com.evola.edt.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 * @author Nikola 23.05.2013.
 * 
 */
@Entity
public class DrivingSchool extends BaseEntity {

	@Column(length = 500)
	@Size(max = 500)
	private String name;

	@Column(length = 1000)
	@Size(max = 1000)
	private String legalName;

	@Column(length = 200)
	@Size(max = 200)
	private String city;

	@Column(length = 200)
	@Size(max = 200)
	private String address;

	@Column(length = 200)
	@Size(max = 200)
	private String categories;

	@Column(length = 200)
	@Size(max = 200)
	private String website;

	@Column(columnDefinition = "text")
	private String aboutUs;

	@Column(length = 256)
	@Size(max = 256)
	private String country;

	@Column(length = 256)
	@Size(max = 256)
	private String phone;

	@Column(length = 256)
	@Size(max = 256)
	private String phone2;

	@Column(length = 256)
	@Size(max = 256)
	private String fax;

	@Column(length = 256)
	@Size(max = 256)
	private String email;

	@Column(length = 256)
	@Size(max = 256)
	private String workingHoursMonday;

	@Column(length = 256)
	@Size(max = 256)
	private String workingHoursTuesday;

	@Column(length = 256)
	@Size(max = 256)
	private String workingHoursWednesday;

	@Column(length = 256)
	@Size(max = 256)
	private String workingHoursThursday;

	@Column(length = 256)
	@Size(max = 256)
	private String workingHoursFriday;

	@Column(length = 256)
	@Size(max = 256)
	private String workingHoursSaturday;

	@Column(length = 256)
	@Size(max = 256)
	private String workingHoursSunday;

	@Column(length = 1000)
	@Size(max = 1000)
	private String googleMapsURL;

	@Column(length = 1000)
	@Size(max = 1000)
	private String uniqueName;

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "school")
	private Set<DrivingSchoolEmployee> employees = new HashSet<DrivingSchoolEmployee>();

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "school")
	private Set<DrivingSchoolCar> cars = new HashSet<DrivingSchoolCar>();

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "school")
	private Set<DrivingSchoolMark> marks = new HashSet<DrivingSchoolMark>();

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "school")
	private Set<DrivingSchoolNotification> notifications = new HashSet<DrivingSchoolNotification>();

	@Column(name = "creationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@ManyToOne
	private User author;

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "drivingSchool")
	private Set<DrivingSchoolStudent> students = new HashSet<DrivingSchoolStudent>();

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "drivingSchool")
	private Set<DrivingSchoolMember> members = new HashSet<DrivingSchoolMember>();
	
	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "drivingSchool")
	private Set<UserDrivingSchoolMembershipRequest> membershipRequest = new HashSet<UserDrivingSchoolMembershipRequest>();
	
	@Column(length = 500)
	@Size(max = 500)
	private String facebookURL;
	
	@Column(length = 500)
	@Size(max = 500)
	private String twitterURL;
	
	/**
	 * @author Daci 03.01.2014.
	 */
	@Column
	private Double categoryBPrice;
	
	@Column
	private Double averageMark;
	
	@Column
	private Boolean isHidden;
	
	@Column
	private Boolean hasPermit;
	
	@Column
	private String permitNumber;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date permitDate;
	
	@Column
	private String registryNumber;
	
	@OneToOne(cascade=CascadeType.REMOVE, orphanRemoval=true, mappedBy = "drivingSchool")
	private DrivingSchoolSiteLicense license;

	public DrivingSchool() {
		super();
	}

	public DrivingSchool(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

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

	public Set<DrivingSchoolEmployee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<DrivingSchoolEmployee> employees) {
		this.employees = employees;
	}

	public Set<DrivingSchoolCar> getCars() {
		return cars;
	}

	public void setCars(Set<DrivingSchoolCar> cars) {
		this.cars = cars;
	}

	public Set<DrivingSchoolNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<DrivingSchoolNotification> notifications) {
		this.notifications = notifications;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueId) {
		this.uniqueName = uniqueId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Set<DrivingSchoolStudent> getStudents() {
		return students;
	}

	public void setStudents(Set<DrivingSchoolStudent> students) {
		this.students = students;
	}

	public Set<DrivingSchoolMember> getMembers() {
		return members;
	}

	public void setMembers(Set<DrivingSchoolMember> members) {
		this.members = members;
	}
	
	public Set<DrivingSchoolMark> getMarks() {
		return marks;
	}

	public void setMarks(Set<DrivingSchoolMark> marks) {
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

	public DrivingSchoolSiteLicense getLicense() {
		return license;
	}

	public void setLicense(DrivingSchoolSiteLicense license) {
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

	public Set<UserDrivingSchoolMembershipRequest> getMembershipRequest() {
		return membershipRequest;
	}

	public void setMembershipRequest(Set<UserDrivingSchoolMembershipRequest> membershipRequest) {
		this.membershipRequest = membershipRequest;
	}
	
}
