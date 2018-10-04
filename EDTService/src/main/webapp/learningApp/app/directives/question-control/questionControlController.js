angular
		.module('edt')
		.directive(
				'questionControlWidget', ['$http', 
				function($http) {

					return {
						scope : {
							question : '=question',
							hideDiscussions: '=hideDiscussions'
						},
						restrict : 'AE',
						replace : true,
						templateUrl : 'learningApp/app/directives/question-control/questionControlWidget.html',
						controller : function($scope, $rootScope, $http,
								$state, questionService, ngDialog) {

							$scope.isTestingMode = $rootScope.isTestingMode;
							
							if($scope.hideDiscussions == undefined)
								$scope.hideDiscussions = false;

							$scope.answerLetters = [ 'a', 'b', 'c', 'd', 'e',
									'f', 'g', 'h', 'i', 'j', 'k' ];

							$scope.getAnswerLetter = function(index) {

								return $scope.answerLetters[index];
							};

							$scope.showMessages = function() {

								ngDialog.open({
									template: 'learningApp/app/components/discussion/discussionView.html',
									controller: 'discussionController',
									data: {questionId: $scope.question.id,
										pageIndex: 0,
										question: $scope.question},
									className: 'ngdialog-theme-discussion'
								});
								
// $state.go('discussion', {
// questionId : $scope.question.id,
// pageIndex : 0
// });

							};

							$scope.getTitleColor = function() {

								if (!$scope.isTestingMode
										|| $scope.question == null
										|| $scope.question.isUnconfirmed)
									return '#E3A21A';

								if ($scope.question.isCorrectlyAnswered)
									return '#51A825';
								else
									return '#FF002A';
							};

							$scope.getTitleStyle = function() {

								if (!$scope.isTestingMode
										|| $scope.question == null
										|| $scope.question.isUnconfirmed)
									return 'panel-warning';

								if ($scope.question.isCorrectlyAnswered)
									return 'panel-success';
								else
									return 'panel-danger';
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

									questionService.saveTesting(
											$scope.question.id, isCorrect);
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

									var answerIsCorrect = answer.correct
											&& answer.isUserSelected
											|| !answer.correct
											&& !answer.isUserSelected;

									allAnswersOk = allAnswersOk
											&& answerIsCorrect;
								}

								return allAnswersOk;
							};

							$scope.answerClicked = function(answer) {

								// ako je vec odgovoreno, ne moze se opet
								// odgovarati
								if(!$scope.question.isUnconfirmed)
									return
								
								if ($scope.isMultiSelect($scope.question)) {

									// checkbox
									answer.isUserSelected = !answer.isUserSelected;
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
						}
					};
				}]);
