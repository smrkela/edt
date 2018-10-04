jQuery(function($) {
	handleSubmit($);
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

						var valid = attachments.length > 0;

						if (!valid) {
							showValidationMessage("formValidationMessage",
									"Morate izabrati barem jednu sliku.");
							return;
						}

						var dto = {};

						fillFormDTO(dto, [ "relatedId", "id" ]);

						dto.attachments = attachments;

						doAjaxPost(
								"rest/drivingSchoolAdministration/addDrivingSchoolAlbumImages",
								dto, 'administracija-auto-skole/galerija?id='
										+ dto.relatedId);

					});
};
