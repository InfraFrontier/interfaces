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
    Document   : submissionForm11
    Created on : 15-Mar-2012, 16:28:57
    Author     : phil
--%>
<%    response.setHeader("Cache-Control", "no-cache");
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
        <script language="Javascript" type="text/javascript">
            function CallParent()
            {
                $('#fileList').load('../ajaxReturn.emma', {encID: "${sessionScope.getprev}", submissionFileType: "ADDITIONAL", funct: "fileList"});
            }
        </script>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        <script type="text/javascript" src="../js/popWin.js"></script>
        
    </head>
    <body onKeyPress="return disableEnterKey(event)">
        <br/>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>

        <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <h4>
                                    Additional information (Step ${stepCurrent} of ${stepTotal})
                                </h4>
                                <form:form method="POST" commandName="command">

                                    <spring:bind path="command.past_requests">
                                        <div class="field">
                                            <p><strong>How many requests for this strain have you received in the last 6 months?<sup><font color="red">*</font></sup></strong></p>
                                            <div class="input">
                                                <form:select path="${status.expression}" id="${status.expression}">
                                                    <c:forEach var="it" begin="0" end="11" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${it!=11}"><form:option value="${it}">${it}</form:option></c:when>
                                                            <c:otherwise>
                                                                <form:option value="${it}">More than 10</form:option>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach> 
                                                </form:select>
                                            </div>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                        </div>
                                    </spring:bind>                    
                                    <br />
                                    <spring:bind path="command.deposited_elsewhere">
                                        <div class="field">
                                            <p><strong>Is this strain being deposited with any other institution or company intending to make it available for distribution?<sup><font color="red">*</font></sup></strong></p>
                                                        <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <p><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes (please explain below)<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</p><br />
                                                </spring:bind>
                                            <div id="depositedElsewhereText" style="display: none">
                                                <spring:bind path="command.deposited_elsewhere_text">
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                    <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5"></form:textarea>
                                                    </div>

                                                </div>
                                        </spring:bind>
                                    </div>
                                    <script type="text/javascript" >
                                        jQuery(document).ready(function() {

                                            if ($('input[name=deposited_elsewhere]:checked').val() == "yes") {
                                                $("#depositedElsewhereText").show("slow");
                                            }
                                            $("#deposited_elsewhere-yes").click(function() {
                                                $("#depositedElsewhereText").show("slow");
                                            });

                                            $("#deposited_elsewhere-no").click(function() {
                                                $("#depositedElsewhereText").hide("slow");
                                            });

                                            $("#deposited_elsewhere-not_known").click(function() {
                                                $("#depositedElsewhereText").hide("slow");
                                            });
                                        });
                                    </script>

                                    <spring:bind path="command.similar_strains">
                                        <div class="field">
                                            <p><strong>Are other laboratories producing similar strains?<sup><font color="red">*</font></sup></strong></p>
                                                        <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <p><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</p><br />
                                            </div>

                                        </div>
                                    </spring:bind>

                                    <spring:bind path="command.ip_rights">
                                        <div class="field">
                                            <p><strong>Are there any intellectual property rights or patented technologies linked to this strain?<sup><font color="red">*</font></sup></strong></p>
                                                        <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <p><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes (please explain below)<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</p><br />

                                            </spring:bind>
                                            <div id="ipRightsText"  style="display: none">
                                                <spring:bind path="command.ip_rights_text">
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                    <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5"></form:textarea>
                                                    </div>
                                                </div>

                                            </div>
                                    </spring:bind>
                                    <script type="text/javascript" >
                                        jQuery(document).ready(function() {
                                            if ($('input[name=ip_rights]:checked').val() == "yes") {
                                                $("#ipRightsText").show("slow");
                                            }

                                            $("#ip_rights-yes").click(function() {
                                                $("#ipRightsText").show("slow");
                                            });

                                            $("#ip_rights-no").click(function() {
                                                $("#ipRightsText").hide("slow");
                                            });

                                            $("#ip_rights-not_known").click(function() {
                                                $("#ipRightsText").hide("slow");
                                            });
                                        });
                                    </script>
                                    <spring:bind path="command.exclusive_owner">
                                        <div class="field">
                                            <p><strong>Is the producer the exclusive owner of this strain<sup><font color="red">*</font></sup></strong></p>
                                                        <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <p><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No (please list names of additional owners with affiliation and e-mail address below)<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</p><br />

                                            </spring:bind>
                                            <div id="exclOwnerText"  style="display: none">
                                                <spring:bind path="command.exclusive_owner_text">     
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                    <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5"></form:textarea>
                                                    </div>
                                                </div>
                                            </div>
                                    </spring:bind>
                                    <script type="text/javascript" >
                                        jQuery(document).ready(function() {
                                            if ($('input[name=exclusive_owner]:checked').val() == "no") {
                                                $("#exclOwnerText").show("slow");
                                            }
                                            $("#exclusive_owner-yes").click(function() {
                                                $("#exclOwnerText").hide("slow");
                                            });

                                            $("#exclusive_owner-no").click(function() {
                                                $("#exclOwnerText").show("slow");
                                            });

                                            $("#exclusive_owner-not_known").click(function() {
                                                $("#exclOwnerText").hide("slow");
                                            });
                                        });
                                    </script>

                                    <spring:bind path="command.owner_permission">
                                        <div class="field">
                                            <p><strong>Do you have permission from all owners to deposit this strain in the EMMA repository?<sup><font color="red">*</font></sup></strong></p>
                                                        <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <p><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No (please explain below)<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</p><br />
                                            </div>
                                        </spring:bind>
                                        <div id="ownerPermissionText"  style="display: none">
                                            <spring:bind path="command.owner_permission_text">
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5"></form:textarea>
                                                </div>


                                            </div>
                                    </spring:bind>
                                    <script type="text/javascript" >
                                        jQuery(document).ready(function() {
                                            if ($('input[name=owner_permission]:checked').val() == "no") {
                                                $("#ownerPermissionText").show("slow");
                                            }
                                            $("#owner_permission-yes").click(function() {
                                                $("#ownerPermissionText").hide("slow");
                                            });

                                            $("#owner_permission-no").click(function() {
                                                $("#ownerPermissionText").show("slow");
                                            });

                                            $("#owner_permission-not_known").click(function() {
                                                $("#ownerPermissionText").hide("slow");
                                            });
                                        });
                                    </script>
                                    <spring:bind path="command.delayed_release">
                                        <div class="field">
                                            <p><strong>Do you require <a target="_blank" href="https://www.infrafrontier.eu/delayed-release">delayed release</a> for this strain?<sup><font color="red">*</font></sup></strong></p>
                                                        <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <p><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes (please explain below)<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</p><br />
                                                </spring:bind>
                                            <div id="delayedReleaseText"  style="display: none">
                                                <spring:bind path="command.delayed_release_text">
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                    <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5"></form:textarea>
                                                </div>
                                            </spring:bind>
                                        </div>
                                    </div>

                                    <script type="text/javascript" >
                                        jQuery(document).ready(function() {
                                            if ($('input[name=delayed_release]:checked').val() == "yes") {
                                                $("#delayedReleaseText").show("slow");
                                            }
                                            $("#delayed_release-yes").click(function() {
                                                $("#delayedReleaseText").show("slow");
                                            });

                                            $("#delayed_release-no").click(function() {
                                                $("#delayedReleaseText").hide("slow");
                                            });

                                            $("#delayed_release-not_known").click(function() {
                                                $("#delayedReleaseText").hide("slow");
                                            });
                                        });
                                    </script>
                                    <div class="boxcontainer">

                                        <p><strong>How many mice of breeding age could you provide and when?</strong><br />
                                            Mice of breeding age must be provided. Minimum of 5 females and 5 males for freezing as homozygotes. Minimum of 5 males for freezing as heterozygotes.<br />
                                            Provision of more mice than the specified minimum will considerably accelerate the freezing process</p>
                                        <p>&nbsp;</p>
                                        <div class="box half first"><label>Month</label>
                                            <spring:bind path="command.mice_avail_month">
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <form:select path="${status.expression}" id="${status.expression}">
                                                    <form:option value="">Please select..</form:option>
                                                    <c:forEach var="it" begin="1" end="12" varStatus="status">
                                                        <form:option value="${it}">${it}</form:option>
                                                    </c:forEach> 
                                                </form:select>
                                            </spring:bind>

                                            <label>Males</label><spring:bind path="command.mice_avail_males">
                                                <form:select path="${status.expression}" id="${status.expression}">
                                                    <form:option value="">Please select..</form:option>
                                                    <c:forEach var="it" begin="0" end="7" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${it!=7}"><form:option value="${it}">${it}</form:option></c:when>
                                                            <c:otherwise>
                                                                <form:option value="${it}">More than 6</form:option>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach> 
                                                </form:select>
                                            </spring:bind>
                                        </div>
                                        <div class="box half second"><label>Year</label>
                                            <spring:bind path="command.mice_avail_year">
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <form:select path="${status.expression}" id="${status.expression}">
                                                    <form:option value="">Please select..</form:option>
                                                    <c:forEach var="it" begin="0" end="5" varStatus="status">
                                                        <form:option value="${(sessionScope.startYear)+it}">${(sessionScope.startYear)+it}</form:option>
                                                    </c:forEach> 
                                                </form:select>
                                            </spring:bind>
                                            <label>Females</label><spring:bind path="command.mice_avail_females">
                                                <form:select path="${status.expression}" id="${status.expression}">
                                                    <form:option value="">Please select..</form:option>
                                                    <c:forEach var="it" begin="0" end="11" varStatus="status">
                                                        <c:choose>
                                                            <c:when test="${it!=11}"><form:option value="${it}">${it}</form:option></c:when>
                                                            <c:otherwise>
                                                                <form:option value="${it}">More than 10</form:option>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach> 
                                                </form:select>
                                            </spring:bind>
                                        </div>
                                    </div>
                                    <div class="boxcontainer">
                                        <p><strong>Additional materials of interest (you can upload up to five attachments)</strong></p>
                                        <div class="input">
                                           <a href='javascript:void(0)' onClick="javascript:gmyWin = openWindow('fileUploadForm.emma?submissionID=${sessionScope.getprev}&submissionFileType=ADDITIONAL', gmyWin);
                                                return false;" title="Opens a new window">Upload attachment</a>
                                            <div id="fileList" name="fileList"></div>      
                                            <script type="text/javascript" >
                                                $('#fileList').load('../ajaxReturn.emma', {encID: "${sessionScope.getprev}", submissionFileType: "ADDITIONAL", funct: "fileList"});
                                            </script>
                                        </div>
                                        <div class="validation_error_message">
                                            &nbsp;
                                        </div>
                                    </div>
                                    <p>
                                        <%@include file="submissionFormControlButtons_inc.jsp"%>
                                    </p>
                                    <br/><br/>
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