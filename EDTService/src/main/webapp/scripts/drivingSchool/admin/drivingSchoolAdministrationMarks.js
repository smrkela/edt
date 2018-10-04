function deleteMark(id) {
	if (confirm('Da li stvarno želite da obrišete komentar i ocenu?')) {
		jQuery
				.ajax({
					url : baseUrl
							+ "rest/drivingSchoolAdministration/removeDrivingSchoolMark",
					type : "POST",
					dataType : "json",
					data : {
						"id" : id
					},
					success : function(data) {
						if (data.status == "OK") {
							window.location.reload();
						} else {
							showSingleValidationMessage(
									"formValidationMessage", data.message);
						}

					},
					error : function(data) {
						alert("error");
					}
				});
	} else {
		// Do nothing!
	}
};
