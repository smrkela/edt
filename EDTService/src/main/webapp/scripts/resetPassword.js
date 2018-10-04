var rpUrl;
var tokenUrlParam;

jQuery(function($) {
	rpUrl = baseUrl + "rest/user/resetPassword";
	tokenUrlParam = getUrlParameter("token");
	handlepr($);
});

function handlepr($) {

	$("#prButton")
			.click(

					function() {
						var validationMessages = new Array();
						var prPassword1 = $("#prPassword1").val();
						var prPassword2 = $("#prPassword2").val();
						if (!hasText(prPassword1)) {
							validationMessages[0] = $("#prPassword1").attr(
									"data-message");
						}
						if (!hasText(prPassword2)) {
							validationMessages[1] = $("#prPassword2").attr(
									"data-message");
						}

						if (prPassword1 != prPassword2) {
							validationMessages[2] = "Unesene šifre nisu jednake";
						}

						showValidationMessage("prValidationMessage",
								validationMessages);
						if (validationMessages.length > 0) {
							return false;
						}

						$
								.post(rpUrl, {
									password : prPassword1,
									token : tokenUrlParam
								})
								.done(function() {
									$("#prFieldset").hide();
									$("#prSuccessMessage").fadeIn(500);
								})
								.fail(
										function(data) {

											if (data && data.responseText) {
												var status = JSON
														.parse(data.responseText);
												showSingleValidationMessage(
														"prValidationMessage",
														status.message);
											} else {

												showSingleValidationMessage(
														"prValidationMessage",
														"Promena šifre neuspešna. Desila se greška tokom izvršenja. Vaša šifra nije promenjena.");
											}
										});
						return false;
					});
}