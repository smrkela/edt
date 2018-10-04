jQuery(function($) {
	handleSubmit($);
	addTooltips([ "name", "description" ]);
});

function handleSubmit($) {
	$('#form')
			.submit(
					function(e) {
						e.preventDefault();

						var valid = validateRequiredFields([ "name" ]);

						if (!valid) {
							return;
						}

						var dto = {};

						fillFormDTO(dto, [ "name", "description", "relatedId",
								"id" ]);

						doAjaxPost(
								"rest/drivingSchoolAdministration/addDrivingSchoolAlbum",
								dto, 'administracija-auto-skole/galerija?id='
										+ dto.relatedId);

					});
};
