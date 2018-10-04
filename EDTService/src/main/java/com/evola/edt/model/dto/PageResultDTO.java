package com.evola.edt.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.evola.edt.model.BaseEntity;

/**
 * @author Nikola 19.05.2013.
 * 
 */
@XmlRootElement
@XmlSeeAlso(PageDTO.class)
public class PageResultDTO<T extends BaseDTO> {

	private Integer number;
	private Integer numberOfElements;
	private Long totalElements;
	private Integer totalPages;
	private Boolean hasNextPage;
	private Boolean hasPreviousPage;

	private Boolean firstPage;
	private Boolean lastPage;

	private List<T> dtos = new ArrayList<T>();

	@XmlAttribute
	public Boolean getHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(Boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	@XmlAttribute
	public Boolean getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(Boolean firstPage) {
		this.firstPage = firstPage;
	}

	@XmlAttribute
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@XmlAttribute
	public Integer getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(Integer numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	@XmlAttribute
	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	@XmlAttribute
	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	@XmlAttribute
	public Boolean getHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(Boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	@XmlElement
	public List<T> getDtos() {
		return dtos;
	}

	@XmlAttribute
	public Boolean getLastPage() {
		return lastPage;
	}

	public void setLastPage(Boolean lastPage) {
		this.lastPage = lastPage;
	}

	public void setDtos(List<T> dtos) {
		this.dtos = dtos;
	}

}
