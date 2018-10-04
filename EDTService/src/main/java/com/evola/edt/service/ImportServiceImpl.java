package com.evola.edt.service;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.evola.edt.model.DrivingCategory;
import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionAnswer;
import com.evola.edt.model.QuestionCategory;
import com.evola.edt.model.QuestionImage;
import com.evola.edt.repository.DrivingCategoryRepository;
import com.evola.edt.repository.QuestionCategoryRepository;
import com.evola.edt.repository.QuestionRepository;
import com.evola.edt.service.exception.EDTServiceException;
import com.evola.edt.web.security.UserCredentialsManager;

/**
 * @author Nikola 03.04.2013.
 * 
 */
@Named
public class ImportServiceImpl implements ImportService {

	@Inject
	private QuestionRepository questionRepository;

	@Inject
	private QuestionCategoryRepository questionCategoryRepository;

	@Inject
	private DrivingCategoryRepository drivingCategoryRepository;
	
	@Inject
	private UserCredentialsManager credentialsManager;

	@Transactional
	public void importQuestions(String questionsXMLString) {
		
		credentialsManager.checkSystemAdministration();

		Document xml = buildDocument(questionsXMLString);

		NodeList questionNodes = xml.getElementsByTagName("question");

		int skippedQuestions = 0;
		int importedQuestions = 0;

		for (int i = 0; i < questionNodes.getLength(); i++) {

			Node questionNode = questionNodes.item(i);

			if (questionNode.getNodeType() == Node.ELEMENT_NODE) {

				Element questionElement = (Element) questionNode;

				String questionId = questionElement.getAttribute("id");

				if (questionAllreadyExists(questionId)) {
					skippedQuestions++;
					continue;
				}

				String numberString = questionElement.getAttribute("number");
				String text = questionElement.getAttribute("text");
				String remark = questionElement.getAttribute("remark");
				String pointsString = questionElement.getAttribute("points");
				Integer points = Integer.parseInt(pointsString);
				String helpUrl = questionElement.getAttribute("help-url");

				Question q = new Question();

				q.setQuestionId(questionId);
				q.setNumber(numberString);
				q.setText(text);
				q.setRemark(remark);
				q.setPoints(points);
				q.setHelpUrl(helpUrl);

				Set<QuestionCategory> questionCategories = getQuestionCategories(questionElement);
				q.setQuestionCategories(questionCategories);

				Set<DrivingCategory> drivingCategories = getDrivingCategories(questionElement);
				q.setDrivingCategories(drivingCategories);

				Set<QuestionAnswer> questionAnswers = getQuestionAnswers(q,
						questionElement);
				q.setQuestionAnswers(questionAnswers);

				Set<QuestionImage> questionImages = getQuestionImages(q,
						questionElement);
				q.setQuestionImages(questionImages);

				questionRepository.save(q);

				importedQuestions++;
			}
		}

	}

