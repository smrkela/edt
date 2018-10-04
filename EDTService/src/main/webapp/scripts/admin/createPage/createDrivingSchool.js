jQuery(function($) {

	handleSubmit($);
	addTooltips([ "name", "uniqueName" ]);
});

function handleSubmit($) {
	$('#form').submit(
			function(e) {
				e.preventDefault();

				var valid = validateRequiredFields([ "name", "uniqueName" ]);

				if (!valid) {
					return;
				}

				var dto = {};

				fillFormDTO(dto, [ "name", "uniqueName", "id" ]);
				
				var isHidden = document.getElementById("isHidden").checked;
				var hasPermit = document.getElementById("hasPermit").checked;
				
				dto.isHidden = isHidden == true ? "true" : "false";
				dto.hasPermit = hasPermit == true ? "true" : "false";

				doAjaxPost("rest/admin/saveDrivingSchool", dto,
						'spisak-auto-skola/'+dto.uniqueName+'/profil-auto-skole');

			});
};
