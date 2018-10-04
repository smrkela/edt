jQuery(function() {

	handleSubmit();
	addTooltips([ "validFrom", "validTo", "active", "licenseType" ]);

});

function handleSubmit() {
	jQuery('#form')
			.submit(
					function(e) {
						e.preventDefault();

						var valid = validateRequiredFields([ "validFrom",
								"validTo", "active", "licenseType" ]);

						if (!valid) {
							return;
						}

						var dto = {};

						fillFormDTO(dto, [ "id", "schoolId", "validFrom",
								"validTo", "active", "licenseType" ]);
						
						//posebno vadimo vrednost za checkbox 
						dto.active = jQuery("#active").is(":checked");

						doAjaxPost("rest/admin/saveDrivingSchoolLicense", dto,
								'administracija/auto-skole/prikazi?id='
										+ dto.schoolId);

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
	jQuery('#validFromDiv').datepicker({
		format : 'yyyy-mm-dd'
	});
	jQuery('#validToDiv').datepicker({
		format : 'yyyy-mm-dd'
	});

	/*----------- END datepicker CODE -------------------------*/

}
/*--------------------------------------------------------
 END FORM-GENERAL.HTML SCRIPTS
 ---------------------------------------------------------*/

