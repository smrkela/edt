// This controller loads categories and stats of categories,
// Also it loads user stats

angular.module('edt.controllers').controller('discussionController', [ '$scope', '$rootScope', '$stateParams', '$http', '$state', 'groupColorService', 'UserService', 'smoothScroll',

function($scope, $rootScope, $stateParams, $http, $state, groupColorService, UserService, smoothScroll) {

	var questionId, pageIndex, question;

	if ($stateParams && $stateParams.hasOwnProperty('questionId')) {

		questionId = $stateParams.questionId;
		pageIndex = $stateParams.hasOwnProperty('pageIndex') ? $stateParams.pageIndex : 0;
		question = null;
	} else {

		questionId = $scope.ngDialogData.questionId;
		pageIndex = $scope.ngDialogData.pageIndex;
		question = $scope.ngDialogData.question;
	}

	$scope.questionId = questionId;
	$scope.question = question;
	$scope.messages = [];
	$scope.isLoading = false;
	$scope.isWritingMessage = false;
	$scope.messageText = "";
	$scope.deletedMessage = null;
	$scope.isSavingMessage = false;
	$scope.currentPage = pageIndex + 1;
	$scope.totalPages = 0;
	$scope.loadedQuestion = null;
	$scope.showLoadedQuestion = true;
	$scope.messageNotification = "";

	$scope.MAX_MESSAGE_SIZE = 1000;

	$scope.load = function(page) {

		$scope.isLoading = true;

		$http.get('rest/learningApp/getQuestionMessages' + '?questionId=' + ($scope.questionId) + "&pageIndex=" + (page - 1)).success(function(data, status, headers, config) {

			$scope.isAdministrator = data['is-administrator'];
			$scope.currentPage = data['current-page-index'] + 1;
			$scope.totalPages = data['total-pages'];
			$scope.pageSize = data['page-size'];
			$scope.totalMessages = data['total-messages'];

			$scope.messages = data["question-message"];

			$scope.loadedQuestion = data['question'];

			$scope.isLoading = false;

		}).error(function(data) {

			$scope.isLoading = false;

		});
	};

	$scope.writeMessageClicked = function() {

		$scope.repliedMessage = null;
		$scope.isWritingMessage = true;
	};

	$scope.cancelMessage = function() {

		$scope.isWritingMessage = false;
		$scope.messageText = "";
	};

	$scope.getMessageNumber = function(message) {

		var index = $scope.messages.indexOf(message);
		var messageNumber = index + 1 + $scope.pageSize * ($scope.currentPage - 1);

		return "#" + messageNumber;
	};

	$scope.hasReplyMessage = function(message) {

		var parent = message['parent-message-id'];

		return parent;
	};

	$scope.getRepliedMessage = function(message) {

		var isMale = message['parent-message-user-is-male'];
		var text = message['parent-message-user-name'];

		if (isMale)
			text += " je rekao ";
		else
			text += " je rekla ";

		text += ":";

		return text;
	};

	$scope.replyMessageClicked = function(message) {

		$scope.repliedMessage = message;
		$scope.isWritingMessage = true;
		
		var topPos = document.getElementById('messageWritingDiv').offsetTop;
		var dialog=document.getElementsByClassName('ngdialog')[0];
		
		dialog.scrollTop = topPos;
		
		//var element = document.getElementById('messageWritingDiv');
		
		//smoothScroll(element);
	};

	$scope.isDeleteVisible = function(message) {

		var result = false;
		var messageUserId = message['user-id'];
		var userId = UserService.getUser().id;

		// ako je korisnik administrator ili ako je on i napisao
		// poruku onda dozvoljavamo brisanje
		if ($scope.isAdministrator || userId == messageUserId)
			result = true;

		return result;
	};

	$scope.deleteMessageClicked = function(message) {

		if ($scope.deleteMessage != null)
			return;

		$scope.deletedMessage = message;

		$http.get('rest/question/deleteQuestionMessage?questionMessageId=' + message.id).success(function(data) {

			var index = $scope.messages.indexOf($scope.deletedMessage);

			$scope.messages.splice(index, 1);

			$scope.deletedMessage = null;

			if ($scope.question)
				$scope.question['number-of-messages']--;

			$scope.totalMessages--;
		}).error(function(data) {

			$scope.deletedMessage = null;

			console.log("Error deleting message: " + data);
		});
	};

	/**
	 * Cuvanje poruke.
	 */
	$scope.saveMessage = function() {

		var message = $scope.messageText;

		message = message != null ? message.trim() : null;

		if (!message) {

			$scope.messageNotification = "Unesi tekst pre čuvanja poruke.", "Da bi sačuvao poruku, moraš ukucati tekst.";
			return;
		}

		// poruka ne sme da ima vise od 2000 karaktera
		if (message.length > 2000) {

			$scope.messageNotification = "Poruka ne može da ima više od 1000 slova.", "Smanji tekst poruke.";
			return;
		}

		var parentMessageId = $scope.repliedMessage ? $scope.repliedMessage.id : null;

		$scope.isSavingMessage = true;

		var url = 'rest/learningApp/saveQuestionMessage?questionId=' + $scope.questionId + '&messageText=' + message;

		if (parentMessageId)
			url += "&parentMessageId=" + parentMessageId;

		$http.get(url).success(onMessageSaved).error(onMessageError);
	};

	function onMessageSaved(data) {

		// uspesno sacuvana poruka
		// treba da je dodamo medju ostale i zatvorimo editor

		$scope.messages.push(data);

		$scope.isWritingMessage = false;
		$scope.messageText = "";

		$scope.isSavingMessage = false;

		// povecavamo broj poruka za ovo pitanje
		if ($scope.question)
			$scope.question['number-of-messages']++;

		$scope.totalMessages++;
	}

	function onMessageError(data) {

		$scope.isSavingMessage = false;

		$scope.messageNotification = "Poruka nije uspešno sačuvana. Pokušaj ponovo.";
	}

	$scope.paginationOnPage = function(page) {

		if (page > 0 && page <= $scope.totalPages)
			$scope.load(page);
	};
	
	$scope.close = function(){
		
		$scope.closeThisDialog();
	}

	$scope.load($scope.currentPage);
} ]);

// RENDERERS

angular.module('edt').directive('discussionRenderer', ['$http', function($http) {

	return {
		scope : true,
		restrict : 'AE',
		replace : false,
		templateUrl : 'learningApp/app/components/discussion/controls/discussionRenderer.html',
		controller : function($scope, $rootScope, $http) {

			// CODE HERE
		}
	};
}]);
