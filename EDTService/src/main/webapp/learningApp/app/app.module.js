var edt = angular.module('edt', [ 'edt.controllers', 'edt.services', 'ui.router', 'ngCookies', 'ui.check', 'ngDialog', 'ngAnimate', 'smoothScroll', 'angular-google-analytics' ]);

/**
 * MAIN MODULE
 */

// INTERCEPTORS
// definisemo interceptor-a
edt.factory('authorizationInterceptor', [ '$rootScope', '$q', '$window', '$location', function(scope, $q, $window, $location) {

	var interceptor = {

		response : function(response) {

			var data = response.data;
			var isLogin = data && (typeof data == 'string') && data.indexOf('login_form') > 0;
			var isWelcome = $location.$$path == '/welcome'

			if (isLogin && !isWelcome) {

				// $window.location.reload();
				$location.path('/welcome');

				return $q.reject(response);
			}

			// otherwise
			return response;// $q.reject(response);
		},

		responseError : function(response) {

			return response;
		}
	};

	return interceptor;

} ]);

angular.module('edt').config([ '$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider', 'AnalyticsProvider',

function($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, AnalyticsProvider) {

	$stateProvider.state('welcome', {
		url : '/dobrodosli',
		controller : 'welcomeController',
		templateUrl : 'learningApp/app/components/welcome/welcomeView.html'
	}).state('login', {
		url : '/login',
		controller : 'loginController',
		templateUrl : 'learningApp/app/components/login/loginView.html'
	}).state('guest-prepare', {
		url : '/grost-priprema',
		controller : 'guestPrepareController',
		templateUrl : 'learningApp/app/components/guest-prepare/guestPrepareView.html'
	}).state('user-app-main', {
		url : '/',
		controller : 'userAppMainController',
		templateUrl : 'learningApp/app/components/user-app-main/userAppMainView.html'
	}).state('user-app-group', {
		url : '/grupa/:groupId/:selectedLessonId',
		controller : 'userAppGroupController',
		templateUrl : 'learningApp/app/components/user-app-group/userAppGroupView.html'
	}).state('guest-app-main', {
		url : '/gost/:drivingCategoryId',
		controller : 'guestAppMainController',
		templateUrl : 'learningApp/app/components/guest-app-main/guestAppMainView.html'
	}).state('guest-app-group', {
		url : '/grupa/:drivingCategoryId/:groupId',
		controller : 'guestAppGroupController',
		templateUrl : 'learningApp/app/components/guest-app-group/guestAppGroupView.html'
	}).state('user-app-lesson', {
		url : '/lekcija/:groupId/:lessonId/:type/:isPartial/:repeatCount/:isFavorite',
		controller : 'userAppLessonController',
		templateUrl : 'learningApp/app/components/user-app-lesson/userAppLessonView.html'
	}).state('discussions', {
		url : '/diskusije/:page/:search',
		controller : 'discussionsController',
		templateUrl : 'learningApp/app/components/discussions/discussionsView.html'
	}).state('mydiscussions', {
		url : '/moje-diskusije/:page/:search',
		controller : 'myDiscussionsController',
		templateUrl : 'learningApp/app/components/mydiscussions/myDiscussionsView.html'
	}).state('discussion', {
		url : '/diskusija/:questionId/:pageIndex',
		controller : 'discussionController',
		templateUrl : 'learningApp/app/components/discussion/discussionView.html'
	}).state('user-app-activity', {
		url : '/aktivnost/:type',
		controller : 'userAppActivityController',
		templateUrl : 'learningApp/app/components/user-app-activity/userAppActivityView.html'
	});

	$urlRouterProvider.otherwise('/dobrodosli');
	// $locationProvider.html5Mode(true);

	$httpProvider.interceptors.push('authorizationInterceptor');

	// setup your account
	AnalyticsProvider.setAccount('UA-43047679-1');
	// automatic route tracking (default=true)
	AnalyticsProvider.trackPages(true);
	// Optional set domain (Use 'none' for testing on localhost)
	AnalyticsProvider.setDomainName('vozacisrbije.com');
	// Use display features plugin
	AnalyticsProvider.useDisplayFeatures(true);
	// Use analytics.js instead of ga.js
	AnalyticsProvider.useAnalytics(true);
	// Ignore first page view.
	AnalyticsProvider.ignoreFirstPageLoad(true);
	// Enable enhanced link attribution module for analytics.js or ga.js
	AnalyticsProvider.useEnhancedLinkAttribution(true);
	// Change the default page event name. This is useful for ui-router, which fires $stateChangeSuccess instead of $routeChangeSuccess
	AnalyticsProvider.setPageEvent('$stateChangeSuccess');
} ]);

edt.run([ '$rootScope', '$http', 'UserService', '$templateCache', 'Analytics', function($scope, $http, UserService, $templateCache, Analytics) {

	$scope.$on('$stateChangeStart', function(e, toState, toParams, fromState, fromParams) {

		UserService.loadUser();
	});

	// $templateCache.removeAll();

} ]);

/**
 * SERVICES
 */

angular.module('edt.services', []);

angular.module('edt.controllers', []);

angular.module('edt.controllers').controller('mainController', [ '$scope', 'pageService', 'UserService', function($scope, pageService, UserService) {

	$scope.pageService = pageService;
	$scope.UserService = UserService;
} ]);

/**
 * CONSTANTS
 */

