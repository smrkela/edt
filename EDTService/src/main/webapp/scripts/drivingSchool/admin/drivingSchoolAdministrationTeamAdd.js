jQuery(function($) {
	initializeUploadForm();
	handleSubmit($);
	addTooltips([ "firstName", "lastName", "role", "experience", "comment" ]);
});

var attachments;

function setAttachments(variableName, atts) {

	attachments = atts;
}

function handleSubmit($) {
	$('#form').submit(function(e) {
		e.preventDefault();

		var valid = validateRequiredFields([ "firstName", "lastName", "role" ]);

		if (!valid) {
			return;
		}

		var dto = {};

		fillFormDTO(dto, [ "firstName", "lastName", "role", "experience", "comment", "schoolId", "id", "orderIndex" ]);

		if (attachments && attachments.length > 0)
			dto.image = attachments[0].id;

		doAjaxPost("rest/drivingSchoolAdministration/addDrivingSchoolEmployee", dto, 'administracija-auto-skole/nas-tim?id=' + dto.schoolId);

	});
};
