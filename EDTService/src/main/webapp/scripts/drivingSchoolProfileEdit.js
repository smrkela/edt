$(function() {
	initializeUploadForm();
});
var uploadedFileId;
function initializeUploadForm() {


						var options = {
							beforeSend : function() {
								$("#message").html("");
							},
							uploadProgress : function(event, position, total,
									percentComplete) {
								$("#progress").attr("value",percentComplete);

							},
							success : function() {
								$("#progress").attr("value",100);
							},
							complete : function(response) {
								$("#message").html(
										"<font color='green'>Fajl je poslat</font>");
								uploadedFileId = response.responseText;
							},
							error : function() {
								$("#message")
										.html(
												"<font color='red'> Greška pri slanju fajla.</font>");

							}

						};

						$("#uploadForm").ajaxForm(options);
};
