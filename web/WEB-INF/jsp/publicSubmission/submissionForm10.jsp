<%-- 
    Document   : submissionForm10
    Created on : 30-Jan-2012, 14:50:45
    Author     : phil
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:bind path="command.*" />
<c:set var="CVRToolsDAO" value='${sessionScope.CVRToolsDAO}'></c:set>
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
        <div id="research_value" class="step">
            <h2>
                Research value (Step ${stepCurrent} of ${stepTotal})
            </h2>
            <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>
       <div id="wrapper">
            <div id="container">
        <div class="region region-content">
         <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
        <div class="form visible">
            <div class="boxcontainer">
            <form:form method="POST" commandName="command">

                <spring:bind path="command.human_condition">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Does this strain model a human condition or disease?<sup><font color="red">*</font></sup></strong></label>
                        <div class="input">
                            <label><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" title="For OMIM IDs please &lt;a href='http://www.ncbi.nlm.nih.gov/omim' target='_blank'&gt;search OMIM&lt;/a&gt;, a database of human genes and genetic disorders. Insert the numeric ID and divide by semicolon if more than one." />Yes (please explain below)</label><br />
                            <label><form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No</label><br />
                            <label><form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</label><br />
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>


                <spring:bind path="command.human_condition_more">        
                    <div class="field" id="${status.expression}">
                        <label for="${status.expression}">Please enter the <a href="http://omim.org/search?index=entry&sort=score+desc%2C+prefix_sort+desc&start=1&limit=10&search=" target="_blank">Online Mendelian Inheritance in Man</a> identifiers that apply to the human condition or disease:</label>
                        <div class="input">
                            <form:input  id="${status.expression}" path="${status.expression}" /><form:errors path="${status.expression}" cssClass="error" />
                            <br/>
                        </spring:bind>

                        <spring:bind path="command.human_condition_text">
                            <div>
                                <label for="${status.expression}">If OMIM IDs are not available, please describe the human condition or disease below:</label>
                            </div>
                            <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5"></form:textarea>
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.research_areas">
                    <div class="field">
                        <label class="label"><strong>Research areas</strong></label>
                        <div class="input">
                            
                            
                            <form:select path="${status.expression}" id="${status.expression}"  title="Please specify the area of research relating to the strain that is being submitted.">

                                <form:option value='0'>Please select...</form:option>
                                <c:forEach var="researchArea" items="${command.catDAO}">

                                    <form:option value='${researchArea[0]}'>${researchArea[1]}</form:option>
                                </c:forEach>               
                            </form:select>
                          <%--  <form:select path="${status.expression}" id="${status.expression}">
                                <form:option value="">Please select..</form:option>
                                <form:option value="apoptosis" >Apoptosis</form:option>
                                <form:option value="cancer" >Cancer</form:option>
                                <form:option value="cardiovascular" >Cardiovascular</form:option>
                                <form:option value="cell_biology" >Cell biology</form:option>
                                <form:option value="dermatology" >Dermatology</form:option>
                                <form:option value="developmental_biology" >Developmental biology</form:option>
                                <form:option value="diabetes_obesity" >Diabetes/Obesity</form:option>
                                <form:option value="endocrinology" >Endocrinology</form:option>
                                <form:option value="hematology" >Hematology</form:option>
                                <form:option value="immunology_inflammation" >Immunology and Inflammation</form:option>
                                <form:option value="internal_organ" >Internal/Organ</form:option>
                                <form:option value="metabolism" >Metabolism</form:option>
                                <form:option value="neurobiology" >Neurobiology</form:option>
                                <form:option value="reproduction" >Reproduction</form:option>
                                <form:option value="sensorineural" >Sensorineural</form:option>
                                <form:option value="virology" >Virology</form:option>
                                <form:option value="Other" >Other (please specify)</form:option>
                            </form:select>--%>
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                  </spring:bind>
                
                <div class="field">
                    <label class="label"><strong>Other research areas</strong></label>
                    <spring:bind path="command.research_areas_other_text">
                        <form:input  id="${status.expression}" path="${status.expression}" /><form:errors path="${status.expression}" cssClass="error" />
                    </spring:bind>
                </div>
                
                    <spring:bind path="command.research_tools">
                    <div class="field"><label class="label"><strong>Research tools</strong></label>
                    <div class="input">
                        <form:select path="${status.expression}" id="${status.expression}" multiple="true">
                            <form:option value="">Please select..</form:option>
                        <c:forEach var="obj" items="${CVRToolsDAO}">
                            <c:set var="rtool" value="${obj}"/>
                            <form:option value="${rtool['id']}" >${rtool['description']} (${rtool['code']})</form:option>
                        </c:forEach>
                        </form:select>
                    </div>
 <form:errors path="${status.expression}" cssClass="error" />
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

    </body>
</html>
