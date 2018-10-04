package com.evola.edt.service.dto.helpers;

public class RealTestOverviewTestDTO {

	private Long id;
	private String name;
	private Long totalResults;
	private Boolean hasUserTakenTest = false;
	private Long totalUserTests;
	private Long totalUserPassedTests;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(Long totalResults) {
		this.totalResults = totalResults;
	}
	public Boolean getHasUserTakenTest() {
		return hasUserTakenTest;
	}
	public void setHasUserTakenTest(Boolean hasUserTakenTest) {
		this.hasUserTakenTest = hasUserTakenTest;
	}
	public Long getTotalUserTests() {
		return totalUserTests;
	}
	public void setTotalUserTests(Long totalUserTests) {
		this.totalUserTests = totalUserTests;
	}
	public Long getTotalUserPassedTests() {
		return totalUserPassedTests;
	}
	public void setTotalUserPassedTests(Long totalUserPassedTests) {
		this.totalUserPassedTests = totalUserPassedTests;
	}
	
}
