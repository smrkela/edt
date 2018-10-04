package com.evola.edt.service.dto;

import java.util.ArrayList;
import java.util.List;

public class UploadImagesDTO {
	
	private String relatedId;
	private String id;
	private List<AttachmentDTO> attachments = new ArrayList<AttachmentDTO>();
	
	public String getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(String relatedId) {
		this.relatedId = relatedId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

}
