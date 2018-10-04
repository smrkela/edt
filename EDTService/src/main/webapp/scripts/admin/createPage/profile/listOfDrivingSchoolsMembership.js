jQuery(function($) {
});

/*--------------------------------------------------------
BEGIN FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
function formGeneral() {

	jQuery('.with-tooltip').tooltip({
		selector : ".input-tooltip"
	});

		
	/*----------- BEGIN chosen CODE -------------------------*/

	jQuery(".chzn-select").chosen();
	jQuery(".chzn-select-deselect").chosen({
		allow_single_deselect : true
	});
	/*----------- END chosen CODE -------------------------*/

	/*----------- BEGIN uniform CODE -------------------------*/
	jQuery('.uniform').uniform();
	/*----------- END uniform CODE -------------------------*/

	/*----------- BEGIN toggleButtons CODE -------------------------*/
	jQuery('#normal-toggle-button').toggleButtons();

	jQuery('#callback-toggle-button').toggleButtons({
		onChange : function($el, status, e) {
			console.log($el, status, e);
			jQuery('#magic-text').text("Status is: " + status);
		}
	});
	jQuery('#text-toggle-button').toggleButtons({
		width : 220,
		label : {
			enabled : "Bizstrap",
			disabled : "Admin"
		}
	});
	jQuery('#danger-toggle-button').toggleButtons({
		style : {
			// Accepted values ["primary", "danger", "info", "success",
			// "warning"] or nothing
			enabled : "danger",
			disabled : "info"
		}
	});
	/*----------- END toggleButtons CODE -------------------------*/

	/*----------- BEGIN dualListBox CODE -------------------------*/
	jQuery.configureBoxes();
	/*----------- END dualListBox CODE -------------------------*/

}
/*--------------------------------------------------------
END FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/



/*--------------------------------------------------------
BENIG TABLES.HTML SCRIPTS
---------------------------------------------------------*/
function bizstrapTable() {

    /*----------- BEGIN action table CODE -------------------------*/
    
    jQuery('#actionTable button.edit').on('click', function() {
        jQuery('#editModal').modal({
            show: true
        });
        
        // tabela sa redovima podataka
        var tableRowsWithData = jQuery(this).closest('tr').children('td').eq(1).children('div').children('table').children('tbody').children('tr'); 
        
        //prvi red (eq(0)), druga celija (eq(1))....
        var drivingSchool = tableRowsWithData.eq(0).children('td').eq(1).children('b').children('a').html(); //auto škola
        var address = tableRowsWithData.eq(1).children('td').eq(1).html(); //adresa
        var status = tableRowsWithData.eq(4).children('td').eq(1).html(); //status
        var city = tableRowsWithData.eq(5).children('td').eq(1).html(); //grad
        var requestDate = tableRowsWithData.eq(6).children('td').eq(1).html(); //datum kreiranja zahteva
        var comment = tableRowsWithData.eq(7).children('td').eq(1).html(); //komentar ucenika
        var receiveNotifications = tableRowsWithData.eq(8).children('td').eq(1).html(); //primaj obavestenja
        var decisionDate = tableRowsWithData.eq(9).children('td').eq(1).html(); //datum odluke
        var decisionComment = tableRowsWithData.eq(10).children('td').eq(1).html();//komentar odluke
        var confirmationToken = tableRowsWithData.eq(11).children('td').eq(1).html();//token
        
        jQuery('#editModal #drivingSchool').val(drivingSchool);
        jQuery('#editModal #drivingSchoolAddress').val(address + ", " + city);
        jQuery('#editModal #status').val(status);
        jQuery('#editModal #creationDate').val(requestDate);
        jQuery('#editModal #comment').val(comment);
        if(receiveNotifications == "true"){
        	jQuery('#editModal #receiveNotifications').prop('checked', true);
        } else {
        	jQuery('#editModal #receiveNotifications').prop('checked', false);
        }
        jQuery('#editModal #decisionDate').val(decisionDate);
        jQuery('#editModal #decisionComment').val(decisionComment);
        jQuery('#editModal #confirmationToken').val(confirmationToken);
        
        
        if(status == "Odobren"){
        	jQuery('#editModal #status').css({'background-color': '#B7E6A4'});
        }
        
        if(status == "Odbijen"){
        	jQuery('#editModal #status').css({'background-color': '#ECC1C1'});
        }
        
        if(status == "U čekanju"){
        	jQuery('#editModal #status').css({'background-color': '#EEEEEE'});
        }
        
        
        if(status == "Odobren" && receiveNotifications == "true"){
        	jQuery('#editModal #receiveNotificationsBtn').hide();
        	jQuery('#editModal #notReceiveNotificationsBtn').show();
        } 
        
        if(status == "Odobren" && receiveNotifications != "true") {
        	jQuery('#editModal #receiveNotificationsBtn').show();
        	jQuery('#editModal #notReceiveNotificationsBtn').hide();
        }
        
        if(status == "Odbijen" || status == "U čekanju") {
        	jQuery('#editModal #receiveNotificationsBtn').hide();
        	jQuery('#editModal #notReceiveNotificationsBtn').hide();
        }

    });
    /*----------- END action table CODE -------------------------*/

}
/*--------------------------------------------------------
 END TABLES.HTML SCRIPTS
 ---------------------------------------------------------*/


/*--------------------------------------------------------
POCETAK - prijem obavestenja
---------------------------------------------------------*/

jQuery('#editModal #receiveNotificationsBtn').on('click', function() {
	
	var token = jQuery('#editModal #confirmationToken').val();
	handleNotification('1', token);
	
});


jQuery('#editModal #notReceiveNotificationsBtn').on('click', function() {

	var token = jQuery('#editModal #confirmationToken').val();
	handleNotification('2', token);

});


function handleNotification(decision, token) {
	
	$.post(updateReceiveNotificationsUrl, { decision : decision, token : token })
		  	.done(function() {
		  		
		  			window.location = listOfMembershipsUrl;
		  			
		  		})
			.fail(function(data) {
						if (data && data.responseText) {
							var status = JSON.parse(data.responseText);
							showSingleValidationMessage("validationMessage", status.message);
						}
				  }
				 );
}

/*--------------------------------------------------------
KRAJ - prijem obavestenja
---------------------------------------------------------*/