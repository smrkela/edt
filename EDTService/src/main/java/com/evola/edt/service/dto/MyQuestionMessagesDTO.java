package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="my-question-messages")
public class MyQuestionMessagesDTO {
	
	private List<MyQuestionMessageDTO> messages;
	private Integer page;
	private Integer totalPages;
	private Integer pageSize;
	private Integer totalMessages;

	@XmlElement(name="question-message")
	public List<MyQuestionMessageDTO> getMessages() {
		return messages;
	}

	public void setMessages(List<MyQuestionMessageDTO> messages) {
		this.messages = messages;
	}

	@XmlAttribute(name="page")
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	@XmlAttribute(name="total-pages")
	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	@XmlAttribute(name="page-size")
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@XmlAttribute(name="total-messages")
	public Integer getTotalMessages() {
		return totalMessages;
	}

	public void setTotalMessages(Integer totalMessages) {
		this.totalMessages = totalMessages;
	}


}
