var baseUrl;
var registerUserUrl;
var resetPasswordUrl;
var registerSuccessUrl;
var loginUrl;
var logoutUrl;
var membershipRequestDecisionUrl;
var membershipRequestUrl;
var membershipSuccessNewUrl;
var membershipSuccessExistingUrl;
var connectToExistingStudentUrl;
var connectToExistingStudentSuccessUrl;
var removeStudentFromMembershipUrl;
var deleteStudentUrl;
var deleteMembershipRequestUrl;
var updateReceiveNotificationsUrl;
var listOfMembershipsUrl;

jQuery(document).ready(function($) {

	// jQuery('.main-content').hide().fadeIn(600);
	setUpUrls();
	handleLogout();
});

function showValidationMessage(idMessage, validationMessages) {

	var messageDiv = jQuery("#" + idMessage);
	messageDiv.html("");
	messageDiv.hide();
	for ( var i in validationMessages) {
		var message = validationMessages[i];
		messageDiv.append("<p>" + message + "</p>");
	}
	if (validationMessages.length > 0) {
		messageDiv.fadeIn(500);
	}
}
function showSingleValidationMessage(idMessage, validationMessage) {
	var messageDiv = jQuery("#" + idMessage);
	messageDiv.html("");
	messageDiv.hide();
	messageDiv.append("<p>" + validationMessage + "</p>");
	messageDiv.fadeIn(500);
}

function hasText(text) {
	return text != null && jQuery.trim(text) != "";
}

function isValidEmail(email) {
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (!filter.test(email)) {
		return false;
	}
	return true;
}

function handleLogout() {
	jQuery('#logout').click(function(e) {
		e.preventDefault();
		jQuery.get(logoutUrl).done(function() {
			window.location = baseUrl;
		}).fail(function() {
			alert("Error");
		});
		return false;
	});
}

function setUpUrls() {
	baseUrl = jQuery("#baseUrl").val();
	registerUserUrl = baseUrl + "rest/user/registerUser";
	registerSuccessUrl = baseUrl + "registracija-uspesna";
	loginUrl = baseUrl + "login";
	logoutUrl = baseUrl + "logout";
	resetPasswordUrl = baseUrl + "rest/user/askForResetPassword";
	membershipRequestDecisionUrl = baseUrl + "rest/drivingSchoolAdministration/membershipRequestDecision"; 
	membershipRequestUrl = baseUrl + "administracija-auto-skole/zahtevi-za-clanstvo?id=";
	membershipSuccessNewUrl = baseUrl + "administracija-auto-skole/ucenici/dodaj?id=";
	membershipSuccessExistingUrl = baseUrl + "administracija-auto-skole/ucenici/povezi-sa-postojecim?id=";
	connectToExistingStudentUrl = baseUrl + "rest/drivingSchoolAdministration/connectToExistingStudent";
	connectToExistingStudentSuccessUrl = baseUrl + "administracija-auto-skole/zahtevi-za-clanstvo?id=";
	removeStudentFromMembershipUrl = baseUrl + "rest/drivingSchoolAdministration/removeStudentFromMembership";
	deleteStudentUrl = baseUrl + "administracija-auto-skole/ucenici/obrisi?id={0}&studentId={1}";
	deleteMembershipRequestUrl = baseUrl + "rest/drivingSchoolAdministration/deleteMembershipRequest";
	updateReceiveNotificationsUrl = baseUrl + "rest/drivingSchoolAdministration/updateReceiveNotifications";
	listOfMembershipsUrl = baseUrl + "lista-clanstava";
}

function getUrlParameter(name) {
	var results = new RegExp('[\\?&amp;]' + name + '=([^&amp;#]*)')
			.exec(window.location.href);
	if (results != null) {
		return results[1];
	} else {
		return null;
	}
};

function addTooltips(fieldIdsArray) {

	for ( var i = 0; i < fieldIdsArray.length; i++) {

		var fieldId = fieldIdsArray[i];

		var fieldName = "#" + fieldId;

		jQuery(fieldName).tooltip({
			placement : "right",
		});
	}
}

