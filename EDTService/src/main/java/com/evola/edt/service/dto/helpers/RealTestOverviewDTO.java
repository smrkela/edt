package com.evola.edt.service.dto.helpers;

import java.util.List;

public class RealTestOverviewDTO {

	private List<RealTestOverviewGroupDTO> groups;
	private Integer totalUsers = 0;
	private Integer totalTestsDone = 0;
	private Integer totalTestsByCurrentUser = 0;
	private Integer totalPassedTestsByCurrentUser = 0;

	public List<RealTestOverviewGroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<RealTestOverviewGroupDTO> groups) {
		this.groups = groups;
	}

	public Integer getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}

	public Integer getTotalTestsDone() {
		return totalTestsDone;
	}

	public void setTotalTestsDone(Integer totalTestsDone) {
		this.totalTestsDone = totalTestsDone;
	}

	public Integer getTotalTestsByCurrentUser() {
		return totalTestsByCurrentUser;
	}

	public void setTotalTestsByCurrentUser(Integer totalTestsByCurrentUser) {
		this.totalTestsByCurrentUser = totalTestsByCurrentUser;
	}

	public Integer getTotalPassedTestsByCurrentUser() {
		return totalPassedTestsByCurrentUser;
	}

	public void setTotalPassedTestsByCurrentUser(Integer totalPassedTestsByCurrentUser) {
		this.totalPassedTestsByCurrentUser = totalPassedTestsByCurrentUser;
	}

}
