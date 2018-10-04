/**
 * 
 */
package com.evola.edt.service.dto;

import java.util.List;

import com.evola.edt.model.dto.DrivingSchoolDTO;

/**
 * @author Daci, 20.01.2014.
 *
 */
public class DrivingSchoolsDTO {
	private List<DrivingSchoolDTO> schools;
	private Boolean isFirstPage;
	private Boolean isLastPage;
	private long totalSchools;
	private int totalPages;
	
	
	
	public List<DrivingSchoolDTO> getSchools() {
		return schools;
	}
	
	public void setSchools(List<DrivingSchoolDTO> schools) {
		this.schools = schools;
	}
	
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
	
	public long getTotalSchools() {
		return totalSchools;
	}
	
	public void setTotalSchools(long totalSchools) {
		this.totalSchools = totalSchools;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
