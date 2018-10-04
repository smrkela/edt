angular.module('edt.services').service('uuidService', function() {

	function _p8(s) {

		var p = (Math.random().toString(16) + "000000000").substr(2, 8);
		return s ? "-" + p.substr(0, 4) + "-" + p.substr(4, 4) : p;
	}

	this.generate = function() {

		return _p8() + _p8(true) + _p8(true) + _p8();
	};
});

angular.module('edt.services').service('groupColorService', function() {

	this.getGroupColor = function(groupId) {

		// grupe pocinju od 1 pa umanjujemo za 1
		groupId--;

		var colors = [ '#00aba9', '#603cba', '#ee1111', '#00a300', '#b91d47', '#2d89ef', '#e3a21a' ];

		if (groupId < 0)
			groupId = 0;

		if (groupId >= colors.length)
			groupId = colors.length - 1;

		return colors[groupId];
	};
});

angular.module('edt.services').factory('pageService', function() {

	var sufix = " - Vozači Srbije";
	var title = "Aplikacija za učenje" + sufix;
	var navigationId = "home";

	return {

		title : function() {
			return title;
		},
		setTitle : function(newTitle) {
			title = newTitle + sufix
		},
		navigationId : function() {
			return navigationId;
		},
		setNavigationId : function(newId) {
			navigationId = newId
		}
	}

});

angular.module('edt.services').service('dateService', function() {

	this.getPrettyMonthAndYear = function(date) {

		if (!date)
			return "";

		var monthNames = [ "Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar" ];

		return date.getFullYear() + " " + monthNames[date.getMonth()];
	};

	this.getPrettyDayOfWeek = function(date) {

		if (!date)
			return "";

		var dayOfWeek = date.getDay();

		if (dayOfWeek == 0)
			dayOfWeek = 7;

		var datePart = date.getDate() + "";

		if (datePart.length == 1)
			datePart = "0" + datePart;

		var dayOfWeekNames = [ "Ponedeljak", "Utorak", "Sreda", "Četvrtak", "Petak", "Subota", "Nedelja" ];

		return datePart + ". " + dayOfWeekNames[dayOfWeek - 1];
	}

});

angular.module('edt.services').factory('UserService', ['$http', function($http) {

	var user = null;
	var isLoading = false;

	function onUserLoaded(data){
		
		user = data;
		isLoading = false;
	}
	
	return {

		getUser : function() {
			return user;
		},
		
		loadUser : function(){
			
			//vec je ucitan
			if(user != null)
				return;
			
			//u toku je ucitavanje, ne ucitavaj opet
			if(isLoading)
				return;
			
			$http.get('rest/learningApp/getUser').success(onUserLoaded);
		}
	}

}]);
