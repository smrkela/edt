jQuery.noConflict();

/*--------------------------------------------------------
 BEGIN FORM-GENERAL.HTML SCRIPTS
 ---------------------------------------------------------*/
function formGeneral() {

	jQuery('.with-tooltip').tooltip({
		selector : ".input-tooltip"
	});

	/*----------- BEGIN tagsInput CODE -------------------------*/
	// jQuery('#tags').tagsInput();
	/*----------- END tagsInput CODE -------------------------*/

	/*----------- BEGIN chosen CODE -------------------------*/

	jQuery(".chzn-select").chosen();
	jQuery(".chzn-select-deselect").chosen({
		allow_single_deselect : true
	});
	/*----------- END chosen CODE -------------------------*/

	/*----------- BEGIN uniform CODE -------------------------*/
	jQuery('.uniform').uniform();
	/*----------- END uniform CODE -------------------------*/

	/*----------- BEGIN toggleButtons CODE -------------------------*/
	jQuery('#normal-toggle-button').toggleButtons();

	jQuery('#callback-toggle-button').toggleButtons({
		onChange : function($el, status, e) {
			console.log($el, status, e);
			jQuery('#magic-text').text("Status is: " + status);
		}
	});
	jQuery('#text-toggle-button').toggleButtons({
		width : 220,
		label : {
			enabled : "Bizstrap",
			disabled : "Admin"
		}
	});
	jQuery('#danger-toggle-button').toggleButtons({
		style : {
			// Accepted values ["primary", "danger", "info", "success",
			// "warning"] or nothing
			enabled : "danger",
			disabled : "info"
		}
	});
	/*----------- END toggleButtons CODE -------------------------*/

	/*----------- BEGIN dualListBox CODE -------------------------*/
	jQuery.configureBoxes();
	/*----------- END dualListBox CODE -------------------------*/
}
/*--------------------------------------------------------
 END FORM-GENERAL.HTML SCRIPTS
 ---------------------------------------------------------*/

// Daci, 10.01.2013.
// Servis za preracunavanje prosecnih ocena auto skola
jQuery("#calculateAverageMark")
		.click(
				function(e) {
					e.preventDefault();

					var averageMarkUrl = baseUrl
							+ "rest/admin/recalculateAverageMarks";

					jQuery.post(averageMarkUrl)

					.done(function() {
						jQuery('#confirmationMessageAverageMark').fadeIn(500);
					}).fail(
							function(data) {
								if (data && data.responseText) {
									var status = JSON.parse(data.responseText);
									showSingleValidationMessage(
											"errorMessageAverageMark",
											status.message);
								}
							});
				});

// Daci, 10.01.2013.
// Servis za preracunavanje cene obuke za B kategoriju
jQuery("#calculateCategoryBPrice").click(
		function(e) {
			e.preventDefault();

			var recalculateCategoryBPriceUrl = baseUrl
					+ "rest/admin/recalculateCategoryBPrice";

			jQuery.post(recalculateCategoryBPriceUrl)

			.done(function() {
				jQuery('#confirmationMessageCategoryBPrice').fadeIn(500);
			}).fail(
					function(data) {
						if (data && data.responseText) {
							var status = JSON.parse(data.responseText);
							showSingleValidationMessage(
									"errorMessageCategoryBPrice",
									status.message);
						}
					});

		});

// Daci, 25.01.2013.
// Servis za sinhronizaciju baza Sajta i Foruma - promena vrednosti u combobox-u
// za tip sinhronizacije
jQuery("#sync-type").change(function(e) {
	e.preventDefault();

	var selectedSyncType = $('#sync-type').val();

	if (hasText(selectedSyncType)) {

		if (selectedSyncType == "Kompletna sinhronizacija baza") {
			// $('#user-name-div').fadeOut(500);
			$('#user-email-div').fadeOut(500);
		}

		if (selectedSyncType == "Sinhronizacija korisnika") {
			// $('#user-name-div').fadeIn(500);
			$('#user-email-div').fadeIn(500);
		}
	} else {
		// if( $('#user-name-div').is(':visible') ) {
		// $('#user-name-div').fadeOut(500);
		// }

		if ($('#user-email-div').is(':visible')) {
			$('#user-email-div').fadeOut(500);
		}
	}

});

// Daci, 25.01.2013.
// Servis za sinhronizaciju baza Sajta i Foruma - klik na dugme
jQuery("#synchronizeDB")
		.click(
				function() {

					var selectedSyncType = jQuery('#sync-type').val();
					var sendMail = jQuery("#poslatiMail").prop("checked");
					// alert(sendMail);
					var syncDBURL = null;

					if (hasText(selectedSyncType)) {
						if (selectedSyncType == "Kompletna sinhronizacija baza") {
							syncDBURL = baseUrl
									+ "administracija/adminServisi/sinhronizacijaBaza?sync-type="
									+ selectedSyncType + "&send-notifications="
									+ sendMail;
							// alert(syncDBURL);
							window.location.href = syncDBURL;
						}

						if (selectedSyncType == "Sinhronizacija korisnika") {

							// var userName = jQuery('#user-name').val();
							var userEmail = jQuery('#user-email').val();

							if (hasText(userEmail)) { // hasText(userName) &&
								syncDBURL = baseUrl
										+ "administracija/adminServisi/sinhronizacijaBaza?sync-type="
										+ selectedSyncType + "&user-email="
										+ userEmail + "&send-notifications="
										+ sendMail; // "&user-name=" + userName
								// +
								// alert(syncDBURL);
								window.location.href = syncDBURL;
							} else {
								showSingleValidationMessage(
										"errorValidationMessage",
										"E-MAIL KORISNIKA MORA BITI UPISAN!");
							}
						}
					} else {
						showSingleValidationMessage("errorValidationMessage",
								"MORA BITI SELEKTOVAN TIP SINHRONIZACIJE!");
					}
				});

jQuery("#calculateDailyTest").click(
		function(e) {
			e.preventDefault();

			var dailyTestUrl = baseUrl + "rest/admin/generateDailyTest";

			jQuery.post(dailyTestUrl)

			.done(function() {
				jQuery('#confirmationMessageDailyTest').fadeIn(500);
			}).fail(
					function(data) {

						if (data && data.responseText) {

							var status = JSON.parse(data.responseText);
							showSingleValidationMessage(
									"errorMessageDailyTest", status.message);
						}
					});
		});

jQuery("#updateUserExperience").click(
		function(e) {
			e.preventDefault();

			var dailyTestUrl = baseUrl + "rest/admin/updateUserExperience";

			jQuery.post(dailyTestUrl)

			.done(function() {
				jQuery('#confirmationMessageUserExperience').fadeIn(500);
			}).fail(
					function(data) {

						if (data && data.responseText) {

							var status = JSON.parse(data.responseText);
							showSingleValidationMessage(
									"errorMessageUserExperience",
									status.message);
						}
					});
		});
