jQuery(function($) {

	handleSubmit($);
	addTooltips([ "title", "uniqueName", "smallPreview", "normalPreview",
			"content", "category", "orderIndex" ]);

	initTinyMceRich("content,normalPreview");
});

var smallImages;
var normalImages;

function setAttachments(variableName, atts) {

	if (variableName == "small")
		smallImages = atts;

	if (variableName == "normal")
		normalImages = atts;
}

function handleSubmit($) {
	$('#form').submit(
			function(e) {
				e.preventDefault();

				var valid = validateRequiredFields([ "title", "uniqueName",
						"smallPreview", "category",
						"orderIndex" ]);

				if (!valid) {
					return;
				}

				var dto = {};

				fillFormDTO(dto, [ "title", "uniqueName", "smallPreview", "id",
						"orderIndex" ]);
				
				dto.content = tinyMCE.get("content").getContent();
				dto.normalPreview = tinyMCE.get("normalPreview").getContent();

				var categoryValue = $("#category").val();
				var categoryDTO = {
					id : categoryValue
				};
				dto.categoryDTO = categoryDTO;

				if (smallImages && smallImages.length > 0)
					dto.smallPreviewImage = smallImages[0].id;

				if (normalImages && normalImages.length > 0)
					dto.normalPreviewImage = normalImages[0].id;

				doAjaxPost("rest/page/admin/savePage", dto,
						'administracija/vesti');

			});
};
//
// function savePageHandler() {
// $("#createPageButton").click(
// function(e) {
// e.preventDefault();
// var pageDTO = {};
// var titleValue = $("#createPageTitle").val();
// var uniqueNameValue = $("#createPageUniqueName").val();
// var contentValue = $("#createPageContent").val();
// var pageCategoryValue = $("#createPageCategory").val();
// var smallPreview = $("#createPageSmallPreview").val();
// var validationMessages = new Array();
// if (!hasText(titleValue)) {
// validationMessages[0] = $("#createPageTitle").attr(
// "data-message");
// }
// if (!hasText(uniqueNameValue)) {
// validationMessages[1] = $("#createPageUniqueName").attr(
// "data-message");
// }
// if (!hasText(contentValue)) {
// validationMessages[2] = $("#createPageContent").attr(
// "data-message");
// }
//
// if (!hasText(pageCategoryValue)) {
// validationMessages[3] = $("#createPageCategory").attr(
// "data-message");
// }
//
// if (!hasText(smallPreview)) {
// validationMessages[4] = $("#createPageSmallPreview").attr(
// "data-message");
// }
//
// showValidationMessage("createPageValidationMessage",
// validationMessages);
// if (validationMessages.length > 0) {
// return false;
// }
// if (mode == "edit") {
// pageDTO.id = pageId;
// }
// pageDTO.title = titleValue;
// pageDTO.uniqueName = uniqueNameValue;
// pageDTO.content = contentValue;
// pageDTO.smallPreview = smallPreview;
// var categoryDTO = {};
// categoryDTO.id = pageCategoryValue;
// pageDTO.categoryDTO = categoryDTO;
// $
// .ajax({
// url : baseUrl + "rest/page/admin/savePage",
// type : "POST",
// dataType : "json",
// contentType : "application/json",
// data : JSON.stringify(pageDTO),
// success : function(data) {
// if (data.status == "OK") {
// window.location = baseUrl;
// } else {
// showSingleValidationMessage(
// "createPageValidationMessage",
// data.message);
// }
//
// },
// error : function(data) {
// alert("error");
// }
// });
// });
// };
