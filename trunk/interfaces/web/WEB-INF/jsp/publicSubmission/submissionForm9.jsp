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
    Document   : submissionForm9
    Created on : 30-Jan-2012, 14:50:11
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
<spring:bind path="command.*" />
<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Mutant Mouse Strain Submission Wizard - Step ${stepCurrent} of ${stepTotal}</title>
        <script language="Javascript" type="text/javascript"> 
            function CallParent() 
            { 
                //alert(" This is parent window ");
                $('#SANITARYSTATUSfileList').load('../ajaxReturn.emma',{encID:"${sessionScope.getprev}", submissionFileType: "SANITARYSTATUS",funct: "fileList"});
            } 
        </script>
        <style type="text/css">@import url(../css/default.css);</style>
          <script type="text/javascript" src="../js/popWin.js"></script>

        <style type="text/css" media="all">@import url("https://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/ebi.css");</style>

        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        <script type="text/javascript" src="../js/tooltip.js"></script>

    </head>
    <body onKeyPress="return disableEnterKey(event)">
        <br/>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
        <div id="breeding" class="step">
            <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>
            <div id="wrapper">
                <div id="container">
                    <div class="region region-content">
                        <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                            <div class="form visible">
                                <div class="boxcontainer">
                                    <h4>
                                        Breeding (Step ${stepCurrent} of ${stepTotal})
                                    </h4>
                                    <p>Fertility and reproduction statistics, husbandry requirements and sanitary status of the mutant mouse strain you want to deposit in EMMA.<br/>
                                        <i>Please note that only few of the fields on this page are mandatory. However, if detailed information is available, EMMA would appreciate if you enter as much data as possible.</i>
                                        <br/><br/>
                                    </p>

                                    <form:form method="POST" commandName="command">
                                        <spring:bind path="command.homozygous_viable"> 
                                            <div class="field">
                                                <p><strong>Are homozygous mice viable?<sup><font color="red">*</font></sup></strong></p>
                                                            <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br />
                                                    <form:radiobutton id="${status.expression}-only_males" path="${status.expression}" value="males only" />Only males<br />
                                                    <form:radiobutton id="${status.expression}-only_females" path="${status.expression}" value="females only" />Only females<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known<br />
                                                </div>

                                            </div>
                                        </spring:bind>   

                                        <spring:bind path="command.homozygous_fertile">
                                            <div class="field">
                                                <p><strong>Are homozygous mice fertile?<sup><font color="red">*</font></sup></strong></p>
                                                            <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br />
                                                    <form:radiobutton id="${status.expression}-only_males" path="${status.expression}" value="males only" />Only males<br />
                                                    <form:radiobutton id="${status.expression}-only_females" path="${status.expression}" value="females only" />Only females<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known<br />
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.heterozygous_fertile">
                                            <div class="field">
                                                <p><strong>Are heterozygous/hemizygous mice fertile?<sup><font color="red">*</font></sup></strong></p>
                                                            <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br />
                                                    <form:radiobutton id="${status.expression}-only_males" path="${status.expression}" value="males only" />Only males<br />
                                                    <form:radiobutton id="${status.expression}-only_females" path="${status.expression}" value="females only" />Only females<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known<br />
                                                </div>

                                            </div>
                                        </spring:bind>  

                                        <spring:bind path="command.homozygous_matings_required">
                                            <div class="field">
                                                <p><strong>Are homozygous matings required?<sup><font color="red">*</font></sup></strong></p>
                                                            <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes (please explain below)<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br />

                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known<br />
                                                    <div id="homoReqText"  style="display: none">
                                                        <spring:bind path="command.homozygous_matings_required_text">
                                                            <form:errors path="${status.expression}" cssClass="error" />
                                                            <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title=""></form:textarea>&nbsp;<span class="tooltip" data-tooltip="<p>Please explain why homozygous matings are required.</p>">? Help</span>
                                                        </spring:bind>
                                                    </div>
                                                </div>
                                            </div>

                                        </spring:bind>
                                        <script>
                                            if ($('input[name=homozygous_matings_required]:checked').val() == "yes") {
                                                $("#homoReqText").show("slow");
                                            }
                                            
                                            $("#homozygous_matings_required-yes").click(function () {
                                                $("#homoReqText").show("slow");
                                            });
                            
                                            $("#homozygous_matings_required-no").click(function () {
                                                $("#homoReqText").hide("slow");
                                            });
                            
                                            $("#homozygous_matings_required-not_known").click(function () {
                                                $("#homoReqText").hide("slow");
                                            });            
                                        </script>


                                        <spring:bind path="command.reproductive_maturity_age">
                                            <div class="field">
                                                <p><strong>Average age of reproductive maturity (weeks)</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:select path="${status.expression}" id="${status.expression}">
                                                        <form:option value="">Please select..</form:option>
                                                        <form:option value="5">less than 6</form:option>
                                                        <form:option value="6">6</form:option>
                                                        <form:option value="7">7</form:option>
                                                        <form:option value="8">8</form:option>
                                                        <form:option value="9">9</form:option>
                                                        <form:option value="10">more than 9</form:option>
                                                    </form:select>
                                                </div>


                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.reproductive_decline_age">
                                            <div class="field">
                                                <p><strong>Average age of reproductive decline (months)</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:select path="${status.expression}" id="${status.expression}">
                                                        <form:option value="">Please select..</form:option>
                                                        <form:option  value="4">less than 4</form:option>
                                                        <form:option  value="4">4</form:option>
                                                        <form:option  value="5">5</form:option>
                                                        <form:option  value="6">6</form:option>
                                                        <form:option  value="7">7</form:option>
                                                        <form:option  value="8">8</form:option>
                                                        <form:option  value="9">9</form:option>
                                                        <form:option  value="10">10</form:option>
                                                        <form:option  value="11">11</form:option>
                                                        <form:option  value="12">12</form:option>
                                                        <form:option  value="13">more than 12</form:option>
                                                    </form:select>
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.gestation_length">
                                            <div class="field">
                                                <p><strong>Average length of gestation (days)</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:select path="${status.expression}" id="${status.expression}">
                                                        <form:option value="0">Please select..</form:option>
                                                        <form:option value="18">18</form:option>
                                                        <form:option value="19">19</form:option>
                                                        <form:option value="20">20</form:option>
                                                        <form:option value="21">21</form:option>
                                                        <form:option value="22">22</form:option>
                                                        <form:option value="23">23</form:option>
                                                    </form:select>
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.pups_at_birth">
                                            <div class="field">
                                                <p><strong>Average number of pups at birth</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:select path="${status.expression}" id="${status.expression}">
                                                        <form:option value="0">Please select..</form:option>
                                                        <form:option value="1">1</form:option>
                                                        <form:option value="2">2</form:option>
                                                        <form:option value="3">3</form:option>
                                                        <form:option value="4">4</form:option>
                                                        <form:option value="5">5</form:option>
                                                        <form:option value="6">6</form:option>
                                                        <form:option value="7">7</form:option>
                                                        <form:option value="8">8</form:option>
                                                        <form:option value="9">9</form:option>
                                                        <form:option value="10">10</form:option>
                                                        <form:option value="11">11</form:option>
                                                        <form:option value="12">12</form:option>
                                                        <form:option value="13">13</form:option>
                                                        <form:option value="14">14</form:option>
                                                    </form:select>
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.pups_at_weaning">
                                            <div class="field">
                                                <p><strong>Average number of pups surviving to weaning</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:select path="${status.expression}" id="${status.expression}">
                                                        <form:option value="0">Please select..</form:option>
                                                        <form:option value="1">1</form:option>
                                                        <form:option value="2">2</form:option>
                                                        <form:option value="3">3</form:option>
                                                        <form:option value="4">4</form:option>
                                                        <form:option value="5">5</form:option>
                                                        <form:option value="6">6</form:option>
                                                        <form:option value="7">7</form:option>
                                                        <form:option value="8">8</form:option>
                                                        <form:option value="9">9</form:option>
                                                        <form:option value="10">10</form:option>
                                                        <form:option value="11">11</form:option>
                                                        <form:option value="12">12</form:option>
                                                        <form:option value="13">13</form:option>
                                                        <form:option value="14">14</form:option>
                                                    </form:select>
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.weaning_age">
                                            <div class="field">
                                                <p><strong>Recommended weaning age (days)</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:input id="${status.expression}" path="${status.expression}" size="2" maxlength="2"/>
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.litters_in_lifetime">
                                            <div class="field">
                                                <p><strong>Average number of litters in lifetime</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:select path="${status.expression}" id="${status.expression}">
                                                        <form:option value="">Please select..</form:option>
                                                        <form:option value="1">1</form:option>
                                                        <form:option value="2">2</form:option>
                                                        <form:option value="3">3</form:option>
                                                        <form:option value="4">4</form:option>
                                                        <form:option value="5">5</form:option>
                                                        <form:option value="6">6</form:option>
                                                        <form:option value="7">7</form:option>
                                                        <form:option value="8">more than 7</form:option>
                                                    </form:select>
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.breeding_performance">
                                            <div class="field">
                                                <p><strong>Breeding performance</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:select path="${status.expression}" id="${status.expression}">
                                                        <form:option value="good">Please select..</form:option>
                                                        <form:option value="poor">Poor</form:option>
                                                        <form:option value="good">Good</form:option>
                                                        <form:option value="excellent">Excellent</form:option>
                                                    </form:select>
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.husbandry_requirements">
                                            <div class="field">
                                                <p><strong>Husbandry requirements</strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please describe any special dietary, environmental, medical, housing, handling requirements.</p>">? Help</span></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title=""></form:textarea>
                                                    </div>

                                                </div>
                                        </spring:bind>

                                        <spring:bind path="command.immunocompromised">
                                            <div class="field">
                                                <p><strong>Are mice immunocompromised?<sup><font color="red">*</font></sup></strong></p>
                                                            <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes<br />
                                                    <form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No<br />
                                                    <form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not Known<br /><br />
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.sanitary_status">
                                            <div class="field">
                                                <p><strong>Sanitary status</strong></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5"></form:textarea>
                                                    <spring:bind path="command.sanitary_status_file"><a href='javascript:void(0)' id="fileupload" onClick="javascript:gmyWin=openWindow('fileUploadForm.emma?submissionID=${sessionScope.getprev}&submissionFileType=SANITARYSTATUS',gmyWin);return false;" title="Opens a new window">Upload as attachment</a></spring:bind>
                                                        <div id="SANITARYSTATUSfileList" name="SANITARYSTATUSfileList"></div>      
                                                        <script type="text/javascript" > 
                                                        $('#SANITARYSTATUSfileList').load('../ajaxReturn.emma',{encID:"${sessionScope.getprev}", submissionFileType: "SANITARYSTATUS",funct: "fileList"});
                                                    </script>

                                                </div>

                                            </div>

                                        </spring:bind>

                                        <spring:bind path="command.welfare">
                                            <div class="field">
                                                <p><strong>Animal welfare</strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please enter the mouse welfare terms that apply to this mutant mouse strain.</p>">? Help</span> <a href="http://www.mousewelfareterms.org/" target="_BLANK">http://www.mousewelfareterms.org/</a></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:input  id="${status.expression}" path="${status.expression}" title="" maxlength="50"/>
                                                </div>

                                            </div>
                                        </spring:bind>

                                        <spring:bind path="command.remedial_actions">
                                            <div class="field">
                                                <p><strong>Remedial actions</strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please enter the remedial actions necessary to ensure the welfare of this mutant mouse strain.</p>">? Help</span></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title=""></form:textarea>
                                                    </div>

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

        <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>

    </body>
</html>
