/*
 * Daci, 27.01.2014.
 */

jQuery.noConflict();

//jQuery(function($) {
//	addTooltips([ "sync-type", "user-name", "user-email" ]);
//});


jQuery(document).ready(function($) {

  /*----------- BEGIN TABLESORTER CODE -------------------------*/
  /* required jquery.tablesorter.min.js*/
  jQuery(".sortableTable").tablesorter();
  /*----------- END TABLESORTER CODE -------------------------*/
  
});


/*--------------------------------------------------------
BEGIN FORM-GENERAL.HTML SCRIPTS
---------------------------------------------------------*/
function formGeneral() {

 jQuery('.with-tooltip').tooltip({
     selector: ".input-tooltip"
 });

 /*----------- BEGIN tagsInput CODE -------------------------*/
 //jQuery('#tags').tagsInput();
 /*----------- END tagsInput CODE -------------------------*/

 /*----------- BEGIN chosen CODE -------------------------*/

 jQuery(".chzn-select").chosen();
 jQuery(".chzn-select-deselect").chosen({
     allow_single_deselect: true
 });
 /*----------- END chosen CODE -------------------------*/


 /*----------- BEGIN uniform CODE -------------------------*/
 jQuery('.uniform').uniform();
 /*----------- END uniform CODE -------------------------*/

 /*----------- BEGIN toggleButtons CODE -------------------------*/
 jQuery('#normal-toggle-button').toggleButtons();

 jQuery('#callback-toggle-button').toggleButtons({
     onChange: function($el, status, e) {
         console.log($el, status, e);
         jQuery('#magic-text').text("Status is: " + status);
     }
 });
 jQuery('#text-toggle-button').toggleButtons({
     width: 220,
     label: {
         enabled: "Bizstrap",
         disabled: "Admin"
     }
 });
 jQuery('#danger-toggle-button').toggleButtons({
     style: {
         // Accepted values ["primary", "danger", "info", "success", "warning"] or nothing
         enabled: "danger",
         disabled: "info"
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