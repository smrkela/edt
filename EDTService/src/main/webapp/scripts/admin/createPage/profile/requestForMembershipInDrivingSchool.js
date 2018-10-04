jQuery(function($) {
	handleSubmit($);
	addTooltips([ "drivingSchool", "eMail", "comment"]);
});
var attachments;

function handleSubmit($) {
	$('#form').submit(function(e) {
		e.preventDefault();
		
		var drivingSchool = document.getElementById("drivingSchool");
		var drivingSchoolID = drivingSchool.options[drivingSchool.selectedIndex].value;

		if (drivingSchoolID == ""){
			jQuery("#tooltipForDrivingSchool").tooltip({
				placement : "right",
			});
			jQuery("#tooltipForDrivingSchool").tooltip("show");
			return;
		}
		
		//ako je prikazana validaciona poruka (auto skola nema mail) i trazi se e-mail onda tek proveriti da li je isti u ispravnom formatu
		var email = jQuery("#eMail").val();
		
		if ($("#emailValidation").is(":visible")){
			if (!isValidEmail(email)) {
				jQuery("#eMail").tooltip("show");
				return;
			}
		}
		
		//provera duzine polja komentara (ne sme biti vece od 2000 karaktera)
		var commentValue = jQuery("#comment").val();
		
		if (commentValue.length > 2000){
			jQuery("#comment").tooltip("show");
			return;
		}
		
		var dto = {};

		fillFormDTO(dto, [ "comment", "eMail" ]);
		
		fillFormDTOCheckboxes(dto, [ "receiveNotifications" ]);
		
		var drivingSchoolDTO = {
			id : drivingSchoolID
		};
		
		dto.drivingSchool = drivingSchoolDTO;

		
		jQuery.ajax({
			url : baseUrl + "rest/admin/submitRequestForMembership",
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify(dto),
			success : function(data) {
				if (data.status == "OK") {
					window.location.assign(baseUrl + "zahtev-za-clanstvo-uspesno-poslat");
				} else {
					var validationMessage = jQuery("#formValidationMessage");
					validationMessage.html("");
					validationMessage.hide();
					validationMessage.append("<p>" + data.message + "</p>");
					validationMessage.fadeIn(500);
					
					if (! $("#emailValidation").is(":visible")) {
						var emailValidation = jQuery("#emailValidation");
						emailValidation.hide();
						emailValidation.fadeIn(500);	
					}
				}
			}
		});
	});
};

/*--------------------------------------------------------
BEGIN FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
function formGeneral() {

	jQuery('.with-tooltip').tooltip({
		selector : ".input-tooltip"
	});

		
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