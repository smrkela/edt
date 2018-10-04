package com.evola.edt.service.dto;

import java.util.List;

import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;

public class DrivingSchoolNotificationsDTO {
	
	private List<DrivingSchoolNotificationDTO> notifications;
	private Boolean isFirstPage;
	private Boolean isLastPage;

	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public Boolean getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public List<DrivingSchoolNotificationDTO> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<DrivingSchoolNotificationDTO> notifications) {
		this.notifications = notifications;
	}

}
