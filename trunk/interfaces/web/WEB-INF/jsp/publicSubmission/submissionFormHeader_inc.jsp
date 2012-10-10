        
<%--
Header for all pages in the form submission
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="percentageComplete" value="${(sessionScope.pageCount / sessionScope.totalStepCount * 100)}"></c:set>
<script>
    $(function() {
        $( "#progressbar" ).progressbar({
            value: "${percentageComplete}"
        });
    });
</script>
<div id="progressbar" style="width:200px;height:10px;margin-left: auto;margin-right: auto;}"></div>