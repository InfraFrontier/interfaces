<%-- 
    Document   : submissionForm9
    Created on : 30-Jan-2012, 14:50:11
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
        <style type="text/css">@import url(../css/emmastyle.css);</style>
        <script type="text/javascript" src="../js/popWin.js"></script>
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
    </head>
    <body onKeyPress="return disableEnterKey(event)">
        <div id="breeding" class="step">
            <h2>
                Breeding (Step ${stepCurrent} of ${stepTotal})
            </h2>
            <%@include file="submissionFormHeader_inc.jsp"%>
            <p>
                Fertility and reproduction statistics, husbandry requirements and sanitary status of the mutant mouse strain you want to deposit in EMMA.
            </p>

            <form:form method="POST" commandName="command">
                <spring:bind path="command.homozygous_viable"> 
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Are homozygous mice viable?<sup><font color="red">*</font></sup></strong></label>

                        <div class="input">
                            <label><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes</label><br />
                            <label><form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No</label><br />
                            <label><form:radiobutton id="${status.expression}-only_males" path="${status.expression}" value="males only" />Only males</label><br />
                            <label><form:radiobutton id="${status.expression}-only_females" path="${status.expression}" value="females only" />Only females</label><br />
                            <label><form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</label><br />
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>   

                <spring:bind path="command.homozygous_fertile">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Are homozygous mice fertile?<sup><font color="red">*</font></sup></strong></label>
                        <div class="input">
                            <label><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes</label><br />
                            <label><form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No</label><br />
                            <label><form:radiobutton id="${status.expression}-only_males" path="${status.expression}" value="males only" />Only males</label><br />
                            <label><form:radiobutton id="${status.expression}-only_females" path="${status.expression}" value="females only" />Only females</label><br />
                            <label><form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</label><br />
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.heterozygous_fertile">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Are heterozygous/hemizygous mice fertile?<sup><font color="red">*</font></sup></strong></label>
                        <div class="input">
                            <label><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes</label><br />
                            <label><form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No</label><br />
                            <label><form:radiobutton id="${status.expression}-only_males" path="${status.expression}" value="males only" />Only males</label><br />
                            <label><form:radiobutton id="${status.expression}-only_females" path="${status.expression}" value="females only" />Only females</label><br />
                            <label><form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</label><br />
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>  

                <spring:bind path="command.homozygous_matings_required">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Are homozygous matings required?<sup><font color="red">*</font></sup></strong></label>
                        <div class="input">
                            <label><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes (please explain below)</label><br />
                            <label><form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No</label><br />

                            <label><form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not known</label><br />
                            <div id="homoReqText"  style="display: none">
                                <spring:bind path="command.homozygous_matings_required_text">
                                    <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title="Please explain why homozygous matings are required."></form:textarea>
                                </spring:bind>
                            </div>
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>

                </spring:bind>
                <script>
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
                        <label class="label" for="${status.expression}"><strong>Average age of reproductive maturity (weeks)</strong></label>
                        <div class="input">
                            <form:select path="${status.expression}" id="${status.expression}">
                                <form:option value="">Please select..</form:option>
                                <form:option value="5-">less than 6</form:option>
                                <form:option value="6">6</form:option>
                                <form:option value="7">7</form:option>
                                <form:option value="8">8</form:option>
                                <form:option value="9">9</form:option>
                                <form:option value="10">more than 9</form:option>
                            </form:select>
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />

                    </div>
                </spring:bind>

                <spring:bind path="command.reproductive_decline_age">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Average age of reproductive decline (months)</strong></label>
                        <div class="input">
                            <form:select path="${status.expression}" id="${status.expression}">
                                <form:option value="">Please select..</form:option>
                                <form:option  value="4-">less than 4</form:option>
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
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.gestation_length">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Average length of gestation (days)</strong></label>
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
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.pups_at_birth">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Average number of pups at birth</strong></label>
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
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.pups_at_weaning">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Average number of pups surviving to weaning</strong></label>
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
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.weaning_age">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Recommended weaning age (days)</strong></label>
                        <div class="input">
                            <form:input id="${status.expression}" path="${status.expression}" size="2" maxlength="2"/>
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.litters_in_lifetime">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Average number of litters in lifetime</strong></label>
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
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.breeding_performance">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Breeding performance</strong></label>
                        <div class="input">
                            <form:select path="${status.expression}" id="${status.expression}">
                                <form:option value="good">Please select..</form:option>
                                <form:option value="poor">Poor</form:option>
                                <form:option value="good">Good</form:option>
                                <form:option value="excellent">Excellent</form:option>
                            </form:select>
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.husbandry_requirements">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Husbandry requirements</strong></label>
                        <div class="input">

                            <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title="Please describe any special dietary, environmental, medical, housing, handling requirements."></form:textarea>
                            </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.immunocompromised">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Are mice immunocompromised?<sup><font color="red">*</font></sup></strong></label>

                        <div class="input">
                            <label><form:radiobutton id="${status.expression}-yes" path="${status.expression}" value="yes" />Yes</label><br />
                            <label><form:radiobutton id="${status.expression}-no" path="${status.expression}" value="no" />No</label><br />
                            <label><form:radiobutton id="${status.expression}-not_known" path="${status.expression}" value="not known" />Not Known</label><br /><br />
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.sanitary_status">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Sanitary status</strong></label>
                        <div class="input">
                            <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5"></form:textarea>
                                <div>
                                    <label> <spring:bind path="command.sanitary_status_file"><a href='javascript:void(0)' onClick="javascript:gmyWin=openWindow('fileUploadForm.emma?submissionID=${param.getprev}&submissionFileType=SANITARYSTATUS',gmyWin);return false;" title="Opens a new window">Upload as attachment</a></spring:bind></label>
                                </div>
                            </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.welfare">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Animal welfare</strong></label>
                        <div class="input">
                            <form:input  id="${status.expression}" path="${status.expression}" title="Please enter the mouse welfare terms that apply to this mutant mouse strain (from <http://www.mousewelfareterms.org>)."/>
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.remedial_actions">
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>Remedial actions</strong></label>
                        <div class="input">
                            <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title="Please enter the remedial actions necessary to ensure the welfare of this mutant mouse strain."></form:textarea>
                            </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>
                <p>
                <table width="150" cellspacing="8" class="formNav">
                    <tr>
                        <td colspan="2" align='center'><input type="submit" value="Next" name="_target10" /><br/></td>
                    </tr>
                    <tr>
                        <td ><input type="submit" value="Previous" name="_target8" /></td>
                        <td ><input type="submit" value="Cancel" name="_cancel" /></td>
                    </tr>
                </table>
            </p>
            <br/><br/>
        </form:form>
    </div>

</body>
</html>
