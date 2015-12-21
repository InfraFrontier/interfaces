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
    Document   : submissionForm7
    Created on : 30-Jan-2012, 14:49:17
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
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <style type="text/css" media="all">@import url("https://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/ebi.css");</style>

        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        <script type="text/javascript" src="../js/tooltip.js"></script>
        <%--  <script type="text/javascript" src="../js/jquery-ui-1.8.5.custom.min"></script>
     <script type="text/javascript" src="../js/jquery-1.6.1.min"></script>--%>
        <script type="text/javascript" src="../js/jquery.parsequery.js"></script>
        <script type="text/javascript" src="../js/jquery.parsequery.min.js"></script>
        <script type="text/javascript" src="../js/biblioData.js?<%= new java.util.Date()%>"></script>
        <style type="text/css">@import url(../css/default.css);</style>
        
    </head>
    <body onKeyPress="return disableEnterKey(event)">
        <script type="text/javascript">
            $(document).ready(function() {
                initTooltips();	
            });
        </script>
        <br/>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
                <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>
        <div id="container">
            <div class="region region-content">
                <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                    <div class="form visible">
                        <div class="boxcontainer">
                            <h4>
                                References (Step ${stepCurrent} of ${stepTotal})
                            </h4>
                            <p>
                                If the mouse mutant strain you want to deposit in EMMA has been published, please enter the bibliographic information of one or more related publications. For the PubMed ID please <a target='PUBMED' href='http://www.pubmed.gov'>search PubMed</a>, a bibliographic database of biomedical articles.
                                <br/><br/></p>
                                <form:form method="POST" commandName="command"> 
                                <input type="hidden" name="encID" id="encID" value="${param.getprev}"/>
                                <spring:bind path="command.published">
                                    <div class="field">
                                        <p><strong>Has this mouse mutant strain been published or accepted for publication?<sup><font color="red">*</font></sup></strong><br/><br/></p>
                                        <div class="input">
                                            <form:radiobutton path="${status.expression}" value="yes" id="published-yes" />Yes (please enter bibliographic information below)<br />
                                            <form:radiobutton path="${status.expression}" value="yes" id="published-acc" />Accepted (please enter bibliographic information below)<br />
                                            <form:radiobutton path="${status.expression}" value="no" id="published-no" />No<br />
                                            <form:radiobutton path="${status.expression}" value="not known" id="published-not_known" />Not known<br />
                                        </div>
                                        <form:errors path="${status.expression}" cssClass="error" />

                                    </div>
                                </spring:bind>


                                <spring:bind path="command.notes">
                                    <fieldset class="reference" style="display: none" id="reference" >
                                        <legend>Reference</legend>
                                        <div class="field reference_descr">
                                            <p><strong>Short description<sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please select or enter a short description for this reference.</p>">? Help</span></p>
                                            <div class="input">
                                                <form:select path="${status.expression}" id="${status.expression}" title="">
                                                    <form:option value="">Please select..</form:option>
                                                    <form:option value="Generation of the mutant mouse strain">Generation of the mutant mouse strain</form:option>
                                                    <form:option value="Description of the mutant phenotype"> Description of the mutant phenotype</form:option> 
                                                    <form:option value="Cloning/characterization of the affected gene">Cloning/characterization of the affected gene</form:option>
                                                    <form:option  value="Other">Other (please specify)</form:option>
                                                </form:select>
                                            </spring:bind>
                                        </div>
                                        <spring:bind path="command.notesadditional">
                                            <div id="additionalNotes"  style="display: none">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>

                                                <form:errors path="${status.expression}" cssClass="error" />
                                            </div> 
                                        </div>
                                    </spring:bind>
                                    <script>
                                        $("#notes").click(function () {
                                            var optionValue = $("select#notes").val();
                                            if(optionValue == 'Other'){
                                                $("#additionalNotes").show("slow");
                                            } else {
                                                $("#additionalNotes").hide("fast");
                                            }
                                        });        
                                    </script>
                                    <spring:bind path="command.pubmed_id">
                                        <div class="field reference_pmid">
                                            <p><strong>PubMed ID (if available, if not just complete the fields below.)</strong></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input><br/>(Fields auto populated from PubMed using PubMed ID digits only. Leave PubMed ID field to initiate)
                                                </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>
                                    </spring:bind>
                                    <div id="bibRef" name="bibRef"></div>
                                    <script type="text/javascript" > 
                                        $('#pubmed_id').focusout(function() {
                                            $('#bibRef').load('../ajaxReturn.emma',{pubmedid:$('#pubmed_id').val(), funct: "pubMed",query: "biblios",notes:$('#notes').val(), notesadditional:$('#notesadditional').val()});
                                        });
                                    </script>
                                    <spring:bind path="command.title">
                                        <div class="field reference_title">
                                            <label class="label" for="${status.expression}"><strong>Title</strong></label>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.author1">                   
                                        <div class="${status.expression}">
                                            <label class="label" for="author1"><strong>Authors</strong></label>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.journal">   
                                        <div class="${status.expression}">
                                            <label class="label" for="journal"><strong>Journal/Book<sup><font color="red">*</font></sup></strong></label>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>

                                                </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>

                                    </spring:bind>

                                    <spring:bind path="command.year"> 
                                        <div class="field reference_year">
                                            <label class="label" for="${status.expression}"><strong>Year<sup><font color="red">*</font></sup></strong></label>
                                            <div class="input">

                                                <form:input  id="${status.expression}" path="${status.expression}" maxlength="4"></form:input>
                                                </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.volume">    
                                        <div class="field reference_volume">
                                            <label class="label" for="${status.expression}"><strong>Volume</strong></label>

                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>

                                    </spring:bind> 

                                    <spring:bind path="command.pages"> 
                                        <div class="field reference_pages">
                                            <label class="label" for="${status.expression}"><strong>Pages<sup><font color="red">*</font></sup></strong></label>

                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>
                                    </spring:bind>
                                    <p>&nbsp;</p>
                                    <%--<spring:bind path="command.removeReference">--%>    
                                    <div>
                                        <input value="Clear input fields" type="button" class="btn" id="removeReference" onClick="javascript:removeBibDetails();" />
                                    </div>
                                    <p>&nbsp;</p>
                                    <input type="hidden" name="id_biblio" id="id_biblio"  value=""/>
                                </fieldset>
                                <%--    <p>
                                        <spring:bind path="command.addReference"> 
                                            <form:input id="${status.expression}" path="${status.expression}"></form:input>
                                        </spring:bind>
                                    </p>
                                --%>
                                <script>
                                    if ($('input[name=published]:checked').val() == "yes") {
                                        $("#reference").show("slow");
                                        
                                    }
                                    $("#published-yes").click(function () {
                                        removeBibDetails();
                                        $("#reference").show("slow");
                                    });
                                    $("#published-acc").click(function () {
                                        removeBibDetails();
                                        $("#reference").show("slow");
                                    });
                                    $("#published-no").click(function () {
                                        $("#reference").hide("slow");
                                    });
                            
                                    $("#published-not_known").click(function () {
                                        $("#reference").hide("slow");
                                    });            
                                </script>
                                <c:choose><c:when test="${empty param.getprev}"><c:set var="action" value="none"/></c:when><c:otherwise><c:set var="action" value="get"/></c:otherwise></c:choose>
                                        <div id="subBiblios" name="subBiblios">
                                            <script type="text/javascript" > 
                                                $('#subBiblios').load('ajaxBiblios.emma',{
                                                    action: "${action}",
                                                    query: "edit",
                                                    Id_sub:$('#encID').val()
                                                });
                                    </script>
                                </div>

                                <p>
                                    <%@include file="submissionFormControlButtons_inc.jsp"%>
                                </p> 
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>   
    </body>
</html>
