package com.evola.edt.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Nikola 14.05.2013.
 * 
 */
@Entity
public class Page extends BaseEntity {
	
	@Column(length = 256)
	@Size(max = 256)
	private String title;
	
	@Column(columnDefinition = "text")
	private String content;
	
	@Column(length = 100, unique = true)
	@Size(max = 100)
	private String uniqueName;

	@Column(name="creationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@ManyToOne
	@NotNull
	private User author;
	
	@Column(length=2048)
	@Size(max = 2048)
	private String smallPreview;
	
	private String smallPreviewImage;
	
	@Column(length=8196)
	@Size(max = 8196)
	private String normalPreview;
	
	private String normalPreviewImage;
	
	@ManyToOne
	@NotNull
	private PageCategory category;
	
	//NEWS|INFORMATION|HELP
	@Column(nullable=false)
	private String pageType;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="page")
	private Set<PageComment> questionAnswers = new HashSet<PageComment>();
	
	/**
	 * @author Daci, 06.03.2014.
	 */
	private Integer orderIndex;
	
	

	Page() {
		// required by jpa
	}

	public Page(String title, String content, PageCategory pageCategory) {
		super();
		this.title = title;
		this.content = content;
		this.category = pageCategory;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public PageCategory getCategory() {
		return category;
	}

	public void setCategory(PageCategory category) {
		this.category = category;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getSmallPreview() {
		return smallPreview;
	}

	public void setSmallPreview(String smallPreview) {
		this.smallPreview = smallPreview;
	}

	public String getSmallPreviewImage() {
		return smallPreviewImage;
	}

	public void setSmallPreviewImage(String smallPreviewImage) {
		this.smallPreviewImage = smallPreviewImage;
	}

	public String getNormalPreview() {
		return normalPreview;
	}

	public void setNormalPreview(String normalPreview) {
		this.normalPreview = normalPreview;
	}

	public String getNormalPreviewImage() {
		return normalPreviewImage;
	}

	public void setNormalPreviewImage(String normalPreviewImage) {
		this.normalPreviewImage = normalPreviewImage;
	}

	public Set<PageComment> getQuestionAnswers() {
		return questionAnswers;
	}

	public void setQuestionAnswers(Set<PageComment> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	
	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

}
