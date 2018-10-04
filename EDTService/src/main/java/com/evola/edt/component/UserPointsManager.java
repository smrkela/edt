package com.evola.edt.component;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.evola.edt.model.User;
import com.evola.edt.repository.UserQuestionStatLearnRepository;
import com.evola.edt.repository.UserQuestionStatTestRepository;
import com.evola.edt.repository.UserRepository;

@Component
public class UserPointsManager {

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserQuestionStatLearnRepository learnRepository;

	@Inject
	private UserQuestionStatTestRepository testRepository;

	public void questionLearned(User user) {

		Integer learnedQuestions = user.getLearnedQuestions();

		if (learnedQuestions == null)
			learnedQuestions = 0;

		learnedQuestions++;

		Integer points = user.getPoints();

		if (points == null)
			points = 0;

		points++;

		user.setLearnedQuestions(learnedQuestions);
		user.setPoints(points);
	}

	public void questionTested(User user, boolean isCorrect) {

		// ako odgovor nije tacan onda ne radimo osvezavanje
		if (isCorrect == false)
			return;

		Integer testedQuestions = user.getTestedQuestions();

		if (testedQuestions == null)
			testedQuestions = 0;

		testedQuestions++;

		Integer points = user.getPoints();

		if (points == null)
			points = 0;

		points++;

		user.setTestedQuestions(testedQuestions);
		user.setPoints(points);
	}

	public void questionTestedAndUpdated(User user, boolean isCorrect, boolean wasCorrect) {

		// ako je odgovor bio netacan i sada je netacan onda ne radimo nista
		if (isCorrect == false && wasCorrect == false)
			return;

		// ako je odgovor bio tacan i sada je tacan ne radimo nista
		if (isCorrect == true && wasCorrect == true)
			return;

		int delta = 1;

		// ako je odgovor bio netacan a sada je tacan onda povecavamo broj poena
		if (wasCorrect == false && isCorrect == true)
			delta = 1;

		// ako je odgovor bio tacan a sada je netacan onda smanjujemo broj poena
		if (wasCorrect == true && isCorrect == false)
			delta = -1;

		Integer testedQuestions = user.getTestedQuestions();

		if (testedQuestions == null)
			testedQuestions = 0;

		testedQuestions += delta;

		Integer points = user.getPoints();

		if (points == null)
			points = 0;

		points += delta;

		user.setTestedQuestions(testedQuestions);
		user.setPoints(points);
	}

	public void updateAllUsers() {

		List<User> all = userRepository.findAll();

		for (User user : all) {

			Long allLearns = learnRepository.countAllLearns(user);
			Long allCorrectTests = testRepository.countAllCorrectTests(user);

			int learns = allLearns != null ? allLearns.intValue() : 0;
			int tests = allCorrectTests != null ? allCorrectTests.intValue() : 0;

			int points = learns + tests;

			user.setLearnedQuestions(learns);
			user.setTestedQuestions(tests);
			user.setPoints(points);
		}
	}
}
