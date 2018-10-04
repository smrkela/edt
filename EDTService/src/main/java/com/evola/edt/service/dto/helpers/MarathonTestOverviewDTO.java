package com.evola.edt.service.dto.helpers;

import java.util.List;

public class MarathonTestOverviewDTO {

	private Integer totalUsers = 0;
	private Integer totalTestsDone = 0;
	private Integer totalTestsByCurrentUser = 0;
	private Integer totalCorrectness = 0;
	private Integer totalCorrectnessByCurrentUser = 0;
	private List<MarathonTestOverviewTestDTO> tests;
	
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
	public Integer getTotalCorrectness() {
		return totalCorrectness;
	}
	public void setTotalCorrectness(Integer totalCorrectness) {
		this.totalCorrectness = totalCorrectness;
	}
	public Integer getTotalCorrectnessByCurrentUser() {
		return totalCorrectnessByCurrentUser;
	}
	public void setTotalCorrectnessByCurrentUser(Integer totalCorrectnessByCurrentUser) {
		this.totalCorrectnessByCurrentUser = totalCorrectnessByCurrentUser;
	}
	public List<MarathonTestOverviewTestDTO> getTests() {
		return tests;
	}
	public void setTests(List<MarathonTestOverviewTestDTO> tests) {
		this.tests = tests;
	}
	
}
