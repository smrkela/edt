package com.evola.edt.jcr;

import java.io.InputStream;
import java.util.Calendar;

import org.springframework.util.Assert;

/**
 * @author Nikola
 * 
 */
public class Metadata {
	private Calendar createdOn;
	private Calendar updatedOn;
	private String createdBy;
	private String updatedBy;
	private String description;
	private Double size;
	private String extension;
	private String client;
	private String mimeType;
	private InputStream preview;
	private InputStream filePreview;

	/**
	 * @author Nikola
	 */
	public Metadata() {
		super();
	}

	/**
	 * @author Nikola
	 */
	public Metadata(String mimeType, Calendar createdOn, String createdBy) {
		super();
		Assert.notNull(mimeType);
		Assert.notNull(createdOn);
		Assert.notNull(createdBy);
		this.createdOn = createdOn;
		this.createdBy = createdBy;
	}

	/**
	 * @author Nikola
	 */
	public Calendar getCreatedOn() {
		return createdOn;
	}

	/**
	 * @author Nikola
	 */
	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @author Nikola
	 */
	public Calendar getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * @author Nikola
	 */
	public void setUpdatedOn(Calendar updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * @author Nikola
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @author Nikola
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @author Nikola
	 */

	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @author Nikola
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public InputStream getPreview() {
		return preview;
	}

	public void setPreview(InputStream preview) {
		this.preview = preview;
	}

	public InputStream getFilePreview() {
		return filePreview;
	}

	public void setFilePreview(InputStream filePreview) {
		this.filePreview = filePreview;
	}

}
