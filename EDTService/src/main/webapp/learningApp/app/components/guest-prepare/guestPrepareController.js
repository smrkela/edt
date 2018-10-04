angular.module('edt.controllers').controller('guestPrepareController', [ '$scope', '$http', '$state', function($scope, $http, $state) {

	$scope.init = function() {

		$http.get('rest/learningApp/getGuestPrepare').success(function(data) {

			$scope.drivingCategories = data.drivingCategories;
			$scope.selectedDrivingCategory = $scope.drivingCategories[1];

		}).error(function(data, status, headers, config) {

			console.log(data);
		});
	};

	$scope.startLearning = function() {

		$state.go('guest-app-main', {
			drivingCategoryId : $scope.selectedDrivingCategory.id
		});
	};

	$scope.init();
} ]);