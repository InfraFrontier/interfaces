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
        <style type="text/css">@import url(../css/emmastyle.css);</style>
        <script type="text/javascript" src="../js/submission.js"></script>
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
    </head>
    <body onKeyPress="return disableEnterKey(event)">

        <h1>Submitter (Step ${stepCurrent} of ${stepTotal})</h1>
        <%@include file="submissionFormHeader_inc.jsp"%>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <form:form method="POST" commandName="command">


                                    <div id="start" class="step">
                                        <h2> Start </h2>
                                        <p> Please enter your email address for identification. For your convenience, if you have started or 
                                            completed a mutant mouse strain submission previously in the last 12 months, you will be prompted to 
                                            either resume your incomplete submission from where you left off, or, if the submission was completed, 
                                            you will be asked whether you want to reuse your contact information. </p>
                                            <%--  <spring:bind path="command.peopleDAO.email">--%>
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
        <footer id="footer">
            <div class="innerfooter">        
                <div id="toplink"><a href="#top">to top</a></div>        	
                <div id="fn">
                </div>            
                <div id="tn">   
                </div>        
            </div>
            <div id="footerline">        	
                <div class="innerfooter">
                    <div class="splithalf">
                        <div class="half">
                            © Infrafrontier 2013 - all rights reserved
                        </div>
                        <div class="half">
                            <div id="bn">
                                <div class="region region-usernavi">
                                    <div class="region region-usernavi">
                                        <div id="block-menu-block-4" class="block block-menu-block">
                                            <div class="menu-block-wrapper menu-block-4 menu-name-user-menu parent-mlid-0 menu-level-1">
                                                <ul class="menu"><li class="first leaf menu-mlid-433"><a href="/infrafrontier-research-infrastructure/public-relations/contacts" title="">Contact</a></li>
                                                    <li class="leaf menu-mlid-506"><a href="/imprint" title="">Imprint</a></li>
                                                    <li class="leaf menu-mlid-2"><a href="/user">My account</a></li>
                                                    <li class="last leaf menu-mlid-15"><a href="/user/logout">Log out</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>            
            </div>
        </footer>
    </body>
</html>
