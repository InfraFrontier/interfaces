<%-- 
    Document   : SubmissionForm8
    Created on : 30-Jan-2012, 14:49:45
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
         
        <script type="text/javascript" src="../js/popWin.js"></script>
        
        <style type="text/css" media="all">@import url("http://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/ebi.css");</style>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
<script type="text/javascript" src="http://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/js/default.js"></script>
       
<script type="text/javascript">
    function CallParent(qs) {
        //alert(type)
        var param = /[?&]submissionFileType=([^&]+)/i;
        var match = param.exec(qs);
        if (match != null) {
            fileType = match[1];
        } else {
            fileType = "";
        }
        //alert(file_type);
        $('#fileList' + fileType).load('../ajaxReturn.emma',{encID:"${param.getprev}", submissionFileType: fileType,funct: "fileList"});
    } 
</script>
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
                                <h4>
                                    Characterization (Step ${stepCurrent} of ${stepTotal})
                                </h4>
                                <p>
                                    Please enter information on how you characterize the mouse strain you want to deposit in EMMA.
                                </p>
                                <form:form method="POST" commandName="command" >
                                    <spring:bind path="command.genotyping">
                                        <div class="field"
                                             <p><strong>By genotyping</strong>&nbsp;<span class="tooltip" data-tooltip="<p>e.g., sequence of PCR primers and PCR settings, Southern probes and hybridization protocol. <%--A good template for PCR genotyping is available &lt;a href='genotyping-template.doc' target='_blank'&gt;here&lt;/a&gt;.--%>">? Help</span></p>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title=""></form:textarea>
                                                <a href='javascript:void(0)' onClick="javascript:gmyWin=openWindow('fileUploadForm.emma?submissionID=${sessionScope.getprev}&submissionFileType=GENO',gmyWin);return false;" title="Opens a new window">Upload as attachment</a>
                                            </div>
                                        
                                        </div>
                                    </spring:bind>
                                    <div id="fileListGENO" name="fileListGENO"></div>
                                    
                                    <script type="text/javascript">
                                        $("#fileListGENO").load('../ajaxReturn.emma',{encID:"${param.getprev}", submissionFileType: "GENO",funct: "fileList",time:"+11<%= new java.util.Date().getTime()%>"});
                                    </script>
                                    <spring:bind path="command.phenotyping">
                                        <div class="field">

                                            <p><strong>By phenotyping</strong>&nbsp;<span class="tooltip" data-tooltip="<p>e.g., coat colour, etc.</p>">? Help</span></p>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title=""></form:textarea>

                                                    <a href='javascript:void(0)' onClick="javascript:gmyWin=openWindow('fileUploadForm.emma?submissionID=${sessionScope.getprev}&submissionFileType=PHENO',gmyWin);return false;" title="Opens a new window">Upload as attachment</a>

                                            </div>
                                        </spring:bind>
                                        <div id="fileListPHENO" name="fileListPHENO"></div>
                                        <script type="text/javascript">
                                            $("#fileListPHENO").load('../ajaxReturn.emma',{encID:"${param.getprev}", submissionFileType: "PHENO",funct: "fileList",time:"+12<%= new java.util.Date().getTime()%>"});
                                        </script>
                                    </div>

                                    <spring:bind path="command.othertyping">           
                                        <div class="field">
                                            <p><strong>By any other means that are not genotyping or phenotyping</strong></p>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" ></form:textarea><a href='javascript:void(0)' onClick="javascript:gmyWin=openWindow('fileUploadForm.emma?submissionID=${sessionScope.getprev}&submissionFileType=OTHER',gmyWin);return false;" title="Opens a new window">Upload as attachment</a>                            
                                                </div>
                                                <div id="fileListOTHER" name="fileListOTHER"></div>
                                              <script type="text/javascript">
                                            $("#fileListOTHER").load('../ajaxReturn.emma',{encID:"${param.getprev}", submissionFileType: "OTHER",funct: "fileList",time:"+13<%= new java.util.Date().getTime()%>"});
                                        </script>
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
        <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>
