jQuery(function($) {

	handleSubmit($);
});

function handleSubmit($) {
	$('#form').submit(function(e) {
		e.preventDefault();

		var dto = {};
		dto.id = $("#id").val();
		dto.categoryId = $("#categoryId").val();
		dto.name = $("#title").val();
		dto.orderIndex = $("#orderIndex").val();
		dto.questionIds = new Array();

		var allQuestionsValid = true;

		$("input[name='questionId']").each(function(index) {

			var val = $("#" + index).val();

			if (!hasText(val))
				allQuestionsValid = false;

			dto.questionIds.push(val);
		});

		if (!hasText(dto.name) || !hasText(dto.orderIndex) || !allQuestionsValid) {

			showSingleValidationMessage("formValidationMessage", "Nisu uneti svi podaci.");

			return;
		}

		$.ajax({
			url : baseUrl + "rest/admin/saveRealTest",
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify(dto),
			success : function(data) {
				if (data.status == "OK") {
					window.location = baseUrl + 'administracija/testovi';
				} else {
					showSingleValidationMessage("formValidationMessage", data.message);
				}
			},
			error : function(data) {
				alert("error");
			}
		});

		return false;
	});
};
