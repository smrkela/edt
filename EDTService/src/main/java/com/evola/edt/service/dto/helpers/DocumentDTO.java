package com.evola.edt.service.dto.helpers;

import java.util.Date;

import com.evola.edt.model.dto.UserDTO;

public class DocumentDTO {
	
	private String id;
	private String parentId;
	private String path;
	private String name;
	private String description;
	private UserDTO author;
	private Date creationDate;
	private int numOfDocuments;
	private Double size;
	private boolean isMisc = false;
	
	//id objekta kojem dokument pripada (npr id skole)
	private String relatedId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UserDTO getAuthor() {
		return author;
	}
	public void setAuthor(UserDTO author) {
		this.author = author;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public int getNumOfDocuments() {
		return numOfDocuments;
	}
	public void setNumOfDocuments(int numOfDocuments) {
		this.numOfDocuments = numOfDocuments;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public String getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(String relatedId) {
		this.relatedId = relatedId;
	}
	
	public boolean isNew() {

		return id == null;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public boolean isMisc() {
		return isMisc;
	}
	public void setMisc(boolean isMisc) {
		this.isMisc = isMisc;
	}
	
	

}
