jQuery(function($) {
	handleSubmit($);
	addTooltips([ "name", "description" ]);
});

function handleSubmit($) {
	$('#form').submit(
			function(e) {
				e.preventDefault();

				var valid = validateRequiredFields([ "name" ]);

				if (!valid) {
					return;
				}

				var dto = {};

				fillFormDTO(dto, [ "name", "description", "id" ]);

				var albumId = $("#albumId").val();

				doAjaxPost("rest/admin/updateImageAlbumImage", dto,
						'administracija/slike/uredi-slike?id=' + albumId);

			});
};
