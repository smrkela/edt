jQuery(function($) {
});


function formGeneral() {

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
            "sInfoThousands": ",",
            "sLoadingRecords": "Učitavanje...",
            
            //vrednosti koje se koriste u tabeli na stranici
        	"sEmptyTable": "Ne postoje podaci za izabrani kriterijum.",
            "sLengthMenu": "Prikaži _MENU_ obaveštenja",
            "sSearch": "Pretraga:",
            "oPaginate": {
        		"sFirst": "Prva",
        		"sLast": "Poslednja",
        		"sPrevious": "Prethodna",
            	"sNext": "Sledeća"
        	}
        }
    });
}