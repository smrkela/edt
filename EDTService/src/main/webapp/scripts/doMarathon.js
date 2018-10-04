var app = angular.module('marathonApp', []);

app.controller('controller', function($scope, $http) {

	$scope.question = null;
	$scope.questionIndex = 0;
	$scope.timeTakenString = "";
	$scope.correctQuestions = 0;
	$scope.incorrectQuestions = 0;
	$scope.skippedQuestions = 0;
	$scope.totalPoints = 0;
	
	// first, setup current data and load the first question
	var marathonId = jQuery('#marathonId').val();

	var loadInitialQuestion = function() {

		$http.get(baseUrl + 'rest/learning/maraton/polaganje/pocetak?marathonId=' + marathonId).success(function(response) {

			$scope.applyResponse(response);
		});
	};
	
	$scope.applyResponse = function(response){
		
		$scope.question = $scope.setupQuestion(response.question);
		$scope.questionIndex = response.questionIndex;
		$scope.timeTakenString = response.timeTakenString;
		$scope.correctQuestions = response.correctQuestions;
		$scope.incorrectQuestions = response.incorrectQuestions;
		$scope.skippedQuestions = response.skippedQuestions;
		$scope.totalPoints = response.totalPoints;
		
		var questionId = response.questionId;
		var questionUrlTitle = response.questionUrlTitle;
		
		$scope.questionHelpLink = baseUrl + 'ucenje/pitanja/' + questionId + '/' + questionUrlTitle;
	};
	
	$scope.setupQuestion = function(question){
		
		question.isUnconfirmed = true;
		question.isAnswered = false;

		// svakom odgovoru dodeljujemo da nije odgovoreno
		var answers = question['question-answer'];

		for ( var answerIndex in answers) {

			var answer = answers[answerIndex];

			answer.isUserSelected = false;
		}
		
		return question;
	};
	
	$scope.getTitleStyle = function() {

		if ($scope.question == null
				|| $scope.question.isUnconfirmed)
			return 'panel-warning';

		if ($scope.question.isCorrectlyAnswered)
			return 'panel-success';
		else
			return 'panel-danger';
	};
	
	$scope.getAnswerStyle = function(answer){
		
		var style = '';
		
		var q = $scope.question;
		
		if(!q.isUnconfirmed){
			
			style = answer.correct ? 'answerCorrect' : 'answerIncorrect';
		}

		return style;
	};
	
	$scope.answerClicked = function(answer) {

		// ako je vec odgovoreno, ne moze se opet
		// odgovarati
		if(!$scope.question.isUnconfirmed)
			return
		
		if ($scope.isMultiSelect($scope.question)) {

			// checkbox
			// don't do anything, it's already modified
			// answer.isUserSelected = !answer.isUserSelected;
		} else {

			// radio

			// unselect all answers
			var answers = $scope.question['question-answer'];

			for ( var answerIndex in answers) {

				var a = answers[answerIndex];

				a.isUserSelected = false;
			}

			answer.isUserSelected = true;
		}

		$scope.refreshAnswers();
	};
	
	$scope.isMultiSelect = function(question) {

		var correctAnswersCount = 0;
		var answers = question['question-answer'];

		for ( var answerIndex in answers) {

			var answer = answers[answerIndex];

			if (answer.correct)
				correctAnswersCount++;

			if (correctAnswersCount > 1)
				break;
		}

		return correctAnswersCount > 1;
	};
	
	$scope.refreshAnswers = function() {

		// prvo treba da setujemo sve odgovore na
		// neselektovane
		// pa onda da obradimo onog koji je izabran

		var answers = $scope.question['question-answer'];

		var totalAnswered = 0;
		var correctAnswersCount = 0;

		for ( var answerIndex in answers) {
			var answer = answers[answerIndex];

			if (answer.isUserSelected)
				totalAnswered++;

			if (answer.correct)
				correctAnswersCount++;
		}

		$scope.question.isAnswered = totalAnswered == correctAnswersCount;

		var isCorrect = $scope.isCorrectlyAnswered();

		if ($scope.question.isAnswered) {

			// TODO
		}

		$scope.question.isCorrectlyAnswered = isCorrect;

		// dispatchEvent(new
		// Event("isUserSelectedChanged"));
	};
	
	$scope.isCorrectlyAnswered = function() {

		var answers = $scope.question['question-answer'];
		var allAnswersOk = true;

		for ( var answerIndex in answers) {

			var answer = answers[answerIndex];

			var answerIsCorrect = answer.correct == answer.isUserSelected;

			allAnswersOk = allAnswersOk	&& answerIsCorrect;
		}

		return allAnswersOk;
	};
	
	$scope.confirmAnswers = function(){
	
		// ako nije dat odgovor, ne potvrdjujemo pitanje
		if(!$scope.question.isAnswered)
			return;
		
		$scope.question.isUnconfirmed = false;
	};
	
	$scope.skip = function(){
		
		$http.get(baseUrl + 'rest/learning/maraton/polaganje/preskoci?marathonId=' + marathonId).success(function(response) {

			$scope.applyResponse(response);
		});
	};
	
	$scope.submit = function(){
		
		var data = {marathonId: marathonId};
		data.answers = [];
		
		// izvlacimo odgovore korisnika
		var answers = $scope.question['question-answer'];
		
		for ( var answerIndex in answers) {

			var answer = answers[answerIndex];
			
			if(answer.isUserSelected)
			data.answers.push(answer.id);
		}
		
		$http.post(baseUrl + 'rest/learning/maraton/polaganje/podnesi', data).success(function(response) {

			$scope.applyResponse(response);
		});
	};
	
	$scope.getImageUrl = function(image){
		
		var url = baseUrl + 'image?path='+ image.url;
		
		return url;
	};

	// ucitavamo prvo pitanje
	loadInitialQuestion();
});
