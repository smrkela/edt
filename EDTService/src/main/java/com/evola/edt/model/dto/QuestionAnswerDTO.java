package com.evola.edt.model.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@XmlRootElement(name="question-answer")
public class QuestionAnswerDTO extends BaseDTO {

	private QuestionDTO questionDTO;
	private String text;
	private Boolean correct;
	private Boolean userSelected;
	private String letter;
	private Integer orderIndex;

	public QuestionDTO getQuestionDTO() {
		return questionDTO;
	}

	public void setQuestionDTO(QuestionDTO questionDTO) {
		this.questionDTO = questionDTO;
	}

	@XmlAttribute(name="text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@XmlAttribute(name="correct")
	public Boolean getCorrect() {
		return correct;
	}

	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}

	@XmlAttribute(name="user-selected")
	public Boolean getUserSelected() {
		return userSelected;
	}

	public void setUserSelected(Boolean userSelected) {
		this.userSelected = userSelected;
	}

	@XmlAttribute(name="letter")
	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	@XmlAttribute(name="order-index")
	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

}
