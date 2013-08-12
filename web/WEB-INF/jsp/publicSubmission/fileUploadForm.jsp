<%-- 
    Document   : fileUploadForm
    Created on : Sep 13, 2012, 2:27:20 PM
    Author     : phil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style type="text/css">@import url(../css/default.css);</style>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>


    <body>
        <br/>
        <p><img src="" height="1" width="145"/><img src="../images/infrafrontier/logo-infrafrontier.png"/></p>
        <br/>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <h4>Submission supporting file upload</h4>
                                <c:if test="${not empty message}">
                                    <font color="green"><c:out value="${message}" /></font>
                                    <c:set var="message" value="" scope="session" />
                                </c:if>

                                <form:form method="POST" commandName="fileUploadForm" enctype="multipart/form-data">
                                    <br/><br/>
                                    <div id="errors" name="errors" align="center"><form:errors path="*" cssClass="errorblock" /></div>
                                    <br/>
                                    <input type="hidden" value="${sessionScope.getprev}" name="submissionID" id="submissionID">
                                    <input type="hidden" value="${param.submissionFileType}" name="submissionFileType" id="submissionFileType">
                                    Please select a PDF file, with a maximum size, not exceeding 2MB to upload : <input type="file"  name="file" id="file"></input>
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
                                    <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>