package com.evola.edt.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.utils.social.SocialMediaService;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@XmlRootElement(name = "user")
public class UserDTO extends BaseDTO {

	private String className = "User";
	private String email;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Date lastLogin;
	private Boolean isMale;
	private Integer questionsPerPage;
	private DrivingCategoryDTO drivingCategory;
	private List<UserQuestionStatLearnDTO> learningStats = new ArrayList<UserQuestionStatLearnDTO>();
	private List<UserQuestionStatTestDTO> testingStats = new ArrayList<UserQuestionStatTestDTO>();
	private Date registrationDate;
	private String profileImage;
	private Date activationDate;
	private Integer learnedQuestions;
	private Integer testedQuestions;
	private Integer points;
	private SocialMediaService signInProvider;

	@XmlAttribute(name = "class-name")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@XmlAttribute(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlAttribute(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlAttribute(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlAttribute(name = "firstName")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlAttribute(name = "lastName")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlAttribute(name = "last-login")
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@XmlElement(name = "learning-stat")
	public List<UserQuestionStatLearnDTO> getLearningStats() {
		return learningStats;
	}

	public void setLearningStats(List<UserQuestionStatLearnDTO> learningStats) {
		this.learningStats = learningStats;
	}

	@XmlElement(name = "testing-stat")
	public List<UserQuestionStatTestDTO> getTestingStats() {
		return testingStats;
	}

	public void setTestingStats(List<UserQuestionStatTestDTO> testingStats) {
		this.testingStats = testingStats;
	}

	@XmlAttribute(name = "isMale")
	public Boolean getIsMale() {
		return isMale;
	}

	public void setIsMale(Boolean isMale) {
		this.isMale = isMale;
	}

	@XmlAttribute(name = "questions-per-page")
	public Integer getQuestionsPerPage() {
		return questionsPerPage;
	}

	public void setQuestionsPerPage(Integer questionsPerPage) {
		this.questionsPerPage = questionsPerPage;
	}

	@XmlElement(name = "driving-category")
	public DrivingCategoryDTO getDrivingCategory() {
		return drivingCategory;
	}

	public void setDrivingCategory(DrivingCategoryDTO drivingCategory) {
		this.drivingCategory = drivingCategory;
	}

	@XmlAttribute(name = "registration-date")
	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getNormalProfileImagePath() {

		return "/users/" + getId() + "/normalImage";
	}
	
	public String getSmallProfileImagePath() {

		return "/users/" + getId() + "/smallImage";
	}

	@XmlAttribute(name="profileImage")
	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	
	@XmlAttribute(name = "activation-date")
	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	@XmlAttribute(name="learned-questions")
	public Integer getLearnedQuestions() {
		return learnedQuestions;
	}

	public void setLearnedQuestions(Integer learnedQuestions) {
		this.learnedQuestions = learnedQuestions;
	}

	@XmlAttribute(name="tested-questions")
	public Integer getTestedQuestions() {
		return testedQuestions;
	}

	public void setTestedQuestions(Integer testedQuestions) {
		this.testedQuestions = testedQuestions;
	}

	@XmlAttribute(name="points")
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@XmlAttribute(name="sign-in-provider")
	public SocialMediaService getSignInProvider() {
		return signInProvider;
	}

	public void setSignInProvider(SocialMediaService signInProvider) {
		this.signInProvider = signInProvider;
	}

}
