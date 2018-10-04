jQuery(function($) {
	handleRegister($);
	addTooltips(["firstName", "lastName", "username", "password" , "passwordRepeat", "email", "emailRepeat", "registerForm input[name=isMale]"]);
});

function handleRegister($) {
	jQuery('#registerForm')
			.submit(
					function(e) {
						e.preventDefault();
						var validationMessages = new Array();
						
						var signInProvider = jQuery("#signInProvider");

						var valid = true;

						var firstName = jQuery("#firstName").val();
						if (!hasText(firstName)) {
							jQuery("#firstName").tooltip("show");
							valid = false;
						}
						var lastName = jQuery("#lastName").val();
						if (!hasText(lastName)) {
							jQuery("#lastName").tooltip("show");
							valid = false;
						}
						var username = jQuery("#username").val();
						if (!hasText(username)) {
							jQuery("#username").tooltip("show");
							valid = false;
						}
						
						var password = jQuery("#password").val();
						if (!hasText(password)) {
							jQuery("#password").tooltip("show");
							valid = false;
						}
						
						var passwordRepeat = jQuery("#passwordRepeat").val();
						if (password != passwordRepeat) {
							jQuery("#passwordRepeat").tooltip("show");
							valid = false;
						}
						
						var email = jQuery("#email").val();
						if (!isValidEmail(email)) {
							jQuery("#email").tooltip("show");
							valid = false;
						}
						var emailRepeat = jQuery("#emailRepeat").val();
						if (email != emailRepeat) {
							jQuery("#emailRepeat").tooltip("show");
							valid = false || signInProvider;
						}
						var isMale = jQuery(
								"#registerForm input[name=isMale]:radio:checked")
								.val();
						if (isMale == null) {
							jQuery("#registerForm input[name=isMale]").tooltip(
									"show");
							valid = false;
						}
						
						var category = document.getElementById("drivingCategory");
						var drivingCategory = category.options[category.selectedIndex].value;
						
						if (!hasText(drivingCategory)) {
							jQuery("#tooltipForDrivingCategory").tooltip({
								placement : "right",
							});
							jQuery("#tooltipForDrivingCategory").tooltip("show");
							valid = false;
						}

						if (!valid) {
							return;
						}
						
						var serializedForm = jQuery("#registerForm").serialize();
						showValidationMessage("registerFormValidationMessage",
								validationMessages);
						if (validationMessages.length > 0) {
							return false;
						}

						$
								.post(registerUserUrl, serializedForm)
								.done(
										function() {
											window.location = registerSuccessUrl;
										})
								.fail(
										function(data) {
											if (data && data.responseText) {
												var status = JSON
														.parse(data.responseText);
												showSingleValidationMessage(
														"registerFormValidationMessage",
														status.message);
											}
										});
					});
};


/*--------------------------------------------------------
BEGIN FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
function formGeneral() {

	jQuery('.with-tooltip').tooltip({
		selector : ".input-tooltip"
	});

		
	/*----------- BEGIN chosen CODE -------------------------*/

	jQuery(".chzn-select").chosen();
	jQuery(".chzn-select-deselect").chosen({
		allow_single_deselect : true
	});
	/*----------- END chosen CODE -------------------------*/

}
/*--------------------------------------------------------
END FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
