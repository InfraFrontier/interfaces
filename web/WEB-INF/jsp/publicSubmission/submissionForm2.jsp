<%-- 
    Document   : submissionForm2
    Created on : 30-Jan-2012, 14:45:19
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
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<spring:bind path="command.*" />
<c:set var="PeopleDAO" value='${requestScope.userdaos}'></c:set>
<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />


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
        <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>

        <%-- <script type="text/javascript" src="../js/jquery-ui-1.8.5.custom.min"></script>
    <script type="text/javascript" src="../js/jquery-1.6.1.min"></script>--%>
        <script type="text/javascript" src="../js/modalwindows.js"></script>
        <script type="text/javascript" src="../js/jquery.parsequery.js"></script>
        <script type="text/javascript" src="../js/jquery.parsequery.min.js"></script>
        <script type="text/javascript" charset="UTF-8" src="../js/peopleData.js?<%= new java.util.Date()%>"></script>
        <script type="text/javascript" src="../js/jquery.floatobject-1.0.js"></script>
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
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"></a></p>

        <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>
        ${requestScope.recall_window}
        <div id="dialog-confirm" title="Recall Previous Submission?">
            <c:if test="${not empty requestScope.previousSub}">
                <c:if test="${requestScope.recall_window != 'Yes'}">
                    <c:set var="submissionDAO"  value="${requestScope.previousSub}"/>
                    <c:set var="step"  value="${submissionDAO.step}"/>

                    <script>
                        $(function() {   
                            $( "#dialog-confirm" ).dialog({
                                resizable: false,
                                height:280,
                                modal: true       
                            });
                        });
                    </script>
                   
                    <form method="POST" id="loadprev" action="interstitialSubmit.emma">
                        <input type="hidden" name="getprev" id="getprev" value="${submissionDAO.encryptedId_sub}" class="text ui-widget-content ui-corner-all" />
                        <input type="hidden" name="_target${submissionDAO.step}" id="_target${submissionDAO.step}" value="Next" class="text ui-widget-content ui-corner-all" />

                        <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Would you like to continue the submission<c:if test="${not empty submissionDAO.strain_name}"> for strain named ${submissionDAO.strain_name}</c:if> that you started on <c:out value="${submissionDAO.timestamp}"/> but never completed?</p>
                                <p>
                                By selecting 'Yes' you will have to re-accept the Terms and Conditions before being taken to your previous submission.</p><br/><br/> 
                            <p><center><button id="No" type="button" name="No" value="No">No</button>&nbsp;&nbsp;
                                <input type="button" name="recall_window" value="Yes" onclick="javascript:window.location.assign('submissionForm.emma?getprev=${submissionDAO.encryptedId_sub}&recall_window=Yes');">
                        </center></p>
                    </form>
                    <script>
                        $( "#No" )
                        .click(function() {
                            //redirect
                            $( "#dialog-confirm" ).dialog( "close" );
                        });
                    </script>
                </div>
            </c:if>
        </c:if>

        <div id="wrapper">

            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <h4>
                                    Submitter (Step ${stepCurrent} of ${stepTotal})
                                </h4>
                                <p>
                                    Please enter your contact information.
                                </p>
                              <%--  <c:if test="${not empty PeopleDAO}">
                                    <c:forEach var="obj" items="${PeopleDAO}" end="1">
                                        <c:set var="user" value="${obj}"/>
                                        <c:set var="nameForPeopleImage" value="${user['firstname']}&nbsp${user['surname']}"/>
                                    </c:forEach>
                                    <div id="loadUserDetails" name="loadUserDetails">
                                        <script type="text/javascript">show_modal('user_window');</script>
                                        <a class='activate_modal' name='user_window' href='javascript:void(0)'><img src="../images/people.png" border="0" width="32" height="32" align="absmiddle"/></a><br/><a class='activate_modal' name='user_window' href='javascript:void(0)'>Use these details<c:if test="${not empty nameForPeopleImage}"><br/>for ${nameForPeopleImage}</c:if></a>
                                        </div>
                                        <script type="text/javascript">$("#loadUserDetails").makeFloat({x:2,y:"current",speed:"normal"});</script>  
                                </c:if>
                              --%>
                                <div id='mask' class='close_modal'></div>
                                <div id='user_window' class='modal_window'>
                                    <c:if test="${not empty PeopleDAO}">
                                        The following user data is held associated with the e-mail address  ${command.submitter_email}<%--${command.peopleDAO.email}--%> :<br/>
                                        <c:forEach var="obj" items="${PeopleDAO}">
                                            <p class="userdetailspanel">
                                                <c:set var="user" value="${obj}"/>
                                                <c:set var="lab" value="${obj.labsDAO}"/>
                                                Name: ${user['title']}&nbsp;${user['firstname']}&nbsp;
                                                ${user['surname']}<br/>
                                                Phone: ${user['phone']}<br/>
                                                <c:if test="${not empty user['fax']}">Fax: ${user['fax']}<br/></c:if>
                                                <c:if test="${not empty user.labsDAO['name']}">Institute: ${user.labsDAO['name']}<br/></c:if>
                                                <c:if test="${not empty user.labsDAO['dept']}">Department: ${user.labsDAO['dept']}<br/></c:if>
                                                <c:if test="${not empty user.labsDAO['addr_line_1']}">Address Line 1: ${user.labsDAO['addr_line_1']}<br/></c:if>
                                                <c:if test="${not empty user.labsDAO['addr_line_2']}">Address Line 2: ${user.labsDAO['addr_line_2']}<br/></c:if>
                                                <c:if test="${not empty user.labsDAO['town']}">Town: ${user.labsDAO['town']}<br/></c:if>
                                                <c:if test="${not empty user.labsDAO['province']}">County: ${user.labsDAO['province']}<br/></c:if>
                                                <c:if test="${not empty user.labsDAO['postcode']}">Postcode: ${user.labsDAO['postcode']}<br/></c:if>
                                                <c:if test="${not empty user.labsDAO['country']}">Country: ${user.labsDAO['country']}<br/></c:if>
                                                    <br/>Would you like to use the details above ?&nbsp;
                                                    <input type="button" id="modal" name="yes" onClick="populateUserDetails('?uid=${user['id_per']}\
&email=${user['email']}&title=${user['title']}&firstname=${user['firstname']}&surname=${user['surname']}&phone=${user['phone']}\
&fax=${user['fax']}&institute=${user.labsDAO['name']}&dept=${user.labsDAO['dept']}\
&addr_line_1=${user.labsDAO['addr_line_1']}&addr_line_2=${user.labsDAO['addr_line_2']}\
&town=${user.labsDAO['town']}&county=${user.labsDAO['province']}\
&postcode=${user.labsDAO['postcode']}&country=${user.labsDAO['country']}&ilar=${user.ilarDAO['labcode']}&fieldset=submitter&auth=${user.labsDAO['authority']}')" 
                                                           value="Yes"/><br/>
                                            </p>
                                        </c:forEach>
                                        Or click anywhere else to return to the form.
                                    </c:if>
                                </div>
                                <form:form method="POST" commandName="command">
                                    <%-- <spring:bind path="command.peopleDAO.email">--%>
                                    <spring:bind path="command.submitter_email">
                                        <div class="field">
                                            <p><strong>Email<sup><font color="red">*</font></sup></strong></p>
                                            <div class="input">
                                                <span name="${status.expression}" id="${status.expression}">${status.value}</span>
                                            </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>
                                    </spring:bind>
                                    <%--<spring:bind path="command.peopleDAO.title">--%>
                                    <spring:bind path="command.submitter_title">
                                        <div class="field">
                                            <p><strong>Title</strong></p>
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

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_firstname">
                                        <div class="field">
                                            <p><strong>First name <sup><font color="red">*</font></sup></strong></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_lastname">   
                                        <div class="field">
                                            <p><strong>Last Name
                                                    <sup><font color="red">*</font></sup></strong></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_tel">   
                                        <div class="field">
                                            <p><strong>Phone Number
                                                    <sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>The phone number must begin with + 
                                                                                                          followed by the country code and contain only numbers, hyphens, spaces, or parentheses. 
                                                                                                          An extension must begin with x followed by the extension number, 
                                                                                                          e.g., +1 (234) 567-8900 x123</p>">? Help</span></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}" title=""></form:input>
                                                </div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_fax">   
                                        <div class="field">
                                            <p><strong>Fax Number
                                                    <sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>The fax number must begin with + 
                                                                                                          followed by the country code and contain only numbers, hyphens, spaces, or parentheses. 
                                                                                                          An extension must begin with x followed by the extension number, 
                                                                                                          e.g., +1 (234) 567-8900 x123</p>">? Help</span></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}" title=""></form:input></div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_inst"> 
                                        <div class="field">
                                            <p><strong>Institution
                                                    <sup><font color="red">*</font></sup></strong></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_dept"> 
                                        <div class="field">
                                            <p><strong>Department</strong></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_addr_1"> 
                                        <div class="field">
                                            <p><strong>Address line 1/Street address
                                                    <sup><font color="red">*</font></sup></strong></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_addr_2">
                                        <div class="field">
                                            <p><strong>Address line 2</strong></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_city">     
                                        <div class="field">
                                            <p><strong>City
                                                    <sup><font color="red">*</font></sup></strong></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>   

                                    <spring:bind path="command.submitter_county">  
                                        <div class="field">
                                            <p><strong>County/Province/State</strong></p
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.submitter_postcode"> 
                                        <div class="field">
                                            <p><strong>Postcode/Zipcode
                                                    <sup><font color="red">*</font></sup></strong></p>
                                            <div class="input">
                                                <form:input  id="${status.expression}" path="${status.expression}"></form:input>
                                                </div>

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind> 

                                    <spring:bind path="command.submitter_country"> 
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

                                            <form:errors path="${status.expression}" cssClass="error" />

                                        </div>
                                    </spring:bind>


                                    <%-- Hidden input field for submitter id to populate per_id_per --%>
                                    <spring:bind path="command.per_id_per_sub">
                                        <form:hidden path="${status.expression}" id="${status.expression}"></form:hidden>
                                    </spring:bind>
                                    <input type="hidden" name="submitter_authority" id="submitter_authority"/>
                                    <p>
                                        <%@include file="submissionFormControlButtons_inc.jsp"%>
                                    </p>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>

            <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>
