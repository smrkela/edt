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
		var notificationId = $("#notificationId").val();

		$.post(baseUrl + "saveNotificationComment", {
			notificationId : notificationId,
			comment : text
		}).done(function() {
			location.reload();
		});
	});
};
