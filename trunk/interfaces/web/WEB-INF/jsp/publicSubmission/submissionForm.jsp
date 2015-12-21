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
    Document   : submissionForm0
    Created on : 30-Jan-2012, 14:44:12
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
        <title>EMMA Mutant Mouse Strain Submission Wizard</title>
        <style type="text/css">@import url(../css/default.css);</style>

    </head>
    <body>
        <br/>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
        <p>&nbsp;</p>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <h4>EMMA Mutant Mouse Strain Submission Wizard</h4>
                                <form:form method="POST" >


                                    <p>
                                        Dear Submitting Investigator,
                                    </p>
                                    <p>
                                        Please use this wizard to provide detailed information about the mutant mouse strain that you want to 
                                        submit to EMMA. This information is used to evaluate your submission request. Please note that if the 
                                        request is approved the information you provide will be published on the EMMA website.
                                    </p>
                                    <p>
                                        The online form is a step-through form and you will be able to download a printable version before form submission. Please fill the form as accurately as you can using "unknown", "not applicable" or "none" if you do not 
                                        possess the information requested. A sample submission is provided <a href="../downloads/EMMA-example-submission.pdf" target="_blank">here</a> for your 
                                        convenience to examine all the information that is required.
                                    </p>
                                    <p>
                                        <b>Multiple strains must be submitted separately. One strain per submission.</b>
                                    </p>
                                    <p>
                                        If the information provided through the submission form is clear enough, EMMA will forward it to the external 
                                        EMMA Evaluation Committee and inform depositors about the outcome of the evaluation by e-mail within 60 days.
                                    </p>
                                    <p>
                                        We strongly recommend that you carefully read the <a target="emma" href="${BASEURL}procedures">
                                            EMMA Procedures</a> on this website. These documents describe the responsibilities EMMA has in 
                                        maintaining and distributing the submitted strains as well as the responsibilities assumed by the 
                                        submitter.
                                    </p>

                                    <p>
                                        Thank you for your interest in the European Mouse Mutant Archive.
                                    </p>
                                    <p>&nbsp;</p>
                                    <center>
                                        <p>
                                            <spring:bind path="command.termsAgreed">
                                                <form:checkbox onclick="" id="${status.expression}" path="${status.expression}" value="true"/>&nbsp;I have read the information above and agree to the <a href="${BASEURL}procedures/legal-issues/emma-repository-conditions-and-mtas">EMMA Terms &amp; Conditions</a><form:errors path="${status.expression}" cssClass="error" />
                                            </spring:bind>
                                        </p>
                                    </center>
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
        <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>
