jQuery(function($) {
	handleSendMessage($);
	addTooltips([ "inputName", "inputEmail", "comment" ]);
	initMap($);
});

function addTooltips() {
	jQuery("#inputName").tooltip({
		placement : "right",
	});

	jQuery("#inputEmail").tooltip({
		placement : "right",
	});

	jQuery("#comment").tooltip({
		placement : "right",
	});
}

function handleSendMessage($) {
	jQuery('#contactForm')
			.submit(
					function(e) {
						e.preventDefault();
						var validationMessages = new Array();
						var successMessages = new Array();

						var valid = validateRequiredFields([ "inputName",
								"inputEmail", "comment" ]);

						if (!valid) {
							return;
						}

						var serializedForm = jQuery("#contactForm").serialize();
						showValidationMessage("contactFormValidationMessage",
								validationMessages);
						if (validationMessages.length > 0) {
							return false;
						}

						var contactUrl = baseUrl
								+ "rest/kontakt/posalji-mail-auto-skola";

						jQuery
								.post(contactUrl, serializedForm)
								.done(function() {

									$("#successfullySentMessage").fadeIn(900);

									jQuery("#inputName").val(function() {
										return this.defaultValue;
									});

									jQuery("#inputEmail").val(function() {
										return this.defaultValue;
									});

									jQuery("#comment").val(function() {
										return this.defaultValue;
									});

								})
								.fail(
										function(data) {
											if (data && data.responseText) {
												var status = JSON
														.parse(data.responseText).message;

												if (status == "Pogrešno unet sigurnosni kod.") {
													showSingleValidationMessage(
															"errorSendingMessageWrongCode",
															status);
												} else {
													$("#errorSendingMessage")
															.fadeIn(900);
												}

											}
										});
					});
};

function initMap($) {

	var name = $("#name").val();
	var city = $("#city").val();
	var address = $("#address").val();
	var position = $("#position").val();
	var latLng = position.split(",");
	var lat = latLng[0];
	var lng = latLng[1];

	var info = "<span class='gmap'><b>Auto &Scaron;kola " + name
			+ "</b></span><span class='gmap'>" + address
			+ "</span><span class='gmap'> " + city + "</span>";

	var myOptions = {
		zoom : 15,
		center : new google.maps.LatLng(lat, lng),
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

	map = new google.maps.Map(document.getElementById("gmap_canvas"), myOptions);

	marker = new google.maps.Marker({
		map : map,
		position : new google.maps.LatLng(lat, lng),
		title: name
	});

	infowindow = new google.maps.InfoWindow({
		content : info
	});

	google.maps.event.addListener(marker, "click", function() {
		infowindow.open(map, marker);
	});

	infowindow.open(map, marker);
}
