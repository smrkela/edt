package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.DrivingCategoryDTO;
import com.evola.edt.model.dto.UserDTO;

@XmlRootElement(name="user-profile")
public class UserProfileDTO {

	private UserDTO user;
	private List<DrivingCategoryDTO> drivingCategories;
	
	@XmlElement(name="user")
	public UserDTO getUser() {
		return user;
	}
	
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	@XmlElement(name="driving-category")
	public List<DrivingCategoryDTO> getDrivingCategories() {
		return drivingCategories;
	}
	
	public void setDrivingCategories(List<DrivingCategoryDTO> drivingCategories) {
		this.drivingCategories = drivingCategories;
	}


}
