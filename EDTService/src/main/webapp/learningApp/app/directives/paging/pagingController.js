angular.module('edt.controllers').directive('pagingWidget', ['$http', function($http) {

	return {
		scope : true,
		restrict : 'AE',
		replace : false,
		templateUrl : 'learningApp/app/directives/paging/pagingWidget.html',
		link : function(scope, elem, attrs) {

			scope.$watch('currentPage', function(value) {

				scope.paginationRecalculate();
			});

			scope.$watch('totalPages', function(value) {

				scope.paginationRecalculate();
			});
		},
		controller : function($scope, $rootScope, $http, $state) {

			$scope.paginationRecalculate = function() {
				// $scope.currentPage = 3;
				// $scope.totalPages = 10;
				// treba da izracunamo koje stranice da prikazemo
				$scope.paginationNext = $scope.currentPage < $scope.totalPages;
				$scope.paginationPrevious = $scope.currentPage > 1;
				$scope.paginationItems = [];

				$scope.paginationFirst = $scope.totalPages > 0;
				$scope.paginationLast = $scope.totalPages > 1;

				var numbersToShow = [];
				var delta = 2;

				if ($scope.totalPages > 2) {

					for (var i = $scope.currentPage - delta; i <= $scope.currentPage + delta; i++) {

						if (i > 1 && i < $scope.totalPages)
							numbersToShow.push(i);
					}
				}

				$scope.paginationLeftSeparator = $scope.totalPages > 2 && !numbersToShow.contains(2);
				$scope.paginationRightSeparator = $scope.totalPages > 2 && !numbersToShow.contains($scope.totalPages - 1);

				$scope.paginationItems = numbersToShow;
			};

			$scope.paginationOnNext = function() {

				$scope.paginationOnPage(parseInt($scope.currentPage) + 1);
			};
		}
	};
}]);
