jQuery(function($) {

	handleSubmit($);
	addTooltips([ "question-id", "number", "text", "remark", "points",
			"help-url" ]);
});

function handleSubmit($) {
	$('#form').submit(
			function(e) {
				e.preventDefault();

				var valid = validateRequiredFields([ "question-id", "number",
						"text", "points" ]);

				if (!valid) {
					return;
				}

				var dto = {};

				fillFormDTO(dto, [ "question-id", "number", "text", "remark",
						"points", "help-url", "id" ]);

				doAjaxPost("rest/admin/saveQuestion", dto,
						'administracija/pitanja');

			});
};
