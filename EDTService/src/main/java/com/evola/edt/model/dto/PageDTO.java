package com.evola.edt.model.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 14.05.2013.
 * 
 */
@XmlRootElement
public class PageDTO extends BaseDTO {
	private String title;
	private String content;
	private String smallPreview;
	private String uniqueName;
	private UserDTO authorDTO;
	private Date creationDate;
	private String smallPreviewImage;
	private PageCategoryDTO categoryDTO;
	private String normalPreview;
	private String normalPreviewImage;
	private int numOfComments = 0;
	
	/**
	 * @author Daci, 06.03.2014.
	 */
	private Integer orderIndex;
	
	
	
	

	public PageDTO() {
		super();
	}

	public PageDTO(Long id, String title, String content) {
		super(id);
		this.title = title;
		this.content = content;
	}

	@XmlElement
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@XmlElement
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement
	public PageCategoryDTO getCategoryDTO() {
		return categoryDTO;
	}

	public void setCategoryDTO(PageCategoryDTO categoryDTO) {
		this.categoryDTO = categoryDTO;
	}

	@XmlElement
	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	@XmlElement
	public UserDTO getAuthorDTO() {
		return authorDTO;
	}

	public void setAuthorDTO(UserDTO author) {
		this.authorDTO = author;
	}

	@XmlElement
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@XmlElement
	public String getSmallPreview() {
		return smallPreview;
	}

	public void setSmallPreview(String smallPreview) {
		this.smallPreview = smallPreview;
	}

	@XmlElement
	public String getSmallPreviewImage() {
		return smallPreviewImage;
	}

	public void setSmallPreviewImage(String smallPreviewImage) {
		this.smallPreviewImage = smallPreviewImage;
	}

	@XmlElement
	public String getNormalPreview() {
		return normalPreview;
	}

	public void setNormalPreview(String normalPreview) {
		this.normalPreview = normalPreview;
	}

	@XmlElement
	public String getNormalPreviewImage() {
		return normalPreviewImage;
	}

	public void setNormalPreviewImage(String normalPreviewImage) {
		this.normalPreviewImage = normalPreviewImage;
	}

	@XmlElement
	public int getNumOfComments() {
		return numOfComments;
	}

	public void setNumOfComments(int numOfComments) {
		this.numOfComments = numOfComments;
	}

	public String getSmallImagePath() {

		return "/pages/" + getId() + "/smallPreview";
	}

	public String getNormalImagePath() {

		return "/pages/" + getId() + "/normalPreview";
	}
	
	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
	
}
