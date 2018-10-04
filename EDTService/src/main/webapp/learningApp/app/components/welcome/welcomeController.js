// Welcome controller checks if the user is logged in or not, if he is
// then a redirection is done to the applications main page, else standard 
// welcome page is displayed

angular.module('edt.controllers').controller('welcomeController', [ '$scope', '$rootScope', '$http', '$state',

function($scope, $rootScope, $http, $state) {

	$scope.loadUser = function() {

		$http.get('rest/learningApp/getUser').success(function(data, status, headers, config) {

			var isLogin = data && (typeof data == 'string') && data.indexOf('login_form') > 0;

			if (isLogin || !data) {

				// user is not logged in

			} else {

				$rootScope.user = data;
				$rootScope.authenticated = true;

				// redirect to main app page
				$state.go('user-app-main');
			}

		}).error(function(data) {

			$rootScope.user = null;
			$rootScope.authenticated = false;
		});
	};

	$scope.loadUser();

} ]);