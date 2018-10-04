jQuery(function() {

	handleSubmit();
	addTooltips([ "dateFrom", "dateTo" ]);

});

function handleSubmit() {
	jQuery('#form')
			.submit(
					function(e) {
						e.preventDefault();

						var valid = validateRequiredFields([ "dateFrom", "dateTo"]);

						if (!valid) {
							return;
						}

						var dto = {};

						fillFormDTO(dto, [ "dateFrom", "dateTo"]);

						window.location = baseUrl + 'administracija/reports?startDate='+dto.dateFrom+'&finishDate='+dto.dateTo;
					});
};

function formGeneral() {

	jQuery('.with-tooltip').tooltip({
		selector : ".input-tooltip"
	});

	/*----------- BEGIN datepicker CODE -------------------------*/
	jQuery('#dateFromDiv').datepicker({
		format : 'yyyy-mm-dd'
	});
	jQuery('#dateToDiv').datepicker({
		format : 'yyyy-mm-dd'
	});

	/*----------- END datepicker CODE -------------------------*/

}

