// This controller loads categories and stats of categories,
// Also it loads user stats

angular.module('edt.controllers').controller('userAppGroupController', [ '$scope', '$rootScope', '$stateParams', '$http', '$state', 'groupColorService', 'pageService',

function($scope, $rootScope, $stateParams, $http, $state, groupColorService, pageService) {

	pageService.setTitle("Izbor lekcije za učenje");
	pageService.setNavigationId("HOME");

	$scope.groupId = $stateParams.groupId;
	$scope.selectedLessonId = $stateParams.selectedLessonId;

	// current learning index (1-3), set if not present in root
	// scope
	if (!$rootScope.hasOwnProperty("currentLearn"))
		$rootScope.currentLearn = 1;

	$scope.selectedLesson = null;

	// loading lessons
	$scope.loadLessons = function() {

		$http.get('rest/learningApp/getLessons/' + $stateParams.groupId).success(function(data, status, headers, config) {

			var lessonsDTO = data.lessons;
			var groupsDTO = data.groups;

			$scope.groupName = lessonsDTO['category-name'];
			$scope.lessons = lessonsDTO.lessons;
			$scope.categoryId = lessonsDTO['category-id'];
			$scope.groups = groupsDTO.group;

			var learned = 0;
			var tested = 0;
			var total = 0;

			// sada vadimo statistike
			for ( var index in $scope.lessons) {

				learned += $scope.lessons[index].learn1;
				tested += $scope.lessons[index].test1;
				total += $scope.lessons[index]["number-of-questions"];
			}

			$scope.learnedOnce = learned;
			$scope.testedOnce = tested;
			$scope.totalQuestions = total;

			$scope.currentProgress = (learned + tested) * 100 / (2 * total);

			// ako je izabrana neka lekcija, treba je oznaciti
			if ($scope.selectedLessonId && $scope.lessons.length >= $scope.selectedLessonId)
				$scope.selectedLesson = $scope.lessons[$scope.selectedLessonId - 1];

		}).error(function(data) {

		});
	};

	// function to update current learn
	$scope.updateCurrentLearn = function(index) {

		$rootScope.currentLearn = index;
	};

	// getting current learning stat
	$scope.getLearnStat = function(group) {

		return group['learn' + $rootScope.currentLearn];
	};

	// getting current learning stat
	$scope.getTestStat = function(group) {

		return group['test' + $rootScope.currentLearn];
	};

	// getting total group percent
	$scope.getLessonTotalPercent = function(group) {

		var learned = $scope.getLearnStat(group);
		var tested = $scope.getTestStat(group);
		var total = group["number-of-questions"];

		var value = (learned + tested) * 100 / (2 * total);

		value = Math.min(value, 100);

		return value;
	};

	// getting learn/test percent
	$scope.getLearnPercent = function(group) {

		var learnStat = $scope.getLearnStat(group);
		var totalQuestions = group['number-of-questions'];

		return learnStat * 100 / totalQuestions;
	};

	// getting learn/test percent
	$scope.getTestPercent = function(group) {

		var testStat = $scope.getTestStat(group);
		var totalQuestions = group['number-of-questions'];

		return testStat * 100 / totalQuestions;
	};

	$scope.startLearning = function(lesson) {

		$state.go('user-app-lesson', {
			groupId : $scope.groupId,
			lessonId : lesson.id,
			type : 'ucenje'
		});
	};

	$scope.startTesting = function(lesson) {

		$state.go('user-app-lesson', {
			groupId : $scope.groupId,
			lessonId : lesson.id,
			type : 'provera'
		});
	};

	$scope.getGroupColor = function(groupId) {

		return groupColorService.getGroupColor(groupId);
	};

	$scope.getLearnTooltip = function(lesson) {

		return $scope.getTooltip($scope.currentLearn, true, $scope.getLearnPercent(lesson));
	};

	$scope.getTestTooltip = function(lesson) {

		return $scope.getTooltip($scope.currentLearn, false, $scope.getTestPercent(lesson));
	};

	$scope.getTooltip = function(index, isLearn, progress) {

		var text = "";

		var p = progress;

		if (isLearn)
			text += "Naučeno ";
		else
			text += "Provereno ";

		text += p + "% ";

		if (index == 1)
			text += "prvi put.";
		else if (index == 2)
			text += "drugi put.";
		else if (index == 3)
			text += "treći put.";

		if (progress < 1) {
			if (isLearn)
				text += " Klikni da učis samo preostala pitanja.";
			else
				text += " Klikni da proveriš samo preostala pitanja.";
		}

		return text;
	};

	$scope.goToPartial = function(lesson, isLearn) {

		var lessonId = lesson.id;
		var type = isLearn ? "ucenje" : "provera";
		var repeatCount = $scope.currentLearn;

		$state.go('user-app-lesson', {
			groupId : $scope.groupId,
			lessonId : lessonId,
			type : type,
			repeatCount : repeatCount,
			isPartial : true
		});
	};

	$scope.goToFavorites = function(lesson, isLearn) {

		var lessonId = lesson.id;
		var type = isLearn ? "ucenje" : "provera";

		$state.go('user-app-lesson', {
			groupId : $scope.groupId,
			lessonId : lessonId,
			type : type,
			isFavorite : true
		});
	};

	$scope.onLessonClick = function(lesson) {

		$scope.selectedLesson = lesson;
	};

	$scope.loadLessons();

} ]);

angular.module('edt').directive('userLessonRenderer', [ '$http', function($http) {

	return {
		scope : true,
		restrict : 'AE',
		replace : true,
		templateUrl : 'learningApp/app/components/user-app-group/controls/userLessonRenderer.html',
		controller : function($scope, $rootScope, $http) {

			$scope.isRollOver = false;

			$scope.recalculateLesson = function() {

				$scope.total = $scope.lesson['number-of-questions'];
				$scope.totalPercent = $scope.getLessonTotalPercent($scope.lesson);
				$scope.learnPercent = $scope.getLearnPercent($scope.lesson);
				$scope.testPercent = $scope.getTestPercent($scope.lesson);
				$scope.learnCount = $scope.getLearnStat($scope.lesson);
				$scope.testPercent = $scope.getTestPercent($scope.lesson);
				$scope.testCount = $scope.getTestStat($scope.lesson);
				$scope.learnTooltip = $scope.getLearnTooltip($scope.lesson);
				$scope.testTooltip = $scope.getTestTooltip($scope.lesson);
			};

			$scope.mouseEnter = function() {

				$scope.isRollOver = true;
			};

			$scope.mouseLeave = function() {

				$scope.isRollOver = false;
			};

			$scope.recalculateLesson();
		},
		link : function(scope, elem, attrs) {

			scope.$watch('currentLearn', function(value) {

				scope.recalculateLesson();
			});

		}
	};
} ]);