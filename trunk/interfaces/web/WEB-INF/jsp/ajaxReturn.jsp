<%-- 
    Document   : ajaxReturn.jsp
    Created on : 09-Feb-2010, 15:23:11
    Author     : phil

--%>
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    response.setHeader("Cache-Control", "no-store");
%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--<script type="text/javascript" src="../js/jquery.parsequery.js"></script>
<script type="text/javascript" src="../js/jquery.parsequery.min.js"></script>--%>

<c:set var="keyRef" value='${returnedOut}' scope="page"></c:set>
<c:choose>
    <c:when test='${not empty keyRef["ajaxReturn"] && empty param.query}'>
        <c:forEach var="strain"  items='${keyRef["ajaxReturn"]}'>
            ${strain[0]}|${strain[1]}|${strain[2]}
        </c:forEach>
    </c:when>
    <c:otherwise>
        <c:if test='${param.query=="bg"}'>
            <c:choose>
                <c:when test='${param.list=="cryo"}'>
                    <c:forEach var="bg"  items='${keyRef["ajaxReturn"]}'>
                        ${bg.name}
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach var="bg"  items='${keyRef["ajaxReturn"]}'>
                        ${bg[1]}
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:if test='${param.query=="es"}'>

            <c:forEach var="es"  items='${keyRef["ajaxReturn"]}'>
                ${es}
            </c:forEach>
        </c:if>
    </c:otherwise>
</c:choose>
<c:if test='${empty keyRef["ajaxReturn"] &&  not empty param.display}'>
    <c:if test="${param.display == 'disabled'}" >
        <c:set var="displayClass" value="disabled"/>
        <c:set var="fieldDisplay" value="disabled"/>
    </c:if>   
    <c:if test="${param.display == 'enabled'}" >
        <c:set var="displayClass" value=""/>
        <c:set var="fieldDisplay" value=""/>
    </c:if>
</c:if>
<%--
JSON return for submission form people data fro PI and contact
--%>

<c:if test='${not empty keyRef["JSON"] && param.funct == "peopleCall"}'>
    <%--        <html>
<head>
<meta http-equiv="Content-Type" content="application/json, charset=UTF-8" />

        <title></title>
    </head>
    <body>
        ${keyRef.JSON}
        <c:forEach  varStatus="status" var="item"  items='${keyRef.JSON}'>
            ${item.firstname} ${item.surname}<br/>
            <br/>
        </c:forEach>
    --%>  
    <c:set var="PeopleDAO" value="${keyRef.peopleDAO}" />  
    <c:if test="${not empty PeopleDAO}">
        The following user data is held associated with the e-mail address  ${command.peopleDAO.email} :<br/>
        <c:forEach var="obj" items="${PeopleDAO}">
            <p class="userdetailspanel">
                <c:set var="user" value="${obj}"/> 
                Name: ${user['firstname']}&nbsp;
                ${user['surname']}<br/>
                Email: ${user['email']}<br/>
                Phone: ${user['phone']}<br/>
                <c:if test="${not empty user['fax']}">Fax: ${user['fax']}<br/></c:if>
                <c:if test="${not empty user.labsDAO['name']}">Institute: ${user.labsDAO['name']}<br/></c:if>
                <c:if test="${not empty user.labsDAO['dept']}">Department: ${user.labsDAO['dept']}<br/></c:if>
                <c:if test="${not empty user.labsDAO['addr_line_1']}">Address Line 1: ${user.labsDAO['addr_line_1']}<br/></c:if>
                <c:if test="${not empty user.labsDAO['addr_line_2']}">Address Line 2: ${user.labsDAO['addr_line_2']}<br/></c:if>
                <c:if test="${not empty user.labsDAO['town']}">Town: ${user.labsDAO['town']}<br/></c:if>
                <c:if test="${not empty user.labsDAO['province']}">County: ${user.labsDAO['province']}<br/></c:if>
                <c:if test="${not empty user.labsDAO['postcode']}">Postcode: ${user.labsDAO['postcode']}<br/></c:if>
                <c:if test="${not empty user.labsDAO['country']}">Country: ${user.labsDAO['country']}<br/></c:if>
                <br/>Would you like to use the details above ?&nbsp;
                <input type="button" id="modal" name="yes" onClick="populateUserDetails('?uid=${user['id_per']}\
&email=${user['email']}&title=${user['title']}&firstname=${user['firstname']}&surname=${user['surname']}&phone=${user['phone']}\
&fax=${user['fax']}&institute=${user.labsDAO['name']}&dept=${user.labsDAO['dept']}\
&addr_line_1=${user.labsDAO['addr_line_1']}&addr_line_2=${user.labsDAO['addr_line_2']}\
&town=${user.labsDAO['town']}&county=${user.labsDAO['province']}\
&postcode=${user.labsDAO['postcode']}&country=${user.labsDAO['country']}&ilar=${user.ilarDAO['labcode']}&fieldset=${param.fieldset}')" 
                       value="Yes"/><br/>
            </p>
        </c:forEach>
        Or click anywhere else to return to the form.
    </c:if>
    <%--</body>
</html> --%>
</c:if>
<c:set var="paper" value="${keyRef.paper}" />  
<c:if test="${not empty paper}">
    <script type="text/javascript" >
        populateBibDetails("?title=${paper[0]}&author1=${paper[1]}&author2=${paper[2]}\
&journal=${paper[3]}&year=${paper[4]}&volume=${paper[5]}&issue=${paper[6]}&pages=${paper[7]}\
&paperid=${paper[8]}&biblioid=${paper[9]}&notes=${paper[10]}&notesadditional=${paper[11]}&published=yes",0);

    </script>
</c:if>
<c:if test="${empty paper}">
    <%--error message here--%>
</c:if>

<c:set var="mutations" value="${keyRef.mutations}" />
<c:if test="${not empty mutations}">
    <script type="text/javascript" >
        populateMutDetails("?mutation_allele_mgi_symbol=${mutations[0]}&mutation_chrom=${mutations[1]}\
&mutation_chrom_anomaly_descr=${mutations[2]}&Mutation_chrom_anomaly_name=${mutations[3]}\
&mutation_dominance_pattern=${mutations[4]}&mutation_es_cell_line=${mutations[5]}&mutation_founder_line_number=${mutations[6]}\
&mutation_gene_mgi_symbol=${mutations[7]}&mutation_mutagen=${mutations[8]}&mutation_original_backg=${mutations[9]}\
&mutation_original_backg_text=${mutations[10]}&mutation_plasmid=${mutations[11]}&mutation_promoter=${mutations[12]}\
&mutation_subtype=${mutations[13]}&mutation_transgene_mgi_symbol=${mutations[14]}&mutation_type=${mutations[15]}\
&mutationcount=${mutations[16]}&mutationid=${mutations[17]}&mutid=${mutations[17]}",0);
    </script>
</c:if>

<c:set var="files" value="${keyRef.fileListing}" scope="page"/>

<c:if test="${not empty files}">
    <br/>
    <c:forEach var="file" items="${files}" varStatus="status">
        <c:if test="${status.count <= 5}"><img src="../images/pdf_icon.gif" width="28" height="29" border ="0" valign="middle"/>&nbsp;${file} uploaded<br/></c:if></c:forEach> 
        <br/>
</c:if>          