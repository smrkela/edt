jQuery.noConflict();

jQuery(function($) {
});

jQuery("#submit").click(function(e) {
	e.preventDefault();

	submitForm();
});

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

function handleSubmit($) {
	$('#form').submit(function(e) {
		e.preventDefault();

		submitForm();
	});
};

/*--------------------------------------------------------
 DACI -> sort combobox
 ---------------------------------------------------------*/
jQuery("#sort-type").change(submitForm);

function submitForm() {

	var searchCity = jQuery('#search-city').val();
	var searchCategory = jQuery('#search-category').val();
	var searchMarkFrom = jQuery('#search-mark-from').val();
	var searchMarkTo = jQuery('#search-mark-to').val();
	var searchPriceFrom = jQuery('#search-price-from').val();
	var searchPriceTo = jQuery('#search-price-to').val();
	var searchText = jQuery('#search-text').val();
	var sortType = jQuery('#sort-type').val();
	var currentindex = 0;

	var listOfDrivingSchoolURL = baseUrl + "spisak-auto-skola?"
			+ "search-text=" + searchText.trim() + "&search-city=" + searchCity
			+ "&search-category=" + searchCategory + "&search-mark-from="
			+ searchMarkFrom + "&search-mark-to=" + searchMarkTo
			+ "&search-price-from=" + searchPriceFrom.trim() + "&search-price-to="
			+ searchPriceTo.trim() + "&sort-type=" + sortType + "&startingIndex="
			+ currentindex;

	// alert(listOfDrivingSchoolURL);

	window.location.href = listOfDrivingSchoolURL;
}

// Daci, 24.02.2014.
// Resetovanje svih polja na search formi
jQuery("#resetSearch").click(
		function(e) {
			e.preventDefault();

			// reset nam je postavljanje svih vrednosti na pocetne,
			// uz zadrzavanje sort-a i vracanje na pocetnu stranicu

			var sortType = jQuery('#sort-type').val();

			var listOfDrivingSchoolURL = baseUrl + "spisak-auto-skola?"
					+ "sort-type=" + sortType;

			// alert(listOfDrivingSchoolURL);

			window.location.href = listOfDrivingSchoolURL;
		});


//Daci, 04.03.2014.
jQuery(function() {
	jQuery("#form").keypress(function (e) {
        if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
        	jQuery("#form").submit();
            return false;
        } else {
            return true;
        }
    });
});