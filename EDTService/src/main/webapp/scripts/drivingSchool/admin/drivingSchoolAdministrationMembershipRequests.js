jQuery(function($) {
});


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
            "sInfoThousands":",",
            "sLoadingRecords":"Učitavanje...",
            
            //vrednosti koje se koriste u tabeli na stranici
        	"sEmptyTable": "Ne postoje podaci za izabrani kriterijum.",
        	"sLengthMenu": "Prikaži _MENU_ zahteva",
            "sSearch": "Pretraga:",
            "oPaginate": {
        		"sFirst":"Prva",
        		"sLast":"Poslednja",
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
        var valUserName = jQuery(this).closest('tr').children('td').eq(0); //ime ucenika
        var valUserEmail = jQuery(this).closest('tr').children('td').eq(1); //e-mail ucenika
        var valCreationDate = jQuery(this).closest('tr').children('td').eq(2); //Datum zahteva
        var valStatus = jQuery(this).closest('tr').children('td').eq(3); //Status
        var valComment = jQuery(this).closest('tr').children('td').eq(5); //comment
        var valReceiveNotifications = jQuery(this).closest('tr').children('td').eq(6); //receiveNotifications
        var valConfirmationToken = jQuery(this).closest('tr').children('td').eq(7); //confirmationToken
        var valDecisionDate = jQuery(this).closest('tr').children('td').eq(8); //decisionDate
        var valDecisionComment = jQuery(this).closest('tr').children('td').eq(9); //decisionComment
        
        var valStudentName = jQuery(this).closest('tr').children('td').eq(10); //studentName
        var valStudentEmail = jQuery(this).closest('tr').children('td').eq(11); //studentEmail
        var valStudentPhone = jQuery(this).closest('tr').children('td').eq(12); //studentPhone
        var valStudentAddress = jQuery(this).closest('tr').children('td').eq(13); //studentAddress
        
        
        jQuery('#editModal #userName').val(valUserName.html());
        jQuery('#editModal #userEmail').val(valUserEmail.html());
        
        var checked = valReceiveNotifications.html();
        if(checked == "true"){
        	jQuery('#editModal #receiveNotifications').prop('checked', true);
        } else {
        	jQuery('#editModal #receiveNotifications').prop('checked', false);
        }

        jQuery('#editModal #comment').val(valComment.html());
        jQuery('#editModal #creationDate').val(valCreationDate.html());
        jQuery('#editModal #status').val(valStatus.html());
        jQuery('#editModal #decisionComment').val(valDecisionComment.html());
        jQuery('#editModal #tokenConfirmation').val(valConfirmationToken.html());
        
        if (valStudentName.html()){
        	jQuery('#editModal #studentForm').show();
        	jQuery('#editModal #studentName').val(valStudentName.html());
        	jQuery('#editModal #studentEmail').val(valStudentEmail.html());
        	jQuery('#editModal #studentPhone').val(valStudentPhone.html());
        	jQuery('#editModal #studentAddress').val(valStudentAddress.html().replace(/null/g, ''));
        } else {
        	jQuery('#editModal #studentForm').hide();
		}
        
        if(valStatus.html() == "Odobren" || valStatus.html() == "Odbijen"){
        	jQuery('#editModal #decisionDateDiv').show();
        	jQuery('#editModal #decisionDate').val(valDecisionDate.html());
        	jQuery('#editModal #decisionComment').attr('disabled','disabled');
        	
        	jQuery('#editModal #submitAndNewBtn').hide();
        	jQuery('#editModal #submitAndExistingBtn').hide();
        	jQuery('#editModal #rejectBtn').hide();
        	jQuery('#editModal #cancelBtn').hide();
        	
        	jQuery('#editModal #closeBtn').show(); 	
        	
        	if (valStudentName.html()){
        		jQuery('#editModal #newStudentBtn').hide();
        		jQuery('#editModal #existingStudentBtn').hide();
        	} else {
        		if(valStatus.html() == "Odobren"){
	        		jQuery('#editModal #newStudentBtn').show();
	        		jQuery('#editModal #existingStudentBtn').show();
        		}
        	}
        } else {
        	jQuery('#editModal #decisionDateDiv').hide();
        	
        	jQuery('#editModal #decisionComment').removeAttr('disabled');
        	
        	jQuery('#editModal #submitAndNewBtn').show();
        	jQuery('#editModal #submitAndExistingBtn').show();
        	jQuery('#editModal #rejectBtn').show();
        	jQuery('#editModal #cancelBtn').show();
        	jQuery('#editModal #closeBtn').hide();
        	jQuery('#editModal #newStudentBtn').hide();
    		jQuery('#editModal #existingStudentBtn').hide();
        }
        
    });
    
    
    
    jQuery('#dataTable button.delete').on('click', function() {
        jQuery('#deleteModal').modal({
            show: true
        });
        var schoolId = jQuery('#schoolId').val();
        var valConfirmationToken = jQuery(this).closest('tr').children('td').eq(7); //confirmationToken

        jQuery('#deleteModal #schoolId').val(schoolId);
        jQuery('#deleteModal #token').val(valConfirmationToken.html());
        
    });
    /*----------- END action table CODE -------------------------*/

}


