package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.DailyTestUserResultDTO;

@XmlRootElement(name = "daily-test-overview")
public class MobileDailyTestOverviewDTO {

	private boolean isTodayTestDone;
	private int totalTestsDone;
	private int totalTests;
	private int totalPointsEarned;
	private int todayTestPoints;
	private int todayTestPosition;
	private int currentPosition;
	private int totalTestUsers;
	private int totalTodayTestUsers;
	private List<MobileDailyTestUserResultDTO> todayResults;
	private String todayTestId;
	
	@XmlAttribute(name="is-today-test-done")
	public boolean isTodayTestDone() {
		return isTodayTestDone;
	}
	public void setTodayTestDone(boolean isTodayTestDone) {
		this.isTodayTestDone = isTodayTestDone;
	}
	
	@XmlAttribute(name="total-tests-done")
	public int getTotalTestsDone() {
		return totalTestsDone;
	}
	public void setTotalTestsDone(int totalTestsDone) {
		this.totalTestsDone = totalTestsDone;
	}
	
	@XmlAttribute(name="total-tests")
	public int getTotalTests() {
		return totalTests;
	}
	public void setTotalTests(int totalTests) {
		this.totalTests = totalTests;
	}
	
	@XmlAttribute(name="total-points-earned")
	public int getTotalPointsEarned() {
		return totalPointsEarned;
	}
	public void setTotalPointsEarned(int totalPointsEarned) {
		this.totalPointsEarned = totalPointsEarned;
	}
	
	@XmlAttribute(name="today-test-points")
	public int getTodayTestPoints() {
		return todayTestPoints;
	}
	public void setTodayTestPoints(int todayTestPoints) {
		this.todayTestPoints = todayTestPoints;
	}
	
	@XmlAttribute(name="current-position")
	public int getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	@XmlAttribute(name="total-test-users")
	public int getTotalTestUsers() {
		return totalTestUsers;
	}
	public void setTotalTestUsers(int totalTestUsers) {
		this.totalTestUsers = totalTestUsers;
	}
	
	@XmlAttribute(name="today-test-position")
	public int getTodayTestPosition() {
		return todayTestPosition;
	}
	public void setTodayTestPosition(int todayTestPosition) {
		this.todayTestPosition = todayTestPosition;
	}
	
	@XmlAttribute(name="total-today-test-users")
	public int getTotalTodayTestUsers() {
		return totalTodayTestUsers;
	}
	public void setTotalTodayTestUsers(int totalTodayTestUsers) {
		this.totalTodayTestUsers = totalTodayTestUsers;
	}
	
	@XmlElement(name="today-result")
	public void setTodayResults(List<MobileDailyTestUserResultDTO> results) {

		todayResults = results;
	}
	public List<MobileDailyTestUserResultDTO> getTodayResults(){
		
		return todayResults;
	}
	
	@XmlAttribute(name="today-test-id")
	public String getTodayTestId() {
		return todayTestId;
	}
	public void setTodayTestId(String todayTestId) {
		this.todayTestId = todayTestId;
	}

}
