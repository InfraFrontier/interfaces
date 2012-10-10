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
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <style type="text/css">@import url(../css/emmastyle.css);</style>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
    </head>

    <body>
        <h2>Submission supporting file upload</h2>
    <center>
        
        <c:if test="${not empty message}">
            <font color="green"><c:out value="${message}" /></font>
            <c:set var="message" value="" scope="session" />
        </c:if>
    </center>
        <form:form method="POST" commandName="fileUploadForm" enctype="multipart/form-data">
        <br/><br/>
        <div id="errors" name="errors" align="center"><form:errors path="*" cssClass="errorblock" /></div>
        <br/>
        <br/>
    <input type="hidden" value="${sessionScope.getprev}" name="submissionID" id="submissionID">
    <input type="hidden" value="${param.submissionFileType}" name="submissionFileType" id="submissionFileType">
    Please select a file to upload : <input type="file" name="file" id="file"></input>
    <input type="submit" id="upload" name="upload" value="upload" />
    <span>
        <form:errors path="file" cssClass="error" />
    </span>
</form:form>       
</body>
</html>