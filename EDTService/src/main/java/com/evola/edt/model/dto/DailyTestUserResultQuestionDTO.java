/**
 * 
 */
package com.evola.edt.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.evola.edt.model.QuestionAnswer;

@XmlRootElement(name="question")
public class DailyTestUserResultQuestionDTO extends BaseDTO {

	private DailyTestUserResultDTO dailyTestUserResult;

	private List<QuestionAnswerDTO> userAnswers = new ArrayList<QuestionAnswerDTO>();

	private QuestionDTO question;

	private Boolean isCorrect = false;

	private Boolean hasAnswered = false;

	@XmlElement(name="user-result")
	public DailyTestUserResultDTO getDailyTestUserResult() {
		return dailyTestUserResult;
	}

	public void setDailyTestUserResult(DailyTestUserResultDTO dailyTestUserResult) {
		this.dailyTestUserResult = dailyTestUserResult;
	}

	@XmlElement(name="answer")
	public List<QuestionAnswerDTO> getUserAnswers() {
		return userAnswers;
	}

	public void setUserAnswers(List<QuestionAnswerDTO> userAnswers) {
		this.userAnswers = userAnswers;
	}

	@XmlElement(name="question")
	public QuestionDTO getQuestion() {
		return question;
	}

	public void setQuestion(QuestionDTO question) {
		this.question = question;
	}

	@XmlAttribute(name="is-correct")
	public Boolean getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	@XmlAttribute(name="has-answered")
	public Boolean getHasAnswered() {
		return hasAnswered;
	}

	public void setHasAnswered(Boolean hasAnswered) {
		this.hasAnswered = hasAnswered;
	}
	
	public boolean isCorrectlyAnswered(long answerId) {

		// nalazimo odgovor na ovo pitanje i gledamo da li je korisnik odgovorio i tacno odgovorio

		boolean isCorrect = false;

		for (QuestionAnswerDTO answerDTO : userAnswers) {

			if (answerDTO.getId().equals(answerId)) {

				isCorrect =answerDTO.getCorrect(); // answerDTO.getUserSelected() != null && answerDTO.getUserSelected() && answerDTO.getCorrect();
			}
		}

		return isCorrect;
	}
	
	public boolean isIncorrectlyAnswered(long answerId) {

		// nalazimo odgovor na ovo pitanje i gledamo da li je korisnik odgovorio i netacno odgovorio

		boolean isIncorrect = false;

		for (QuestionAnswerDTO answerDTO : userAnswers) {

			if (answerDTO.getId().equals(answerId)) {

				isIncorrect = !answerDTO.getCorrect();//answerDTO.getUserSelected() != null && answerDTO.getUserSelected() && !answerDTO.getCorrect();
			}
		}

		return isIncorrect;
	}

}
