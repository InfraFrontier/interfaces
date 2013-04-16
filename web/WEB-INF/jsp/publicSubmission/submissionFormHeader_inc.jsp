        
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
<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />
<style type="text/css">@import url(../css/default.css);</style>

<c:set var="percentageComplete" value="${(stepCurrent / stepTotal * 100)}"></c:set>
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
<script language="JavaScript">
function disableEnterKey(e)
{
var key;
if(window.event)
    key = window.event.keyCode;     //IE
else
    key = e.which;     //firefox
if(key == 13)
    return false;
else
    return true;
}
</script>
<br/>
<%--<div id="breadcrumbs" name="breadcrumbs" class="breadcrumbs">--%>
<div id="block-infrablocks-infraformtest" class="block block-infrablocks">
<center>
    
  <%--  <c:forEach var="title" items="${stepTitles}" varStatus="status">
        <c:if test="${sessionScope.pageCount > status.count}"><c:set var="separator" value=" << "/><c:set var="liStyle" value="last"/></c:if>
        <c:if test="${sessionScope.pageCount <= status.count}"><c:set var="separator" value=" >> "/></c:if>
        <c:if test="${status.last}"><c:set var="title" value="${fn:replace(title, ']', '')}"/><c:set var="separator" value=""/><c:set var="liStyle" value="last"/></c:if>
        <c:if test="${status.first}"><c:set var="title" value="${fn:replace(title, '[', '')}"/><c:set var="class" value="first active"/></c:if>
        <font class="breadcrumbs"><c:if test="${sessionScope.pageCount == status.count}"><strong>${title}</strong></c:if><c:if test="${sessionScope.pageCount != status.count}">${title}</c:if>${separator}</font>
         </c:forEach>
  --%>
        <ul class="progress clearfix">
  <c:forEach var="title" items="${stepTitles}" varStatus="status">
      <c:set var="liStyle" value=""/>
       <c:if test="${sessionScope.pageCount > status.count}"><c:set var="separator" value=" << "/><c:set var="liStyle" value="last"/></c:if>
        <c:if test="${sessionScope.pageCount <= status.count}"><c:set var="separator" value=" >> "/></c:if>
      <c:if test="${status.last}"><c:set var="title" value="${fn:replace(title, ']', '')}"/><c:set var="separator" value=""/><c:set var="liStyle" value="last"/></c:if>
        <c:if test="${status.first}"><c:set var="title" value="${fn:replace(title, '[', '')}"/><c:set var="liStyle" value=""/></c:if>
        <c:if test="${sessionScope.pageCount == status.count}"><c:set var="liStyle" value="first active"/></c:if>
        <li class="${liStyle}"><c:if test="${sessionScope.pageCount == status.count}">${title}</c:if><c:if test="${sessionScope.pageCount != status.count}">${title}</c:if>${separator}</li>
    </c:forEach>
      </ul>           
  </center>  
</div>
<br/>
<div id="progressbar" style="width:200px;height:10px;margin-left: auto;margin-right: auto;}"></div>
<div id="build" style="width:200px;height:10px;margin-left: auto;margin-right: auto;color: #cccccc;}">TEST BUILD V1.1.1</div>