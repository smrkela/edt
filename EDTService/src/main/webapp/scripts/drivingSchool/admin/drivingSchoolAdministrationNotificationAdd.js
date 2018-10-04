jQuery(function($) {
	handleSubmit($);
	addTooltips([ "title", "contentText" ]);
	handleCheckBoxes();
	
	$("#contentText").jqte();
});


/**
 * Daci, 10.01.2015.
 */
function handleCheckBoxes(){
	
	var checked = jQuery("#sendNotification").is(":checked");
	var notificationId = jQuery("#id").val();
	
	if (notificationId){
		//izmena postojeceg obavestenja
		if(checked){
			//ako je obavestenje vec poslato blokirati ponovno stikliranje i slanje mail-ova
			document.getElementById("sendNotification").disabled = true;
			document.getElementById("requestNotificationReadConfirmation").disabled = true;
		} else {
			document.getElementById("requestNotificationReadConfirmation").disabled = true;
		};
	} else {
		//novo obavestenje
		document.getElementById("sendNotification").disabled = false;
		document.getElementById("requestNotificationReadConfirmation").disabled = true;
	}
}


function handleSubmit($) {
	$('#form')
			.submit(
					function(e) {
						e.preventDefault();

						var valid = validateRequiredFields([ "title", "contentText" ]);

						if (!valid) {
							return;
						}

						var dto = {};

						fillFormDTO(dto, [ "title", "schoolId", "id" ]);
						
						//posebno uzimamo contentText jer ne smemo koristiti content rec
						dto.content = $("#contentText").val();
						
						fillFormDTOCheckboxes(dto, [ "sendNotification", "requestNotificationReadConfirmation" ]);

						doAjaxPost("rest/drivingSchoolAdministration/addDrivingSchoolNotification",
									dto,
									'administracija-auto-skole/obavestenja?id='
									+ dto.schoolId);
					});
};

/**
 * Daci, 08.01.2015.
 */
jQuery("#sendNotification")
		.click(
				function(e) {

					var checked = jQuery("#sendNotification").is(":checked");
					if(checked){
						document.getElementById("requestNotificationReadConfirmation").disabled = false;
					} else {
						document.getElementById("requestNotificationReadConfirmation").disabled = true;
						jQuery("#requestNotificationReadConfirmation").attr("checked", false);
					}

				});


jQuery('#form #cancelForm').on('click', function() {
	
	window.location = baseUrl + 'administracija-auto-skole/obavestenja?id='+jQuery("#schoolId").val();

});