jQuery(function($) {

	handleSubmit($);
	addTooltips([ "title", "uniqueName", "orderIndex", "category" ]);

	initTinyMceRich("content");
});

function handleSubmit($) {
	$('#form').submit(
			function(e) {
				e.preventDefault();

				var valid = validateRequiredFields([ "title", "uniqueName", "orderIndex" ]);

				if (!valid) {
					return;
				}

				var dto = {};

				fillFormDTO(dto, [ "title", "uniqueName", "id", "orderIndex" ]);
				
				dto.content = tinyMCE.get("content").getContent();
				
				var categoryValue = $("#category").val();
				var categoryDTO = {
					id : categoryValue
				};
				
				dto.categoryDTO = categoryDTO;

				doAjaxPost("rest/page/admin/saveHelp", dto, 'administracija/pomoc');

			});
};