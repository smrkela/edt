package com.evola.edt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

@Entity
public class DrivingSchoolMark extends BaseEntity {

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull()
	private User author;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private DrivingSchool school;

	@Column
	@Size(max = 2000)
	@NotNull
	private String comment;

	@Column
	@NotNull
	private Integer mark;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public DrivingSchool getSchool() {
		return school;
	}

	public void setSchool(DrivingSchool school) {
		this.school = school;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	@AssertTrue(message = "Mark must be between 0 and 11")
	public Boolean getValidateMark() {
		return mark != null && mark > 0 && mark < 11;
	}
}