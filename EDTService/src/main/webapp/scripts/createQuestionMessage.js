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
		var pageUniqueName = $("#questionId").val();

		$.post(baseUrl + "saveQuestionMessage", {
			questionId : pageUniqueName,
			message : text
		}).done(function(result) {
			location.reload();
		});
	});
};
