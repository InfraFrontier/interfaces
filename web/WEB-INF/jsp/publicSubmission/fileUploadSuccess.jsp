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
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA File Upload - Success Page</title>
        
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
        <style type="text/css">@import url(../css/default.css);</style>
                <script language="Javascript" type="text/javascript">
window.opener.CallParent(window.location.search);
</script>
    </head>
    <body>
        <br/>
        <p><img src="" height="1" width="145"/><img src="../images/infrafrontier/logo-infrafrontier.png"/></p>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <h4>EMMA File Upload - Success Page</h4>

                                    <p align=center>
                                        <c:if test="${not empty message}">
                                            <font color="green"><c:out value="${message}" /></font>
                                            <c:set var="message" value="" scope="session" />
                                        </c:if>
                                        <br />
                                        <br />
                                        Return to the submission form <a href="javascript:void(0)" onclick="javascript:window.close();">here</a>.
                                    </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body> 
</html>