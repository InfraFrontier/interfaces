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
<c:set var="subBibliosDAO"  value="${keyRef['bibliodaos']}"></c:set>
<c:set var="count"  value="${keyRef['count']}"></c:set>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<!DOCTYPE html>
<div name="addBib" id="addBib">
    <p>
        <input value="Add Biblio" type="button" id="add_biblio" /> You can add ${10 - count} more reference<c:if test="${10 - count != 1}">s</c:if>.
    </p>
</div>
<h3>Bibliographic references for Submission</h3>
<script type="text/javascript" >  $("#addBib").show(); </script>
<c:choose>
    <c:when test="${count<=0}">No references added.</c:when>
    <c:otherwise>
        <table width="60%" align="center">
            <tr>
                <th>Pubmed ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Journal/Year/Volume/Pages</th>
                <th>Action</th>
            </tr>
            <c:forEach var="bib" items="${subBibliosDAO}">
                <tr>
                    <td>${bib.pubmed_id})</td>
                    <td>${bib.title}</td>
                    <td>${bib.author1}</td>
                    <td>${bib.journal} / ${bib.year} / ${bib.volume} / ${bib.pages}</td>
                    <td><a href="javascript:void(0)" title="${bib.id_biblio}" id="editReference_${bib.id_biblio}">Edit</a> / <a href="javascript:void(0)"  id="deleteReference_${bib.id_biblio}" title="${bib.id_biblio}">Remove</a></td>
                </tr>
            </c:forEach> 
        </table>
    </c:otherwise>
</c:choose>
<script type="text/javascript" > 
    $("a[id^='editReference']").click(function() {
        alert("Handler for edit mutation called. FUNCTIONALITY NOT IN PLACE YET!");

        $('#subMutations').load('ajaxMutations.emma',{
            action: "edit",
            Id_sub:$('#encID').val(),
            Id_bib:$(this).attr("title"),
            pubmed_id:$('#pubmed_id').val(),
            title:$('#title').val(),
            author1:$('#author1').val(),
            journal:$('#journal').val(),
            year:$('#year').val(),
            volume:$('#volume').val(),
            pages:$('#pages').val()
        });
        alert($(this).attr("title"));
    });
        
    $("a[id^='deleteReference']").click(function() {
        alert("Your bibliographic reference will be deleted from your submission\n\n\
Are you sure you wish to continue? ");
        $('#subBiblios').load('ajaxBiblios.emma',{
            action: "delete",
            Id_sub:$('#encID').val(),
            Id_bib:$(this).attr("title")
        });
    });
    $("addBib").hide();

</script>
<script type="text/javascript" > 
    $('#add_mutation').click(function() {
        $('#subMutations').load('ajaxMutations.emma',{
            action: "add",
            Id_sub:$('#encID').val(), 
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
<c:if test="${count < 10}"><script type="text/javascript" >  $("#addBib").show(); </script></c:if>
