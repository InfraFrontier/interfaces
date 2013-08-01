<%-- 
    Document   : ajaxFileListing
    Created on : Jul 29, 2013, 11:47:17 AM
    Author     : phil
Used by submission form to display to user the files that have been uploaded

--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<c:set var="keyRef" value='${returnedOut}'></c:set>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery.parsequery.js"></script>
<script type="text/javascript" src="../js/jquery.parsequery.min.js"></script>
<!DOCTYPE html>

   file listing::--
            <c:forEach var="file" items="${keyRef['fileListing']}">${file}<br/></c:forEach> 

        
 
