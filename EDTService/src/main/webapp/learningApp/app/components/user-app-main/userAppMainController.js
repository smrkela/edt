// This controller loads categories and stats of categories,
// Also it loads user stats

angular.module('edt.controllers').controller('userAppMainController', [ '$scope', '$rootScope', '$http', '$state', 'groupColorService', 'pageService',

function($scope, $rootScope, $http, $state, groupColorService, pageService) {

	pageService.setTitle("Glavna");
	pageService.setNavigationId("HOME");

	// current learning index (1-3), set if not present in root scope
	if (!$rootScope.hasOwnProperty("currentLearn"))
		$rootScope.currentLearn = 1;

	// loading groups
	$scope.loadGroups = function() {

		$http.get('rest/learningApp/getGroups').success(function(data, status, headers, config) {

			$scope.groups = data.groups.group;
			$scope.discussions = data.discussions;
			$scope.userPreview = data.user;

		}).error(function(data) {

		});
	};

	// function to update current lear
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
	$scope.getGroupTotalPercent = function(group) {

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

	// load user experience data
	$scope.loadUserExperience = function() {

		$http.get('rest/learningApp/loadUserExperience').success(function(data) {

			$scope.currentLevel = data["current-level"];
			$scope.currentProgress = data["current-progress"] * 100;
			$scope.experiencePoints = data["experience-points"];
			$scope.learnedQuestions = data["learned-questions"];
			$scope.testedQuestions = data["tested-questions"];
			$scope.thisLevelPoints = data["current-level-experience-points"];
			$scope.nextLevelPoints = data["next-level-experience-points"];
		});
	};

	$scope.getGroupColor = function(groupId) {

		return groupColorService.getGroupColor(groupId);
	};

	$scope.loadGroups();
	$scope.loadUserExperience();
} ]);

angular.module('edt').directive('userDetails', [ '$http', function($http) {

	return {
		scope : true,
		restrict : 'AE',
		replace : true,
		templateUrl : 'learningApp/app/components/user-app-main/controls/userAppMainUserDetailsComponent.html',
		controller : function($scope, $rootScope, $http) {

			// CODE HERE
		}
	};
} ]);

angular.module('edt').directive('userDiscussions', [ '$http', function($http) {

	return {
		scope : true,
		restrict : 'AE',
		replace : true,
		templateUrl : 'learningApp/app/components/user-app-main/controls/userAppMainDiscussionsComponent.html',
		controller : function($scope, $rootScope, $http) {

			// CODE HERE
		}
	};
} ]);