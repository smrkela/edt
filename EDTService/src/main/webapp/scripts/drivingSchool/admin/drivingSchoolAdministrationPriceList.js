jQuery(function($) {
	
	handleSubmit($);
	
	initTinyMceSimple("description");
});

function handleSubmit($) {
	$('#form')
			.submit(
					function(e) {
						e.preventDefault();
						var description = tinyMCE.get("description").getContent();
						var priceListDTO = {};
						var drivingSchoolDTO = {};
						priceListDTO.drivingSchoolDTO = drivingSchoolDTO;
						priceListDTO.id = $("#priceListId").val();
						var schoolId = $("#schoolId").val();
						var schoolUniqueName = $("#schoolUniqueName").val();
						priceListDTO.drivingSchoolDTO.id = schoolId;
						priceListDTO.description = description;
						priceListDTO.priceListPriceDTOs = new Array();
						var count = 0;
						$("input[type='text']").each(function(index) {

						});

						$("input[type='text']")
								.each(
										function(index) {

											var priceListPriceDTO = {};
											var priceListSubCategoryDTO = {};
											var priceDTO = {};
											priceListPriceDTO.priceListSubCategoryDTO = priceListSubCategoryDTO;
											var priceListCategoryDTO = {};
											priceListPriceDTO.priceListCategoryDTO = priceListCategoryDTO;
											priceListPriceDTO.priceListSubCategoryDTO.id = $(
													this).attr("subcategoryId");
											priceListPriceDTO.priceListCategoryDTO.id = $(
													this).attr("categoryId");
											priceDTO.currency = "RSD";
											var value = $(this).val();
											if (hasText(value)) {
												priceDTO.value = value;
												priceListPriceDTO.priceDTO = priceDTO;
												priceListDTO.priceListPriceDTOs[count++] = priceListPriceDTO;
											}
										});

						$
								.ajax({
									url : baseUrl
											+ "rest/drivingSchoolAdministration/addDrivingSchoolPriceList",
									type : "POST",
									dataType : "json",
									contentType : "application/json",
									data : JSON.stringify(priceListDTO),
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

						return false;
					});
};
