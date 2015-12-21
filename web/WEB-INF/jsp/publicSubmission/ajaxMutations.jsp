<%--
  #%L
  InfraFrontier
  $Id:$
  $HeadURL:$
  %%
  Copyright (C) 2015 EMBL-European Bioinformatics Institute
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  --%>
<%-- 
    Document   : ajaxMutations
    Created on : 13-Jun-2012, 16:39:04
    Author     : phil
--%>
<%
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        response.setHeader("Cache-Control", "no-store");
%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<c:set var="keyRef"  value="${returnedOut}"></c:set>
<c:set var="subMutsDAO"  value="${keyRef['mutdaos']}"></c:set>
<c:set var="singleSubMutsDAO"  value="${keyRef['SubMutDAO']}"></c:set>
<c:set var="count"  value="${keyRef['count']}"></c:set>
<c:set var="sessencID"  value="${sessionScope.getprev}"></c:set>


<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<!DOCTYPE html>

<div name="addMut" id="addMut">
    <p>
        <input value="Record this mutation" class="btn big" type="button" id="add_mutation" style="background: #ea5b0b;"/> <c:if test="${empty command}">You can add ${10 - count} more mutation<c:if test="${10 - count != 1}">s</c:if>.</c:if>
    </p>
</div>
    <div name="editMut" id="editMut" style="display: none">
    <p>
        <input value="Edit Mutation" class="btn" type="button" id="edit_mutation" /> You can add ${10 - count} more mutation<c:if test="${10 - count != 1}">s</c:if>.
    </p>
</div>
<p>&nbsp;</p>
                       
<h4>Mutations for Submission</h4>
<script type="text/javascript" >  $("#addMut").show(); </script>
<c:choose>
    <c:when test="${not empty command}">
        <c:if test="${not empty command}">
    <spring:bind path="command.*">
        <c:if test="${not empty status.errorMessages}">
            <center>
                <c:forEach var="error" items="${status.errorMessages}">
                    <font color="red"><c:out value="${error}" escapeXml="false" /> </font>
                    <br />
                </c:forEach>
            </c:if>
        </spring:bind>
    </c:if>  
    </c:when>
    <c:when test="${count<=0}">No mutations added. You must record at least one mutation.</c:when>
    <c:otherwise>
        <div class="boxcontainer">       
            <div class="box full">
                <table width="100%">
                    <thead>
                        <tr>
                            <th>Original Background</th>
                            <th>Mutation Type</th>
                            <th>Mutation Subtype</th>
                            <th>Dominance Pattern</th>
                            <th>Affected Gene</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="mut" items="${subMutsDAO}">
                            <tr>
                                <td>${mut.backgroundDAO.name}</td>
                                <td>${mut.mutation_type}</td>
                                <td>${mut.mutation_subtype}</td>
                                <td>${mut.mutation_dominance_pattern}</td>
                                <td>${mut.mutation_gene_mgi_symbol}</td> 
                                <td><a href="javascript:void(0)" class="icon edit" title="${mut.id}" id="editmutation_${mut.id}">Edit</a>&nbsp;&nbsp;<a href="javascript:void(0)"  class="icon remove" id="deletemutation_${mut.id}" title="${mut.id}">Remove</a></td>
                            </tr>
                        </c:forEach> 
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<script type="text/javascript" > 
    $("a[id^='editmutation']").click(function() {
        $("#addMut").hide();
        $("#editMut").show();
        alert("Mutation details will be returned to the fields for editing.Click Edit Mutation to save changes.");
        $('#mutRef').load('../ajaxReturn.emma',{mutid:$(this).attr("title"), funct: "mutationEdit"});
    });
    
    //edit_mutation
      $('#edit_mutation').click(function() {
        $('#subMutations').load('ajaxMutations.emma',{
            action: "edit",
            action2: "editRecord",
            Id_sub:$('#encID').val(),
            Id_mut:$('#id_mut').val(),
            mutation_allele_mgi_symbol:$('#mutation_allele_mgi_symbol').val(),
            mutation_chrom:$('#mutation_chrom').val(), 
            mutation_chrom_anomaly_name:$('#mutation_chrom_anomaly_name').val(), 
            mutation_chrom_anomaly_descr:$('#mutation_chrom_anomaly_descr').val(), 
            mutation_dominance_pattern:$('#mutation_dominance_pattern').val(), 
            mutation_es_cell_line:$('#mutation_es_cell_line').val(), 
            mutation_founder_line_number:$('#mutation_founder_line_number').val(), 
            mutation_gene_mgi_symbol:$('#mutation_gene_mgi_symbol').val(), 
            mutation_mutagen:$('#mutation_mutagen').val(), 
            mutation_original_backg:$('#mutation_original_backg').val(), 
            mutation_original_backg_text:$('#mutation_original_backg_text').val(), 
            mutation_plasmid:$('#mutation_plasmid').val(), 
            mutation_promoter:$('#mutation_promoter').val(),
            mutation_subtype:$('#mutation_subtype').val(),
            mutation_transgene_mgi_symbol:$('#mutation_transgene_mgi_symbol').val(),
            mutation_type:$('#mutation_type').val()
        });
  });
        
    $("a[id^='deletemutation']").click(function() {
        var r= confirm("Your mutation will be deleted from your submission\n\n\ ");
        if (r==true)
        {
            $('#subMutations').load('ajaxMutations.emma',{
                action: "delete",
                Id_sub:"${sessencID}",
                Id_mut:$(this).attr("title")
            });
        }
    });
    $("addMut").hide();

</script>

<script type="text/javascript" > 
    $('#add_mutation').click(function() {
        // alert("ID Value is:: ${sessencID}")
        $('#subMutations').load('ajaxMutations.emma',{
            action: "add",
            Id_sub:$('#encID').val(),
            IDFromSession:"${sessencID}",
            mutation_allele_mgi_symbol:$('#mutation_allele_mgi_symbol').val(),
            mutation_chrom:$('#mutation_chrom').val(), 
            mutation_chrom_anomaly_name:$('#mutation_chrom_anomaly_name').val(), 
            mutation_chrom_anomaly_descr:$('#mutation_chrom_anomaly_descr').val(), 
            mutation_dominance_pattern:$('#mutation_dominance_pattern').val(), 
            mutation_es_cell_line:$('#mutation_es_cell_line').val(), 
            mutation_founder_line_number:$('#mutation_founder_line_number').val(), 
            mutation_gene_mgi_symbol:$('#mutation_gene_mgi_symbol').val(), 
            mutation_mutagen:$('#mutation_mutagen').val(), 
            mutation_original_backg:$('#mutation_original_backg').val(), 
            mutation_original_backg_text:$('#mutation_original_backg option:selected').text(), 
            mutation_plasmid:$('#mutation_plasmid').val(), 
            mutation_promoter:$('#mutation_promoter').val(),
            mutation_subtypeCH:$('#mutation_subtypeCH').val(),
            mutation_subtypeIN:$('#mutation_subtypeIN').val(),
            mutation_subtypeTM:$('#mutation_subtypeTM').val(),
            mutation_transgene_mgi_symbol:$('#mutation_transgene_mgi_symbol').val(),
            mutation_type:$('#mutation_type').val()
        });
        clear_form_elements(document.getElementById('mutation'));//added philw during it meeting july to try and resolve retained data reported by raffaele
    });
</script>
<c:if test="${count >= 10}"><script type="text/javascript" >  $("#addMut").hide(); </script></c:if>
<c:if test="${empty count || count < 10}"><script type="text/javascript" >  $("#addMut").show(); </script></c:if>

