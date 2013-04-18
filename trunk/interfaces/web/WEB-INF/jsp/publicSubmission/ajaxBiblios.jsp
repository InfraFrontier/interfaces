<%-- 
    Document   : ajaxBiblios
    Created on : 12-Jul-2012, 11:46:43
    Author     : phil
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<c:set var="keyRef"  value="${returnedOut}"></c:set>
<c:set var="subBibliosDAO"  value="${keyRef['subBiblios']}"></c:set>
<c:set var="count"  value="${keyRef['count']}"></c:set>
<c:set var="sessencID"  value="${sessionScope.getprev}"></c:set>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery.parsequery.js"></script>
<script type="text/javascript" src="../js/jquery.parsequery.min.js"></script>
<!DOCTYPE html>
<div name="addBib" id="addBib">
    <p>
       <input value="Add Biblio" class="btn" type="button" id="add_biblio" /> You can add ${10 - count} more reference<c:if test="${10 - count != 1}">s</c:if>.
    </p>
</div>
<div name="editBib" id="editBib" style="display: none">
    <p>
        <input value="Edit Biblio" class="btn" type="button" id="edit_biblio" /> You can add ${10 - count} more reference<c:if test="${10 - count != 1}">s</c:if>.
    </p>
</div>
    <p>&nbsp;</p>
<h4>Bibliographic references for Submission</h4>
<script type="text/javascript" >  $("#addBib").show(); </script>
<c:choose>
    <c:when test="${empty count || count<=0}">No references added.</c:when>
    <c:otherwise>
         <div class="boxcontainer">
            <div class="box full">
        <table width="100%">
            <thead>
            <tr>
                <th>Pubmed ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Journal/Year/Volume/Pages</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="bib" items="${subBibliosDAO}">
                <tr>
                    <td>${bib.pubmed_id}</td>
                    <td>${fn:substring(bib.title,0,25)}...</td>
                    <td>${bib.author1}</td>
                    <td>${bib.journal} / ${bib.year} / ${bib.volume} / ${bib.pages}</td> 
                    <td><a href="javascript:void(0)" class="icon edit" title="${bib.id_biblio}" id="editReference_${bib.id_biblio}">Edit</a>&nbsp;&nbsp;<a href="javascript:void(0)"  class="icon remove" id="deleteReference_${bib.id_biblio}" title="${bib.id_biblio}">Remove</a></td>
                </tr>
            </c:forEach> 
                </tbody>
        </table>
    </c:otherwise>
</c:choose>
<script type="text/javascript" > 
    //$("#editBib").show();
    $("a[id^='editReference']").click(function() {
        $("#addBib").hide();
        $("#editBib").show();
        alert("Publication details will be returned to the fields for editing.");
        $('#bibRef').load('../ajaxReturn.emma',{biblioid:$(this).attr("title"), funct: "bibliosEdit"});

        // alert($(this).attr("title"));
 
    });
    
    //edit_biblio
    $('#edit_biblio').click(function() {
        
         $('#subBiblios').load('ajaxBiblios.emma',{
            action: "edit",
            action2: "editRecord",
            Id_sub:"${sessencID}",/*$('#encID').val(),*/ 
            Id_bib:$('#id_biblio').val(),
            id_biblio:$('#id_biblio').val(),
            pubmed_id:$('#pubmed_id').val(),
            title:$('#title').val(),
            author1:$('#author1').val(),
            journal:$('#journal').val(),
            year:$('#year').val(),
            volume:$('#volume').val(),
            pages:$('#pages').val()
            
        });
        removeBibDetails();
        });
        
    $("a[id^='deleteReference']").click(function() {
        alert("Your bibliographic reference will be deleted from your submission\n\n\
Are you sure you wish to continue? ");
        $('#subBiblios').load('ajaxBiblios.emma',{
            action: "delete",
            Id_sub:"${sessencID}",//$('#encID').val(),
            Id_bib:$(this).attr("title")
        });
    });
    $("#addBib").hide();

</script>
<script type="text/javascript" > 
    $('#add_biblio').click(function() {
      //  alert("Adding biblio ${sessencID}" );
        $('#subBiblios').load('ajaxBiblios.emma',{
            action: "add",
            Id_sub:"${sessencID}",//$('#encID').val(), 
            Id_bib:$(this).attr("title"),
            pubmed_id:$('#pubmed_id').val(),
            title:$('#title').val(),
            author1:$('#author1').val(),
            journal:$('#journal').val(),
            year:$('#year').val(),
            volume:$('#volume').val(),
            pages:$('#pages').val()
        });
    });
</script>
<c:if test="${count >= 10}"><script type="text/javascript" >  $("#addBib").hide(); </script></c:if>
<c:if test="${empty count || count < 10}"><script type="text/javascript" >  $("#addBib").show(); </script></c:if>
