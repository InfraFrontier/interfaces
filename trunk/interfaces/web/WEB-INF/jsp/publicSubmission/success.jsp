<%-- 
    Document   : success
    Created on : Nov 23, 2012, 11:58:31 AM
    Author     : phil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="baseurl" value="${sessionScope.BASEURL}" scope="session" />
<c:set var="GOOGLEANAL" value="${sessionScope.GOOGLEANAL}" scope="session" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Submission Form Success Page</title>
        <style type="text/css">@import url(../css/default.css);</style>
        <SCRIPT>
            (function(i,s,o,g,r,a,m){
                i['GoogleAnalyticsObject']=r;
                i[r]=i[r]||function(){
                    (i[r].q=i[r].q||[]).push(arguments)
                },i[r].l=1*new Date();
                a=s.createElement(o),
                m=s.getElementsByTagName(o)[0];
                a.async=1;
                a.src=g;
                m.parentNode.insertBefore(a,m)
            })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
            ga('create', '${GOOGLEANAL}', 'infrafrontier.eu');
            ga('send', 'pageview');
        </SCRIPT>
    </head>
    <body>
        <br/>
        <p><img src="" height="1" width="145"/><a href="${baseurl}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
        <br/>
        <div id="wrapper">
            <div id="container">
                <div class="region region-content">
                    <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                        <div class="form visible">
                            <div class="boxcontainer">
                                <h4>EMMA Submission Form Success Page</h4>
                                Your submission to EMMA was successful. you can now:-<br/><br/>
                                <a href="submissionForm.emma">Create another submission</a> or return to the <a href="http://www.infrafrontier.eu/">Infrafrontier homepage</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>
