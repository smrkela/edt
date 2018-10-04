// This controller loads categories and stats of categories,
// Also it loads user stats

angular.module('edt.controllers').controller('guestAppMainController', [ '$scope', '$rootScope', '$http', '$state', '$stateParams',

function($scope, $rootScope, $http, $state, $stateParams) {

	// current learning index (1-3)
	$rootScope.currentLearn = 1;

	$scope.drivingCategoryId = $stateParams.drivingCategoryId;

	// loading groups
	$scope.loadGroups = function() {

		$http.get('rest/learningApp/getGuestGroups/' + $scope.drivingCategoryId).success(function(data, status, headers, config) {

			$scope.groups = data.group;

		}).error(function(data) {

		});
	};

	$scope.showLessons = function(group) {

		$state.go('guest-app-group', {
			drivingCategoryId : $scope.drivingCategoryId,
			groupId : group.id
		});
	};

	$scope.loadGroups();
} ]);