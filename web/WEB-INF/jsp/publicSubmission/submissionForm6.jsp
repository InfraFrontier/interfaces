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
    Document   : submissionForm6
    Created on : 30-Jan-2012, 14:48:36
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
<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Mutant Mouse Strain Submission Wizard - Step ${stepCurrent} of ${stepTotal}</title>
        <style type="text/css">@import url(../css/default.css);</style>
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        <style type="text/css" media="all">@import url("https://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/ebi.css");</style>
        <script type="text/javascript" src="https://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/js/default.js"></script>
          
    </head>
    <body onKeyPress="return disableEnterKey(event)">
        <br/>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
            <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>

        <div id="container">
            <div class="region region-content">
                <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                    <div class="form visible">
                        <div class="boxcontainer">
                            <h4>
                                Phenotype (Step ${stepCurrent} of ${stepTotal})
                            </h4>
                            <p>
                                Please enter the phenotype information of the mouse mutant strain you want to deposit in EMMA.<br/><br/>
                            </p>
                            <form:form method="POST" commandName="command"> 
                                <spring:bind path="command.homozygous_phenotypic_descr">
                                    <form:errors path="${status.expression}" cssClass="error" />
                                    <div class="field">
                                        <p><strong>Phenotypic description of homozygous mice<sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>A short description of the mutant phenotype of homozygous mice (this will be used in the public web listing, see an example).</p>">? Help</span></p>
                                        <div class="input">
                                            <form:textarea id="${status.expression}" path="${status.expression}"  cols="50" rows="5" 
                                                           title=""></form:textarea>
                                            </div>
                                        
                                    </div>
                                </div>
                            </spring:bind>
                            <spring:bind path="command.heterozygous_phenotypic_descr">
                                <form:errors path="${status.expression}" cssClass="error" />
                                <div class="field">
                                    <p><strong>Phenotypic description of heterozygous/hemizygous mice<sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>A short description of the mutant phenotype of heterozygous/hemizygous mice (this will be used in the public web listing, see an example).</p>">? Help</span></p>
                                    <div class="input">
                                        <form:textarea id="${status.expression}" path="${status.expression}"  cols="50" rows="5" 
                                                       title=""></form:textarea>      
                                        </div>
                                    
                                </div>
                            </spring:bind>
                            <p>
                                <%@include file="submissionFormControlButtons_inc.jsp"%>
                            </p>
                        </form:form>
                        <%--       </div>
                           </div>
                                    </div>--%>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>
