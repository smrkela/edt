// This controller loads categories and stats of categories,
// Also it loads user stats

angular.module('edt.controllers').controller('myDiscussionsController', [ '$scope', '$rootScope', '$stateParams', '$http', '$state', 'groupColorService', 'ngDialog', 'pageService',

function($scope, $rootScope, $stateParams, $http, $state, groupColorService, ngDialog, pageService) {

	pageService.setTitle("Moje diskusije");
	pageService.setNavigationId("MY_DISCUSSIONS");

	$scope.search = $stateParams.search; // null, "", "asd"...
	$scope.totalPages = 1;
	$scope.currentPage = $stateParams.page;
	$scope.hasNoMessages = false;
	$scope.usedSearch = "";

	$scope.load = function(currentPage) {

		$http.get('rest/learningApp/getMyQuestionMessages' + '?startPage=' + (currentPage - 1) + "&searchText=" + $scope.search).success(function(data, status, headers, config) {

			$scope.totalPages = data['total-pages'];
			$scope.pageSize = data['page-size'];
			$scope.totalMessages = data['total-messages'];
			$scope.messages = data['question-message'];
			$scope.currentPage = currentPage;
			$scope.hasNoMessages = $scope.totalMessages == 0;
			$scope.usedSearch = $scope.search;

		}).error(function(data) {

		});
	};

	$scope.hasAnswer = function(message) {

		var answerDate = message['answer-date'];

		return answerDate != undefined && answerDate != null;
	};

	$scope.getMessageNumber = function(index, currentPage) {

		var messageNumber = index + 1 + $scope.pageSize * (currentPage - 1);

		return "#" + messageNumber;
	};

	$scope.showMessages = function(questionId) {

		ngDialog.open({
			template : 'learningApp/app/components/discussion/discussionView.html',
			controller : 'discussionController',
			data : {
				questionId : questionId,
				pageIndex : 0
			},
			className : 'ngdialog-theme-discussion'
		});
	};

	$scope.paginationOnPage = function(page) {

		if (page > 0 && page <= $scope.totalPages)
			$state.go('mydiscussions', {
				page : page
			});
		// $scope.load(page);
	};

	$scope.onSearch = function() {

		$state.go('mydiscussions', {
			page : 1,
			search : $scope.search
		});
	};

	$scope.onRemoveSearch = function() {

		$state.go('mydiscussions', {
			page : 1,
			search : ""
		});
	};

	$scope.load($scope.currentPage);
} ]);

// RENDERERS

angular.module('edt').directive('myDiscussionRenderer', [ '$http', function($http) {

	return {
		scope : true,
		restrict : 'AE',
		replace : true,
		templateUrl : 'learningApp/app/components/mydiscussions/controls/myDiscussionRenderer.html',
		controller : function($scope, $rootScope, $http) {

			// CODE HERE
		}
	};
} ]);
