package com.evola.edt.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.evola.edt.utils.social.Role;
import com.evola.edt.utils.social.SocialMediaService;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Entity
public class User extends BaseEntity implements UserDetails {

	/**
	 * @author Nikola 21.04.2013.
	 */
	private static final long serialVersionUID = 1L;
	@Column(unique = true)
	@NotNull
	@Size(min = 1)
	@Email
	private String email;
	@Column(unique = true)
	@NotNull
	@Size(min = 1)
	private String username;
	@NotNull
	@Size(min = 1)
	private String password;
	@NotNull
	@Size(min = 1)
	private String firstName;
	@NotNull
	@Size(min = 1)
	private String lastName;
	@NotNull
	private Boolean isMale;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	private Integer questionsPerPage = 10;
	private boolean enabled = false;
	@ManyToOne
	private DrivingCategory drivingCategory;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
	private Set<UserQuestionStatLearn> learnStatistics = new HashSet<UserQuestionStatLearn>();
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
	private Set<UserQuestionStatTest> testingStatistics = new HashSet<UserQuestionStatTest>();
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Collection<EDTGrantedAuthority> authorities = new HashSet<EDTGrantedAuthority>();
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDate;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
	private Set<DrivingSchoolStudent> drivingSchoolMemberships = new HashSet<DrivingSchoolStudent>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
	private Set<UserDrivingSchoolMembershipRequest> membershipRequest = new HashSet<UserDrivingSchoolMembershipRequest>();
	
	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
	private Set<UserQuestionFavorite> questionFavorites = new HashSet<UserQuestionFavorite>();
	
	@Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "sign_in_provider", length = 20)
    private SocialMediaService signInProvider;
	
	/**
	 * @author Daci 06.03.2014.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date activationDate;

	private Integer learnedQuestions;
	private Integer testedQuestions;
	private Integer points;

	public User() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<UserQuestionStatLearn> getLearnStatistics() {
		return learnStatistics;
	}

	public void setLearnStatistics(Set<UserQuestionStatLearn> learnStatistics) {
		this.learnStatistics = learnStatistics;
	}

	public Set<UserQuestionStatTest> getTestingStatistics() {
		return testingStatistics;
	}

	public void setTestingStatistics(Set<UserQuestionStatTest> testingStatistics) {
		this.testingStatistics = testingStatistics;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Boolean getIsMale() {
		return isMale;
	}

	public void setIsMale(Boolean isMale) {
		this.isMale = isMale;
	}

	public Integer getQuestionsPerPage() {
		return questionsPerPage;
	}

	public void setQuestionsPerPage(Integer questionsPerPage) {
		this.questionsPerPage = questionsPerPage;
	}

	public DrivingCategory getDrivingCategory() {
		return drivingCategory;
	}

	public void setDrivingCategory(DrivingCategory drivingCategory) {
		this.drivingCategory = drivingCategory;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<EDTGrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Set<DrivingSchoolStudent> getDrivingSchoolMemberships() {
		return drivingSchoolMemberships;
	}

	public void setDrivingSchoolMemberships(Set<DrivingSchoolStudent> drivingSchoolMemberships) {
		this.drivingSchoolMemberships = drivingSchoolMemberships;
	}
	
	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Integer getLearnedQuestions() {
		return learnedQuestions;
	}

	public void setLearnedQuestions(Integer learnedQuestions) {
		this.learnedQuestions = learnedQuestions;
	}

	public Integer getTestedQuestions() {
		return testedQuestions;
	}

	public void setTestedQuestions(Integer testedQuestions) {
		this.testedQuestions = testedQuestions;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public SocialMediaService getSignInProvider() {
		return signInProvider;
	}

	public void setSignInProvider(SocialMediaService signInProvider) {
		this.signInProvider = signInProvider;
	}
	
	public Set<UserDrivingSchoolMembershipRequest> getMembershipRequest() {
		return membershipRequest;
	}

	public void setMembershipRequest(Set<UserDrivingSchoolMembershipRequest> membershipRequest) {
		this.membershipRequest = membershipRequest;
	}

	@Override
	public String toString() {

		return getUsername();
	}
	
	@Transient
	public String getNormalProfileImagePath() {

		return "/users/" + getId() + "/normalImage";
	}
	
	@Transient
	public String getSmallProfileImagePath() {

		return "/users/" + getId() + "/smallImage";
	}

}
