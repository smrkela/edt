jQuery(function($) {
	initializeUploadForm();
	handleSubmit($);
	addTooltips([ "make", "model", "type", "year", "description" ]);
});

var attachments;

function setAttachments(variableName, atts) {

	attachments = atts;
}

function handleSubmit($) {
	$('#form')
			.submit(
					function(e) {
						e.preventDefault();

						var valid = validateRequiredFields([ "make", "model", "year"]);

						if (!valid) {
							return;
						}

						var dto = {};

						fillFormDTO(dto, [ "id", "schoolId", "make", "model", "type", "year", "description", "orderIndex" ]);

						if (attachments && attachments.length > 0)
							dto.image = attachments[0].id;

						doAjaxPost(
								"rest/drivingSchoolAdministration/addDrivingSchoolCar",
								dto, 'administracija-auto-skole/vozni-park?id='
										+ dto.schoolId);

					});
};
