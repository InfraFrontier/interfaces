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
    Document   : submissionForm
    Created on : 26-Jan-2012, 10:53:06
    Author     : phil
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<spring:bind path="command.*"/>

<c:set var="keyRef" value='${command}'></c:set>

<c:set var="PeopleDAO" value='${keyRef["peopleDAO"]}'></c:set>
<c:set var="LabsDAO" value='${ArchiveDAO["labsDAO"]}'></c:set>
<%@page import="org.emmanet.util.Configuration" %>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML> 
<html lang="en">
    <head>
        <title>
            EMMA Mutant Mouse Strain Submission Wizard
        </title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
        <link type="text/css" href="css/token-input.css" rel="stylesheet" media="screen" charset="UTF-8" />
        <link type="text/css" href="css/submission.css" rel="stylesheet" media="screen" charset="UTF-8" />
        <script type="text/javascript" src="js/json2.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/jquery-1.6.1.min.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/jquery.tokeninput.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/jquery.qtip-1.0.0-rc3.min.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/jquery.easydate-0.2.4.min.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/jquery.store.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/jquery.form.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/jquery.validate.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/bbq.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/jquery-ui-1.8.5.custom.min.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/jquery.form.wizard-3.0.5.js" charset="UTF-8">
        </script>
        <script type="text/javascript" src="js/submission.js" charset="UTF-8">
        </script>
    </head>
    <body>
        <h1>
            EMMA Mutant Mouse Strain Submission Wizard
        </h1>
        <form id="submission" method="POST" action="." enctype="multipart/form-data" class="bbq">
            <div id="splash" class="step">
                <p>
                    Dear Submitting Investigator,
                </p>
                <p>
                    Please use this wizard to provide detailed information about the mutant mouse strain that you want to submit to EMMA. This information is used to evaluate your submission request. Please note that if the request is approved the information you provide will be published on the EMMA website.
                </p>
                <p>
                    Please fill the form as accurately as you can using "unknown", "not applicable" or "none" if you do not possess the information requested. A sample submission is provided <a href="#">here</a> for your convenience to examine all the information that is required.
                </p>
                <p>
                    <b>Multiple strains must be submitted separately. One strain per submission.</b>
                </p>
                <p>
                    We strongly recommend that you carefully read the <a target="PDF" href="/procedures/costs.php">EMMA Procedures</a> on this website. These documents describe the responsibilities EMMA has in maintaining and distributing the submitted strains as well as the responsibilities assumed by the submitter.
                </p>
                <p>
                    If your browser does not support forms you can <a href="mailto:emma@infrafrontier.eu">e-mail</a> all the complete strain information to EMMA.
                </p>
                <p>
                    Within 60 days you will be notified by e-mail of the result of the evaluation of your submission request.
                </p>
                <p>
                    Thank you for your interest in the European Mouse Mutant Archive.
                </p>
                <div class="field">
                    <div class="input">
                        <label><input type="checkbox" name="agree" value="" id="agree" />&nbsp;I have read the information above and agree to the <a href="#">EMMA Terms &amp; Conditions</a>!</label>
                    </div>
                    <div class="validation_error_message">
                        &nbsp;
                    </div>
                </div>
            </div>
          
            <div id="buttons">
                <div>
                    <input disabled="disabled" id="prev" value="Previous" type="reset" />&nbsp;<input id="next" value="Submit" type="submit" />
                </div>
            </div>
            <ul id="completed_steps"></ul>
        </form>
    </body>
</html>

