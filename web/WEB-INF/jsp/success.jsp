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

                                    Return to the EMMA Strain List <a href="https://dev.infrafrontier.eu/search">here</a>.
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
