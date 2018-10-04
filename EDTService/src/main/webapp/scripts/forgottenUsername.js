jQuery(function($) {
	handleForm($);
	$("#forgottenUsernameEmail").tooltip({
		placement : "right",
	});
});

function handleForm($) {
	$("#forgottenUsernameButton").click(
			function(e) {
				e.preventDefault();

				var email = $("#forgottenUsernameEmail").val();
				var valid = true;

				if (!isValidEmail(email)) {
					$("#forgottenUsernameEmail").tooltip("show");
					valid = false;
				}

				if (!valid) {
					return false;
				}

				var recaptcha_challenge_field = $("#recaptcha_challenge_field")
						.val();
				var recaptcha_response_field = $("#recaptcha_response_field")
						.val();

				$.post(baseUrl + 'rest/user/sendForgottenUsername', {
					email : email,
					recaptcha_challenge_field : recaptcha_challenge_field,
					recaptcha_response_field : recaptcha_response_field
				}).done(function() {
					$('#forgottenUsernameForm').hide();
					$('#confirmationMessage').fadeIn(500);
				}).fail(
						function(data) {
							if (data && data.responseText) {
								var status = JSON.parse(data.responseText);
								showSingleValidationMessage(
										"forgottenUsernameValidationMessage",
										status.message);
							}
						});

			});
}