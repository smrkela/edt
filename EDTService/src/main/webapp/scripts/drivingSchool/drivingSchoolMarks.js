jQuery(function($) {
	handleSubmit($);
	addTooltips([ "mark", "comment" ]);
});

function removeMark(id) {

	if (confirm('Da li stvarno želite da obrišete komentar i ocenu?')) {
		jQuery
				.ajax({
					url : baseUrl
							+ "rest/drivingSchoolAdministration/removeDrivingSchoolMark",
					type : "DELETE",
					data : {
						"id" : id
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "OK") {
							window.location.reload();
						} else {
							alert(data.message);
						}

					},
					error : function(data) {
						alert(data);
					}
				});
	}
}
function handleSubmit($) {
	$('#form')
			.submit(
					function(e) {
						e.preventDefault();
						var valid = validateRequiredFields([ "mark", "comment" ]);
						if (!valid) {
							return;
						}
						var dto = {};
						var drivingSchoolDTO = {};
						var schoolId = $('#schoolId').val();
						drivingSchoolDTO.id = schoolId;
						dto.school = drivingSchoolDTO;
						fillFormDTO(dto, [ "mark", "comment" ]);
						$
								.ajax({
									url : baseUrl
											+ "rest/drivingSchoolAdministration/addDrivingSchoolMark",
									type : "POST",
									dataType : "json",
									contentType : "application/json",
									data : JSON.stringify(dto),
									success : function(data) {
										if (data.status == "OK") {
											window.location.reload();
										} else {
											showSingleValidationMessage(
													"formValidationMessage",
													data.message);
										}

									},
									error : function(data) {
										alert(data);
									}
								});

					});
};
