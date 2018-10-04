angular.module('edt.controllers').controller('loginController', ['$scope', 'authService', '$state',
		function($scope, authService, $state) {

			$scope.buttonText = 'Uloguj se';
			$scope.invalidLogin = false;
			
			$scope.login = function(){
				
				$scope.buttonText = 'Logovanje...';
				
				authService.login($scope.credentials.username, $scope.credentials.password).then(function(data){
					
					$state.go('home-auth');
				
				}, function(err){
				
					$scope.invalidLogin = true;
					
				}).finally(function() {
					
					$scope.buttonText = "Login";
					
				});
				
			};
		}]);
