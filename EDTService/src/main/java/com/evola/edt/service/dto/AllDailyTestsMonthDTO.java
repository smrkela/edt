package com.evola.edt.service.dto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.evola.edt.utils.DateUtils;

@XmlRootElement(name="month")
public class AllDailyTestsMonthDTO {
	

	private List<AllDailyTestsTestDTO> tests = new LinkedList<AllDailyTestsTestDTO>();
	private int year;
	private int month;

	@XmlElement(name="test")
	public List<AllDailyTestsTestDTO> getTests() {
		return tests;
	}

	public void setTests(List<AllDailyTestsTestDTO> tests) {
		this.tests = tests;
	}

	@XmlAttribute(name="year")
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@XmlAttribute(name="month")
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public boolean isForDate(Date date) {

		DateTime dateTime = new DateTime(date);
		
		return dateTime.getYear() == year && dateTime.getMonthOfYear() == month;
	}
	

	public String getMonthName(){
		
		return DateUtils.getMonthName(month);
	}
	

}
