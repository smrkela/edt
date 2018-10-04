jQuery(function($) {
	initializeUploadForm();
	handleSubmit($);
	addTooltips([ "firstName", "lastName", "comment", "email", "phone", "isMale", "address", "city", "registerDate", "isTheoryCompleted", "isTheoryPassed", 
	              "theoryPassedDate", "isPracticeCompleted", "isPracticePassed", "practicePassedDate", "isFirstAidPassed", "firstAidPassedDate", "isAllPassed", 
	              "allPassedDate", "categoryId", "sendNotifications", "inviteToJoin", "tooltipForCategory"]);
	
// ukoliko se novi ucenik kreira na osnovu zahteva za clanstvom u nekoj auto skoli, onda nema smisla slati poziv za prikljucenje tom uceniku posto je on vec korisnik sajta
	if (jQuery("#membershipRequestToken").val() && jQuery("#userId").val()) {
		jQuery('#inviteToJoin').prop('checked', false);
		jQuery("#inviteToJoin").attr("disabled", true);
	}
	
});

function handleSubmit($) {
	$('#form').submit(function(e) {
		e.preventDefault();

		var valid = validateRequiredFields(["firstName", "lastName", "categoryId"]); //"email", "phone", 
		
		var category = document.getElementById("categoryId");
		var categoryID = category.options[category.selectedIndex].value;

		if (!valid) {
			if (categoryID == ""){
				jQuery("#tooltipForCategory").tooltip({
					placement : "right",
				});
				jQuery("#tooltipForCategory").tooltip("show");
			}
			return;
		}
		
		// ukoliko je stiklirano slanje obavestenja korisniku a nije unet e-mail
		if (document.getElementById("sendNotifications").checked){
			if (!validateRequiredFields(["email"])){
				jQuery("#invitationValidationMessage").fadeIn(500);
				return;
			}
		}
		
		// ukoliko je stiklirano slanje poziva korisniku a nije unet e-mail
		if (document.getElementById("inviteToJoin").checked){
			if (!validateRequiredFields(["email"])){
				jQuery("#invitationValidationMessage").fadeIn(500);
				return;
			}
		}
		
		var dto = {};

		fillFormDTO(dto, ["firstName", "lastName", "comment", "email", "phone", "address", "city", "registerDate", "theoryPassedDate", "practicePassedDate", 
		                  "firstAidPassedDate", "allPassedDate", "categoryId", "schoolId", "id", "membershipRequestToken", "userId"]);
		fillFormDTOCheckboxes(dto, ["isTheoryCompleted", "isTheoryPassed", "isPracticeCompleted", "isPracticePassed", "isFirstAidPassed", "sendNotifications", 
		                            "isAllPassed", "inviteToJoin"]);
		fillFormDTORadio(dto, "form", ["isMale"]);
		
		
		doAjaxPost("rest/drivingSchoolAdministration/addDrivingSchoolStudent", dto, 'administracija-auto-skole/ucenici?id=' + dto.schoolId);
	});
};

/*--------------------------------------------------------
BEGIN FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
function formGeneral() {

	jQuery('.with-tooltip').tooltip({
		selector : ".input-tooltip"
	});

	/*----------- BEGIN datepicker CODE -------------------------*/
	jQuery('#registerDateDiv').datepicker({
		format : 'yyyy-mm-dd'
	});
	jQuery('#theoryPassedDateDiv').datepicker({
		format : 'yyyy-mm-dd'
	});
	jQuery('#practicePassedDateDiv').datepicker({
		format : 'yyyy-mm-dd'
	});
	jQuery('#firstAidPassedDateDiv').datepicker({
		format : 'yyyy-mm-dd'
	});
	jQuery('#allPassedDateDiv').datepicker({
		format : 'yyyy-mm-dd'
	});

	/*----------- END datepicker CODE -------------------------*/
	
	
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


jQuery('#form #cancelForm').on('click', function() {
	
	window.location = baseUrl + 'administracija-auto-skole/ucenici?id='+jQuery("#schoolId").val();

});