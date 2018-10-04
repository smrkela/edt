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

						var albumId = $("#albumId").val();

						doAjaxPost(
								"rest/drivingSchoolAdministration/updateDrivingSchoolAlbumImage",
								dto,
								'administracija-auto-skole/galerija/uredi-slike?id='
										+ dto.relatedId + "&albumId=" + albumId);

					});
};
