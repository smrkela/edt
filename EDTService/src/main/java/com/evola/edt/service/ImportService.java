package com.evola.edt.service;

public interface ImportService {

	/**
	 * @author Nikola 03.04.2013.
	 * @return
	 */
	public abstract void importQuestions(String questionsXMLString);

	public abstract void importQuestionCategories(String questionCategoriesXML);

	public abstract void importDrivingCategories(String drivingCategoriesXML);

}