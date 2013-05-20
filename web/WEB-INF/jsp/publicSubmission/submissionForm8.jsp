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
                
      <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        
        <style type="text/css" media="all">@import url("http://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/ebi.css");</style>
        <script type="text/javascript" src="http://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/js/default.js"></script>
          <%--     <script type="text/javascript" src="../js/jquery-ui-1.8.5.custom.min"></script>
        <script type="text/javascript" src="../js/jquery-1.6.1.min"></script>--%>
        <script type="text/javascript" src="../js/popWin.js"></script>
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
                    <div class="field">
                        <label class="label" for="${status.expression}"><strong>By genotyping</strong></label>

                        <div class="input">
                            <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title="e.g., sequence of PCR primers and PCR settings, Southern probes and hybridization protocol. A good template for PCR genotyping is available &lt;a href='genotyping-template.doc' target='_blank'&gt;here&lt;/a&gt;."></form:textarea>
                            <a href='javascript:void(0)' onClick="javascript:gmyWin=openWindow('fileUploadForm.emma?submissionID=${sessionScope.getprev}&submissionFileType=GENO',gmyWin);return false;" title="Opens a new window">Upload as attachment</a>
                        </div>
                        <form:errors path="${status.expression}" cssClass="error" />
                    </div>
                </spring:bind>

                <spring:bind path="command.phenotyping">
                    <div class="field">

                        <p><strong>By phenotyping</strong>&nbsp;<span class="tooltip" data-tooltip="<p><b>Tooltip</b><br/>e.g., coat colour, etc.</p>">? Help</span></p>
                        <div class="input">
                            <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" title=""></form:textarea>
                            
                                    <a href='javascript:void(0)' onClick="javascript:gmyWin=openWindow('fileUploadForm.emma?submissionID=${sessionScope.getprev}&submissionFileType=PHENO',gmyWin);return false;" title="Opens a new window">Upload as attachment</a>
                                
                            
                            <form:errors path="${status.expression}" cssClass="error" />
                            </div>
                              </spring:bind>
                        </div>
                  
                    <spring:bind path="command.othertyping">           
                        <div class="field">
                            <p><strong>By any other means that are not genotyping or phenotyping</strong></p>
                            <div class="input">
                                <form:textarea id="${status.expression}" path="${status.expression}" cols="50" rows="5" ></form:textarea><a href='javascript:void(0)' onClick="javascript:gmyWin=openWindow('fileUploadForm.emma?submissionID=${sessionScope.getprev}&submissionFileType=OTHER',gmyWin);return false;" title="Opens a new window">Upload as attachment</a>                            
                            </div>
                            <form:errors path="${status.expression}" cssClass="error" />
                        </div>
                    </spring:bind>
                    <p>
<%@include file="submissionFormControlButtons_inc.jsp"%>
               </p>
                   </div>
    </div>
             </div>
            </div>
        </div>
        </div>
        </form:form>
    </div> 
             <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
</body>
</html>
       <%-- <label><a href="javascript:void(0);" onclick="openDialog();">Upload as attachment</a>    
                                                                  <div name="fileupload" id="fileupload"></div>
                                                                
                                              <script type="text/javascript" > 
                                                  function openDialog(){
                                                      $('#fileupload').load('fileUploadForm.emma',{
                                                          submissionID: "${param.getprev}",
                                                          submissionFileType:"PHENO"
                                                      }).dialog({modal:true,width:640,height:480});
                                                  }
                                              </script>--%>