angular.module('edt.services').service('questionService', [ '$http', '$interval', 'uuidService', function($http, $interval, uuidService) {

	var self = this;

	this.learningSavedQuestions = {};
	this.learningSessionUUID = null;
	this.testingSavedQuestions = {};
	this.testingSessionUUID = null;
	this.isTestingMode = false;
	this.isInProgress = false;
	this.learningTimer = null;
	this.learningDelay = 1000; // milisekundi

	this.startLearning = function() {

		self.learningSavedQuestions = {};
		self.learningSessionUUID = uuidService.generate();
		self.isTestingMode = false;
		self.isInProgress = true;
	};

	this.startTesting = function() {

		self.testingSavedQuestions = {};
		self.testingSessionUUID = uuidService.generate();
		self.isTestingMode = true;
		self.isInProgress = true;
	};

	this.saveLearning = function(questionId) {

		if (!questionId)
			return;

		if (self.learningSavedQuestions.hasOwnProperty(questionId))
			return;

		// resetujemo timer ako je vec aktivan
		if (self.learningTimer) {

			$interval.cancel(self.learningTimer);
		}

		self.learningTimer = $interval(function() {

			// resetujemo timer da moze opet da se koristi
			$interval.cancel(self.learningTimer);

			$http.get('rest/statistics/saveLearning?questionId=' + questionId + '&sessionUid=' + self.learningSessionUUID).success(function(data, status, headers, config) {

				// ne radimo nista
			});

			// cuvamo medju sacuvanim pitanjima
			self.learningSavedQuestions[questionId] = questionId;

		}, self.learningDelay, 1);
	};

	this.saveTesting = function(questionId, isCorrect) {

		if (!questionId)
			return;

		var isUpdate = false;

		if (self.testingSavedQuestions.hasOwnProperty(questionId))
			isUpdate = true;

		$http.get('rest/statistics/saveTesting?questionId=' + questionId + '&isCorrect=' + isCorrect + '&isUpdate=' + isUpdate + '&sessionUid=' + self.testingSessionUUID).success(function(data, status, headers, config) {

			// ne radimo nista
		});

		// cuvamo medju sacuvanim pitanjima
		self.testingSavedQuestions[questionId] = questionId;
	};

} ]);