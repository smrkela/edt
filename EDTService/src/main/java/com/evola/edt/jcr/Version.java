package com.evola.edt.jcr;

import java.util.Calendar;

/**
 * @author nikola
 * 
 */
public class Version {

	private Integer revision;
	private Calendar date;
	private String comment;
	private String author;

	public Version(Integer revision, Calendar date, String comment, String author) {
		super();
		this.revision = revision;
		this.date = date;
		this.comment = comment;
		this.author = author;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
