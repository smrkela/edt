// This controller loads categories and stats of categories,
// Also it loads user stats

angular.module('edt.controllers').controller('guestAppGroupController', [ '$scope', '$rootScope', '$stateParams', '$http', '$state',

function($scope, $rootScope, $stateParams, $http, $state) {

	$scope.drivingCategoryId = $stateParams.drivingCategoryId;
	$scope.groupId = $stateParams.groupId;

	// loading lessons
	$scope.loadLessons = function() {

		$http.get('rest/learningApp/getGuestLessons/' + $scope.drivingCategoryId + '/' + $scope.groupId).success(function(data, status, headers, config) {

			$scope.groupName = data['category-name'];
			$scope.lessons = data.lessons;
			$scope.groupId = data['category-id'];

		}).error(function(data) {

		});
	};

	$scope.loadLessons();

} ]);