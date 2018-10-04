jQuery(function($) {
	handleResetPassword($);
	$("#resetPasswordEmail").tooltip({
		placement : "right",
	});
});

function handleResetPassword($) {
	$("#resetPasswordLink").click(function(e) {
		e.preventDefault();
		resetResetPasswordForm();
		$('#resetPasswordModal').modal('show');
	});
	$("#resetPasswordButton").click(
			function(e) {
				e.preventDefault();
				
				var email = $("#resetPasswordEmail").val();
				var recaptcha_challenge_field = $("#recaptcha_challenge_field").val();
				var recaptcha_response_field = $("#recaptcha_response_field").val();
				
				var valid = true;
				if (!isValidEmail(email)) {
					$("#resetPasswordEmail").tooltip("show");
					valid = false;
				}
				if (!valid) {
					return false;
				}

				$.post(resetPasswordUrl, {
					email : email,
					recaptcha_challenge_field : recaptcha_challenge_field,
					recaptcha_response_field : recaptcha_response_field
				}).done(function() {
					$('#resetPasswordForm').hide();
					$('#confirmationMessage').fadeIn(500);
				}).fail(
						function(data) {
							if (data && data.responseText) {
								var status = JSON.parse(data.responseText);
								showSingleValidationMessage(
										"resetPasswordValidationMessage",
										status.message);
							}
						});

			});
}