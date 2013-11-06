function populateMutDetails(userString,refNo){
   // alert("User string="+ userString);
    var q = $.parseQuery(userString);
   // alert("mutData.js id val = " + q.mutid);
    $('#mutation_allele_mgi_symbol').val(q.mutation_allele_mgi_symbol);
    $('#mutation_chrom').val(q.mutation_chrom);
    $('#mutation_chrom_anomaly_name').val(q.mutation_chrom_anomaly_name);
    $('#mutation_chrom_anomaly_descr').val(q.mutation_chrom_anomaly_descr);
    $('#mutation_dominance_pattern').val(q.mutation_dominance_pattern);
    $('#mutation_es_cell_line').val(q.mutation_es_cell_line);
    $('#mutation_founder_line_numb§er').val(q.mutation_founder_line_number);
    $('#mutation_gene_mgi_symbol').val(q.mutation_gene_mgi_symbol);
    $('#mutation_mutagen').val(q.mutation_mutagen);
    $('#mutation_original_backg').val(q.mutation_original_backg);
    $('#mutation_original_backg_text').val(q.mutation_original_backg_text);
    $('#mutation_plasmid').val(q.mutation_plasmid);
    $('#mutation_promoter').val(q.mutation_promoter);
    $('#mutation_subtype').val(q.mutation_subtype);
    $('#mutation_transgene_mgi_symbol').val(q.mutation_transgene_mgi_symbol);
    $('#mutation_type').val(q.mutation_type);
    $('#mutationcount').val(q.mutationcount);
    $('#mutationid').val(q.mutationid);
    $('#id_mut').val(q.mutid);
    $("#mutation_type").change(mutFieldDisplay);
    mutFieldDisplay();
}
      
            
            
           