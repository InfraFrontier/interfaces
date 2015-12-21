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
    Document   : submissionForm10
    Created on : 30-Jan-2012, 14:50:45
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
<spring:bind path="command.*" />
<c:set var="CVRToolsDAO" value='${sessionScope.CVRToolsDAO}'></c:set>
<c:set var="catsDAO" value='${sessionScope.categoriesDAO}'></c:set>
<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Mutant Mouse Strain Submission Wizard - Step ${stepCurrent} of ${stepTotal}</title>
        <style type="text/css">@import url(../css/default.css);</style>
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <style type="text/css" media="all">@import url("https://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/ebi.css");</style>

        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        <script type="text/javascript" src="../js/tooltip.js"></script>
        
    </head>
    <body onKeyPress="return disableEnterKey(event)">
        <br/>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
        <div id="research_value" class="step">

            <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>
            <div id="wrapper">
                <div id="container">
                    <div class="region region-content">
                        <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                            <div class="form visible">
                                <div class="boxcontainer">
                                    <h4>
                                        Research value (Step ${stepCurrent} of ${stepTotal})
                                    </h4>
                                    <form:form method="POST" commandName="command">

                                        <spring:bind path="command.human_condition">
                                            <div class="field">
                                                <strong>Does this strain model a human condition or disease?<sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>For OMIM IDs please search OMIM a database of human genes and genetic disorders using the link supplied. Insert the numeric ID and divide by semicolon if more than one.</p>">? Help</span></p>
                                                <div class="input">
                                                    <p><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" title="" />Yes (please explain below)<br/>
                                                        <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br/>
                                                        <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</p><br/>
                                                </div>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                            </div>
                                        </spring:bind>
                                            
                                        <div id="humanConditionText" style="display: none">
                                            
                                            <spring:bind path="command.human_condition_more"> 
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="field" id="${status.expression}">
                                                    <p><strong>Please enter the <a href="http://omim.org/search?index=entry&sort=score+desc%2C+prefix_sort+desc&start=1&limit=10&search=" target="_blank">Online Mendelian Inheritance in Man</a> identifiers that apply to the human condition or disease:</strong></p>
                                                        <form:input  id="${status.expression}" path="${status.expression}" />
                                                        <br/>
                                                        </div>
                                                    </spring:bind>
                                               <br />
                                                <spring:bind path="command.human_condition_text">
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                        <p><strong>If OMIM IDs are not available, please describe the human condition or disease below:</strong></p>
                                                        <div class="field" id="${status.expression}">
                                                    <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5"></form:textarea>
                                                        </spring:bind>   
                                                </div>  
                                            </div>
                                            <script>
                                            
                                                if ($('input[name=human_condition]:checked').val() == "yes") {
                                                    $("#humanConditionText").show("slow");
                                                }
                                            
                                                $("#human_condition-yes").click(function () {
                                                    $("#humanConditionText").show("slow");
                                                });
                            
                                                $("#human_condition-no").click(function () {
                                                    $("#humanConditionText").hide("slow");
                                                });
                            
                                                $("#human_condition-not_known").click(function () {
                                                    $("#humanConditionText").hide("slow");
                                                });            
                                            </script>
                                            <br />
                                            <spring:bind path="command.research_areas">
                                                <div class="field">
                                                    <p><strong>Research areas</strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please specify the area(s) of research relating to the strain that is being submitted.</p>">? Help</span><br/>
                                                    <font size="-1">To select multiple options:- For windows: Hold down the control (ctrl) button For Mac: Hold down the command button</font></p>
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                    <c:if test="${empty command.catDAO}"><c:set var="cats" value="${catsDAO}"></c:set></c:if>
                                                    <c:if test="${not empty command.catDAO}"><c:set var="cats" value="${command.catDAO}"></c:set></c:if>
                                                            <div class="input">
                                                        <form:select path="${status.expression}" id="${status.expression}"  title="" multiple="true">
                                                            <form:option value='0'>Please select...</form:option>
                                                            <c:forEach var="researchArea" items="${cats}">
                                                                <form:option value='${researchArea[0]}'>${researchArea[1]}</form:option>
                                                            </c:forEach>               
                                                        </form:select>
                                                    </div>

                                                </div>
                                            </spring:bind>
                                            <br />
                                            <div class="field">
                                                <p><strong>Other research areas</strong></p>

                                                <spring:bind path="command.research_areas_other_text">
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                    <form:input  id="${status.expression}" path="${status.expression}" />
                                                </spring:bind>
                                            </div>
                                            <br />
                                            <spring:bind path="command.research_tools">
                                                <div class="field"><p><strong>Research tools</strong><br/>
                                                    <font size="-1">To select multiple options:- For windows: Hold down the control (ctrl) button For Mac: Hold down the command button</font></p>
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                    <div class="input">
                                                        <form:select path="${status.expression}" id="${status.expression}" multiple="true">
                                                            <form:option value="">Please select..</form:option>
                                                            <c:forEach var="obj" items="${CVRToolsDAO}">
                                                                <c:set var="rtool" value="${obj}"/>
                                                                <c:choose><c:when test="${rtool['code'] == 'LEX' || rtool['code'] == 'DEL'  || rtool['code'] == 'EUC' }"></c:when><c:otherwise><form:option value="${rtool['id']}" >${rtool['description']} (${rtool['code']})</form:option></c:otherwise></c:choose>
                                                            </c:forEach>
                                                        </form:select>
                                                    </div>

                                                </div>
                                            </spring:bind>
                                            <p>
                                                <%@include file="submissionFormControlButtons_inc.jsp"%>
                                            </p>
                                        </form:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>
