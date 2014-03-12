<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:if test="${requestScope.ERROR == 'TRUE'}"><c:redirect url="invalidurlerror.emma"/></c:if>
<c:if test="${requestScope.UNENCRYPTEDID == 'TRUE'}"><c:redirect url="secure.emma"/></c:if>

<spring:bind path="command.*" />
<c:set var="eligibleCountryValue" value="${command.eligible_country}"/>
<c:set var="availabilities" value="${command.availabilities}"/>
<c:set var="distCentre" value="${command.distributionCentre}"/>
<%
    String newReq = request.getParameter("new");
    response.setHeader("Cache-Control", "max-age=0");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0); //prevents caching at the proxy server  
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>

        <title>EMMA interest registration and strain requests</title>
        <meta content="text/html; charset=UTF-8" http-equiv="content-type">

        <meta content="Phil Wilkinson" name="author">
        <meta content="Request form for registering an interest in strains that have recently arrived to the EMMA Centres and for requesting publicly available strains." name="description">
        <%--<link rel="stylesheet" type="text/css" href="css/emmastyle.css">--%>
        <style type="text/css" media="all">@import url("../css/default.css?mptxkh");</style>
        <script src="../js/ajax.js" type="text/javascript"></script>
        <script type="text/javascript" src="../js/synonymDiv.js"></script>
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
            ga('create', '<c:out value="${GOOGLEANAL}"/>', 'infrafrontier.eu');
            ga('send', 'pageview');
        </SCRIPT>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js">
        </script>
        <script type="text/javascript" src="../js/autocomplete/autocomplete.js"></script>
        <%-- <script type="text/javascript" src="http://dev.jquery.com/view/trunk/plugins/autocomplete/jquery.autocomplete.js"></script>--%>
        <link rel="stylesheet" type="text/css" href="../css/autocomplete/autocomplete.css">
        <%--<link rel="stylesheet" href="http://dev.jquery.com/view/trunk/plugins/autocomplete/jquery.autocomplete.css" type="text/css" />--%>
        <style type="text/css">@import url(../css/calendar-blue.css);</style>
        <script type="text/javascript" src="../js/calendar.js"></script>
        <script type="text/javascript" src="../js/calendar-en.js"></script>
        <script type="text/javascript" src="../js/calendar-setup.js"></script>
        <script type="text/javascript" src="../js/popWin.js"></script>
        <script type="text/javascript" src="../js/taDisplay.js?<%= new java.util.Date()%>"></script>
        <script type="text/javascript">
            /*
             * Fill the contact persons details with the scientists when chosen 'as above'.
             */
            function contactAsAbove (form) {

                var scientist = new Array ('sci_title','sci_firstname','sci_surname','sci_e_mail','sci_phone','sci_fax');
                var contact = new Array ('con_institution','con_dept','con_addr_1','con_addr_2','con_province','con_town','con_postcode','con_country');
                var billing = new Array ('bil_institution','bil_dept','bil_addr_1','bil_addr_2','bil_province','bil_town','bil_postcode','bil_country');
                // alert("contactAsAvive reached " + form);
            <%--  /*
               * 'con_title','con_firstname','con_surname','con_e_mail','con_phone','con_fax',
               * 'bil_title','bil_firstname','bil_surname','bil_e_mail','bil_phone','bil_fax',
               *


        if ( form.con_as_above.checked == true ) {
                for (var i = 0; i < scientist.length; i++) {
                    form.elements[contact[i]].value = form.elements[scientist[i]].value;
                }
        }
                 */
            --%>
                    if ( form.bil_as_above.checked == true ) {
                        for (var i = 0; i < contact.length; i++) {
                            
                            form.elements[billing[i]].value = form.elements[contact[i]].value;
                        }
                    } 
                }
    
                function show(layer) {
                    //alert("show layer :- " + layer);
                    var myLayer=document.getElementById(layer).style.display;
                    document.getElementById(layer).style.display="";
                         
                }
                function hide(layer) {
                    // alert(layer);
                    var myLayer=document.getElementById(layer).style.display;
                    document.getElementById(layer).style.display="none";
                        
                }
                     
                function calHiddenFields(calendar) {
                    var y = calendar.date.getFullYear();
                    var m = calendar.date.getMonth() + 1;
                    var d = calendar.date.getDate();
                    var h = calendar.date.getHours();
                    var mins = calendar.date.getMinutes();
                    var s = calendar.date.getSeconds();
                    var ms = calendar.date.getMilliseconds();
                    if (m < 10) {
                        m = '0' + m;
                    }
                    if (d < 10) {
                        d = '0' + d;
                    }
                    var fmtDate = ""+y+m+d +h+mins+s+ms+"";
                    document.forms[0].timestamp.value=fmtDate;
                    fmtDate = ""+y+"-"+m+"-"+d+" "+h+":"+mins+":"+s+"";
                    document.forms[0].ftimestamp.value=fmtDate;
                }

        </script>   
        <script type="text/javascript">
            $(document).ready(function(){
                $("#insertID").autocomplete("../ajaxReturn.emma",{ mustMatch:1,max:100});


                $('#insertID').result(function(event, data, formatted) {
                    if (data) {
       
                        // Extract the data values          
                        var emma_id = data[0];
                        var name = data[1];
                        var id = data[2];
                        //  alert(emma_id + "\n" + name + "\n" + id);
                        document.forms[0].strain_id.value = emma_id; 
                        document.forms[0].strain_name.value = name;
                        document.forms[0].str_id_str.value = id; 
                    }
                });
        </script>

    </head>
    <body>
        <br/>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
        <br/><br/>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <c:choose> <c:when test="${command.register_interest == '1' && command.id_req == null}" > 
                                        <h4>EMMA Strain Interest Registration Form</h4> 
                                    </c:when>
                                    <c:when test="${(not empty param.status)}" >       
                                        <h4><span id="loginHeader">Request Insert Interface - Logged in as user ${param['user']} </span></h4>

                                        <table style="width: 80%; text-align: left; margin-left: auto; margin-right: auto;" border="0" cellpadding="0" cellspacing="0">
                                            <tbody>
                                                <tr>
                                                    <td class="noborder">
                                                        Use this form to insert requests for strains that can not be ordered via the EMMA Mutant Request Form on the public EMMA webpage. Please start by adding the emma id in the highlighted field below.
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </c:when> 
                                    <c:otherwise>
                                        <h4>EMMA <c:choose><c:when test="${param['type'] eq 'nkiescells'}">ES Cell</c:when><c:otherwise>Mutant</c:otherwise></c:choose> Request Form</h4> 
                                    </c:otherwise> 
                                </c:choose>
                                <c:if test="${(empty param.status)}">

                                    <table style="width: 80%; text-align: left; margin-left: auto; margin-right: auto;" border="0" cellpadding="0" cellspacing="0">
                                        <tbody>
                                            <tr>
                                                <td class="noborder">Dear Scientist<br>
                                                    Please use the following form to 
                                                    <c:choose>
                                                        <c:when test="${command.register_interest == '1' && command.id_req == null}" > 
                                                            indicate your interest in ordering EMMA strains which are currently under development and not yet available.
                                                        </c:when> 
                                                        <c:otherwise>
                                                            request the mouse <c:choose><c:when test="${param['type'] eq 'nkiescells'}">ES Cells</c:when><c:otherwise>strains</c:otherwise></c:choose> archived by EMMA.
                                                        </c:otherwise> 
                                                    </c:choose>
                                                    <br>
                                                    <br>
                                                    Please complete all fields. Required information is marked by a <font color="red">*</font> Click <b>once</b> on the <b>Send</b> button at
                                                    the end of this form to submit your information to EMMA. You  
                                                    <c:choose>
                                                        <c:when test="${command.register_interest == '1' && command.id_req == null}" > 
                                                            will receive automated notice as soon as the strain becomes available and be able to place your strain request in time.
                                                        </c:when> 
                                                        <c:otherwise >
                                                            will receive an automatic email notification to acknowledge receipt of your request.
                                                            <%--TA Options now removed under new grant from 01012013 PJW<b>Transnational Access (TA) applications:</b> Once you select your country of origin, eligible customers can select their order options and submit the compulsory project description in the indicated field. TA applications can only be submitted for strains which are stocked by the EMMA nodes at CNR-Monterotondo, CNRS-CDTA, MRC-MGU, KI, HMGU-IEG, GIE-CERBM and CNB-CSIC.--%>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <br>
                                                    <br>
                                                    Thank you for your interest in the European Mouse Mutant Archive.<br><br>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </c:if>

                                <spring:bind path="command.*">
                                    <c:if test="${not empty status.errorMessages}">
                                        <center>
                                            <c:forEach var="error" items="${status.errorMessages}">
                                                <font color="red"><c:out value="${error}" escapeXml="false" /> </font>
                                                <br />
                                            </c:forEach>
                                        </c:if>
                                    </spring:bind>
                                    <c:if test="${not empty message}">
                                        <font color="green"><c:out value="${message}" /></font>
                                        <c:set var="message" value=""  scope="page" />
                                    </center>
                                </c:if>


                                <%-- <form  class="form" method="post" commandName="command">--%>
                                <form:form method="POST" commandName="command">
                                    <%--spring:bind path="command.eligible_country">--%><input type="hidden" name="eligible_country" id="eligible_country" value="<c:out value='${command.eligible_country}'/>"/><%--</spring:bind>--%>

                                    <c:if test="${(empty param.status)}">
                                        <input type="hidden" id="projectID" name="projectID" value="<c:out value="${param.pid}"/>"/>

                                        <c:if test="${param.pid == '1' || param.pid == '2' || param.pid == '6'}"> <input type="hidden" id="europhenome" name="europhenome" value="no"/><input type="hidden" id="wtsi_mouse_portal" name="wtsi_mouse_portal" value="no"/></c:if>
                                    </c:if>
                                    <c:if test="${(not empty param.status)}">
                                        <input type="hidden" id="europhenome" name="europhenome" value="no"/>
                                        <input type="hidden" id="wtsi_mouse_portal" name="wtsi_mouse_portal" value="no"/>
                                        <table width="100%">
                                            <tr><td class="boxoutB" align="right"><b>Select strain id</b>&nbsp;&nbsp;</td><td class="boxoutB"><input type="text" name="insertID" id="insertID" value="" size="10"  /></td></tr>

                                            <tr><td class="boxoutB" align="right"><b>Request date</b>&nbsp;&nbsp;</td><td class="boxoutB"><input type="text" name="reqDate" id="reqDate" value="<dt:format pattern="yyyy-MM-dd HH:mm:s"><dt:currentTime/></dt:format>" size="10"  />            
                                                        <img src="../images/cal.gif"  id="reqdate" border="0">
                                                        <script type="text/javascript">
                                                                Calendar.setup(
                                                                {
                                                                    inputField  : "reqDate",
                                                                    ifFormat    : "%Y-%m-%d %H:%M:%S",
                                                                    button      : "reqdate",   
                                                                    onUpdate : calHiddenFields
                                                                }
                                                            );
                                                                document.forms[0].elements['insertID'].focus();

                                                        </script></td></tr>
                                                <tr><td class="boxoutB" align="right"><b>Trigger e-mails (default=no)</b>&nbsp;&nbsp;</td><td class="boxoutB"><input type="radio" name="triggerMails" id="triggerMails" value="no" checked  />No<input type="radio" name="triggerMails" id="triggerMails" value="managersonly"  />Centre managers only<input type="radio" name="triggerMails" id="triggerMails" value="yes" />Yes</td></tr>
                                            </table>                          
                                    </c:if>  

                                </div>          
                                <div class="boxcontainer" id="contacts">
                                    <h5>Recipient principal investigator</h5>
                                    <p><strong>Scientist's Title</strong></p>
                                    <spring:bind path="command.sci_title">
                                        <select name="sci_title" >
                                            <option value=""></option>
                                            <c:choose>
                                                <c:when test="${command.sci_title  == 'Mr'}" >
                                                    <option value="Mr" selected>Mr</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Mr">Mr</option> 
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${command.sci_title  == 'Mrs'}" >
                                                    <option value="Mrs" selected>Mrs</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Mrs">Mrs</option> 
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${command.sci_title  == 'Ms'}" >
                                                    <option value="Ms" selected>Ms</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Ms">Ms</option> 
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${command.sci_title  == 'Prof'}" >
                                                    <option value="Prof" selected>Prof</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Prof">Prof</option> 
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${command.sci_title  == 'Dr'}" >
                                                    <option value="Dr" selected>Dr</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Dr">Dr</option> 
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </spring:bind>

                                    <p><strong>Firstname <font color="red">*</font></strong></p>
                                    <spring:bind path="command.sci_firstname">
                                        <input type="text" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" maxlength="50" />
                                    </spring:bind>
                                    <p><strong>Surname <font color="red">*</font></strong></p>

                                    <spring:bind path="command.sci_surname">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" maxlength="50" />
                                    </spring:bind>
                                    <p><strong>E_mail <font color="red">*</font></strong></p>

                                    <spring:bind path="command.sci_e_mail">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" maxlength="100" />
                                    </spring:bind>

                                    <p><strong>Phone <font color="red">*</font></strong></p>

                                    <spring:bind path="command.sci_phone">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="20" maxlength="20" />
                                    </spring:bind>
                                    <p><strong>Fax</strong></p>

                                    <spring:bind path="command.sci_fax">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="20" maxlength="20" />
                                    </spring:bind>
                                    <br />
                                </div>

                                <div class="boxcontainer">
                                    <br/> <h5>Shipping contact (animal facility manager etc.)</h5>
                                    <p><strong>Shipping Contact's Title</strong></p>
                                    <spring:bind path="command.sci_title">
                                        <select name="con_title" >
                                            <option value=""></option>
                                            <c:choose>
                                                <c:when test="${command.con_title  == 'Mr'}" >
                                                    <option value="Mr" selected>Mr</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Mr">Mr</option> 
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${command.con_title  == 'Mrs'}" >
                                                    <option value="Mrs" selected>Mrs</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Mrs">Mrs</option> 
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${command.con_title  == 'Ms'}" >
                                                    <option value="Ms" selected>Ms</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Ms">Ms</option> 
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${command.con_title  == 'Prof'}" >
                                                    <option value="Prof" selected>Prof</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Prof">Prof</option> 
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${command.con_title  == 'Dr'}" >
                                                    <option value="Dr" selected>Dr</option>
                                                </c:when>
                                                <c:otherwise> 
                                                    <option value="Dr">Dr</option> 
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </spring:bind>
                                    <p><strong>Firstname <font color="red">*</font></strong></p>

                                    <spring:bind path="command.con_firstname">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                    </spring:bind>
                                    <p><strong>Surname <font color="red">*</font></strong></p>

                                    <spring:bind path="command.con_surname">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
                                    </spring:bind>
                                    <p><strong>E_mail <font color="red">*</font></strong></p>

                                    <spring:bind path="command.con_e_mail">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                    </spring:bind>
                                    <p><strong>Phone <font color="red">*</font></strong></p>

                                    <spring:bind path="command.con_phone">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="20" />
                                    </spring:bind>
                                    <p><strong>Fax</strong></p>

                                    <spring:bind path="command.con_fax">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="20" />
                                    </spring:bind>
                                    <p><strong>Institution <font color="red">*</font></strong></p>

                                    <spring:bind path="command.con_institution">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"  size="75" />
                                    </spring:bind>
                                    <p><strong>Department</strong></p>

                                    <spring:bind path="command.con_dept">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="75" />
                                    </spring:bind>
                                    <p><strong>Address line 1 <font color="red">*</font></strong></p>

                                    <spring:bind path="command.con_addr_1">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="75" />
                                    </spring:bind>

                                    <p><strong>Address line 2</strong></p>

                                    <spring:bind path="command.con_addr_2">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="75" />
                                    </spring:bind>
                                    <p><strong>County/Province</strong></p>

                                    <spring:bind path="command.con_province">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                    </spring:bind>
                                    <p><strong>Town <font color="red">*</font></strong></p>

                                    <spring:bind path="command.con_town">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                    </spring:bind>
                                    <p><strong>Postcode <font color="red">*</font></strong></p>

                                    <spring:bind path="command.con_postcode">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="10" />
                                    </spring:bind>
                                    <p><strong>Country <font color="red">*</font></strong></p>

                                    <spring:bind path="command.con_country">
                                        <select <%-- hide Sanger strains and FCG strains from TA options as they have none --%> <c:choose><c:when test="${command.lab_id_labo == '1961' || command.lab_id_labo == '6'}"></c:when><c:otherwise><c:choose>
                                                        <c:when test="${command.register_interest == '1' && command.id_req == null}" ><%-- DO NOTHING --%>
                                                        </c:when>
                                                        <c:otherwise> <%--TA Options now removed under new grant from 01012013 PJWonChange="getListValue()--%>
                                                        </c:otherwise>
                                                    </c:choose></c:otherwise></c:choose> id="con_country" name="con_country">
                                                    <option value=""></option>
                                            <c:forEach var="country" items="${command.cvDAO}">
                                                <c:choose><c:when test="${country == command.con_country}"><c:set var="selected" value="selected"/></c:when><c:otherwise><c:set var="selected" value=""/></c:otherwise></c:choose>
                                                <option ${selected} value="${country}">${country}</option>
                                            </c:forEach>
                                        </select>
                                    </spring:bind>

                                </div>
                                <%-- Here starts the billing details ROI value = ${command.register_interest} --%> 

                                <c:if test="${command.register_interest  != 1 || command.id_req != null || not empty param.status}" >
                                    <div class="boxcontainer">
                                        <%--<c:if test="${command.register_interest == '1' && command.id_req == null}" > --%>
                                        <%--<table width="100% ">--%>
                                        <br/> <h5>Billing address.Please provide a billing address<%-- and/or a purchase order number--%>.</h5>
                                        <%-- </table>  style="display: none;" <div title="vat" id="vat" ></div>--%>               

                                        <p><strong>VAT Reference <font color="red">*</font></strong> Only mandatory for orders from EU Countries.</p>
                                        <spring:bind path="command.bil_vat">
                                            <input type="text" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                        </spring:bind>



                                        <p><strong>Purchase Order Reference</strong></p>
                                        <spring:bind path="command.PO_ref">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                        </spring:bind>

                                        <input type="checkbox" name="bil_as_above" id="bil_as_above" value="on" onclick="contactAsAbove(this.form);" />Billing Contact address same as Shipping institute address.

                                        <p><strong>Billing Contacts Title</strong></p>
                                        <spring:bind path="command.bil_title">
                                            <select name="bil_title" >
                                                <option value=""></option>
                                                <c:choose>
                                                    <c:when test="${command.bil_title  == 'Mr'}" >
                                                        <option value="Mr" selected>Mr</option>
                                                    </c:when>
                                                    <c:otherwise> 
                                                        <option value="Mr">Mr</option> 
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${command.bil_title  == 'Mrs'}" >
                                                        <option value="Mrs" selected>Mrs</option>
                                                    </c:when>
                                                    <c:otherwise> 
                                                        <option value="Mrs">Mrs</option> 
                                                    </c:otherwise>
                                                </c:choose>  
                                                <c:choose>
                                                    <c:when test="${command.bil_title  == 'Ms'}" >
                                                        <option value="Ms" selected>Ms</option>
                                                    </c:when>
                                                    <c:otherwise> 
                                                        <option value="Ms">Ms</option> 
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${command.bil_title  == 'Prof'}" >
                                                        <option value="Prof" selected>Prof</option>
                                                    </c:when>
                                                    <c:otherwise> 
                                                        <option value="Prof">Prof</option> 
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${command.bil_title  == 'Dr'}" >
                                                        <option value="Dr" selected>Dr</option>
                                                    </c:when>
                                                    <c:otherwise> 
                                                        <option value="Dr">Dr</option> 
                                                    </c:otherwise>
                                                </c:choose>
                                            </select>
                                        </spring:bind>

                                        <p><strong>Firstname <font color="red">*</font></strong></p>

                                        <spring:bind path="command.bil_firstname">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                        </spring:bind>
                                        <p><strong>Surname <font color="red">*</font></strong></p>

                                        <spring:bind path="command.bil_surname">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
                                        </spring:bind>
                                        <p><strong>E_mail <font color="red">*</font></strong></p>

                                        <spring:bind path="command.bil_e_mail">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                        </spring:bind>
                                        <p><strong>Phone <font color="red">*</font></strong></p>

                                        <spring:bind path="command.bil_phone">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="20" />
                                        </spring:bind>

                                        <p><strong>Fax</strong></p>

                                        <spring:bind path="command.bil_fax">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="20" />
                                        </spring:bind>
                                        <p><strong>Institution <font color="red">*</font></strong></p>

                                        <spring:bind path="command.bil_institution">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"  size="75" />
                                        </spring:bind>
                                        <p><strong>Department</strong></p>

                                        <spring:bind path="command.bil_dept">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="75" />
                                        </spring:bind>
                                        <p><strong>Address line 1 <font color="red">*</font></strong></p>

                                        <spring:bind path="command.bil_addr_1">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="75" />
                                        </spring:bind>
                                        <p><strong>Address line 2</strong></p>

                                        <spring:bind path="command.bil_addr_2">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="75" />
                                        </spring:bind>
                                        <p><strong>County/Province</strong></p>

                                        <spring:bind path="command.bil_province">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                        </spring:bind>
                                        <p><strong>Town <font color="red">*</font></strong></p>

                                        <spring:bind path="command.bil_town">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="50" />
                                        </spring:bind>
                                        <p><strong>Postcode <font color="red">*</font></strong></p>

                                        <spring:bind path="command.bil_postcode">
                                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="10" />
                                        </spring:bind>
                                        <p><strong>Country <font color="red">*</font></strong></p>

                                        <spring:bind path="command.bil_country">
                                            <select name="bil_country">
                                                <option value=""></option>
                                                <c:forEach var="country" items="${command.cvDAO}">
                                                    <c:choose><c:when test="${country == command.bil_country}"><c:set var="selected" value="selected"/></c:when><c:otherwise><c:set var="selected" value=""/></c:otherwise></c:choose>
                                                    <option ${selected} value="${country}">${country}</option>
                                                </c:forEach>
                                            </select>
                                        </spring:bind>

                                        <div class="clear"></div>

                                    
                                </div>
                                        </c:if>
                                <%-- END OF BILLING INFORMATION  --%>


                                <!-- Contact div end -->
                                <c:choose><c:when test="${not empty param.status}"><c:set var="readonlyfield" value="READONLY"/></c:when><c:otherwise><c:set var="readonlyfield" value="READONLY"/></c:otherwise></c:choose>



                                <div class="boxcontainer">
                                    <h5>Requested <c:choose><c:when test="${param['type'] eq 'nkiescells'}">ES Cell</c:when><c:otherwise>strain</c:otherwise></c:choose>:</h5>
                                    <br />
                                    <p><strong><c:choose><c:when test="${param['type'] eq 'nkiescells'}">Clone</c:when><c:otherwise>EMMA</c:otherwise></c:choose> ID</strong></p><spring:bind path="command.strain_id">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="10" ${readonlyfield} />
                                    </spring:bind>
                                    <p><strong>Strain name</strong></p><spring:bind path="command.strain_name">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="100" ${readonlyfield} />
                                    </spring:bind>
                                    <p><strong>Common name(s)</strong></p><spring:bind path="command.common_name_s">
                                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="100" ${readonlyfield} />
                                    </spring:bind>
                                </div>
                                        
                                <div class="boxcontainer">
                                    <p>
                                    <h5>
                                        <c:choose>
                                            <c:when test="${command.register_interest == '1' && command.id_req == null}" ><strong>Material you intend to request:</strong>
                                            </c:when>
                                            <c:otherwise><strong>Requested Material:</strong>
                                            </c:otherwise>
                                        </c:choose>
                                        <font color="red">*</font>
                                    </h5>
                                    </p>
                                    <c:choose><c:when test="${param['type'] ne 'nkiescells' && command.register_interest != 1}"><br/>
                                            <strong>Distributed from:-</strong> ${distCentre.name}, ${distCentre.country}<br/><br/>
                                            <strong>Current availability:-</strong><br/>
                                            <p>&nbsp;</p>
                                            <spring:bind path="command.req_material">
                                                <c:forEach var="avail" items="${availabilities}">
                                                    <c:choose>
                                                        <c:when test="${fn:startsWith(avail, 'Rederivation')}">
                                                            <c:set var="deliveryTime" value="${command.liveDelivery}"/>
                                                            <c:set var="price" value="${command.liveCost}"/>
                                                            <c:set var="inputFieldValue" value="Rederived"/>
                                                        </c:when>
                                                        <c:when test="${fn:startsWith(avail, 'Live')}">
                                                            <c:set var="deliveryTime" value="${command.liveShelfDelivery}"/>
                                                            <c:set var="price" value="${command.liveShelfCost}"/>
                                                            <c:set var="inputFieldValue" value="live animals"/>
                                                        </c:when>
                                                        <c:when test="${fn:contains(avail, 'sperm')}">
                                                            <c:set var="deliveryTime" value="${command.frozenDelivery}"/>
                                                            <c:set var="price" value="${command.frozenCost}"/>
                                                            <c:set var="inputFieldValue" value="frozen sperm"/>
                                                        </c:when>
                                                        <c:when test="${fn:contains(avail, 'embryos')}">
                                                            <c:set var="deliveryTime" value="${command.frozenDelivery}"/>
                                                            <c:set var="price" value="${command.frozenCost}"/>
                                                            <c:set var="inputFieldValue" value="frozen embryos"/>
                                                        </c:when>
                                                        <c:otherwise>

                                                        </c:otherwise>
                                                    </c:choose>
                                                    <p><input type="radio" name="<c:out value="${status.expression}"/>" value="${inputFieldValue}" class="radio"  /> <strong>${avail}.</strong> Delivered in ${deliveryTime} (after paperwork in place). &euro;${price}, plus shipping costs.</p>
                                                    </c:forEach>  
                                                </spring:bind>
                                                    <p>&nbsp;</p>
                                                    <p>Due to the dynamic nature of our processes, strain availability may change at short notice. The local repository manager will advise you in these circumstances.</p> 
                                        </c:when>
                                        <c:otherwise></c:otherwise>
                                                   
                                    </c:choose>

                                    <c:if test="${command.register_interest  != null || command.req_material != null}" >
                                        <c:choose><c:when test="${command.req_material  == 'first available'}"> <br /><br />You indicated ${command.req_material} therefore, either frozen or live material, depending on current stock, will be supplied.<br /></c:when>
                                            <c:otherwise> 


                                                <c:choose>
                                                    <c:when test="${param['type'] eq 'nkiescells'}">
                                                        <spring:bind path="command.req_material">
                                                            <form:radiobutton id="${status.expression}" path="${status.expression}" value="ES Cells" /><strong>ES Cells</strong>
                                                        </spring:bind>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <%--<p><input type="radio" name="<c:out value="${status.expression}"/>" value="live animals" class="radio"  /><strong>Live Animals</strong></p>
                                                        <p><input type="radio" name="<c:out value="${status.expression}"/>" value="frozen material" class="radio"  /><strong>Frozen Material</strong></p>--%>
                                                    </c:otherwise>
                                                </c:choose>



                                                <c:if test="${command.register_interest == '1' && command.id_req == null}" >

                                                    <p><spring:bind path="command.req_material">
                                                            <c:choose>
                                                                <c:when test="${command.req_material  == 'first available'}" >
                                                                    <input type="radio" name="<c:out value="${status.expression}"/>" value="first available" class="radio" checked />
                                                                </c:when>
                                                                <c:otherwise> 
                                                                    <input type="radio" name="<c:out value="${status.expression}"/>" value="first available" class="radio" />
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <strong>First available</strong>
                                                        </spring:bind>
                                                            </p>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose> 
                                        </c:if>
                                    

                                    <c:if test="${command.register_interest == '1' && command.id_req == null}" >
                                        <p>PLEASE NOTE THAT EMMA CANNOT GUARANTEE THAT MICE WILL BE MADE AVAILABLE. THE POSSIBLE SHIPPING DATE CANNOT BE SCHEDULED WHILE THE STRAIN IS STILL UNDER DEVELOPMENT.
                                        </p>
                                        <spring:bind path="command.roi_date">
                                            <input type="hidden" name="<c:out value="${status.expression}"/>" value="<dt:format pattern="yyyyMMddHHmmss"><dt:currentTime/></dt:format>"/>
                                        </spring:bind>
                                    </c:if>
                                </div> 
                                <div class="boxcontainer">
                                    <br/><br/>
                                    <%--param.pid == '5'
                                    Now using command.lab_id_labo == '1961' to filter out sanger strains rather than pid of 5
                                    28/07/2011
                                    --%>
                                    <c:if test="${fn:containsIgnoreCase(command.strain_name,'nowt') }"> <%--  ||command.lab_id_labo == '1961'  --%><%--${param.pid == '3' || param.pid == '4' ||--%>
                                        <c:choose>
                                            <c:when test="${command.wtsi_mouse_portal  == 'yes'}" >
                                                <c:set var="wtsichecked" value="checked"/>
                                                <c:set var="wtsicheckedno" value=""/>
                                            </c:when>
                                            <c:when test="${command.wtsi_mouse_portal  == 'no'}" >
                                                <c:set var="wtsichecked" value=""/>
                                                <c:set var="wtsicheckedno" value="checked"/><%--checked --%>
                                            </c:when>
                                            <c:otherwise> 
                                                <c:set var="wtsichecked" value=""/>
                                                <c:set var="wtsicheckedno" value=""/>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${command.europhenome  == 'yes'}" >
                                                <c:set var="europhenomechecked" value="checked"/>
                                                <c:set var="europhenomecheckedno" value=""/>
                                            </c:when>
                                            <c:when test="${command.europhenome  == 'no'}" >
                                                <c:set var="europhenomechecked" value=""/>
                                                <c:set var="europhenomecheckedno" value="checked"/><%--checked --%>
                                            </c:when>

                                            <c:otherwise> 
                                                <c:set var="europhenomechecked" value=""/>
                                                <c:set var="europhenomecheckedno" value=""/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <c:if test="${fn:containsIgnoreCase(command.strain_name,'wtsi')}">
                                        Have you selected this line
                                        because you are interested in the phenotype data presented on the<%-- </c:if> <c:if test="fn:containsIgnoreCase(command.strain_name,'wtsi')}">${command.lab_id_labo == '1961' || --%>
                                        <a href='http://www.sanger.ac.uk/mouseportal/<c:if test="${not empty command.common_name_s}">search?query=${command.common_name_s}</c:if>' target="_blank">Wellcome Trust Sanger Institute Mouse Portal</a>? 
                                        <spring:bind path="command.wtsi_mouse_portal">
                                            Yes<form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />&nbsp;&nbsp;&nbsp;
                                            No<form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />
                                        </spring:bind> or the 
                                    </c:if>
                                    <c:if test="${fn:containsIgnoreCase(command.strain_name,'wtsi')}">
                                        <a href='http://www.europhenome.org' target='_blank'>Europhenome website</a>? <%-- param.pid == '3' || param.pid == '4' || command.lab_id_labo == '1961' || --%>
                                        <spring:bind path="command.europhenome">
                                            Yes<form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />&nbsp;&nbsp;&nbsp;
                                            No<form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />
                                        </spring:bind>
                                    </c:if>
                                    <%-- <c:if test="${(param.pid == '3' || param.pid == '4') && command.lab_id_labo != '1961' || fn:containsIgnoreCase(command.strain_name,'wtsi')}">
                                         <input type="hidden" id="wtsi_mouse_portal" name="wtsi_mouse_portal" value="no"/>
                                    
                                    <form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes<br />
                                    <input type="radio" name="<c:out value="${status.expression}"/>" value="no" class="radio" ${europhenomecheckedno}/>
                                    <input type="radio" name="<c:out value="${status.expression}" />" value="yes" class="radio" ${europhenomechecked}/>
                                     </c:if>--%>

                                    <p align="left">
                                        &nbsp;
                                    </p> 
                                    <p  style="text-align: center;" align="center">
                                        <c:if test="${empty param.status}">
                                            <c:choose>
                                                <c:when test="${command.register_interest == '1' && command.id_req == null}" >
                                                    <spring:bind path="command.application_type">
                                                        <input type="hidden" name="<c:out value="${status.expression}"/>" value=""/>
                                                    </spring:bind>
                                                    Please read and be aware of the <a href="javascript:void(0)" onClick="javascript:openWindow('../conditions.html');" id="conditionsShow"  title="EMMA Conditions">conditions</a> and associated shipping costs. These will have to be agreed to, when you finally place an order for the mice.

                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${param['type'] ne 'nkiescells'}">
                                                        <spring:bind path="command.terms_read">
                                                            <input type="checkbox"  name="<c:out value="${status.expression}"/>" value="on"<c:if test="${command.terms_read  != null}" >checked</c:if>/>
                                                        </spring:bind>
                                                        Please check this box to confirm you have read <a href="javascript:void(0)" onClick="javascript:openWindow('../conditions.html');" id="conditionsShow" title="EMMA Conditions">the conditions</a> and agree to pay the <a href="${BASEURL}resources-and-services/access-emma-mouse-resources/strain-ordering">service charge</a> plus shipping cost.
                                                        <%-- to resolve issue with null application type for normal requests 07012013--%>
                                                    </c:if>
                                                    <c:if test="${param['type'] == 'nkiescells'}">
                                                        <spring:bind path="command.terms_read">
                                                            <input type="hidden"  name="<c:out value="${status.expression}"/>" value="on"/>
                                                        </spring:bind>
                                                    </c:if>
                                                    <spring:bind path="command.application_type">
                                                        <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" value="request_only" />
                                                    </spring:bind>
                                                    <%-- to resolve issue with null application type for normal requests 07012013   onChange="javascript:ajax('../js/printclear.js','../conditions');return false;"  --%>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <c:if test="${not empty param.status}">

                                            <spring:bind path="command.application_type">
                                                <input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" value="request_only" />
                                            </spring:bind>
                                        </c:if>
                                    </p>
                                    <p style="text-align: center;" class="left">(Click on the "<b>Send</b>"
                                        button <b>once</b> to submit the form to EMMA.)</p>
                                    <p style="text-align: center;" class="left">
                                        <input name="Send" value="Send" class="btn big" type="submit">
                                        <input name="Clear all fields" value="Clear all fields" class="btn big" type="reset">
                                    </p>
                                    <spring:bind path="command.req_status">
                                        <input type="hidden" name="<c:out value="${status.expression}"/>" value="TO_PR" />
                                    </spring:bind>
                                    <c:if test="${command.register_interest  != null}" > 
                                        <c:choose>
                                            <c:when test="${command.id_req  != null}" >
                                                <spring:bind path="command.register_interest">
                                                    <input type="hidden" name="<c:out value="${status.expression}"/>" value="0" />
                                                </spring:bind>
                                            </c:when>
                                            <c:otherwise>
                                                <spring:bind path="command.register_interest">
                                                    <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
                                                </spring:bind>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <spring:bind path="command.timestamp">
                                        <input type="hidden" name="<c:out value="${status.expression}"/>" value="<dt:format pattern="yyyyMMddHHmmss"><dt:currentTime/></dt:format>"/>
                                    </spring:bind>
                                    <spring:bind path="command.ftimestamp">
                                        <input type="hidden" name="<c:out value="${status.expression}"/>" value="<dt:format pattern="yyyy-MM-dd HH:mm:s"><dt:currentTime/></dt:format>"/>
                                    </spring:bind>
                                    <spring:bind path="command.str_id_str">
                                        <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
                                    </spring:bind>
                                    <c:if test="${not empty command.lab_id_labo}">
                                        <spring:bind path="command.lab_id_labo"><input type="hidden" name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                        </spring:bind>
                                    </c:if>

                                </form:form>
                                <%-- </form>--%>
                                <c:choose><c:when test="${command.register_interest == '1' and command.id_req == null}" ><br/>
                                        ${command.id_req}</br></c:when><c:otherwise></c:otherwise></c:choose>
                            </div></div></div></div></div></div>

    <footer id="footer">

        <div class="innerfooter">        
            <div id="toplink"><a href="#top">to top</a></div>        	

            <div id="tn">
                <div class="region region-usernavi">
                    <div id="block-menu-block-4" class="block block-menu-block">

                        <div class="menu-block-wrapper menu-block-4 menu-name-user-menu parent-mlid-0 menu-level-1">

                        </div>

                    </div>
                </div>
            </div>   

            <img  src="../images/infrafrontier/icon/emma-logo-soft.png">
            <img align="right" src="../images/infrafrontier/icon/footerlogo.jpg">
        </div>

        <div id="footerline">        	
            <div class="innerfooter">
                <div class="splithalf">
                    <div class="half">
                        © INFRAFRONTIER 2013 - all rights reserved
                    </div>
                    <div class="half">
                        <div id="bn">
                            <div class="region region-usernavi">

                                <div class="region region-usernavi">
                                    <div id="block-menu-block-4" class="block block-menu-block">

                                        <div class="menu-block-wrapper menu-block-4 menu-name-user-menu parent-mlid-0 menu-level-1">
                                            <ul class="menu"><li class="first leaf menu-mlid-433"><a href="/infrafrontier-research-infrastructure/public-relations/contacts" title="">Contact</a></li>
                                            </ul></div>

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