jQuery('#editModal #submitAndNewBtn').on('click', function() {
	
	submitDecision('APPROVED', 'new');

});


jQuery('#editModal #submitAndExistingBtn').on('click', function() {
	
	submitDecision('APPROVED', 'existing');

});


jQuery('#editModal #rejectBtn').on('click', function() {

	submitDecision('REJECTED', 'reject');

});


function submitDecision(decision, type) {
	
	var token = jQuery('#editModal #tokenConfirmation').val();
	var decisionComment = jQuery('#editModal #decisionComment').val();
	var schoolId = jQuery('#schoolId').val();
	
	$.post(membershipRequestDecisionUrl, {token : token, decisionComment : decisionComment, decision : decision})
		  	.done(function() {
		  			
		  			if(type == "new"){
		  				window.location = membershipSuccessNewUrl + schoolId + "&membership=true&token=" + token;
		  			}
		  			
		  			if(type == "existing"){
		  				window.location = membershipSuccessExistingUrl + schoolId + "&token=" + token;
		  			}
		  			
		  			if(type == "reject"){
		  				window.location = membershipRequestUrl + schoolId;
		  			}
		  			
		  		})
			.fail(function(data) {
						if (data && data.responseText) {
							var status = JSON.parse(data.responseText);
							showSingleValidationMessage("membershipValidationMessage", status.message);
						}
				  }
				 );
}

jQuery('#editModal #removeStudent').on('click', function() {

	var token = jQuery('#editModal #tokenConfirmation').val();
	var schoolId = jQuery('#schoolId').val();
	
	$.post(removeStudentFromMembershipUrl, {membershipRequestToken : token})
  	.done(function() {  			
  			window.location = membershipRequestUrl + schoolId;  			
  		})
	.fail(function(data) {
				if (data && data.responseText) {
					var status = JSON.parse(data.responseText);
					showSingleValidationMessage("membershipValidationMessage", status.message);
				}
		  }
		 );
});


jQuery('#editModal #newStudentBtn').on('click', function() {

	var token = jQuery('#editModal #tokenConfirmation').val();
	var schoolId = jQuery('#schoolId').val();
	window.location = membershipSuccessNewUrl + schoolId + "&membership=true&token=" + token;
	
});


jQuery('#editModal #existingStudentBtn').on('click', function() {

	var token = jQuery('#editModal #tokenConfirmation').val();
	var schoolId = jQuery('#schoolId').val();
	window.location = membershipSuccessExistingUrl + schoolId + "&token=" + token;
	
});


/*--------------------------------------------------------
DACI -> brisanje ucenika
---------------------------------------------------------*/
jQuery('#deleteModal #deleteBtn').on('click', function() {
	
	var schoolId = jQuery('#schoolId').val();
	var token = jQuery('#deleteModal #token').val();

	$.post(deleteMembershipRequestUrl, {id : schoolId, token : token})
  	.done(function() {
  			window.location = membershipRequestUrl + schoolId;
  		})
	.fail(function(data) {
				if (data && data.responseText) {
					var status = JSON.parse(data.responseText);
					showSingleValidationMessage("deleteMembershipRequestValidationMessage", status.message);
				}
		  }
		 );
});