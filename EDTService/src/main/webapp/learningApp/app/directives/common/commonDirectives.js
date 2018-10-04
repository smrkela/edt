angular.module('ui.check', []).directive('icheck', [ '$timeout', '$parse', function($timeout, $parse) {
	return {
		require : 'ngModel',
		link : function($scope, element, $attrs, ngModel) {
			return $timeout(function() {
				var value;
				value = $attrs['value'];

				$scope.$watch($attrs['ngModel'], function(newValue) {
					$(element).iCheck('update');
				});

				return $(element).iCheck({
					checkboxClass : 'icheckbox_flat',
					radioClass : 'iradio_flat'

				}).on('ifChanged', function(event) {
					// if ($(element).attr('type') === 'checkbox' &&
					// $attrs['ngModel']) {
					// $scope.$apply(function () {
					// return ngModel.$setViewValue(event.target.checked);
					// });
					// }
					// if ($(element).attr('type') === 'radio' &&
					// $attrs['ngModel']) {
					// return $scope.$apply(function () {
					// return ngModel.$setViewValue(value);
					// });
					// }

					// selektovanje mroa da radi iskljucivo preko modela tako da
					// a ovaj nacin ignorisemo selekciju direktno nad kontrolom
					$(element).iCheck('toggle');
				});
			});
		}
	};
} ]);

angular.module('edt').directive('ngEnter', function() {
	return function(scope, element, attrs) {
		element.bind("keydown keypress", function(event) {
			if (event.which === 13) {
				scope.$apply(function() {
					scope.$eval(attrs.ngEnter);
				});

				event.preventDefault();
			}
		});
	};
});