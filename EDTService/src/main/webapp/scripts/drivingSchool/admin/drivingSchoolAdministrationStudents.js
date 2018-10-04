jQuery(function($) {
});


/*--------------------------------------------------------
BEGIN FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
function formGeneral() {
	
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
            "sInfoThousands": ",",
            "sLoadingRecords": "Učitavanje...",
            
            //vrednosti koje se koriste u tabeli na stranici
        	"sEmptyTable": "Ne postoje podaci za izabrani kriterijum.",
            "sLengthMenu": "Prikaži _MENU_ učenika",
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
    
    /*----------- BEGIN chosen CODE -------------------------*/

    $(".chzn-select").chosen();
	$(".chzn-select-deselect").chosen({
		allow_single_deselect : true
	});
	/*----------- END chosen CODE -------------------------*/
	
    /*----------- BEGIN action table CODE -------------------------*/
    jQuery('#dataTable button.edit').on('click', function() {
        jQuery('#deleteModal').modal({
            show: true
        });
        
        var valUserId = jQuery(this).closest('tr').children('td').eq(0); //id ucenika
       
        jQuery('#deleteModal #studentId').val(valUserId.html());
        
    });
    /*----------- END action table CODE -------------------------*/
}


/*--------------------------------------------------------
DACI -> sort combobox
---------------------------------------------------------*/
$("#selectStatus").change(submitForm);

function submitForm() {
	
	var selectType = jQuery('#selectStatus').val();

	var listOfStudentsURL = baseUrl + "administracija-auto-skole/ucenici?" + "id=" + jQuery('#schoolId').val() + "&select-type=" + selectType;

	window.location.href = listOfStudentsURL;
}


/*--------------------------------------------------------
DACI -> brisanje ucenika
---------------------------------------------------------*/
jQuery('#deleteModal #deleteBtn').on('click', function() {
	
	var schoolId = jQuery('#schoolId').val();
	var studentId = jQuery('#deleteModal #studentId').val();

	window.location = formatString(deleteStudentUrl, schoolId, studentId);	
});

/*--------------------------------------------------------
END FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