function validateRequiredFields(fieldIdsArray) {

	var valid = true;

	for ( var i = 0; i < fieldIdsArray.length; i++) {

		var fieldId = fieldIdsArray[i];
		var fieldName = "#" + fieldId;

		var fieldValue = jQuery(fieldName).val();

		if (!hasText(fieldValue)) {
			jQuery(fieldName).tooltip("show");
			valid = false;
		}
	}

	return valid;
}


function fillFormDTO(dto, fieldIdsArray) {

	for ( var i = 0; i < fieldIdsArray.length; i++) {

		var fieldId = fieldIdsArray[i];

		var fieldValue = jQuery("#" + fieldId).val();

		if (hasText(fieldValue)) {

			dto[fieldId] = fieldValue;
		} else {

			dto[fieldId] = null;
		}
	}
}

function fillFormDTOCheckboxes(dto, fieldIdsArray) {

	for ( var i = 0; i < fieldIdsArray.length; i++) {

		var fieldId = fieldIdsArray[i];

		var fieldValue = jQuery("#" + fieldId).is(":checked");

		dto[fieldId] = fieldValue;
	}
}


function fillFormDTORadio(dto, form, fieldNamesArray) {
	
	
	for ( var i = 0; i < fieldNamesArray.length; i++) {

		var fieldName = fieldNamesArray[i];

		var fieldValue = jQuery("#" + form + " input[name= " + fieldName + "]:radio:checked").val(); 

		dto[fieldName] = fieldValue;
	}
}


function initializeUploadForm() {

	var options = {
		beforeSend : function() {
			jQuery("#message").html("");
		},
		uploadProgress : function(event, position, total, percentComplete) {
			jQuery("#progress-success").css("width", percentComplete + "%");

		},
		success : function() {
		},
		complete : function(response) {
			jQuery("#message")
					.html("<font color='green'>Fajl je poslat</font>");
			uploadedFileId = response.responseText;
		},
		error : function() {
			jQuery("#message").html(
					"<font color='red'> Greška pri slanju fajla.</font>");

		}

	};

	jQuery("#uploadForm").ajaxForm(options);
};

function initTinyMceRich(fieldIdsString) {

	tinymce
			.init({

				mode : "exact",
				elements : fieldIdsString,
				plugins : "advlist autolink link image lists charmap print preview media table textcolor wordcount",
				tools : "inserttable forecolor backcolor"
			});
}

function initTinyMceSimple(fieldIdsString) {

	tinymce
			.init({

				mode : "exact",
				elements : fieldIdsString,
				plugins : "preview textcolor wordcount",
			});
}

function doAjaxPost(targetUrl, dto, redirectUrl) {

	jQuery.ajax({
		url : baseUrl + targetUrl,
		type : "POST",
		dataType : "json",
		contentType : "application/json",
		data : JSON.stringify(dto),
		success : function(data) {
			if (data.status == "OK") {
				window.location.assign(baseUrl + redirectUrl);
			} else {
				showSingleValidationMessage("formValidationMessage",
						data.message);
			}

		},
		error : function(data) {

			var response = data.responseText;

			if (response) {

				var obj = JSON.parse(response);

				alert("Greška: " + obj.message);
			} else {

				alert("error");
			}
		}
	});

}


function doAjaxPostWithResponse(targetUrl, dto, redirectUrl) {

	jQuery.ajax({
		url : baseUrl + targetUrl,
		type : "POST",
		dataType : "json",
		contentType : "application/json",
		data : JSON.stringify(dto),
		success : callbackFunction,
		error : function(data) {

			var response = data.responseText;

			if (response) {

				var obj = JSON.parse(response);

				alert("Greška: " + obj.message);
			} else {

				alert("error");
			}
		}
	});

}


function formatString () {
    var s = arguments[0];
    for (var i = 0; i < arguments.length - 1; i++) {       
        var reg = new RegExp("\\{" + i + "\\}", "gm");             
        s = s.replace(reg, arguments[i + 1]);
    }
    return s;
}