	private Document buildDocument(String questionsXMLString)
			throws EDTServiceException {
		Document xml;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();

			xml = builder.parse(new ByteArrayInputStream(questionsXMLString
					.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new EDTServiceException(e);
		}
		return xml;
	}

	@Transactional
	public void importQuestionCategories(String categoriesXMLString) {
		
		credentialsManager.checkSystemAdministration();

		Document xml = buildDocument(categoriesXMLString);

		NodeList categoryNodes = xml.getElementsByTagName("question-category");

		int skippedCategories = 0;
		int importedCategories = 0;

		for (int i = 0; i < categoryNodes.getLength(); i++) {

			Node categoryNode = categoryNodes.item(i);

			if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {

				Element categoryElement = (Element) categoryNode;

				String categoryId = categoryElement.getAttribute("id");

				if (questionCategoryRepository.findByCategoryId(categoryId) != null) {
					skippedCategories++;
					continue;
				}

				String name = categoryElement.getAttribute("name");

				QuestionCategory q = new QuestionCategory();

				q.setCategoryId(categoryId);
				q.setName(name);

				questionCategoryRepository.save(q);

				importedCategories++;
			}
		}

	}

	@Transactional
	public void importDrivingCategories(String categoriesXMLString) {

		credentialsManager.checkSystemAdministration();
		
		Document xml = buildDocument(categoriesXMLString);

		NodeList categoryNodes = xml.getElementsByTagName("driving-category");

		int skippedCategories = 0;
		int importedCategories = 0;

		for (int i = 0; i < categoryNodes.getLength(); i++) {

			Node categoryNode = categoryNodes.item(i);

			if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {

				Element categoryElement = (Element) categoryNode;

				String categoryId = categoryElement.getAttribute("id");

				if (drivingCategoryRepository.findByCategoryId(categoryId) != null) {
					skippedCategories++;
					continue;
				}

				String name = categoryElement.getAttribute("name");

				DrivingCategory q = new DrivingCategory();

				q.setCategoryId(categoryId);
				q.setName(name);

				drivingCategoryRepository.save(q);

				importedCategories++;
			}
		}

	}

	@Transactional(readOnly = true)
	private Set<QuestionCategory> getQuestionCategories(Element questionElement)
			throws EDTServiceException {
		
		credentialsManager.checkSystemAdministration();

		NodeList categoryRefNodes = questionElement
				.getElementsByTagName("question-category-ref");

		Set<QuestionCategory> categories = new HashSet<QuestionCategory>();

		for (int i = 0; i < categoryRefNodes.getLength(); i++) {

			Node categoryNode = categoryRefNodes.item(i);

			if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {

				Element categoryElement = (Element) categoryNode;

				String refValue = categoryElement.getAttribute("value");

				QuestionCategory questionCategory = questionCategoryRepository
						.findByCategoryId(refValue);

				if (questionCategory == null)
					throw new IllegalStateException(
							"Invalid question category ref: " + refValue);

				categories.add(questionCategory);
			}
		}

		return categories;
	}

	private Set<DrivingCategory> getDrivingCategories(Element questionElement)
			throws EDTServiceException {

		NodeList categoryRefNodes = questionElement
				.getElementsByTagName("driving-category-ref");

		Set<DrivingCategory> categories = new HashSet<DrivingCategory>();

		for (int i = 0; i < categoryRefNodes.getLength(); i++) {

			Node categoryNode = categoryRefNodes.item(i);

			if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {

				Element categoryElement = (Element) categoryNode;

				String refValue = categoryElement.getAttribute("value");

				DrivingCategory drivingCategory = drivingCategoryRepository
						.findByCategoryId(refValue);

				if (drivingCategory == null)
					throw new IllegalStateException(
							"Invalid driving category ref: " + refValue);

				categories.add(drivingCategory);
			}
		}

		return categories;
	}

	private Set<QuestionAnswer> getQuestionAnswers(Question question,
			Element questionElement) {

		NodeList questionAnswerNodes = questionElement
				.getElementsByTagName("question-answer");

		Set<QuestionAnswer> questionAnswers = new HashSet<QuestionAnswer>();
		
		int orderIndex = 1;

		for (int i = 0; i < questionAnswerNodes.getLength(); i++) {

			Node answerNode = questionAnswerNodes.item(i);

			if (answerNode.getNodeType() == Node.ELEMENT_NODE) {

				Element questionAnswerElement = (Element) answerNode;

				String text = questionAnswerElement.getAttribute("text");
				String correct = questionAnswerElement.getAttribute("correct");

				QuestionAnswer answer = new QuestionAnswer();
				answer.setText(text);
				answer.setQuestion(question);
				answer.setCorrect("true".equals(correct));
				answer.setOrderIndex(orderIndex++);

				questionAnswers.add(answer);
			}
		}

		if (questionAnswers.size() == 0)
			throw new IllegalStateException("Question "
					+ question.getQuestionId() + " doesn't have any answers!");

		return questionAnswers;
	}

	private Set<QuestionImage> getQuestionImages(Question question,
			Element questionElement) {

		NodeList questionImageNodes = questionElement
				.getElementsByTagName("question-image");

		Set<QuestionImage> questionImages = new HashSet<QuestionImage>();
		
		int orderIndex = 1;

		for (int i = 0; i < questionImageNodes.getLength(); i++) {

			Node imageNode = questionImageNodes.item(i);

			if (imageNode.getNodeType() == Node.ELEMENT_NODE) {

				Element questionImageElement = (Element) imageNode;

				String text = questionImageElement.getAttribute("text");
				String url = questionImageElement.getAttribute("url");

				QuestionImage image = new QuestionImage();
				image.setText(text);
				image.setUrl(url);
				image.setQuestion(question);
				image.setOrderIndex(orderIndex++);

				questionImages.add(image);
			}
		}

		return questionImages;
	}

	private boolean questionAllreadyExists(String questionId) {

		Question q = questionRepository.findByQuestionId(questionId);

		return q != null;
	}

}
