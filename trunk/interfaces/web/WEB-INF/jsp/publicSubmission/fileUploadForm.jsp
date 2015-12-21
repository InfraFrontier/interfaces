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
    Document   : fileUploadForm
    Created on : Sep 13, 2012, 2:27:20 PM
    Author     : phil
--%>
<!DOCTYPE html>

<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"    uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="dt"     uri="http://jakarta.apache.org/taglibs/datetime-1.0" %>
<%@ taglib prefix="req"    uri="http://jakarta.apache.org/taglibs/request-1.0" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:set var="baseurl" value="${param.BASEURL}" scope="session" />

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style type="text/css">@import url(../css/default.css);</style>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        
        <title>Upload supporting file(s)</title>
    </head>

    <body>
        <br/>
        <p>
            <img alt="spacer" src="" height="1" width="145"/>
            <a href="${BASEURL}"><img alt="Infrafrontier logo" src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a>
        </p>
        <br/>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <h4>Submission supporting file upload</h4>
                                <c:if test="${not empty message}">
                                    <c:out value="${message}" />
                                    <c:set var="message" value="" scope="session" />
                                </c:if>

                                <form:form method="POST" commandName="fileUploadForm" enctype="multipart/form-data">
                                    <br/><br/>
                                    <div id="errors">
                                        <form:errors path="*" cssClass="errorblock" />
                                    </div>
                                    <br/>
                                    <input type="hidden" value="${sessionScope.getprev}" name="submissionID" id="submissionID" />
                                    <input type="hidden" value="${param.submissionFileType}" name="submissionFileType" id="submissionFileType">
                                        Please select a PDF file, with a maximum size, not exceeding 2MB to upload : <input type="file" name="file" id="file" />
                                    <input type="submit" id="upload" class="btn big" name="upload" value="upload" />
                                    <span>
                                        <form:errors path="file" cssClass="error" />
                                    </span>
                                </form:form>  
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include flush="true" page="submissionFormFooter_inc.jsp" />
    </body>
</html>