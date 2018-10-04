angular.module('edt').directive('leaderboardWidget', ['$http', function($http) {

	return {
		scope : {},
		restrict : 'AE',
		replace : true,
		templateUrl : 'learningApp/app/directives/leaderboard/leaderboardWidget.html',
		controller : function($scope, $http) {

			$scope.typeId = 1; // 1, 2, 3

			// we have multiple loading methods
			$scope.loadLeaderboard = function(typeId) {

				$http.get('rest/learningApp/loadLeaderboard/' + typeId).success(function(data, status, headers, config) {

					$scope.typeId = typeId;
					$scope.users = data.users;

				}).error(function(data) {

				});
			};

			$scope.loadLeaderboard($scope.typeId);
		}
	};
}]);
