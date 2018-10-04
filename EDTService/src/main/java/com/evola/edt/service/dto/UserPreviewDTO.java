package com.evola.edt.service.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.BaseDTO;
import com.evola.edt.model.dto.DrivingSchoolDTO;

@XmlRootElement(name="userPreview")
public class UserPreviewDTO extends BaseDTO {

	private long userId;
	private String username;
	private Date registerDate;
	private String drivingCategoryName;
	private Date learningStartDate;
	private int numOfDailyTestsDone;
	private List<DrivingSchoolDTO> drivingSchools;
	
	public UserPreviewDTO() {
		super();
	}

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public String getDrivingCategoryName() {
		return drivingCategoryName;
	}
	public void setDrivingCategoryName(String drivingCategoryName) {
		this.drivingCategoryName = drivingCategoryName;
	}
	public Date getLearningStartDate() {
		return learningStartDate;
	}
	public void setLearningStartDate(Date learningStartDate) {
		this.learningStartDate = learningStartDate;
	}
	public int getNumOfDailyTestsDone() {
		return numOfDailyTestsDone;
	}
	public void setNumOfDailyTestsDone(int numOfDailyTestsDone) {
		this.numOfDailyTestsDone = numOfDailyTestsDone;
	}
	public List<DrivingSchoolDTO> getDrivingSchools() {
		return drivingSchools;
	}
	public void setDrivingSchools(List<DrivingSchoolDTO> drivingSchools) {
		this.drivingSchools = drivingSchools;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
