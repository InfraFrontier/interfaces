        
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
<style type="text/css">@import url(../css/emmastyle.css);</style>
<c:set var="percentageComplete" value="${(sessionScope.pageCount / sessionScope.totalStepCount * 100)}"></c:set>
        <script  type="text/javascript">
            $( document ).tooltip();
        </script>
<script>
    $(function() {
        $( "#progressbar" ).progressbar({
            value: "${percentageComplete}"
        });
    });
</script>
<br/>
<div id="breadcrumbs" name="breadcrumbs" class="breadcrumbs">
<center>
    
    <c:forEach var="title" items="${stepTitles}" varStatus="status">
        <c:if test="${sessionScope.pageCount > status.count}"><c:set var="separator" value=" << "/></c:if>
        <c:if test="${sessionScope.pageCount <= status.count}"><c:set var="separator" value=" >> "/></c:if>
        <c:if test="${status.last}"><c:set var="title" value="${fn:replace(title, ']', '')}"/><c:set var="separator" value=""/></c:if>
        <c:if test="${status.first}"><c:set var="title" value="${fn:replace(title, '[', '')}"/></c:if>
        <font class="breadcrumbs"><c:if test="${sessionScope.pageCount == status.count}"><strong>${title}</strong></c:if><c:if test="${sessionScope.pageCount != status.count}">${title}</c:if>${separator}</font></c:forEach>
                
  </center>  
</div>
<br/>
<div id="progressbar" style="width:200px;height:10px;margin-left: auto;margin-right: auto;}"></div>
<div id="build" style="width:200px;height:10px;margin-left: auto;margin-right: auto;color: #cccccc;}">TEST BUILD V1.0.1</div>