<%-- 
    Document   : submissionForm4
    Created on : 30-Jan-2012, 14:47:43
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
<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />


<spring:bind path="command.*" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Mutant Mouse Strain Submission Wizard - Step ${stepCurrent} of ${stepTotal}</title>
        <style type="text/css">@import url(../css/default.css);</style>
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <style type="text/css" media="all">@import url("https://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/ebi.css");</style>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        <%--   <script type="text/javascript" src="../js/jquery-ui-1.8.5.custom.min"></script>
     <script type="text/javascript" src="../js/jquery-1.6.1.min"></script>--%>
        <script type="text/javascript" src="../js/modalwindows.js"></script>
        <script type="text/javascript" src="../js/jquery.parsequery.js"></script>
        <script type="text/javascript" src="../js/jquery.parsequery.min.js"></script>
        <script type="text/javascript" src="../js/peopleData.js?<%= new java.util.Date()%>"></script>
        <script type="text/javascript" src="../js/tooltip.js"></script>
                <SCRIPT>
            (function(i,s,o,g,r,a,m){
                i['GoogleAnalyticsObject']=r;
                i[r]=i[r]||function(){
                    (i[r].q=i[r].q||[]).push(arguments)
                },i[r].l=1*new Date();
                a=s.createElement(o),
                m=s.getElementsByTagName(o)[0];
                a.async=1;
                a.src=g;
                m.parentNode.insertBefore(a,m)
            })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
            ga('create', '${GOOGLEANAL}', 'infrafrontier.eu');
            ga('send', 'pageview');
        </SCRIPT>
    </head>
    <body onKeyPress="return disableEnterKey(event)">
        <br/>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
            <form:form method="POST" commandName="command">

            <div id="shipper" class="step">

                <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>
                <div id="wrapper">
                    <div id="container">
                        <div class="region region-content">
                            <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                                <div class="form visible">
                                    <div class="boxcontainer">
                                        <h4> Shipper (Step ${stepCurrent} of ${stepTotal})</h4>
                                        <p>Please enter the contact information of the person in charge of shipping the mice to EMMA (e.g., animal facility manager or lab head). </p>
                                        <div id='mask' class='close_modal'></div>
                                        <div id='user_window' class='modal_window'>
                                            <%-- DIV HOLDS USER DATA RETURNED FROM ajaxReturn.emma?funct=peopleCall&email=xxxxxxx@xxx.xxx --%>
                                        </div><div>
                                            <div>
                                                <input class="btn" type="button" name="fill_shipper_with_submitter_data" 
                                                       id="fill_producer_with_submitter_data" value="Fill with Submitter data" onClick="populateUserDetails('?uid=${command.per_id_per_sub}\
&email=${command.submitter_email}&title=${command.submitter_title}&firstname=${command.submitter_firstname}&surname=${command.submitter_lastname}&phone=${command.submitter_tel}\
&fax=${command.submitter_fax}&institute=${command.submitter_inst}&dept=${command.submitter_dept}\
&addr_line_1=${command.submitter_addr_1}&addr_line_2=${command.submitter_addr_2}\
&town=${command.submitter_city}&county=${command.submitter_county}\
&postcode=${command.submitter_postcode}&country=${command.submitter_country}&fieldset=shipper')"/>
                                                <input id="fill_shipper_with_producer_data" class="btn" type="button" value="Fill with Producer data" name="fill_shipper_with_producer_data" onClick="populateUserDetails('?uid=${command.per_id_per}\
&email=${command.producer_email}&title=${command.producer_title}&firstname=${command.producer_firstname}&surname=${command.producer_lastname}&phone=${command.producer_tel}\
&fax=${command.producer_fax}&institute=${command.producer_inst}&dept=${command.producer_dept}\
&addr_line_1=${command.producer_addr_1}&addr_line_2=${command.producer_addr_2}\
&town=${command.producer_city}&county=${command.producer_county}\
&postcode=${command.producer_postcode}&country=${command.producer_country}&fieldset=shipper')"/>
                                            </div>
                                            <div class="validation_error_message"> &nbsp; </div>
                                        </div>
                                        <spring:bind path="command.shipper_email">
                                            <div class="field">
                                                <label class="label"><strong>Email<sup><font color="red">*</font></sup></strong></label>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                       <%-- <div id="user_details_link" style="display: none"><p><a class='activate_modal' name='user_window' href='javascript:void(0)'><img src="../images/people.png" border="0" width="32" height="32" align="absmiddle"/></a>&nbsp;&nbsp;<a class='activate_modal' name='user_window' href='javascript:void(0)'>Use these user details</a></p></div>--%></div>
                                                                
                                            </div>
                                            <ul id="ulUsers"></ul>
                                        </spring:bind>

                                       <%-- <script type="text/javascript" >              
                                            $('#shipper_email').focusout(function() {
                                                $("#user_details_link").hide();
                                                $('#user_window').load('../ajaxReturn.emma',{email:$('#shipper_email').val(), funct: "peopleCall",fieldset: "shipper"});
                                                $("#user_details_link").show();
                                            });
                                        </script>--%>

                                        <spring:bind path="command.shipper_title">
                                            <div class="field">
                                                <p><strong>Title</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:select path="${status.expression}" id="${status.expression}">
                                                        <form:option value="">Please select : </form:option>
                                                        <form:option value="Mr">Mr</form:option>
                                                        <form:option value="Mrs">Mrs</form:option>
                                                        <form:option value="Ms">Ms</form:option>
                                                        <form:option value="Prof">Prof</form:option>
                                                        <form:option value="Dr">Dr</form:option>
                                                    </form:select>
                                                </div>
                                                
                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.shipper_firstname">
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="field">
                                                <p><strong>First name <sup><font color="red">*</font></sup></strong></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                    </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.shipper_lastname">   
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="field">
                                                <p><strong>Last Name
                                                        <sup><font color="red">*</font></sup></strong></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                    </div>
                                                
                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.shipper_tel">   
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="field">
                                                <p><strong>Phone Number
                                                        <sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>The phone number must begin with + 
                                                                                                              followed by the country code and contain only numbers, hyphens, spaces, or parentheses. 
                                                                                                              An extension must begin with x followed by the extension number, 
                                                                                                              e.g., +1 (234) 567-8900 x123</p>">? Help</span></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}" title=""></form:input>
                                                    </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.shipper_fax">   
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="field">
                                                <p><strong>Fax Number
                                                        <sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>The fax number must begin with + 
                                                                                                              followed by the country code and contain only numbers, hyphens, spaces, or parentheses. 
                                                                                                              An extension must begin with x followed by the extension number, 
                                                                                                              e.g., +1 (234) 567-8900 x123</p>">? Help</span></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}" title=""></form:input></div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.shipper_inst"> 
                                             <form:errors path="${status.expression}" cssClass="error" />
                                             <div class="field">
                                                <p><strong>Institution
                                                        <sup><font color="red">*</font></sup></strong></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                    </div>
                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.shipper_dept"> 
                                             <form:errors path="${status.expression}" cssClass="error" />
                                             <div class="field">
                                                <p><strong>Department</strong></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                    </div>
                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.shipper_addr_1"> 
                                             <form:errors path="${status.expression}" cssClass="error" />
                                             <div class="field">
                                                <p><strong>Address line 1/Street address
                                                        <sup><font color="red">*</font></sup></strong></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                    </div>
                                            </div>
                                        </spring:bind>



                                        <spring:bind path="command.shipper_addr_2">
                                             <form:errors path="${status.expression}" cssClass="error" />
                                             <div class="field">
                                                <p><strong>Address line 2</strong></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                    </div>
                                            </div>
                                        </spring:bind>


                                        <spring:bind path="command.shipper_city">     
                                             <form:errors path="${status.expression}" cssClass="error" />
                                             <div class="field">
                                                <p><strong>City
                                                        <sup><font color="red">*</font></sup></strong></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                    </div>
                                            </div>
                                        </spring:bind>  


                                        <spring:bind path="command.shipper_county">  
                                             <form:errors path="${status.expression}" cssClass="error" />
                                             <div class="field">
                                                <p><strong>County/Province/State</strong></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                    </div>
                                            </div>
                                        </spring:bind>


                                        <spring:bind path="command.shipper_postcode">
                                             <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="field">
                                                <p><strong>Postcode/Zipcode
                                                        <sup><font color="red">*</font></sup></strong></p>
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                    </div>
                                            </div>
                                        </spring:bind> 

                                        <spring:bind path="command.shipper_country"> 
                                             <form:errors path="${status.expression}" cssClass="error" />
                                             <div class="field">
                                                <p><strong>Country
                                                        <sup><font color="red">*</font></sup></strong></p>
                                                <div class="input">
                                                    <%--name="submitter_country" id="submitter_country">--%>
                                                    <form:select path="${status.expression}" id="${status.expression}">
                                                        <form:option value="">Please select : </form:option>
                                                        <form:options  items="${command.cvDAO}"></form:options>
                                                    </form:select>
                                                </div>
                                            </div>
                                        </spring:bind>
                                    </div>

                                    <%--      <div class="validation_error_message"> &nbsp; </div>
                                      </div>--%>

                                    <spring:bind path="command.per_id_per_contact">
                                        <form:hidden path="${status.expression}" id="${status.expression}"></form:hidden>
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
        <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>
