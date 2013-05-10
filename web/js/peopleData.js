/* Handles populating the form fields for people data selected from list */
function populateUserDetails(userString){
    close_modal();
    //alert("User string="+ userString);  
    q = $.parseQuery(userString);
    
    if(q.fieldset=='submitter'){
        $('#per_id_per_sub').val(q.uid);
    }else if (q.fieldset=='producer'){
        $('#per_id_per').val(q.uid);
    }else if (q.fieldset=='shipper'){
       $('#per_id_per_contact').val(q.uid);
    }
   
    $('#'+q.fieldset+'_email').val(q.email);
    $('#'+q.fieldset+'_title').val(q.title);
    $('#'+q.fieldset+'_firstname').val(q.firstname);
    $('#'+q.fieldset+'_lastname').val(q.surname);
    $('#'+q.fieldset+'_tel').val(q.phone);
    $('#'+q.fieldset+'_fax').val(q.fax);
        
    $('#'+q.fieldset+'_inst').val(q.institute);
    $('#'+q.fieldset+'_dept').val(q.dept);
    $('#'+q.fieldset+'_addr_1').val(q.addr_line_1);
    $('#'+q.fieldset+'_addr_2').val(q.addr_line_2);
    $('#'+q.fieldset+'_city').val(q.town);
    $('#'+q.fieldset+'_county').val(q.county);
    $('#'+q.fieldset+'_postcode').val(q.postcode);
    $('#'+q.fieldset+'_country').val(q.country);
    $('#'+q.fieldset+'_authority').val(q.auth);
    $('#'+q.fieldset+'_ilar').val(q.ilar);
}

$('#peopleDAO\\.email').focusout(function() {
    alert('OUT FOCUS');
});


