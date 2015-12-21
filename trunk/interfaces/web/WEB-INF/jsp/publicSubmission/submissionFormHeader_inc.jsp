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
        
<%--
Header for all pages in the form submission
--%>
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    response.setHeader("Cache-Control", "no-store");
%>

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

<c:set var="percentageComplete" value="${(stepCurrent / stepTotal * 100)}"></c:set>

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
<div id="block-infrablocks-infraformtest" class="block block-infrablocks">
    <center>
</div>
<div class="progressTracker">     
    <ol class="progressTracker">
        <c:forEach var="title" items="${stepTitles}" varStatus="status">
            <c:set var="liStyle" value=""/>
            <%-- html commerts used to skirt the inline-block issue that tells css to treat each list item as regular text which includes spaces between words, comment trick eliminates whitespace --%>
            <c:set var="htmlCommentOpen" value="<!--"/>
            <c:set var="htmlCommentClose" value="-->"/>
            <c:if test="${stepCurrent > status.count}"><c:set var="liStyle" value="progressTracker-done"/></c:if>
            <c:if test="${stepCurrent < status.count}"><c:set var="liStyle" value="progressTracker-todo"/></c:if>
            <c:if test="${status.last}"><c:set var="title" value="${fn:replace(title, ']', '')}"/><c:set var="htmlCommentOpen" value=""/></c:if>
            <c:if test="${status.first}"><c:set var="title" value="${fn:replace(title, '[', '')}"/><c:set var="htmlCommentClose" value=""/></c:if>
            <c:if test="${stepCurrent == status.count}"><c:set var="liStyle" value="progressTracker-active"/></c:if>
            ${htmlCommentClose}<li class="${liStyle}">${title}</li>${htmlCommentOpen}
        </c:forEach>
    </ol>
</div> 
<br/>
<div id="build" style="width:200px;height:10px;margin-left: auto;margin-right: auto;color: #cccccc;}">V1.4.5</div>
<p>&nbsp;</p>