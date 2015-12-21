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
    Document   : alleleUpdateInterface
    Created on : Nov 13, 2012, 4:53:57 PM
    Author     : phil
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page import="org.emmanet.util.Configuration" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<spring:bind path="command.*"></spring:bind>
<c:set var="keyRef" value='${command}'></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Allele Update Interface (curation)</title>
        <style type="text/css">@import url(../css/emmastyle.css);</style>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="../js/jquery.parsequery.js"></script>
        <script type="text/javascript" src="../js/jquery.parsequery.min.js"></script>
        <script type="text/javascript" src="../js/autocomplete/autocomplete.js"></script>
        <link rel="stylesheet" type="text/css" href="../css/autocomplete/autocomplete.css">
    </head>
    <body>
        <%-- <span id="loginHeader">Allele Update Interface - Logged in as user <c:out value="${fn:toUpperCase(sessionScope.SPRING_SECURITY_LAST_USERNAME)}"/></span> --%>
        <span id="loginHeader">Allele Update Interface - Logged in as user "<sec:authentication property='principal.username'/>"</span>
        <br /><br />

    <center>
        <c:if test="${not empty status.errorMessages}">
            <c:forEach var="error" items="${status.errorMessages}">
                <font color="red"><c:out value="${error}" escapeXml="false" /> </font>
                <br />
            </c:forEach>
        </c:if>

        <c:if test="${not empty message}">
            <font color="green"><c:out value="${message}" /></font>
            <c:set var="message" value="" scope="session" />
        </c:if>
    </center>
<script type="text/javascript">
$(document).ready(function(){
    $("#mgi_ref").keyup(function(){
       // alert($(this).val());
    });

})

