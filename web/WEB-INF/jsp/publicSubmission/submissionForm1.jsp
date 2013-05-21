<%-- 
    Document   : submissionForm1
    Created on : 30-Jan-2012, 14:44:44
    Author     : phil
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:bind path="command.*" />
<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />
<% java.util.Enumeration EditStrain = session.getAttributeNames();

    java.util.Enumeration enumeration = session.getAttributeNames();
    while (enumeration.hasMoreElements()) {
        Object element = enumeration.nextElement();

        System.out.println(element.toString());
    }

%>

<!DOCTYPE html>
<html >
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Mutant Mouse Strain Submission Wizard - Step ${stepCurrent} of ${stepTotal}</title>
        <style type="text/css">@import url(../css/default.css);</style>
        <script type="text/javascript" src="../js/submission.js"></script>
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        <script>
            // window.jQuery || document.write('<script src="../js/jquery-1.7.2.min.js"><\/script><script src="/js/vendor/jquery-ui-1.7.2.min.js"><\/script>');
        </script>
        <%--<script type="text/javascript" src="../js/jquery-ui-1.8.5.custom.min"></script>
        <script type="text/javascript" src="../js/jquery-1.6.1.min"></script>--%>
    </head>
    <body onKeyPress="return disableEnterKey(event)">
        <br/>
        <p><img src="" height="1" width="145"/><img src="../images/infrafrontier/logo-infrafrontier.png"/></p>
            <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <h4>Submitter (Step ${stepCurrent} of ${stepTotal})</h4>
                                <form:form method="POST" commandName="command">
                                    <div id="start" class="step">
                                        <h4> Start </h4>
                                        <p>Please enter your email address for identification. For your convenience, if you have started or 
                                            completed a mutant mouse strain submission previously in the last 12 months, you will be prompted to 
                                            either resume your incomplete submission from where you left off, or, if the submission was completed, 
                                            you will be asked whether you want to reuse your contact information. 
                                        </p>
                                        <spring:bind path="command.submitter_email">
                                            <div class="field">
                                                <label class="label" for="${status.expression}">
                                                    <strong>
                                                        Email
                                                        <sup>
                                                            <font color="red">*</font>
                                                        </sup>
                                                    </strong>
                                                </label>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input><form:errors path="submitter_email" cssClass="error" />
                                                    </div>
                                                </div>
                                        </spring:bind>
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
        </div>
        <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>
