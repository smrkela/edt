var getPagesUrl;
var hasPrevious = false;
var hasNext = false;
var currentPage = 0;
var pageSize = 3;
$(function() {
	getPagesUrl = baseUrl + "rest/page/getAllPages";
	getPages();
	$("#nextPageLink").click(function(e) {
		e.preventDefault();
		currentPage++;
		getPages();
	});
	$("#previousPageLink").click(function(e) {
		e.preventDefault();
		currentPage--;
		getPages();
	});
});

function getPages() {
	$("#pageRow").hide();
	$
			.get(
					getPagesUrl,
					{
						pageNumber : currentPage,
						pageSize : pageSize
					},
					function(data) {
						if (data != null) {
							var dtos = data.dtos;
							$("#pageRow").html("");
							for ( var i = 0; i < dtos.length; i++) {
								dto = dtos[i];
								$("#pageRow")
										.append(
												"<div class='span4'>		<h2>"
														+ dto.title
														+ "</h2>		<p>"
														+ dto.content
														+ "</p> <a href='page/"+dto.uniqueName+"'>Cela vest</a> </div>");
							}
							$("#pageRow").show("slide");
							togglePaginLinksVisibility(data);
							
						}
					}).fail(function() {
				alert("error");
			});
}
function togglePaginLinksVisibility(data) {
	if (data.hasNextPage) {
		$("#nextPageLink").show();
	} else {
		$("#nextPageLink").hide();
	}
	if (data.hasPreviousPage) {
		$("#previousPageLink").show();
	} else {
		$("#previousPageLink").hide();
	}
}