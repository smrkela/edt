jQuery(function($) {

	handleSubmit($);
	addTooltips([ "comment" ]);
});

function handleSubmit($) {
	$('#form').submit(function(e) {
		e.preventDefault();

		var valid = validateRequiredFields([ "comment" ]);

		if (!valid) {
			return;
		}

		var text = $("#comment").val();
		var pageUniqueName = $("#pageUniqueName").val();

		$.post(baseUrl + "savePageComment", {
			pageUniqueName : pageUniqueName,
			comment : text
		}).done(function() {
			location.reload();
		});
	});
};
