jQuery(function($) {
	handleSubmit($);
	addTooltips([ "firstName", "lastName" ]);
});

var attachments;

function setAttachments(variableName, atts) {

	attachments = atts;
}

function handleSubmit($) {
	$('#form').submit(function(e) {
		e.preventDefault();

		var valid = validateRequiredFields([ "firstName", "lastName", "drivingCategory" ]);
		
		var category = document.getElementById("drivingCategory");
		var drivingCategory = category.options[category.selectedIndex].value;

		if (!valid) {
			if (drivingCategory == ""){
				jQuery("#tooltipForDrivingCategory").tooltip({
					placement : "right",
				});
				jQuery("#tooltipForDrivingCategory").tooltip("show");
			}
			return;
		}
		
		var dto = {};

		fillFormDTO(dto, [ "firstName", "lastName" ]);

		var categoryDTO = {
							id : drivingCategory
						  };
		
		dto["driving-category"] = categoryDTO;

		if (attachments && attachments.length > 0)
			dto.profileImage = attachments[0].id;

		doAjaxPost("rest/admin/updateMyProfile", dto, 'moj-profil');
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

}
/*--------------------------------------------------------
END FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/