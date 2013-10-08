<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="baseurl" value="${sessionScope.BASEURL}" scope="session" />
<c:set var="GOOGLEANAL" value="${sessionScope.GOOGLEANAL}" scope="session" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA Form Submission - Success Page</title>
        <style type="text/css">@import url(css/default.css);</style>
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
                <p>&nbsp;</p>
        <p><img src="" height="1" width="145"/><a href="${baseurl}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
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

                                    Return to the EMMA Strain List <a href="${baseurl}search">here</a>.
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
