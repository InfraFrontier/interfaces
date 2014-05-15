/* Handles populating the form fields for biblio references data selected from list */

function populateBibDetails(userString,refNo){
  //alert("User string="+ userString);
    var q = $.parseQuery(userString);
    $('#title').val(q.title);
    $('#author1').val(q.author1 + ", " + q.author2);
    $('#journal').val(q.journal);
    $('#year').val(q.year);
    $('#volume').val(q.volume);
    $('#pages').val(q.pages);
    $('#pubmed_id').val(q.paperid);
    //alert("BIBLIOID="+ q.biblioid);
    $('#id_biblio').val(q.biblioid);
    $('#notes').val(q.notes);
    $('#notesadditional').val(q.notesadditional);
    $('#reference_descr').val(q.reference_descr);
    $('#published').val("yes");
}
function removeBibDetails(){
    $('#pubmed_id').val(null);
    $('#reference_descr').val(null);
    $('#notes').val(null);
    $('#title').val(null);
    $('#author1').val(null);
    $('#journal').val(null);
    $('#year').val(null);
    $('#volume').val(null);
    $('#pages').val(null);
    $('#id_biblio').val(null);
    $('#notes').val(null);
    $('#notesadditional').val(null);
}

