<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>

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
                    String message = errorCode + " - Page not found: "+url;
                    
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
        
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <html>
        <body>
        <h1>An error occurred!!!</h1>
        <hr>
        <b>Error: <%= errorMessage %></b>
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
