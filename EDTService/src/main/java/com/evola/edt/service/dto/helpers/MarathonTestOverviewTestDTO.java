package com.evola.edt.service.dto.helpers;

import com.evola.edt.utils.DateUtils;

public class MarathonTestOverviewTestDTO {

	private Long id;
	private Long userId;
	private String userName;
	private String profileImagePath;
	private Integer totalQuestions;
	private Integer correctQuestions;
	private Integer wrongQuestions;
	private Integer timeTaken;
	private Integer points;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getTotalQuestions() {
		return totalQuestions;
	}
	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	public Integer getCorrectQuestions() {
		return correctQuestions;
	}
	public void setCorrectQuestions(Integer correctQuestions) {
		this.correctQuestions = correctQuestions;
	}
	public Integer getWrongQuestions() {
		return wrongQuestions;
	}
	public void setWrongQuestions(Integer wrongQuestions) {
		this.wrongQuestions = wrongQuestions;
	}
	public Integer getTimeTaken() {
		return timeTaken;
	}
	public void setTimeTaken(Integer timeTaken) {
		this.timeTaken = timeTaken;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getTimeTakenString() {

		return DateUtils.getTimeTakenString(timeTaken, true);
	}
	
	public Integer getCorrectness(){
		
		int correctness = 0;
		
		if(totalQuestions > 0)
			correctness = correctQuestions * 100 / totalQuestions;
		
		return correctness;
	}
	public String getProfileImagePath() {
		return profileImagePath;
	}
	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	
}
