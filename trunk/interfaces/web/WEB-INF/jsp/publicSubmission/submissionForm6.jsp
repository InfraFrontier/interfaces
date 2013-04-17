<%-- 
    Document   : submissionForm6
    Created on : 30-Jan-2012, 14:48:36
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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Mutant Mouse Strain Submission Wizard - Step ${stepCurrent} of ${stepTotal}</title>
        <style type="text/css">@import url(../css/default.css);</style>
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
    </head>
    <body onKeyPress="return disableEnterKey(event)">
       <%-- <div id="phenotype" class="step">--%>
            <h2>
                Phenotype (Step ${stepCurrent} of ${stepTotal})
            </h2>
<jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>

            <div id="container">
        <div class="region region-content">
         <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
        <div class="form visible">
            <div class="boxcontainer">
            <p>
                Please enter the phenotype information of the mouse mutant strain you want to deposit in EMMA.
            </p>
            <form:form method="POST" commandName="command"> 
                <spring:bind path="command.homozygous_phenotypic_descr">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Phenotypic description of homozygous mice<sup><font color="red">*</font></sup></strong></label>
                        <div class="input">
                            <form:textarea id="${status.expression}" path="${status.expression}"  cols="50" rows="5" 
                                           title="A short description of the mutant phenotype of homozygous mice (this will be used in the public web listing, see an example)."></form:textarea>
                            </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </div>
            </spring:bind>
            <spring:bind path="command.heterozygous_phenotypic_descr">
                <div class="field">
                    <label class="label" for="heterozygous_phenotypic_descr"><strong>Phenotypic description of heterozygous/hemizygous mice<sup><font color="red">*</font></sup></strong></label>
                    <div class="input">
                        <form:textarea id="${status.expression}" path="${status.expression}"  cols="50" rows="5" 
                                       title="A short description of the mutant phenotype of heterozygous/hemizygous mice (this will be used in the public web listing, see an example)."></form:textarea>      
                        </div>
                    <form:errors path="${status.expression}" cssClass="error" />
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
</body>
</html>
