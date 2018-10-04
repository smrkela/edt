package com.evola.edt.model.dto;

import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Daci, 23.04.2013.
 *
 */

@XmlRootElement(name="question-problem-report")
public class QuestionProblemReportDTO extends BaseDTO {

	private String className = "QuestionProblemReport";
	private Date reportingDate;
	private ProblemCategoryDTO problemCategoryDTO;
	private UserDTO userDTO;
	private QuestionDTO questionDTO;
	private String userComment;
	private boolean fixed;
	private Date fixDate;
	
	@XmlAttribute(name="class-name")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ProblemCategoryDTO getProblemCategoryDTO() {
		return problemCategoryDTO;
	}

	public void setProblemCategoryDTO(ProblemCategoryDTO problemCategoryDTO) {
		this.problemCategoryDTO = problemCategoryDTO;
	}

	@XmlAttribute(name="reporting-date")
	public Date getReportingDate() {
		return reportingDate;
	}
	
	public void setReportingDate(Date reportingDate) {
		this.reportingDate = reportingDate;
	}
	
	public UserDTO getUserDTO() {
		return userDTO;
	}
	
	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
	public QuestionDTO getQuestionDTO() {
		return questionDTO;
	}
	
	public void setQuestionDTO(QuestionDTO questionDTO) {
		this.questionDTO = questionDTO;
	}
	
	@XmlAttribute(name="user-comment")
	public String getUserComment() {
		return userComment;
	}
	
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	
	@XmlAttribute(name="fixed")
	public boolean isFixed() {
		return fixed;
	}
	
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	@XmlAttribute(name="fix-date")
	public Date getFixDate() {
		return fixDate;
	}

	public void setFixDate(Date fixDate) {
		this.fixDate = fixDate;
	}
}