function checkMGIRef(mgiID) {
window.open("http://www.informatics.jax.org/searches/accession_report.cgi?id=MGI:"+ window.document.forms[0].elements["mgi_ref"].value, "blank");
}
</script>
    <script type="text/javascript">
        $(document).ready(function(){
           
            $("#strainID").autocomplete("../ajaxReturn.emma",{ mustMatch:1,max:100});


            $('#strainID').result(function(event, data, formatted) {
                if (data) {
       
                    // Extract the data values          
                    var emma_id = data[0];
                    var name = data[1];
                    var id = data[2];
                     
                    // document.forms[0].strainID.value = emma_id; 
                    $('#strain_name').text(name);
                    $('#emma_id').text(emma_id);
                    $('#id').text(id);
                    document.location.replace("?strainID=" + id );     
                }
            });
        });
    </script>
    <c:choose>
        <c:when test="${not empty sessionScope.emmaID || sessionScope.strainName}">
            <center>Editing Alleles for EMMA ID:: ${sessionScope.emmaID}, Strain name: ${sessionScope.strainName}</center>
        </c:when>
        
        <c:when test="${not empty param.strainID || param.strainName}">
            <center>Editing Alleles for EMMA ID:: ${param.strainID}, Strain name: ${param.strainName}</center>
        </c:when>
        <c:otherwise></c:otherwise>
    </c:choose>

    <div id="strain_name" name="strain_name"></div>&nbsp;&nbsp;<div id="emma_id" name="emma_id"></div>&nbsp;&nbsp;<div id="id" name="id"></div>
    <form:form>
        <table> 
            <tr><td class="boxoutB" align="right"><b>Select strain id</b>&nbsp;&nbsp;</td>
                <td class="boxoutB" colspan="3" align="left"><input  id="strainID"  size="10"></input></td>
            </tr>
                        <tr>
                <td colspan="4"><br/></td>
                </tr>
            <c:if test="${not empty sessionScope.emmaID && sessionScope.action == null}">
                <c:forEach var="mut" items="${command}" varStatus="index" >
                    <c:set var="mutDAO" value="${mut.mutationsDAO}"/>
                    <c:set var="allelesDAO" value="${mutDAO.allelesDAO}"/>
                                       <tr>
                        <td><b>Mutation:</b></td> <td>${allelesDAO.name}</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr> <tr>
                        <td><b>Allele name:</b></td> <td>${allelesDAO.name}</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                    <td><b>Alleles Form:</b></td> <td>${allelesDAO.alls_form}</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><b>MGI ref:</b></td> <td><a href="http://www.informatics.jax.org/searches/accession_report.cgi?id=MGI:${allelesDAO.mgi_ref}" target="_mgiWin" id="mgiDetails" text="Search MGI"  name="mgiDetails">${allelesDAO.mgi_ref}</a></td><%-- http://www.informatics.jax.org/searches/accession_report.cgi?id=MGI:${allelesDAO.mgi_ref}" target="_blank" --%>
                <td>&nbsp;</td>
                <td><div id="mgiDetailsDisplay"></div></td>
                </tr>
                <tr>
                <td><b>Gene ID:</b></td> <td>${allelesDAO.genesDAO.name}(${allelesDAO.gen_id_gene})</td>
                <td>&nbsp;</td>
                <td align="right"><input type="button" name="Edit allele ${allelesDAO.id_allel}" value="Edit Allele" onClick="parent.location='?action=edit&alleleID=${allelesDAO.id_allel}&strainID=${sessionScope.emmaID}&strainName=${sessionScope.strainName}'"/></td>
            </tr> 
            <tr> 
               <td colspan="4" class="boxoutB"></td>
                </tr>
        </c:forEach>
    </c:if>
            <%-- EDITING FIELDS HERE --%>
            <c:if test="${param.action == 'edit' || param.action == 'add'}">
                <spring:bind path="command.name">
                    <tr>
                        <td><b>Allele name:</b></td> <td><form:input  id="${status.expression}" path="${status.expression}"></form:input></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                 </spring:bind>
                 <spring:bind path="command.alls_form">
                 <tr>
                    <td><b>Alleles Form:</b></td> <td><form:input  id="${status.expression}" path="${status.expression}"></form:input></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                </spring:bind>
                <spring:bind path="command.mgi_ref">
                  <tr>
                <td><b>MGI ref:</b></td> <td><form:input  id="${status.expression}" path="${status.expression}"></form:input>&nbsp;<a href="javascript:void(0)" id="mgiDetails" text="Search MGI"  name="mgiDetails" onclick="checkMGIRef(this)">Test if exists</a></td>
                <td>&nbsp;</td> 
                </spring:bind>
                </tr>
                <spring:bind path="command.gen_id_gene">
                  <tr>
                <td><b>Gene: ${command.gen_id_gene} </b></td>
                <td>
      <form:select path="${status.expression}" id="${status.expression}"  title="Please select the gene name from the list.">
                <form:option value='0'>Please select gene...</form:option>
            <c:forEach var="gene" items="${sessionScope.allGenes}" varStatus="stat">
                <form:option value='${gene[0]}'>${gene[1]}</form:option>
            </c:forEach>     
            </form:select>  
                </td>
                <td>&nbsp;</td> 
                 <td>&nbsp;</td> 
                </tr>
                </spring:bind>
            </c:if>

 <%--<tr>
        <td colspan="4">
        </td>
    </tr>--%>
    <tr>
        <td colspan="4" class="boxoutB" align="center">
            <c:choose>
                <c:when test="${param.action == 'edit'}"><input type="submit" name="submit" /></c:when>  
            <c:when test="${param.action == 'add'}"><input type="submit" name="submit" /></c:when>
            <c:otherwise><c:if test="${not empty param.strainID}"><input type="button" name="Add new allele" value="Add New Allele" onClick="parent.location='?action=add&strainID=${sessionScope.emmaID}&strainName=${sessionScope.strainName}'"/></c:if></c:otherwise>
            </c:choose>
        </td>
    </tr>

</table>    
</form:form>

</body>
</html>
