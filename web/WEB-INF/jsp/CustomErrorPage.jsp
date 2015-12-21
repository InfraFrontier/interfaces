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
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                        + request.getServerName() + ":" + request.getServerPort()
                        + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


<!--
Unless this text is here, if your page is less than 513 bytes, Internet Explorer will display it's "Friendly Error Page",
and your custom error will never be displayed.  This text is just used as filler.
This is a useless buffer to fill the page to 512 bytes to avoid display of Friendly Error Pages in Internet Explorer
This is a useless buffer to fill the page to 512 bytes to avoid display of Friendly Error Pages in Internet Explorer
This is a useless buffer to fill the page to 512 bytes to avoid display of Friendly Error Pages in Internet Explorer
-->

<%
    try {

        String errorMessage = "[The Custom Error Page was accessed directly]";

        // Make sure we have a pageContext.
        if(pageContext != null) {

            // Get error data
            ErrorData ed = null;
            try {
                ed = pageContext.getErrorData();
            } catch(NullPointerException ne) {

                // Sometimes this call causes a NullPointerException (PageContext.java:514)
                // Catch and ignore it.. it effectively means we can't use the ErrorData
                
                // Times this happens:
                // - error.jsp is accessed directly
            }
            
            // Prepare error report
            if(ed != null) {
                String url = ed.getRequestURI();
                int errorCode = ed.getStatusCode();
                Throwable e = ed.getThrowable();
               
                if(e != null) {
                
                    // Handle JSP exceptions differently, show the lines in a <pre> tag
                    if(e.getMessage() != null && e.getMessage().indexOf("Exception in JSP") != -1)
                        errorMessage = "An error occurred in a JSP file ...\n\n<pre>" + e.getMessage() + "</pre>";
                    else
                        errorMessage = e.getMessage();
    
                } else {
        
                    // HTTP Error (404 or similar)
                    String message = errorCode + " - The following page "+url+" could not be found.";
                    
                    // If referer available, report it
                    if(errorCode == 404) {
                    
                        if(request.getHeader("Referer") != null) {
                        
                            // Check if the referer contains our server name
                            if(request.getHeader("Referer").indexOf(request.getServerName()) != -1) {

                                // Broken link on this site
                                message += "<br />You have followed a broken link on our website.<br />We apologise for the inconvenience.";

                            } else {

                                // Broken link on another site (ask user to contact admin of that site)
                                message += "<br />You have followed a broken link on another website.<br />Please contact the administrator of that site, and let them know the link is not correct.";
                            }
                        }
                    }
                    errorMessage = message;
                }
            }
        }
    
        // A suitable error message is now stored in the errorMessage string variable.
        // You can send this to your support team by email using JavaMail or other similar tools.

        // Show the error message, with some HTML around it
        // This is the area you can customise to add your company logo etc...
        %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Error Page</title>
        <style type="text/css">@import url(<%=path%>/css/default.css);</style>
    </head>
    <body>
        <br/>
        <p><img src="" height="1" width="145"/><img src="<%=path%>/images/infrafrontier/logo-infrafrontier.png"/></p>
        <p>&nbsp;</p>
         <div id="wrapper">
        <div id="container">
        <div class="region region-content">
         <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
        <div class="form visible">
            <div class="boxcontainer">
                  <h4>It seems as though an error has occured.</h4>        
                <hr>
        		<b>Error: <%= errorMessage %></b>
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

        <img  src="<%=path%>/images/infrafrontier/icon/emma-logo-soft.png">
        <img align="right" src="<%=path%>/images/infrafrontier/icon/footerlogo.jpg">
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
       
        <%

    } catch(Throwable e2) {

        // Error in error handler
        out.println("An error has occurred, and could not be reported automatically.\n\n");
        out.println("Please copy the details below into an email and send it to support.\n");
        out.println(e2.toString());
    }
%>
