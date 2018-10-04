package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.dto.QuestionDTO;

@XmlRootElement(name="question-messages")
public class QuestionMessagesDTO {
	
	private List<QuestionMessageDTO> messages;
	private Boolean isAdministrator;
	private Integer totalPages;
	private Integer currentPageIndex;
	private Integer pageSize;
	private Integer totalMessages;
	private QuestionDTO question;

	@XmlElement(name="question-message")
	public List<QuestionMessageDTO> getMessages() {
		return messages;
	}

	public void setMessages(List<QuestionMessageDTO> messages) {
		this.messages = messages;
	}

	@XmlAttribute(name="is-administrator")
	public Boolean getIsAdministrator() {
		return isAdministrator;
	}

	public void setIsAdministrator(Boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}

	@XmlAttribute(name="total-pages")
	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	@XmlAttribute(name="current-page-index")
	public Integer getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(Integer currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
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

	@XmlElement(name="question")
	public QuestionDTO getQuestion() {
		return question;
	}

	public void setQuestion(QuestionDTO question) {
		this.question = question;
	}


}
