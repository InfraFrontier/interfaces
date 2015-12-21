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
        <title>EMMA Form Submission - Success Page</title>
        <style type="text/css">@import url(css/default.css);</style>

    </head>
    <body>
        <p>&nbsp;</p>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
        <p>&nbsp;</p>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <p>&nbsp;</p>
                                <h1>EMMA Form Submission - Success Page</h1>
                                <p align=center>
                                    <c:if test="${not empty message}">
                                        <font color="green"><c:out value="${message}" /></font>
                                        <c:set var="message" value="" scope="session" />
                                    </c:if>
                                    <br />
                                    <br />
                                    <c:choose><c:when test="${(sessionScope.req_material) eq 'ES Cells'}">
                                            Return to the NKI GEMM-ESC archive <a href="${BASEURL}resources-and-services/access-emma-mouse-resources/nki-gemm-esc-archive">here</a>
                                        </c:when>
                                        <c:otherwise>Return to the EMMA Strain List <a href="${BASEURL}search">here</a>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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

            <img  src="images/infrafrontier/icon/emma-logo-soft.png">
            <img align="right" src="images/infrafrontier/icon/footerlogo.jpg">
        </div>

        <div id="footerline">        	
            <div class="innerfooter">
                <div class="splithalf">
                    <div class="half">
                        © Infrafrontier 2013 - all rights reserved
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
