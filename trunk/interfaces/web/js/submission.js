/* 
 * 
 */


//function to control visibility of mutation fields
function mutFieldDisplay() {
    var value = $("#mutation_type").val();
    $("#field\\ mutation_subtype_conditionalCH").hide("slow");
    $("#field\\ mutation_gene_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").hide("slow");
    $("#field\\ mutation_allele_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").hide("slow");
    $("#field\\ mutation_chrom\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").hide("slow");
    $("#field\\ mutation_subtype\\ conditional\\ CH").hide("slow");
    $("#field\\ mutation_subtype\\ conditional\\ IN").hide("slow");
    $("#field\\ mutation_transgene_mgi_symbol\\ conditional\\ TG").hide("slow");
    $("#field\\ mutation_promoter\\ conditional\\ TG").hide("slow");
    $("#field\\ mutation_founder_line_number\\ conditional\\ TG").hide("slow");
    $("#field\\ mutation_plasmid conditional\\ TG").hide("slow");
    $("#field\\ mutation_subtype\\ conditional\\ TM").hide("slow");
    $("#field\\ mutation_chrom_anomaly_name\\ conditional\\ CH").hide("slow");
    $("#field\\ mutation_chrom_anomaly_descr\\ conditional\\ CH").hide("slow");
    $("#field\\ mutation_mutagen\\ conditional\\ IN").hide("slow");
    $("#field\\ mutation_es_cell_line\\ conditional\\ GT\\ TM").hide("slow");
    //alert(value);
    if(value=="CH"){
        //alert("equality");
        $("#field\\ mutation_subtype_conditionalCH").show("slow");
        $("#field\\ mutation_gene_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_allele_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_chrom\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_subtype\\ conditional\\ CH").show("slow");
        $("#field\\ mutation_chrom_anomaly_name\\ conditional\\ CH").show("slow");
        $("#field\\ mutation_chrom_anomaly_descr\\ conditional\\ CH").show("slow");
    }else if(value=="GT"){
        $("#field\\ mutation_gene_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_allele_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_chrom\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_es_cell_line\\ conditional\\ GT\\ TM").show("slow");
    }else if(value=="IN"){
        $("#field\\ mutation_subtype\\ conditional\\ IN").show("slow");
        $("#field\\ mutation_gene_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_allele_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_chrom\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_mutagen\\ conditional\\ IN").show("slow");
    }else if(value=="SP"){
        $("#field\\ mutation_gene_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_allele_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_chrom\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
    }else if(value=="TG"){
        $("#field\\ mutation_transgene_mgi_symbol\\ conditional\\ TG").show("slow");
        $("#field\\ mutation_promoter\\ conditional\\ TG").show("slow");
        $("#field\\ mutation_founder_line_number\\ conditional\\ TG").show("slow");
        $("#field\\ mutation_plasmid\\ conditional\\ TG").show("slow");
    }else if(value=="TM"){
        $("#field\\ mutation_subtype\\ conditional\\ TM").show("slow");
        $("#field\\ mutation_gene_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_allele_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_chrom\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_es_cell_line\\ conditional\\ GT\\ TM").show("slow");
    }else if(value=="XX"){
        $("#field\\ mutation_gene_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_allele_mgi_symbol\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
        $("#field\\ mutation_chrom\\ conditional\\ CH\\ GT\\ IN\\ SP\\ TM\\ XX").show("slow");
    }
}
//clear text areas and form elements
function clear_form_elements(element) {
    alert("About to clear mutation\nDo you want to continue?");
    tags = element.getElementsByTagName('input');
    for(i = 0; i < tags.length; i++) {
        switch(tags[i].type) {
            case 'password':
            case 'text':
                tags[i].value = '';
                break;
            case 'checkbox':
            case 'radio':
                tags[i].checked = false;
                break;
        }
    }
   
    tags = element.getElementsByTagName('select');
    for(i = 0; i < tags.length; i++) {
        if(tags[i].type == 'select-one') {
            tags[i].selectedIndex = 0;
        }
        else {
            for(j = 0; j < tags[i].options.length; j++) {
                tags[i].options[j].selected = false;
            }
        }
    }

    tags = element.getElementsByTagName('textarea');
    for(i = 0; i < tags.length; i++) {
        tags[i].value = '';
    }
   
}
function testcall() {
     alert("Test called\nDo you want to continue?");
}