package com.evola.edt.utils;

import com.evola.edt.model.User;

public class EDTUtils {

	public static Integer calculateUserLevel(User user) {

		Integer learnsL = user.getLearnedQuestions();
		Integer testsL = user.getTestedQuestions();

		int learns = learnsL != null ? learnsL.intValue() : 0;
		int tests = testsL != null ? testsL.intValue() : 0;

		int experiencePoints = learns + tests;
		int level = experiencePoints / EDTSettings.POINTS_PER_LEVEL + 1;
		
		return level;
	}

}
