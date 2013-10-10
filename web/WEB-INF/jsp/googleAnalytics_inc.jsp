<%-- 
    Document   : googleAnalytics_inc
    Created on : Oct 10, 2013, 11:49:28 AM
    Author     : phil
--%>
<%
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        response.setHeader("Cache-Control", "no-store");
%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="GOOGLEANAL" value="${param.GOOGLEANAL}" scope="page" />
<c:set var="baseurl" value="${param.BASEURL}" scope="page" />
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
            ga('create', '<c:out value="${GOOGLEANAL}"/>', 'infrafrontier.eu');
            ga('send', 'pageview');
        </SCRIPT>