jQuery(function($) {
});


/*--------------------------------------------------------
BEGIN FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
function formGeneral() {

	jQuery('.with-tooltip').tooltip({
		selector : ".input-tooltip"
	});
	
	
	/*----------- BEGIN datatable CODE -------------------------*/
    jQuery('#dataTable').dataTable({
        "sDom": "<'pull-right'l>t<'row-fluid'<'span12'fp>>", "sPaginationType": "bootstrap",
        "oLanguage": {
        	"sProcessing": "Procesiranje u toku...",
            "sZeroRecords": "Nije pronađen nijedan rezultat",
            "sInfo": "Prikaz _START_ do _END_ od ukupno _TOTAL_ elemenata",
            "sInfoEmpty": "Prikaz 0 do 0 od ukupno 0 elemenata",
            "sInfoFiltered": "(filtrirano od ukupno _MAX_ elemenata)",
            "sInfoPostFix": "",
            "sSearch": "Pretraga:",
            "sUrl": "",
            "sInfoThousands":",",
            "sLoadingRecords":  "Učitavanje...",
        	"sEmptyTable": "Ne postoje podaci za izabrani kriterijum.",
            "sLengthMenu": "Prikaži _MENU_ elemenata",
            "sSearch": "Pretraga:",
            "oPaginate": {
        		"sFirst": "Prva",
        		"sLast": "Poslednja",
        		"sPrevious": "Prethodna",
            	"sNext": "Sledeća"
        	}
        }
    });
    /*----------- END datatable CODE -------------------------*/    
}

/*--------------------------------------------------------
END FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
