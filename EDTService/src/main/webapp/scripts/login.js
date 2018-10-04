jQuery(document).ready(function($) {

	$(function() {
		addTooltips([ "login_username", "login_password" ]);
		handleClickOnLogin();
	});
	
	function handleClickOnLogin() {
		jQuery('#login_form')
				.submit(
						function(e) {

//							e.preventDefault();
//							
//							var inputUsername = jQuery("#login_username").val();
//							var inputPassword = jQuery("#login_password").val();
//							
//							jQuery.ajax({
////								type: 'GET',
//								url: 'http://localhost/customLogin.php',
////								contentType: "application/jsonp",
//								dataType: "jsonp",
//								jsonp:"loginSuccessful", 
//								crossDomain: true,
//								xhrFields: {
//								      withCredentials: true
//								},
//								data: { username: inputUsername, password: inputPassword},
//								success: function(data) {
//									document.getElementById("login_form").submit();
//								},
//								error: function(response) {
//									document.getElementById("login_form").submit();
//								},
//							});
							
						}
				);
	};
});