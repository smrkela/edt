package com.evola.edt.service.dto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@XmlRootElement(name = "daily-test-list")
public class AllDailyTestsDTO {
	
	private List<AllDailyTestsMonthDTO> months = new LinkedList<AllDailyTestsMonthDTO>();
	private Integer totalTestsByCurrentUser;
	private Integer totalPointsByCurrentUser;
	private Integer totalUsers;
	private Integer totalTestsDone;
	
	@XmlElement(name="month")
	public List<AllDailyTestsMonthDTO> getMonths() {
		return months;
	}
	public void setMonths(List<AllDailyTestsMonthDTO> tests) {
		this.months = tests;
	}
	
	@XmlAttribute(name="total-tests")
	public Integer getTotalTestsByCurrentUser() {
		return totalTestsByCurrentUser;
	}
	public void setTotalTestsByCurrentUser(Integer totalTestsByCurrentUser) {
		this.totalTestsByCurrentUser = totalTestsByCurrentUser;
	}
	
	@XmlAttribute(name="total-points")
	public Integer getTotalPointsByCurrentUser() {
		return totalPointsByCurrentUser;
	}
	public void setTotalPointsByCurrentUser(Integer totalPointsByCurrentUser) {
		this.totalPointsByCurrentUser = totalPointsByCurrentUser;
	}
	
	public void addTest(AllDailyTestsTestDTO testDto) {

		AllDailyTestsMonthDTO monthDTO = getMonth(testDto.getDate());
		
		if(monthDTO == null){
			monthDTO = createMonth(testDto.getDate());
			months.add(monthDTO);
		}
	
		monthDTO.getTests().add(testDto);
	}
	
	private AllDailyTestsMonthDTO createMonth(Date date) {

		AllDailyTestsMonthDTO month = new AllDailyTestsMonthDTO();
		
		DateTime dateTime = new DateTime(date);
		
		month.setYear(dateTime.getYear());
		month.setMonth(dateTime.getMonthOfYear());
		
		return month;
	}
	
	private AllDailyTestsMonthDTO getMonth(Date date) {

		for(AllDailyTestsMonthDTO month : months){
			
			if(month.isForDate(date))
				return month;
		}
		
		return null;
	}
	
	@XmlAttribute(name="total-users")
	public Integer getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}
	
	@XmlAttribute(name="total-tests-done")
	public Integer getTotalTestsDone() {
		return totalTestsDone;
	}
	public void setTotalTestsDone(Integer totalTestsDone) {
		this.totalTestsDone = totalTestsDone;
	}
}
