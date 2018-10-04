jQuery(function($) {
	initMap($);
});


function AutoCenter() {
	//  Create a new viewpoint bound
	var bounds = new google.maps.LatLngBounds();
	//  Go through each...
	$.each(markers, function(index, marker) {
		bounds.extend(marker.position);
	});
	//  Fit these bounds to the map
	map.fitBounds(bounds);
}

function initMap($) {

	var positionParameter = $("#positionParameter").val();
	var pairs = positionParameter.split("*$*");
	var latCenter = 44.265530;
	var lngCenter = 20.636771;
	
	var locations = [];
	
	for(var i = 0; i < pairs.length; i++){
		
		
		var pair = pairs[i];
		var data = pair.split("#$#");
		var name = "Auto škola "+data[0];
		var uniqueName = data[1];
		var address = data[2];
		var city = data[3];
		var latLng = data[4].split(",");
		var lat = latLng[0];
		var lng = latLng[1];
		
		var link = baseUrl + "spisak-auto-skola/"+uniqueName+"/profil-auto-skole";

		var content = "<span class=\"map\"><a href=\""+link+"\"><b>"+name+"</b></a></span><span class=\"map\">"+address+"</span><span class=\"map\">"+city+"</span>";
		
		var combo = [content, lat, lng];
		
		locations.push(combo);
	}
	
	// Setup the different icons and shadows
	var iconURLPrefix = 'http://maps.google.com/mapfiles/ms/icons/';

	var icons = [ iconURLPrefix + 'red-dot.png',
			iconURLPrefix + 'green-dot.png', iconURLPrefix + 'blue-dot.png',
			iconURLPrefix + 'orange-dot.png', iconURLPrefix + 'purple-dot.png',
			iconURLPrefix + 'pink-dot.png', iconURLPrefix + 'yellow-dot.png' ]
	var icons_length = icons.length;

	var shadow = {
		anchor : new google.maps.Point(15, 33),
		url : iconURLPrefix + 'msmarker.shadow.png'
	};

	var map = new google.maps.Map(document.getElementById('map'), {
		zoom : 7,
		center : new google.maps.LatLng(latCenter, lngCenter),
		mapTypeId : google.maps.MapTypeId.ROADMAP,
		mapTypeControl : false,
		streetViewControl : false,
		panControl : true,
		zoomControlOptions : {
			position : google.maps.ControlPosition.LEFT_TOP
		}
	});

	var infowindow = new google.maps.InfoWindow({
		maxWidth : 300
	});

	var marker;
	var markers = new Array();

	var iconCounter = 0;

	// Add the markers and infowindows to the map
	for ( var i = 0; i < locations.length; i++) {
		marker = new google.maps.Marker(
				{
					position : new google.maps.LatLng(locations[i][1],
							locations[i][2]),
					map : map,
					icon : icons[iconCounter],
					shadow : shadow
				});

		markers.push(marker);

		google.maps.event.addListener(marker, 'click', (function(marker, i) {
			return function() {
				infowindow.setContent(locations[i][0]);
				infowindow.open(map, marker);
			}
		})(marker, i));

		iconCounter++;
		// We only have a limited number of possible icon colors, so we may have to restart the counter
		if (iconCounter >= icons_length) {
			iconCounter = 0;
		}
	}

	AutoCenter();
}
