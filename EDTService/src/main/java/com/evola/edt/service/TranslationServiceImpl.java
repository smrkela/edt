package com.evola.edt.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import com.evola.edt.model.ProblemCategory;
import com.evola.edt.model.Question;
import com.evola.edt.model.QuestionAnswer;
import com.evola.edt.model.QuestionCategory;
import com.evola.edt.repository.ProblemCategoryRepository;
import com.evola.edt.repository.QuestionAnswerRepository;
import com.evola.edt.repository.QuestionCategoryRepository;
import com.evola.edt.repository.QuestionRepository;
import com.evola.edt.utils.LatCyrConverter;
import com.evola.edt.utils.LatCyrConverter.ConvertType;

@Named
public class TranslationServiceImpl implements TranslationService {

	@Inject
	private QuestionRepository repositoryQuestion;

	@Inject
	private ProblemCategoryRepository repositoryProblemCategory;

	@Inject
	private QuestionAnswerRepository repositoryQuestionAnswer;

	@Inject
	private QuestionCategoryRepository repositoryQuestionCategory;

	@Override
	@Transactional
	public Boolean translateAll(String pwd) {

		if (!"evola".equals(pwd))
			return false;

		// prevodimo:
		// -question
		// -problemcategory
		// -questionanswer
		// -questioncategory

		LatCyrConverter converter = new LatCyrConverter();
		ConvertType cType = ConvertType.CYR2LAT;

		Iterable<Question> questions = repositoryQuestion.findAll();

		for (Question q : questions) {

			q.setText(converter.convertText(q.getText(), cType));
			q.setRemark(converter.convertText(q.getRemark(), cType));
		}

		Iterable<ProblemCategory> problemCategories = repositoryProblemCategory
				.findAll();

		for (ProblemCategory problem : problemCategories) {

			problem.setName(converter.convertText(problem.getName(), cType));
			problem.setDescription(converter.convertText(
					problem.getDescription(), cType));
		}

		Iterable<QuestionAnswer> answers = repositoryQuestionAnswer.findAll();

		for (QuestionAnswer answer : answers) {

			answer.setText(converter.convertText(answer.getText(), cType));
		}

		Iterable<QuestionCategory> questionCategories = repositoryQuestionCategory
				.findAll();

		for (QuestionCategory questionCategory : questionCategories) {

			questionCategory.setName(converter.convertText(
					questionCategory.getName(), cType));

		}

		return true;
	}

}
