jQuery(function($) {

	handleSubmit($);
	addTooltips([ "name", "orderIndex", "note", "pageType" ]);
});

function handleSubmit($) {
	$('#form').submit(
			function(e) {
				e.preventDefault();

				var valid = validateRequiredFields([ "name", "orderIndex",
						"note", "pageType" ]);

				if (!valid) {
					return;
				}

				var dto = {};

				fillFormDTO(dto, [ "name", "orderIndex", "id", "note",
						"pageType" ]);

				doAjaxPost("rest/page/admin/savePageCategory", dto,
						'administracija/kategorije-vesti');

			});
};
