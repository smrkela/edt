jQuery(function($) {
});


/*--------------------------------------------------------
BEGIN TABLES.HTML SCRIPTS
---------------------------------------------------------*/
function bizstrapTable() {

    /*----------- BEGIN datatable CODE -------------------------*/
    jQuery('#dataTable').dataTable({
        "sDom": "<'pull-right'l>t<'row-fluid'<'span12'fp>>",
        "sPaginationType": "bootstrap",
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

    /*----------- BEGIN action table CODE -------------------------*/
    jQuery('#dataTable button.remove').on('click', function() {
        jQuery(this).closest('tr').remove();
    });
    jQuery('#dataTable button.edit').on('click', function() {
        jQuery('#editModal').modal({
            show: true
        });
        var valStudentName = jQuery(this).closest('tr').children('td').eq(1); //ime ucenika
        var valStudentEmail = jQuery(this).closest('tr').children('td').eq(2); //e-mail ucenika
        var valStudentAddress = jQuery(this).closest('tr').children('td').eq(3); //adresa ucenika
        var valStudentCategory = jQuery(this).closest('tr').children('td').eq(4); //kategorija
        var valStudentPhone = jQuery(this).closest('tr').children('td').eq(6); //telefon
        var valRegisterDate = jQuery(this).closest('tr').children('td').eq(7); //datum registracije
        var valFirstAidPassed = jQuery(this).closest('tr').children('td').eq(8); //položena obuka prve pomoći
        var valFirstAidPassedDate = jQuery(this).closest('tr').children('td').eq(9); //datum polaganja obuke prve pomoći
        var valTheoryPassed = jQuery(this).closest('tr').children('td').eq(10); //položen teorijski deo
        var valTheoryPassedDate = jQuery(this).closest('tr').children('td').eq(11); //datum polaganja teorijskog dela
        var valPracticePassed = jQuery(this).closest('tr').children('td').eq(12); //položen praktični deo
        var valPracticePassedDate = jQuery(this).closest('tr').children('td').eq(13); //datum polaganja praktičnog dela
        var valComment = jQuery(this).closest('tr').children('td').eq(14); //komentar
        
        
        jQuery('#editModal #studentName').val(valStudentName.html());
        jQuery('#editModal #studentEmail').val(valStudentEmail.html());
        jQuery('#editModal #studentAddress').val(valStudentAddress.html());
        jQuery('#editModal #studentCategory').val(valStudentCategory.html());
        jQuery('#editModal #studentPhone').val(valStudentPhone.html());
        jQuery('#editModal #registerDate').val(valRegisterDate.html());
        
        if( valFirstAidPassed.html() == "true" ){
        	jQuery('#editModal #firstAidPassed').show();
        	jQuery('#editModal #firstAidNotPassed').hide();
        } else {
        	jQuery('#editModal #firstAidPassed').hide();
        	jQuery('#editModal #firstAidNotPassed').show();
        }

        jQuery('#editModal #firstAidPassedDate').val(valFirstAidPassedDate.html());

        if( valTheoryPassed.html() == "true" ){
        	jQuery('#editModal #theoryPassed').show();
        	jQuery('#editModal #theoryNotPassed').hide();
        } else {
        	jQuery('#editModal #theoryPassed').hide();
        	jQuery('#editModal #theoryNotPassed').show();
        }
        
        jQuery('#editModal #theoryPassedDate').val(valTheoryPassedDate.html());

        if( valPracticePassed.html() == "true" ){
        	jQuery('#editModal #practicePassed').show();
        	jQuery('#editModal #practiceNotPassed').hide();
        } else {
        	jQuery('#editModal #practicePassed').hide();
        	jQuery('#editModal #practiceNotPassed').show();
        }
        
        jQuery('#editModal #practicePassedDate').val(valPracticePassedDate.html());
        jQuery('#editModal #comment').val(valComment.html());
        
        
        
    });
    /*----------- END action table CODE -------------------------*/

}
/*--------------------------------------------------------
END TABLES.HTML SCRIPTS
---------------------------------------------------------*/


jQuery('#submitStudent').on('click', function() {
	
	var noOfSelectedCheckboxes = 0;
	var studentId = null;
	var membershipRequestId = jQuery("#membershipRequestId").val();
	var schoolId = jQuery("#schoolId").val();
	
	jQuery('#dataTable input:checked').each(function() {
	    studentId = $(this).attr('value');
	    noOfSelectedCheckboxes += 1;
	});
	
	if (noOfSelectedCheckboxes == 0 || noOfSelectedCheckboxes >= 2){
		showSingleValidationMessage("connectionValidationMessage", "Molimo Vas da izaberete samo jednog učenika sa kojim želite da povežete korisnika koji je zatražio " +
									"članstvo u Vašoj auto školi.");
	} else {
		
		$.post(connectToExistingStudentUrl, {membershipRequestId : membershipRequestId, studentId : studentId})
	  	.done(function() {
	  			window.location = connectToExistingStudentSuccessUrl + schoolId;
	  		})
		.fail(function(data) {
					if (data && data.responseText) {
						var status = JSON.parse(data.responseText);
						showSingleValidationMessage("connectionValidationMessage", status.message);
					}
			  }
			 );
	}
	

});