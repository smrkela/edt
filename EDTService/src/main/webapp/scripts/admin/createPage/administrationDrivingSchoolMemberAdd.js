jQuery(function($) {

	handleSubmit($);
	addTooltips([ "email", "role" ]);
});

function handleSubmit($) {
	$('#form')
			.submit(
					function(e) {
						e.preventDefault();

						var valid = validateRequiredFields([ "email", "role" ]);

						if (!valid) {
							return;
						}

						var dto = {};

						fillFormDTO(dto, [ "email", "role", "id", "schoolId" ]);

						doAjaxPost("rest/admin/saveDrivingSchoolMember", dto,
								'administracija/auto-skole/prikazi?id='
										+ dto.schoolId);

					});
};
