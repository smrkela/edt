$(function() {
	handleSendMessage();
	addTooltips();
});

function addTooltips() {
	jQuery("#inputName").tooltip({
		placement : "right",
	});

	jQuery("#inputEmail").tooltip({
		placement : "right",
	});
	
	jQuery("#inputSubject").tooltip({
		placement : "right",
	});
	
	jQuery("#comment").tooltip({
		placement : "right",
	});
}

function handleSendMessage() {
	jQuery('#contactForm')
			.submit(
					function(e) {
						e.preventDefault();
						var validationMessages = new Array();
						
						var valid = validateRequiredFields([ "inputName", "inputEmail", "inputSubject", "comment" ]);

						if (!valid) {
							return;
						}
						
						var serializedForm = jQuery("#contactForm").serialize();
						
						showValidationMessage("contactFormValidationMessage", validationMessages);
						
						if (validationMessages.length > 0) {
							return false;
						}
						
						var recaptcha_challenge_field = jQuery("#recaptcha_challenge_field").val();
						var recaptcha_response_field = jQuery("#recaptcha_response_field").val();
						
						var contactUrl = baseUrl + "rest/kontakt/posalji-mail";
						var contactSuccessUrl = baseUrl + "email-poslat";

						jQuery
								.post(contactUrl, serializedForm)
								.done(
										function() {
											window.location = contactSuccessUrl;
										})
								.fail(
										function(data) {
											if (data && data.responseText) {
												var status = JSON
														.parse(data.responseText);
												showSingleValidationMessage(
														"contactFormValidationMessage",
														status.message);
											}
										});
					});
};
