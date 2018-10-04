jQuery(function($) {
	handleSubmit($);
	addTooltips([ "name", "description" ]);
});

function handleSubmit($) {
	$('#form').submit(function(e) {
		e.preventDefault();

		var valid = validateRequiredFields([ "name" ]);

		if (!valid) {
			return;
		}

		var dto = {};

		fillFormDTO(dto, [ "name", "description", "id" ]);

		doAjaxPost("rest/admin/addImageAlbum", dto, 'administracija/slike');

	});
};
