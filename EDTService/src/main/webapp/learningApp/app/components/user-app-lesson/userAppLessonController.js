// This controller loads categories and stats of categories,
// Also it loads user stats

angular.module('edt.controllers').controller('userAppLessonController', [ '$scope', '$rootScope', '$stateParams', '$http', '$state', 'groupColorService', 'questionService', 'ngDialog', 'pageService',

function($scope, $rootScope, $stateParams, $http, $state, groupColorService, questionService, ngDialog, pageService) {

	$scope.groupId = $stateParams.groupId;
	$scope.lessonId = $stateParams.lessonId;
	$scope.type = $stateParams.type;
	$scope.isPartial = $stateParams.hasOwnProperty('isPartial') ? $stateParams.isPartial : false;
	$scope.isFavorite = $stateParams.hasOwnProperty('isFavorite') ? $stateParams.isFavorite : false;
	$scope.repeatCount = $stateParams.hasOwnProperty('repeatCount') ? $stateParams.repeatCount : false;

	// neke stavke setujemo u root jer nam trebaju sirom
	// aplikacije
	$rootScope.isTestingMode = $scope.type == 'provera';
	$rootScope.isUnconfirmed = true;

	if ($rootScope.isTestingMode)
		pageService.setTitle('Provera lekcije');
	else
		pageService.setTitle('Učenje lekcije');

	pageService.setNavigationId("HOME");

	$scope.title = "momenat...";
	$scope.currentQuestionIndex = 0;
	$scope.currentQuestionIndex = null;
	$scope.groupColor = groupColorService.getGroupColor($scope.groupId);

	// loading lessons
	$scope.loadQuestions = function() {
		$http.get('rest/learningApp/getLessonQuestions/' + $scope.groupId + '/' + $scope.lessonId).success(onQuestionsLoaded).error(onQuestionsLoadError);
	};

	$scope.loadPartial = function() {
		$http.get('rest/learningApp/getRemainingLessonQuestions/' + $scope.groupId + '/' + $scope.lessonId + '/' + $rootScope.isTestingMode + '/' + $scope.repeatCount).success(onQuestionsLoaded).error(onQuestionsLoadError);
	};

	$scope.loadFavorite = function() {
		$http.get('rest/learningApp/getFavoriteLessonQuestions/' + $scope.groupId + '/' + $scope.lessonId).success(onQuestionsLoaded).error(onQuestionsLoadError);
	};

	function onQuestionsLoaded(data, status, headers, config) {

		$scope.groupName = data["group-name"];
		$scope.lessonName = data["lesson-name"];
		$scope.removedQuestionsCount = data["removed-questions-count"];

		$scope.questions = data.question;

		// ubacujemo dodatne propertije u svako pitanje
		for ( var qIndex in $scope.questions) {

			var question = $scope.questions[qIndex];

			question.isUnconfirmed = true;
			question.isAnswered = false;

			// svakom odgovoru dodeljujemo da nije odgovoreno
			var answers = question['question-answer'];

			for ( var answerIndex in answers) {

				var answer = answers[answerIndex];

				answer.isUserSelected = false;
			}
		}

		$scope.currentQuestionIndex = 0;
		$scope.currentQuestion = $scope.getCurrentQuestion();

		// QuestionsParser.assignNumberOfCorrectAnswers(questions);

		// if (questions.length > 0)
		// {
		// selectedIndex=0;
		// currentQuestion=questions.getItemAt(selectedIndex)
		// as Question;

		// saveLearning();
		// }

		// isLoading=false;

		$scope.title = $scope.groupName + ' - ' + $scope.lessonName;

		if ($scope.type == 'ucenje')
			$scope.title += ' - učenje';
		else
			$scope.title += ' - provera';

		// ako je ovo ucenje, sacuvaj ucenje odmah
		if ($rootScope.isTestingMode == false)
			questionService.saveLearning($scope.currentQuestion.id);

		// ako je parcijalno ucenje ili ucenje omiljenih, izracunaj notifikaciju
		$scope.setLessonNotification();
	}

	function onQuestionsLoadError(data) {

		// nista
	}

	$scope.getCurrentQuestion = function() {

		var q = null;

		if ($scope.questions && $scope.questions.length > 0) {

			q = $scope.questions[$scope.currentQuestionIndex];

			$scope.currentQuestion = q;
		}

		return q;
	};

	$scope.nextQuestion = function() {

		$rootScope.isUnfonfirmed = true;

		if ($scope.currentQuestionIndex < $scope.questions.length - 1)
			$scope.currentQuestionIndex++;

		// schedule this learning
		questionService.saveLearning($scope.getCurrentQuestion().id);
	};

	$scope.previousQuestion = function() {

		if ($scope.currentQuestionIndex > 0)
			$scope.currentQuestionIndex--;
	};

	$scope.goBackToLessons = function() {

		$state.go('user-app-group', {
			groupId : $scope.groupId,
			selectedLessonId : $scope.lessonId
		});
	};

	$scope.markFavorite = function() {

		$http.get('rest/question/toggleFavorite?questionId=' + $scope.currentQuestion.id).success(function(data, status, headers, config) {

			$scope.currentQuestion['is-favorite'] = !$scope.currentQuestion['is-favorite'];

		}).error(function(data) {

		});
	};

	$scope.unmarkFavorite = $scope.markFavorite;

	$scope.finishLearning = function() {

		$state.go('user-app-group', {
			groupId : $scope.groupId
		});
	};

	$scope.closeTestResults = function() {

		$scope.resultsPopup.close();

		$state.go('user-app-group', {
			groupId : $scope.groupId
		});
	};

	$scope.finishTesting = function() {

		// belezimo rezultate testa
		var answeredQuestions = 0;
		var correctlyAnswered = 0;
		var incorrectlyAnswered = 0;
		var unansweredQuestions = []; // of questions

		for (var i = 0; i < $scope.questions.length; i++) {

			var question = $scope.questions[i];

			if (question.isAnswered) {

				answeredQuestions++;

				if (question.isCorrectlyAnswered)
					correctlyAnswered++;
				else
					incorrectlyAnswered++;
			} else {

				unansweredQuestions.push(question);
			}
		}

		$scope.totalQuestions = $scope.questions.length;
		$scope.answeredQuestions = answeredQuestions;
		$scope.correctlyAnswered = correctlyAnswered;
		$scope.incorrectlyAnswered = incorrectlyAnswered;

		setDescriptiveStatus();

		$scope.resultsPopup = ngDialog.open({
			template : 'learningApp/app/components/user-app-lesson/components/testResultsComponent.html',
			scope : $scope,
			className : 'ngdialog-theme-discussion',
			showClose : false,
			closeByEscape : false,
			closeByDocument : false
		});
	};

	$scope.confirmQuestion = function() {

		if ($scope.currentQuestion.isAnswered)
			$scope.currentQuestion.isUnconfirmed = false;
	};

	$scope.skipQuestion = function() {

		// ako preskacemo poslednje pitanje onda zavrsavamo test
		if ($scope.currentQuestionIndex == $scope.questions.length - 1) {

			$scope.finishTesting();
		} else {

			$scope.currentQuestionIndex++;
			$scope.currentQuestion.isUnconfirmed = true;
		}
	};

	$scope.setLessonNotification = function() {

		if (!$scope.isFavorite && !$scope.isPartial) {
			message = "";
		}

		var message = "";

		if ($scope.isFavorite) {

			message = "Prikazuju se samo pitanja označena kao omiljena.";
		} else {

			var typeMessagePart = $scope.type == 'ucenje' ? 'učena' : 'proveravana';

			if ($scope.repeatCount == 1)
				message = "Prikazuju se samo pitanja ove lekcije koja nisu " + typeMessagePart + " nikako.";
			else if ($scope.repeatCount == 2)
				message = "Prikazuju se samo pitanja ove lekcije koja nisu " + typeMessagePart + " dva puta.";
			else if ($scope.repeatCount == 3)
				message = "Prikazuju se samo pitanja ove lekcije koja nisu " + typeMessagePart + " tri puta.";
			else
				message = "Prikazuju se samo pitanja ove lekcije koja nisu " + typeMessagePart + " " + $scope.repeatCount + " puta.";
		}

		if ($scope.removedQuestionsCount == 1)
			message += " Jedno pitanje ove lekcije neće biti prikazano.";
		else
			message += " " + $scope.removedQuestionsCount + " pitanja ove lekcije neće biti prikazana.";

		$scope.lessonNotification = message;
	};

	function setDescriptiveStatus() {

		var percent = Math.round($scope.correctlyAnswered / $scope.totalQuestions * 100);

		var status = "";

		if (percent > 100)
			percent = 100;

		if (percent == 100) {

			status = getTopMessage();
		} else if (percent > 95) {

			status = "Bravo, uspešno odogovoreno na " + percent + "% pitanja!";
		} else if (percent >= 80) {

			status = "Odlično, uspešno odgovoreno na " + percent + "% pitanja!";
		} else if (percent >= 60) {

			status = "Dobro je ali može i bolje. Uspešno odgovoreno na " + percent + "% pitanja.";
		} else if (percent >= 40) {

			status = "Nije loše za početak. Uspešno odgovoreno na " + percent + "% pitanja.";
		} else if (percent > 0) {

			status = "Moraš još da vežbaš. Uspešno rešeno svega " + percent + "% pitanja";
		} else {

			status = getBottomMessage();
		}

		$scope.descriptiveStatus = status;
	}
	;

	function getTopMessage() {

		var randomIndex = Math.round(Math.random() * 5);

		var messages = [];

		messages[0] = "Svaka čast! Sve je tačno odgovoreno!";
		messages[1] = "Svaka čast! Sve je tačno odgovoreno!";
		messages[2] = "Svaka čast! Sve je tačno odgovoreno!";
		messages[3] = "Svaka čast! Sve je tačno odgovoreno!";
		messages[4] = "Svaka čast! Sve je tačno odgovoreno!";
		messages[5] = "Svaka čast! Sve je tačno odgovoreno!";

		return messages[randomIndex];
	}
	;

	function getBottomMessage() {

		var randomIndex = Math.round(Math.random() * 5);

		var messages = [];

		messages[0] = "Ništa nije tačno odgovoreno :(";
		messages[1] = "Ništa nije tačno odgovoreno :(";
		messages[2] = "Ništa nije tačno odgovoreno :(";
		messages[3] = "Ništa nije tačno odgovoreno :(";
		messages[4] = "Ništa nije tačno odgovoreno :(";
		messages[5] = "Ništa nije tačno odgovoreno :(";

		return messages[randomIndex];
	}
	;

	// pozivamo ucitavanje
	if ($scope.isPartial)
		$scope.loadPartial();
	else if ($scope.isFavorite)
		$scope.loadFavorite();
	else
		$scope.loadQuestions();

	// start learning or testing
	if ($rootScope.isTestingMode)
		questionService.startTesting();
	else
		questionService.startLearning();

} ]);