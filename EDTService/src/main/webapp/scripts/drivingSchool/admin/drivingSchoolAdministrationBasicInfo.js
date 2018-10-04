jQuery(function($) {
	handleSubmit($);
	addTooltips([ "name", "legalName", "city" ]);
	formGeneral($);
});

function formGeneral($) {

	$('#permitDateDiv').datepicker({
		format : 'yyyy-mm-dd'
	});

}

var attachments;

function setAttachments(variableName, atts) {

	attachments = atts;
}

function handleSubmit($) {
	$('#form')
			.submit(
					function(e) {
						e.preventDefault();

						var valid = validateRequiredFields([ "name",
								"legalName", "country", "city", "address",
								"phone" ]);

						if (!valid) {
							return;
						}

						var dto = {};

						fillFormDTO(dto, [ "name", "legalName", "country",
								"city", "address", "categories", "aboutUs",
								"phone", "phone2", "fax", "website", "email",
								"workingHoursMonday", "workingHoursTuesday",
								"workingHoursWednesday",
								"workingHoursThursday", "workingHoursFriday",
								"workingHoursSaturday", "workingHoursSunday",
								"googleMapsURL", "facebookURL", "twitterURL",
								"permitNumber", "registryNumber", "permitDate" ]);

						var schoolId = $('#schoolId').val();
						var schoolUniqueName = $('#schoolUniqueName').val();

						if (attachments && attachments.length > 0)
							dto.logo = attachments[0].id;

						// dto.logo = uploadedFileId;

						dto.id = schoolId;

						$
								.ajax({
									url : baseUrl
											+ "rest/drivingSchoolAdministration/updateBasicInfo",
									type : "POST",
									dataType : "json",
									contentType : "application/json",
									data : JSON.stringify(dto),
									success : function(data) {
										if (data.status == "OK") {
											window.location = baseUrl
													+ 'administracija-auto-skole/'
													+ schoolUniqueName;
										} else {
											showSingleValidationMessage(
													"formValidationMessage",
													data.message);
										}

									},
									error : function(data) {
										alert("error");
									}
								});

					});
};


