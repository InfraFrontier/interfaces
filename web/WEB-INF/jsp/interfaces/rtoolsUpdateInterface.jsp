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
    Document   : rtoolsUpdateInterface.emma
    Created on : 05-Dec-2008, 10:12:42
    Author  .RToolsDAO    : phil
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="keyRef" value='${command}'></c:set>
<c:set var="RToolsDAO" value='${keyRef}'></c:set>

<spring:bind path="command.*"></spring:bind>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <style type="text/css">@import url(../css/emmastyle.css);</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Research Tools Update Interface</title>
        <script  type="text/javascript" >
            function redirect(selectID,rtoolsID,str_id_str) {
              alert(selectID + "--" + rtoolsID + "--" + str_id_str);
                //var select_id = document.getElementById(selectID);
              // select_id = select_id.options[select_id.selectedIndex].value;
                parent.location="?rtoolsID="+ rtoolsID + "&action=update&EditStrain=" + str_id_str;
            }
        </script>
    </head> 
    <body>
        <span id="loginHeader">Research tools update sub-window - Logged in as user <c:out value="${fn:toUpperCase(sessionScope.SPRING_SECURITY_LAST_USERNAME)}"/></span>
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

         <form:form method="POST" commandName="command">
            <table border="0" width="85%" align="center" >
                <tr>             
                    <td width="25%" align="undefined" valign="top">Research tools:</td>
                    <td  align="undefined" valign="top">
                       <c:choose>
                           <c:when test="${(param.action) == 'update'}">
                               <spring:bind path="rtls_id">
                                   <form:select path="${status.expression}" id="${status.expression}" title="">
                                                    <form:option value="">Please select..</form:option>
                                                    <form:option value="1">Cre recombinase expressing strain (Cre)</form:option>
                                                    <form:option value="2">Strain with loxP-flanked sequences (loxP)</form:option> 
                                                    <form:option value="3">FLP recombinase expressing strain (FLP)</form:option>
                                                    <form:option  value="4">Strain with FRT-flanked sequences (FRT)</form:option>
                                                    <form:option  value="5">Strain with Tet expression system (TET)</form:option>
                                                    <form:option  value="6">Lexicon strains from Wellcome Trust (LEX)</form:option>
                                                    <form:option  value="7">Deltagen strains from Wellcome Trust (DEL)</form:option>
                                                    <form:option  value="9">Strains generated from EUCOMM ES cell resource (EUC)</form:option>
                                                    <form:option  value="10">Strains generated from EUCOMMTools Cre resource (EUCre)</form:option>
                                   </form:select>
                               </spring:bind>
                                      <input type="submit" value="Update">
                           </c:when>
                           <c:otherwise>
                        <c:forEach var="Rtool" items='${RToolsDAO}' varStatus="step">
                            
                            <select name="<c:out value='${status.expression}'/>"  id="<c:out value='${status.expression}'/>" class="ifSelect" disabled>
                                <option <c:if test="${empty Rtool[0]}" >selected </c:if> value=''>Please select</option>
                                <option <c:if test="${Rtool[1]  == 1}" >selected </c:if> value="1">Cre recombinase expressing strain (Cre)</option>
                                <option <c:if test="${Rtool[1]  == 2}" >selected </c:if>value="2">Strain with loxP-flanked sequences (loxP)</option>
                                <option <c:if test="${Rtool[1]  == 3}" >selected </c:if>value="3">FLP recombinase expressing strain (FLP)</option>
                                <option <c:if test="${Rtool[1]  == 4}" >selected </c:if>value="4">Strain with FRT-flanked sequences (FRT)</option>
                                <option <c:if test="${Rtool[1]  == 5}" >selected </c:if>value="5">Strain with Tet expression system (TET)</option>
                                <option <c:if test="${Rtool[1]  == 6}" >selected </c:if>value="6">Lexicon strains from Wellcome Trust (LEX)</option>
                                <option <c:if test="${Rtool[1]  == 7}" >selected </c:if>value="7">Deltagen strains from Wellcome Trust (DEL)</option>
                                <option <c:if test="${Rtool[1]  == 9}" >selected </c:if>value="9">Strains generated from EUCOMM ES cell resource (EUC)</option>
                                <option <c:if test="${Rtool[1]  == 10}" >selected </c:if>value="10">Strains generated from EUCOMMTools Cre resource (EUCre)</option>
                            </select>

                            &nbsp;&nbsp;<input name="update" value="Update" class="button" type="button" onClick="JavaScript:redirect('rtool${step.index}','${Rtool[1]}','${Rtool[0]}');">&nbsp;&nbsp;
                            <input name="delete" value="Delete" class="button" type="button" onClick="parent.location='?rtoolsID=${Rtool[1]}&action=delete'"> 
                            <br/>
                        </c:forEach>

                        <br />
                    </td>
                                               </c:otherwise>
                       </c:choose>
                </tr>
                <tr>
                    <td colspan="2">
                        <p style="text-align: center;" class="left">
                            <input name="Clear updates" value="Cancel update" class="button" type="reset">
                        </p>
                    </td>
                </tr>
            </table>           
        </form:form>
    </body>
</html>
