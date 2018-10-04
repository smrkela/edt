package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "daily-test-top-list")
public class MobileDailyTestTopListDTO {
	
	private List<MobileDailyTestUserResultDTO> users;
	private Integer orderNumber;
	private Boolean isUserInTopList;
	
	@XmlElement(name="user")
	public List<MobileDailyTestUserResultDTO> getUsers() {
		return users;
	}
	public void setUsers(List<MobileDailyTestUserResultDTO> users) {
		this.users = users;
	}
	
	@XmlAttribute(name="order-number")
	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	@XmlAttribute(name="is-user-in-top-list")
	public Boolean getIsUserInTopList() {
		return isUserInTopList;
	}
	public void setIsUserInTopList(Boolean isUserInTopList) {
		this.isUserInTopList = isUserInTopList;
	}

}
