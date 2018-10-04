angular.module('edt.controllers').controller('userAppActivityController', [ '$scope', '$rootScope', '$stateParams', '$http', '$state', 'groupColorService', 'ngDialog', 'pageService', 'dateService',

function($scope, $rootScope, $stateParams, $http, $state, groupColorService, ngDialog, pageService, dateService) {

	$scope.isLearn = $stateParams.type == 'ucenje';
	$scope.hasNoActivity = false;

	if ($scope.isLearn)
		pageService.setTitle("Moja učenja");
	else
		pageService.setTitle("Moje provere");

	pageService.setNavigationId("MY_ACTIVITY");

	$scope.loadLearning = function() {

		$http.get('rest/learningApp/getAllLearningSessions').success(onSessionsLoaded).error(function(data) {

		});
	};

	$scope.loadTesting = function() {

		$http.get('rest/learningApp/getAllTestingSessions').success(onSessionsLoaded).error(function(data) {

		});
	};

	function regroupSessions(values) {

		if (!values || values.length == 0)
			return [];

		var map = {}; // kljuc je "godina-mesec-dan", vrednost je
		// SessionGroup
		var groups = [];
		var sessions = [];

		for (var i = 0; i < values.length; i++) {

			var value = values[i];
			var start = new Date(value.start);

			var session = {
				date : start,
				numberOfQuestions : value['number-of-questions']
			};

			var key = createKey(session.date);

			if (key in map) {

				map[key].addSession(session);
			} else {

				var group = {
					date : session.date,
					title : dateService.getPrettyDayOfWeek(session.date),
					key : key,
					numberOfQuestions : 0,
					sessions : [],
					addSession : function(session) {
						this.numberOfQuestions += session.numberOfQuestions;
						this.sessions.push(session)
					}
				};

				group.addSession(session);

				map[key] = group;
				groups.push(group);
			}
		}

		// sada prolazimo kroz grupe i formiramo grupe za mesece
		var monthGroups = [];
		var monthMap = {}; // kljuc je "godina-mesec"

		for (var i = 0; i < groups.length; i++) {

			var group = groups[i];
			var key = createMonthKey(group.date);

			if (key in monthMap) {

				monthMap[key].addGroup(group);
			} else {

				var monthGroup = {
					date : group.date,
					title : dateService.getPrettyMonthAndYear(group.date),
					key : key,
					numberOfQuestions : 0,
					year : group.date.fullYear,
					month : group.date.month,
					groups : [],
					addGroup : function(group) {
						this.numberOfQuestions += group.numberOfQuestions;
						this.groups.push(group);
					}
				}

				monthGroup.addGroup(group);

				monthMap[key] = monthGroup;
				monthGroups.push(monthGroup);
			}
		}

		return monthGroups;
	}

	function createKey(date) {

		return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
	}

	function createMonthKey(date) {

		return date.getFullYear() + "-" + (date.getMonth() + 1);
	}

	function onSessionsLoaded(data, status, headers, config) {

		var _sessions = [];

		if ($scope.isLearn)
			_sessions = regroupSessions(data['learning-session']);
		else
			_sessions = regroupSessions(data['testing-session']);

		// sada treba proci kroz sve grupe da bi izvukli konacan broj pitanja
		var _total = 0;

		for (var i = 0; i < _sessions.length; i++) {

			var _session = _sessions[i];
			_total += _session.numberOfQuestions;
		}

		$scope.sessions = _sessions;
		$scope.numberOfQuestions = _total;

		if ($scope.isLearn) {
			$scope.title = "Moja učenja";
			$scope.note = "Ukupno " + _total + " pitanja naučeno";
		} else {
			$scope.title = "Moje provere";
			$scope.note = "Ukupno " + _total + " pitanja uspešno provereno";
		}
		
		$scope.hasNoActivity = _total == 0;
	}

	$scope.getDayColor = function(date) {

		var day = date.getDay();

		if (day == 0)
			return "#CA1407";

		if (day == 6)
			return "#3B98DF";

		return "#555555";
	}

	if ($scope.isLearn)
		$scope.loadLearning();
	else
		$scope.loadTesting();
} ]);
